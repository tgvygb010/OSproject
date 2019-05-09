import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;


public class Server 
{

    //static ServerSocket variable
    private static ServerSocket server;

    //socket server port on which it will listen
    private static int port = 1150;

    public static void main(String args[]) throws ClassNotFoundException 
	{
        //create the socket server object

        //keep listens indefinitely until receives 'exit' call or program terminates
        int i =0;
        Thread thread = null;


        try 
		{
            server = new ServerSocket(1150);

            while(true)
			{


                System.out.println("Waiting for the client request answer");
                Socket socket = server.accept();

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                String message = (String) ois.readObject();
                System.out.println("Message Received => " + message);

                Random rand = new Random();
                int newPort = rand.nextInt(9000)+1000;

                MultiThreadRespond mr = new MultiThreadRespond(newPort);
				//MultiThreadRespond mr = new MultiThreadRespond(1150);
                thread = new Thread(mr);
                thread.start();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(""+newPort);
				//oos.writeObject(""+1150);

                ois.close();
                oos.close();
                socket.close();
                i++;

            }
        } catch (IOException ex) {
            try 
			{

                server.close();

            } catch (IOException e) {
                System.err.println("ERROR closing socket: " + e.getMessage());
            }
        }
    }

}
