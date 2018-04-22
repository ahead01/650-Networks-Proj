import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Client {
	private DatagramSocket dgSocket;
	private int sendPort;
	private InetAddress host;
    private PrintWriter out;
    private BufferedReader in;
 
    public void startConnection(String ip, int port) {
        try {
			//clientSocket = new Socket(ip, port);
        	//InetAddress addr = InetAddress.getByName(ip);
        	dgSocket = new DatagramSocket();
        	sendPort = port;
        	host = InetAddress.getByName("localhost");
			//out = new PrintWriter(clientSocket.getOutputStream(), true);
			//in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
 
    
    public String sendMessage() {
    	String resp = null;
    	int bytes = 10;
    	try {
	    	String urlString = "http://httpbin.org/user-agent";
	    	URL url = new URL(urlString);
	    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    	// Set timeouts
	    	con.setConnectTimeout(5000);
	    	con.setReadTimeout(5000);
	    	
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
	    	byte[] buffer = new byte[bytes];
	    	while (in.read(buffer, 0, buffer.length) != -1) {
	    		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, host,sendPort);
				dgSocket.send(packet);
				content.append(buffer);
	    	}
	    	in.close();
	    	System.out.println("Respoonse code: " + status);
	    	System.out.println("Respoonse text: " + content.toString());
	    	//System.out.println("message: " + msg);
	        resp = content.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return resp;
    }
 
    
    public void stopConnection() {
        try {
			in.close();
            out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    public static void main(String[] args) {
/*        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Select options input in the form: w x y\n" +
                "w is the name of a Web server (e.g. www.a.b) from which it requests w’s page;\n " +
                "x is an integer, where z=min(x, 1460) is the number of bytes C will send to S in every packet containing w’s page bytes except the last, which will have any remaining bytes in w’s page;\n " +
                "y is a timeout period in milliseconds. ");
        String w = reader.next();
        int x = reader.nextInt();
        int y = reader.nextInt();
        System.out.println("You entered: " + w + " " + x + " " + y);
        //once finished
        reader.close();*/
        Client myClient =new Client();
        myClient.startConnection("127.0.0.1",12321);
        String response = myClient.sendMessage();
        System.out.println(response);
        myClient.stopConnection();
    }
}
