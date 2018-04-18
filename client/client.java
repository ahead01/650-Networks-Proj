import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client
{
    // initialize socket and input output streams
    private Socket socket            = null;
    private DataInputStream  input   = null;
    private DataOutputStream out     = null;
    private String w;
    private String x;
    private String y;

    // constructor to put ip address and port
    public Client(String w, int port, String x, String y)
    {
        //
        int bytes = min(x, 1460);
        int timeout = y;
        String url = w;

        // establish a connection
        try
        {
            socket = new Socket(127.0.0.1, port);
            System.out.println("Connected");
            String localPort = socket.getLocalPort();
            System.out.println(" On port: " + localPort);
            System.out.println("Enter data to send: ");

            // takes input from provided URL
            URLConnection connection = new URL(url).openConnection();
            ByteArrayInputStream response = new ByteArrayInputStream(byte[] buff);
            response = connection.getInputStream();

            System.out.println("Bytes: " + buff.length);
            // sends output to the socket
            out    = new DataOutputStream(socket.getOutputStream());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }



        // close the connection
        try
        {
            input.close();
            out.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }



    public static void main(String args[])
    {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Select options input in the form: w x y\n" +
                "w is the name of a Web server (e.g. www.a.b) from which it requests w’s page;\n " +
                "x is an integer, where z=min(x, 1460) is the number of bytes C will send to S in every packet containing w’s page bytes except the last, which will have any remaining bytes in w’s page;\n " +
                "y is a timeout period in milliseconds. ");
        String w = reader.next();
        String x = reader.next();
        String y = reader.next();
        System.out.println("You entered: " + w + " " + x + " " + y);
        //once finished
        reader.close();
        Client client = new Client(w, 12321, x, y);
    }
}
