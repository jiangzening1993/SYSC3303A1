import java.io.*;
import java.net.*;

public class Server {

	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendSocket, receiveSocket;

	public Server() {

		try {
			receiveSocket = new DatagramSocket(69);
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void receive() throws Exception {

		byte data[], sendBack[];
		int len;
		String sent, received;

		for (;;) {

			data = new byte[100];
			sendBack = new byte[4];

			receivePacket = new DatagramPacket(data, data.length);
			System.out.println("Server: Waiting for Packet.");

			try {
				System.out.println("Waiting..."); // so we know we're waiting
				receiveSocket.receive(receivePacket);
			} catch (IOException e) {
				System.out.print("IO Exception: likely:");
				System.out.println("Receive Socket Timed Out.\n" + e);
				e.printStackTrace();
				System.exit(1);
			}

			System.out.println("Server: Packet received:");
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

			if (data[0] == 0 && data[1] == 1) {
				sendBack[0] = 0;
				sendBack[1] = 3;
				sendBack[2] = 0;
				sendBack[3] = 1;
			} else if (data[0] == 0 && data[1] == 2) {
				sendBack[0] = 0;
				sendBack[1] = 4;
				sendBack[2] = 0;
				sendBack[3] = 0;
			} else {
				throw new Exception("invalid request");
			}

			// Construct a datagram packet that is to be sent to a specified
			// port on a specified host.
			sendPacket = new DatagramPacket(sendBack, sendBack.length,
					receivePacket.getAddress(), receivePacket.getPort());

			System.out.println("Server: Sending packet:");
			System.out.println("To host: " + sendPacket.getAddress());
			System.out.println("Destination host port: " + sendPacket.getPort());
			len = sendPacket.getLength();
			System.out.println("Length: " + len);
			System.out.println("Containing: ");
			
			for (int j = 0; j < len; j++) {
				System.out.println("Byte " + j + ": " + sendBack[j]);
			}
			sent = new String(data, 0, len);
			System.out.println("String: " + sent);
			
			//create a new datagram socket
			try{
				sendSocket = new DatagramSocket();
			}catch(SocketException e){
				e.printStackTrace();
				System.exit(1);
			}
			
			//send the datagram packet to the client via a new socket
			try{
				sendSocket.send(sendPacket);
			}catch(IOException e){
				e.printStackTrace();
				System.exit(1);
			}
			
			System.out.println("Server: packet sent");
			
			sendSocket.close();
		}
	}
	
	public static void main(String args[]) throws Exception{
		Server s = new Server();
		s.receive();
	}
}
