package hangman;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Random;
import javax.swing.JLabel;


class TCPServer {
    private String password;
    private JLabel correct;
    private StringBuilder passwordHidden;
    private void getPassword() {
        String[] a = {"hercules", "tarzan", "mulan", "incredibles", "pinocchio", "aladin", "cinderella", "frozen", "minions", "zootopia"};
        Random rand = new Random();
        int n = rand.nextInt(10);
        password = a[n];
        passwordHidden.append(password.replaceAll(".", "*"));
        correct.setText(correct.getText() + passwordHidden.toString());
    }
    public static void main(String argv[]) {
        String clientSentence;
        String capitalizedSentence;
        ServerSocket welcomeSocket = null;
        Socket connectionSocket = null;
        Scanner inFromClient = null;
        DataOutputStream outToClient = null;

        try {
            welcomeSocket = new ServerSocket(6789);
            getPassword();
        } catch (IOException e) {
            System.out.println("Cannot create a welcome socket");
            System.exit(1);
        }
        while (true) {
            try {
                System.out.println("The server is waiting ");
                connectionSocket = welcomeSocket.accept();
                inFromClient = new Scanner(connectionSocket.getInputStream());
                outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                clientSentence = inFromClient.nextLine();

                capitalizedSentence  = checkpassword(clientSentence);

                outToClient.writeBytes(capitalizedSentence);
            }
            catch (IOException e) {
                System.out.println("Error cannot create this connection");
            }
            finally {
                try {
                    if (inFromClient != null)
                        inFromClient.close();
                    if (outToClient != null)
                        outToClient.close();
                    if (connectionSocket != null)
                        connectionSocket.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void checkpassword(String clientSentence) {
        String newpasswordHidden;
        newpasswordHidden = passwordHidden;
        char positions[] = new char[passwordHidden.length()];
        for (int i = 0; i < passwordHidden.length(); i++) {
            if (clientSentence == 'positions[i]') {
                positions[i] = 'clientSentence';
            }
        }
        return newpasswordHidden;
    }
}