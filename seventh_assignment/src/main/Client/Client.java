package Client;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
public class Client {
    private static final int port = 6356;
    private static final String address = "localhost";
    private static void receiveFile(String fileName, BufferedReader in) throws IOException {
        StringBuilder fileContent = new StringBuilder();
        String line;

        while (!(line = in.readLine()).equals("End of file")) {
            fileContent.append(line).append("\n");
        }

        Files.write(Paths.get("client_files/" + fileName), fileContent.toString().getBytes());
        System.out.println("[Client] File received: " + fileName);
    }
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(address, port);

        try {
            Scanner scanner = new Scanner(System.in);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(() -> {
                try {
                    String serverResponse;

                    while ((serverResponse = in.readLine()) != null) {
                        if (serverResponse.startsWith("[Client] File response:")) {
                            String fileName = serverResponse.substring("File response:".length());
                            receiveFile(fileName, in);
                        }
                        else {
                            System.out.println(serverResponse);
                        }
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(() -> {
                try {
                    String userInput;

                    while (true) {
                        userInput = scanner.nextLine();

                        out.println(userInput);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            while (true) {}
        }
        finally {
            socket.close();
        }
    }
}
