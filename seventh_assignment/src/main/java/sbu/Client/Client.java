package sbu.Client;

import java.io.*;
import java.util.UUID;
import sbu.FileMessage;
import java.net.Socket;
import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 1234;
    private Socket clientsSocket;
    private PrintWriter outStream;
    private BufferedReader inpStream;

    public InputStream in;
    private final String username;
    private HandleNewMessage hnm;
    public Thread newMessageThread;
    public Client (String username){
        this.username = username;
        initialize();
    }
    public void initialize(){
        try {

            clientsSocket = new Socket(SERVER_IP, SERVER_PORT);

            outStream = new PrintWriter(Client.this.clientsSocket.getOutputStream(), true);
            inpStream = new BufferedReader(new InputStreamReader(Client.this.clientsSocket.getInputStream()));
            in = clientsSocket.getInputStream();

            System.out.println("[CLIENT: " + this.username + "] connected to server.");


            outStream.println("-init");
            outStream.println(this.username);


            if (inpStream.readLine().equals("T")) System.out.println("[CLIENT: " + this.username + "] Initialized Successfully.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void getAllTextMessages() {
        outStream.println("-getTextMessages");


        try {
            int size = Integer.parseInt(inpStream.readLine());

            for (int i = 0; i < size; i++) {
                String text = inpStream.readLine();
                String author = inpStream.readLine();
                LocalDateTime t = LocalDateTime.parse(inpStream.readLine());

                System.out.println("\n " + author);
                System.out.println("\t" + text);
                System.out.println(" " + t.format(DateTimeFormatter.ofPattern("H:mm:ss")));
            }
            if (inpStream.readLine().equals("--finished")) System.out.println("[CLIENT: " + this.username + "] Read All Text Messages Successfully.");

        } catch (IOException e) {
            System.out.println("Problem While Reading All Text Messages From The Server.");
        }
    }
    public ArrayList<FileMessage> getAllFileMessages() {
        outStream.println("-getFileMessages");

        try {
            int size = Integer.parseInt(inpStream.readLine());

            ArrayList<FileMessage> fileMessages = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                String text = inpStream.readLine();
                String author = inpStream.readLine();
                LocalDateTime t = LocalDateTime.parse(inpStream.readLine());
                UUID messageID = UUID.fromString(inpStream.readLine());


                System.out.println("\n " + author);
                System.out.println("\t" + i  + ". " + text);
                System.out.println(" " + t.format(DateTimeFormatter.ofPattern("H:mm:ss")));

                FileMessage m = new FileMessage(text, author, t, messageID);
                fileMessages.add(m);
            }
            if (inpStream.readLine().equals("--finished")) System.out.println("[CLIENT: " + this.username + "] Read All File Messages Successfully.");

            return fileMessages;
        } catch (IOException e) {
            System.out.println("Problem While Reading All File Messages From The Server.");
        }
        return null;
    }
    public void sendMessage(String text){
        outStream.println("-sendMessage");
        outStream.println(text);
    }
    public void receiveFile(String messageID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    outStream.println("-receive");
                    outStream.println(messageID);

                    String fileName = inpStream.readLine();

                    FileOutputStream out = new FileOutputStream("Received_" + fileName + ".txt");

                    byte[] bytes = new byte[16 * 1024];
                    int count;

                    while ( (count = in.read(bytes)) != -1 ){
                        System.out.println("[CLIENT] Inside While...");
                        out.write(bytes, 0, count);
                    }
                    out.flush();

                    out.close();
                    System.out.println("File Received");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }
    public void handleNewMessages(){
        hnm = new HandleNewMessage(inpStream);
        newMessageThread = new Thread(hnm);
        newMessageThread.start();
    }
    public void shutdown(){
        try {
            hnm.close();
            clientsSocket.close();
        } catch (IOException e) {
            System.out.println("[CLIENT] Error During Closing Socket...");
        }
    }
    public String getUsername() {
        return this.username;
    }
    public static void main(String[] args) {
        System.out.println("---Messager---");
        System.out.println("Enter Your Name: ");

        Scanner scanner = new Scanner(System.in);
        String name = scanner.next();

        Client client = new Client(name);

        while (true) {
            System.out.println("choose an option: ");
            System.out.println("1) Download a file: ");
            System.out.println("2) Group Chat: ");
            System.out.println("3) Exit: ");


            String choice = scanner.next();
            if (choice.equals("1"))
                handleDownloadFile(client, scanner);
            else if (choice.equals("2"))
                handleGroupChat(client, scanner);
            else
                break;
        }
    }
    public static void handleDownloadFile(Client client, Scanner scanner){
        System.out.println("\n\n --Download a file--");

        ArrayList<FileMessage> fileMessages = client.getAllFileMessages();

        while (true) {
            System.out.println("\nSelect a file to Download : (e to exit)");
            String message = scanner.nextLine();

            if(message.equals("e")) break;

//            if(Integer.parseInt(message) >= 0) System.out.println("Number!");
//            else System.out.println("String");
            String id = fileMessages.get(4).getMessageID().toString();
            client.receiveFile(id);
        }

    }
    public static void handleGroupChat(Client client, Scanner scanner){
        System.out.println("\n\n --Group Chat--");
        client.getAllTextMessages();
        client.handleNewMessages();
        while (true) {
            System.out.println("\nSend a Message: (e to exit)");
            String message = scanner.nextLine();

            if(message.equals("e")) break;

            client.sendMessage(message);

            System.out.println("\n " + client.getUsername());
            System.out.println("\t" + message);
            System.out.println(" " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("H:mm:ss")));
        }
        try {
            client.newMessageThread.wait();
        } catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}