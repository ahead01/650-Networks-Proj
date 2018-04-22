import java.net.*;
import java.io.*;
/*
 * Author: Austin Dase - 4/20/2018
 * This is the Server for COSC 650, Computer Networks Project
 * Instructor: Dr. Alexander L. Wijesinha
 * Group Paper Topic: QUIC
 * 
 * */
 
public class Server {
	//private ServerSocket serverSocket;
	private DatagramSocket serverSocket;
	private int delayMs;

	public void start(int port, int delay) throws InterruptedException {
		try {
			serverSocket = new DatagramSocket(port);
			int totalBytes = 0;
			delayMs = delay;
			System.out.println("Server listening on port: " + port);
			System.out.println("Intentionally delaying ACK's by " + delayMs + " ms - for testing");
			while(true) {
				byte[] buffer = new byte[256];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				//System.out.println("Waiting for next packet ");
				serverSocket.receive(request);
				byte[] data = request.getData();
				totalBytes += data.length;
				String requestStr = new String(data, 0, request.getLength());
				
				// Printing in a separate thread
				new Thread(new Runnable() {
				     public void run() {
				    	 	System.out.println(requestStr);
				     }
				}).start();

				if(requestStr.toString().equalsIgnoreCase("STOP")) {
					System.out.println("Recieved STOP message - closing socket");
					serverSocket.close();
					break;
				}else {
					if(requestStr.toString().equalsIgnoreCase("TEST")) {
						System.out.println("Recieved TEST message - setting delay to 10 ms");
//						serverSocket.close();
//						break;
						delayMs = 10;
						String s = "ACK : " + totalBytes;
						DatagramPacket dp = new DatagramPacket(s.getBytes() , s.getBytes().length , request.getAddress() , request.getPort());
						Thread.sleep(delayMs);
						serverSocket.send(dp);
					}else {
						String s = "ACK : " + totalBytes;
						DatagramPacket dp = new DatagramPacket(s.getBytes() , s.getBytes().length , request.getAddress() , request.getPort());
						Thread.sleep(delayMs);
						serverSocket.send(dp);
					}

				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Server server=new Server();
		server.start(12321, 65);
	}
}
