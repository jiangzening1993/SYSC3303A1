import java.io.*;
import java.net.*;

public class Host {

	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket receiveSocket, sendSocket, sendReceiveSocket;

	public Host() {
		try {
			receiveSocket = new DatagramSocket(23);
			sendReceiveSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void Transfer() {

		int len, clientPort;
		String sent, received;
		byte data[];
		
		// forever loop
		for (;;) {
			data = new byte[100];

			// receive packet
			receivePacket = new DatagramPacket(data, data.length);
			System.out.println("Intermediate Host: Waiting for packet");

			try {
				receiveSocket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}

			System.out.println("Intermediate Host: Packet received");
			System.out.println("From host: " + receivePacket.getAddress());
			System.out.println("Host port: " + receivePacket.getPort());
			//store the client port, which will be used to send the data from server
			clientPort = receivePacket.getPort();
			len = receivePacket.getLength();
			System.out.println("Length: " + len);
			System.out.println("Containing: ");
			for (int j = 0; j < len; j++) {
				System.out.println("Byte " + j + ": " + data[j]);
			}
			received = new String(data, 0, len);
			System.out.println("String: " + received);

			// forms the packet that send to server
			sendPacket = new DatagramPacket(data, len,
					receivePacket.getAddress(), 69);

			System.out.println("Intermediate Host: sending packet");
			System.out.println("To host: " + sendPacket.getAddress());
			System.out
					.println("Destination host port: " + sendPacket.getPort());
			len = sendPacket.getLength();
			System.out.println("Length: " + len);
			System.out.println("Containing: ");
			for (int j = 0; j < len; j++) {
				System.out.println("Byte " + j + ": " + data[j]);
			}
			sent = new String(data, 0, len);
			System.out.println("String: " + sent);

			// send the packet on sendreceive socket to port 69
			try {
				sendReceiveSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}

			// wait for sendBack from server
			data = new byte[100];
			receivePacket = new DatagramPacket(data, data.length);
			System.out.println("Intermediate Host: waiting for packet");
			try {
				sendReceiveSocket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}

			System.out.println("Intermediate Host: packet received");
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

			sendPacket = new DatagramPacket(data, receivePacket.getLength(),
					receivePacket.getAddress(), clientPort);
			
			System.out.println("Intermediate Host: Sending Packet");
			System.out.println("To host: " + sendPacket.getAddress());
			System.out.println("Destination host port: " + sendPacket.getPort());
			len = sendPacket.getLength();
			System.out.println("Length: " + len);
			System.out.println("Containing: ");
			for (int j = 0; j < len; j++) {
				System.out.println("Byte " + j + ": " + data[j]);
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

	public static void main(String[] args) {
		Host h = new Host();
		h.Transfer();
	}

}
