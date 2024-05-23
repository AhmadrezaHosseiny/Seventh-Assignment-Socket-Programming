package Server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
public class Server {
    private static final int port = 6356;
    private static final int history = 10;
    private static final String directory = "server_files/";
    private static final Set<PrintWriter> clients = new HashSet<>();
    private static final List<String> chatHistory = new ArrayList<>();
    private static class Handler extends Thread {
        private PrintWriter out;
        private BufferedReader in;
        private final Socket socket;
        public Handler(Socket socket) {
            this.socket = socket;
        }
        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                clients.add(out);

                for (String message : chatHistory) {
                    out.println(message);
                }

                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.startsWith("[Server] File request:")) {
                        String fileName = inputLine.substring("[Server] File request:".length());
                        sendFile(fileName, out);
                    }
                    else {
                        String message = "[Client] :" + inputLine;

                        chatHistory.add(message);

                        if (chatHistory.size() > history) {
                            chatHistory.remove(0);
                        }
                        for (PrintWriter client : clients) {
                            client.println(message);
                        }
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (out != null) clients.remove(out);
                try {
                    socket.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        private void sendFile(String fileName, PrintWriter out) {
            try {
                byte[] fileContent = Files.readAllBytes(Paths.get(directory + fileName));

                out.println("[Server] file " + fileName + " response.");
                out.println(new String(fileContent));
            }
            catch (IOException e) {
                out.println("[Server] file " + fileName + " not found.");
            }
        }
    }
    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(port);
        System.out.println("[Server] is running...");

        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        }
        finally {
            listener.close();
        }
    }
}