package sbu.Server;

import sbu.FileMessage;
import sbu.TextMessage;
import java.util.ArrayList;
public class ServerData {
    private static final ArrayList<FileMessage> fileMessages = new ArrayList<>();
    private static final ArrayList<TextMessage> textMessages = new ArrayList<>();
    public static void addFileMessage(FileMessage fileMessage) {
        ServerData.fileMessages.add(fileMessage);
    }
    public static void addTextMessage(TextMessage textMessage) {
        ServerData.textMessages.add(textMessage);
    }
    public static ArrayList<FileMessage> getFileMessages() {
        return new ArrayList<>(fileMessages);
    }
    public static ArrayList<TextMessage> getTextMessages() {
        return new ArrayList<>(textMessages);
    }
}
