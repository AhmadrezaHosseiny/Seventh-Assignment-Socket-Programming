package sbu.Server;

import sbu.FileMessage;
import sbu.TextMessage;
import java.net.Socket;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
public class Server {
    public static final int PORT = 1234;
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(4);
    public static void main(String[] args) {
        initServerData();
        ServerSocket server = null;

        try {
            server = new ServerSocket(PORT);
            System.out.println("[SERVER] Server started. Waiting for client connections...");

            while (true){
                Socket client = server.accept();
                System.out.println("[SERVER] Client Connected: " + client.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(client);

                threadPool.execute(clientHandler);
            }
        }

        catch (IOException e) {
            System.out.println("Error ? : " + e.getMessage());
        }

        finally {
            try {
                if(server != null) server.close();
            }

            catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }

            threadPool.shutdown();
        }
    }
    public static void initServerData(){
        ServerData.addTextMessage(new TextMessage("Hello.", "Ahmadreza"));
        ServerData.addFileMessage(new FileMessage("src/main/java/sbu/Server/data/a-man-without-love-ngelbert-Hmperdinck.txt", "a-man-without-love-ngelbert-Hmperdinck.txt", "Server"));
        ServerData.addFileMessage(new FileMessage("src/main/java/sbu/Server/data/all-of-me-john-legend.txt", "all-of-me-john-legend.txt", "Server"));
        ServerData.addFileMessage(new FileMessage("src/main/java/sbu/Server/data/birds-imagine-dragons.txt", "birds-imagine-dragons.txt", "Server"));
        ServerData.addFileMessage(new FileMessage("src/main/java/sbu/Server/data/blinding-lights-the-weekend.txt", "blinding-lights-the-weekend.txt", "Server"));
        ServerData.addFileMessage(new FileMessage("src/main/java/sbu/Server/data/dont-matter-to-me-drake.txt", "dont-matter-to-me-drake.txt", "Server"));
        ServerData.addFileMessage(new FileMessage("src/main/java/sbu/Server/data/feeling-in-my-body-elvis.txt", "feeling-in-my-body-elvis.txt", "Server"));
        ServerData.addFileMessage(new FileMessage("src/main/java/sbu/Server/data/out-of-time-the-weekend.txt", "out-of-time-the-weekend.txt", "Server"));
        ServerData.addFileMessage(new FileMessage("src/main/java/sbu/Server/data/something-in-the-way-nirvana.txt", "something-in-the-way-nirvana.txt", "Server"));
        ServerData.addFileMessage(new FileMessage("src/main/java/sbu/Server/data/why-you-wanna-trip-on-me-michael-jackson.txt", "why-you-wanna-trip-on-me-michael-jackson.txt", "Server"));
        ServerData.addFileMessage(new FileMessage("src/main/java/sbu/Server/data/you-put-a-spell-on-me-austin-giorgio.txt", "you-put-a-spell-on-me-austin-giorgio.txt", "Server"));
    }
}