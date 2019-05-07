package hangman;

//TCPConcurrentServer.java
import java.io.*;
import java.net.*;

public class TCPConcurrentServer {
    public static void main(String argv[]) {
        ServerSocket welcomeSocket = null;
        try {
            welcomeSocket = new ServerSocket(6789);
            while (true) {
                try {
                    System.out.println("The server is waiting ");
                    Socket connectionSocket = welcomeSocket.accept();
                    EchoThread echoThread = new EchoThread(connectionSocket);
                    echoThread.start();

                    private void getPassword()
                    {
                        String [] a = { "hercules", "tarzan", "mulan", "incredibles","pinocchio","aladin","cinderella","frozen","minions","zootopia" };
                        Random rand = new Random();
                        int n = rand.nextInt(10);
                        password = a[n];

                        passwordHidden.append(password.replaceAll(".", "*"));
                        correct.setText(correct.getText() + passwordHidden.toString());
                    }

                    

                } catch (IOException e) {
                    System.out.println("Cannot create this connection");
                }
            }
        } catch (IOException e) {
            System.out.println("Cannot create a welcome socket");
            System.exit(1);
        }
    }
}
