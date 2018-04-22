import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Client {
	private DatagramSocket dgSocket;
	private int sendPort;
	private InetAddress host;
	private int bytesPerPacket = 1460;
	private String sourceUrl;
//    private PrintWriter out;
//    private BufferedReader in;
 
    public void startConnection(String ip, int port, String w, int x, int y) {
        try {
			//clientSocket = new Socket(ip, port);
        	//InetAddress addr = InetAddress.getByName(ip);
        	int timeoutMs = y;
        	sourceUrl = w;
        	sendPort = port;
        	host = InetAddress.getByName("localhost");
        	System.out.println("Getting data from: " + sourceUrl);
        	if(x < bytesPerPacket) {
        		bytesPerPacket = x;
        		System.out.println("Using bytes per packet of: " + bytesPerPacket);
        	}else{
        		System.out.println("Using default bytes per packet of: " + bytesPerPacket);
        	}
        	System.out.println("Server response timeout (ms): " + timeoutMs);
        	System.out.println("Client socket connecting to server on port : " + sendPort);
        	System.out.println("Hostname : " + host.toString()); 
        	dgSocket = new DatagramSocket();
        	dgSocket.setSoTimeout(timeoutMs);
        	if(dgSocket.isConnected()) {
        		System.out.println("Client socket is sucessfully connected on port: " + dgSocket.getPort()); 
        		System.out.println("Confirming timeout set to: " + dgSocket.getSoTimeout() + " ms");
        	}
			//out = new PrintWriter(clientSocket.getOutputStream(), true);
			//in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
 
    
    public String sendMessage() {
    	String resp = null;
    	int bytes = bytesPerPacket;
    	
    	try {
	    	//String urlString = "http://httpbin.org/user-agent";
    		//String urlString = "http://urlecho.appspot.com/echo?status=200&Content-Type=text%2Fhtml&body=Hello%20world!";
    		//String urlString = "http://httpstat.us/200?sleep=5000";
    		String urlString = sourceUrl;
	    	URL url = new URL(urlString);
	    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    	
	    	// Send GET request
	    	con.setRequestMethod("GET");
	    	
	    	int status = con.getResponseCode();
	    	int contentLenght = con.getContentLength();
	    	//byte[] b = new byte[contentLenght];
	    	String contentType = con.getContentType();
	    	String contentEncoding = con.getContentEncoding();
	    	
	    	System.out.println("Content Length: " + contentLenght);
	    	System.out.println("Content Type: " + contentType);
	    	System.out.println("Content Encoding: " + contentEncoding);
	    	
	    	
	    	InputStream inputStream = con.getInputStream();
	    	//Method 1
	    	DataInputStream in = new DataInputStream(inputStream);
	    	StringBuffer content = new StringBuffer(contentLenght);
	    	
	    	//Method 2
	    	//BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
	    	StringBuilder sb = new StringBuilder();
	    	
	    	byte[] buffer = new byte[bytes];
	    	long startTimeNs = System.nanoTime();
	    	long startTimeMs = System.currentTimeMillis();
	        while (in.read(buffer, 0, buffer.length) != -1) {
	        	//int retryCount = 0;
	    		//sb.append(buffer.toString());
	    		String str = new String(buffer);
	    		System.out.println(str);
	    		content.append(str);
	    		sb.append(str);
	    		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, host,sendPort);
				dgSocket.send(packet);
				long startTimeMsSend = System.currentTimeMillis();
				byte[] buf = new byte[256];
				DatagramPacket respPacket = new DatagramPacket(buf, buf.length);
				try {
					dgSocket.receive(respPacket);
				} catch (SocketTimeoutException e) {
					System.out.println("Response Timeout - ACK not recieved in time! - Re-sending packet");
						try {
							// Block for testing - sending packet with TEST as the contents will change server wait to 10 ms
//				    		byte[] testBuffer = "TEST".getBytes();
//							DatagramPacket testPacket = new DatagramPacket(testBuffer, testBuffer.length, host,sendPort);
//							dgSocket.send(testPacket);
							dgSocket.send(packet);// - commented for testing - uncomment for real run 
							buf = new byte[256];
							respPacket = new DatagramPacket(buf, buf.length);
							dgSocket.receive(respPacket);
						} catch (SocketTimeoutException e1) {
							System.out.println("Re-try limit reached, closing socket connection!");
							System.out.println(e1.getMessage());
				    		byte[] failBuffer = "FAIL".getBytes();
							DatagramPacket failPacket = new DatagramPacket(failBuffer, failBuffer.length, host,sendPort);
							dgSocket.send(failPacket);
						}
				}
				long endTimeMsSend = System.currentTimeMillis();
				String received = new String(respPacket.getData(), 0, respPacket.getLength());
				System.out.println("Server response: " + received);
				long totalPacketTime = endTimeMsSend - startTimeMsSend;
				System.out.println("recieved packet in: " + totalPacketTime + " ms" );
	    	}
	        long endTimeNs   = System.nanoTime();
	        long endTimeMs = System.currentTimeMillis();
	        long totalTimeNs = endTimeNs - startTimeNs;
	        long totalTimeMs = endTimeMs - startTimeMs;
	        System.out.println("DONE");
	    	in.close();
	    	System.out.println("Total transfre time in nano seconds: " + totalTimeNs);
	    	System.out.println("Total transfre time in milli seconds: " + totalTimeMs);
	    	int contentByteLength = content.toString().getBytes().length;
	    	System.out.println("Content Length (bytes): " + contentByteLength);
	    	System.out.println("Respoonse code: " + status);
	    	System.out.println("Respoonse text content: " + content.toString());
	    	System.out.println("Respoonse text sb: " + sb.toString());
	    	//System.out.println("message: " + msg);
	        resp = content.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return resp;
    }
 
    public void stopConnection() {
		byte[] stopBuffer = "STOP".getBytes();
		DatagramPacket stopPacket = new DatagramPacket(stopBuffer, stopBuffer.length, host,sendPort);
		try {
			dgSocket.send(stopPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	dgSocket.disconnect();
    }
    
    
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Select options input in the form: w x y\n" +
                "w is the name of a Web server (e.g. www.a.b) from which it requests w’s page;\n " +
                "x is an integer, where z=min(x, 1460) is the number of bytes C will send to S in every packet containing w’s page bytes except the last, which will have any remaining bytes in w’s page;\n " +
                "y is a timeout period in milliseconds. ( 0 for no timeout )  ");
        System.out.println("Example input: ");
        System.out.println("http://urlecho.appspot.com/echo?status=200&Content-Type=text%2Fhtml&body=Hello%20world! 10 75");
        String w = reader.next();
        int x = reader.nextInt();
        int y = reader.nextInt();
        System.out.println("You entered: " + w + " " + x + " " + y);
        //once finished
        reader.close();
        Client myClient =new Client();
        myClient.startConnection("127.0.0.1",12321,w,x,y);
        String response = myClient.sendMessage();
        System.out.println(response);
        System.out.println("Finished running - closing the socket and stopping the server");
        myClient.stopConnection();
    }
}
