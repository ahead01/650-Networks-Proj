import java.net.*;
import java.io.*;

public class Server {
	//private ServerSocket serverSocket;
	private DatagramSocket serverSocket;

	public void start(int port) {
		try {
			serverSocket = new DatagramSocket(port);
			int totalBytes = 0;
			while(true) {
				byte[] buffer = new byte[256];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				serverSocket.receive(request);
				byte[] data = request.getData();
				totalBytes += data.length;
				String requestStr = new String(data, 0, request.getLength());
				System.out.println(requestStr);
				if(request.toString().equalsIgnoreCase("Stop")) {
					serverSocket.close();
				}else {
					String s = "ACK : " + totalBytes;
					DatagramPacket dp = new DatagramPacket(s.getBytes() , s.getBytes().length , request.getAddress() , request.getPort());
					serverSocket.send(dp);
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	/*            while (true) {
    			out = new PrintWriter(clientSocket.getOutputStream(), true);
    			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    			String greeting = null;
            	greeting = in.readLine();
				if ("stop".equals(greeting)) {
					out.println("hello client");
					break;
				} else {
					out.println("unrecognised greeting: ");
				} 
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void stop() {
        try {
			in.close();
			out.close();
			clientSocket.close();
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }*/
	public static void main(String[] args) {
		Server server=new Server();
		server.start(12321);
		//server.stop();
	}
}
