package sbu.Client;

import java.io.IOException;
import java.io.BufferedReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class HandleNewMessage implements Runnable{
    private boolean isClosed = false;
    private final BufferedReader inputStream;
    public HandleNewMessage(BufferedReader in){
        inputStream = in;
    }
    @Override
    public void run() {
        while (!isClosed){
            try {
                String inp = inputStream.readLine();

                if(inp.equals("-newMessage")) {
                    String text = inputStream.readLine();
                    String author = inputStream.readLine();
                    LocalDateTime t = LocalDateTime.parse(inputStream.readLine());
                    System.out.println("\n\t-NEW MESSAGE-");
                    System.out.println(" " + author);
                    System.out.println("\t" + text);
                    System.out.println(" " + t.format(DateTimeFormatter.ofPattern("H:mm:ss")));
                }
            }

            catch (IOException e) {
                System.out.println("Error During Getting New Message");
            }
        }
        System.out.println("[CLIENT] Handle New Message Thread Stopped!");
    }
    public void close(){
        this.isClosed = true;
    }
}
