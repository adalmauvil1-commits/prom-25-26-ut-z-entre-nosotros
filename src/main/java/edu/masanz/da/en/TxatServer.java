package edu.masanz.da.en;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class TxatServer {

    private static final int PORT = 12345;

    private final Map<String, PrintWriter> mapaClientsWriters = new HashMap<>();

    public static void main(String[] args) {
        new TxatServer().start();
    }

    public void start() {
        System.out.println("Servidor iniciado en el puerto " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                new TxatClientHandler(serverSocket.accept(), mapaClientsWriters).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
