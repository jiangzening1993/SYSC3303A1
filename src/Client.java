import java.io.*;
import java.net.*;

public class Client {

	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendReceiveSocket;

	public Client() {
		try {
			sendReceiveSocket = new DatagramSocket();
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	public void sendAndReceive() {
		
		byte msg[], fileName[], mode[];
		String fileNameS, modeS, sent, received;
		int len;
		
		// set up filename and mode
		fileNameS = "test.txt";
		fileName = fileNameS.getBytes();
		modeS = "octet";
		mode = modeS.getBytes();

		// 11 packets, 5 reads, 5 writes, and 1 invalid.
		for (int i = 1; i < 12; i++) {
			
			msg = new byte[100];
			
			System.out.println("Client: sending a packet " + i);

			// if i = 2, 4, 6, 8, 10 read request
			// if i = 1, 3, 5, 7, 9 write request
			// if i = 11, invalid request
			if (i % 2 == 0) {
				msg[0] = 0;
				msg[1] = 1;
			}
			// set up the msg[1] randomly
			else if (i == 11) {
				msg[0] = 0;
				msg[1] = 9;
			} else {
				msg[0] = 0;
				msg[1] = 2;
			}

			// add filename to msg

			for (int j = 2; j < fileName.length + 2; j++) {
				msg[j] = fileName[j - 2];
			}

			msg[fileName.length + 2] = 0;

			// add mode to msg
			for (int j = 3 + fileName.length; j < mode.length + fileName.length
					+ 3; j++) {
				msg[j] = mode[j - fileName.length - 3];
			}

			msg[fileName.length + mode.length + 3] = 0;
			
			len = fileName.length + mode.length + 4;

			try {
				sendPacket = new DatagramPacket(msg, len,
						InetAddress.getLocalHost(), 23);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				System.exit(1);
			}

			System.out.println("Client: Sending packet");
			System.out.println("To host: " + sendPacket.getAddress());
			System.out
					.println("Destination host port: " + sendPacket.getPort());
			len = sendPacket.getLength();
			System.out.println("Length: " + len);
			System.out.println("Containing: ");
			for (int j = 0; j < len; j++) {
				System.out.println("Byte " + j + ": " + msg[j]);
			}
			sent = new String(msg, 0, len);
			System.out.println("String: " + sent);

			try {
				sendReceiveSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}

			System.out.println("Client: Packet sent.\n");

			byte data[] = new byte[100];
			receivePacket = new DatagramPacket(data, data.length);

			System.out.println("Client: waiting for packet.");

			try {
				sendReceiveSocket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}

			System.out.println("Client: Packet received:");
			System.out.println("From host: " + receivePacket.getAddress());
			System.out.println("Host port: " + receivePacket.getPort());
			len = receivePacket.getLength();
			System.out.println("Length: " + len);
			System.out.println("Containing: ");
			for (int j = 0; j < len; j++) {
				System.out.println("Byte " + j + ": " + data[j]);
			}
			received = new String(data, 0, len);
			System.out.println("String: " + received);

			System.out.println();
		}
		sendReceiveSocket.close();
	}

	public static void main(String args[]) {
		Client c = new Client();
		c.sendAndReceive();
	}
}
