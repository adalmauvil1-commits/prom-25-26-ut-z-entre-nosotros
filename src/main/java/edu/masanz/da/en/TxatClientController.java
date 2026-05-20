package edu.masanz.da.en;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TxatClientController {

    @FXML
    private TextField hostTextField;

    @FXML
    private TextField portTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button connectionButton;

    @FXML
    private TextArea messagesTextArea;

    @FXML
    private TextField messageTextField;

    @FXML
    private Button sendButton;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Thread listenerThread;
    private boolean connected;

    @FXML
    private void initialize() {
        setConnected(false);
    }

    @FXML
    private void handleConnectionButton() {
        if (connected) {
            disconnect(true);
        } else {
            connect();
        }
    }

    @FXML
    private void handleSendButton() {
        sendMessage();
    }

    @FXML
    private void handleMessageAction() {
        sendMessage();
    }

    private void connect() {
        String host = hostTextField.getText().trim();
        String portText = portTextField.getText().trim();
        String name = nameTextField.getText().trim();

        if (host.isEmpty() || portText.isEmpty() || name.isEmpty()) {
            appendMessage("Introduce IP, puerto y nombre.");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(portText);
        } catch (NumberFormatException e) {
            appendMessage("El puerto debe ser un numero.");
            return;
        }

        connectionButton.setDisable(true);
        new Thread(() -> connectInBackground(host, port, name)).start();
    }

    private void connectInBackground(String host, int port, String name) {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(name);
            String response = in.readLine();
            if (!"OK".equals(response)) {
                closeResources();
                Platform.runLater(() -> {
                    appendMessage("Ese nombre no esta disponible. Elige otro.");
                    setConnected(false);
                    connectionButton.setDisable(false);
                });
                return;
            }

            Platform.runLater(() -> {
                appendMessage("Conectado al servidor.");
                setConnected(true);
                connectionButton.setDisable(false);
            });
            startListening();
        } catch (IOException e) {
            closeResources();
            Platform.runLater(() -> {
                appendMessage("No se pudo conectar al servidor.");
                setConnected(false);
                connectionButton.setDisable(false);
            });
        }
    }

    private void startListening() {
        listenerThread = new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    String message = line;
                    Platform.runLater(() -> appendMessage(message));
                }
            } catch (IOException e) {
                if (connected) {
                    Platform.runLater(() -> appendMessage("Conexion cerrada por el servidor."));
                }
            } finally {
                Platform.runLater(() -> disconnect(false));
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    private void sendMessage() {
        if (!connected || out == null) {
            return;
        }

        String message = messageTextField.getText().trim();
        if (message.isEmpty()) {
            return;
        }

        out.println(message);
        messageTextField.clear();

        if (message.equals("BYE")) {
            disconnect(false);
        }
    }

    private void disconnect(boolean sendBye) {
        if (sendBye && out != null) {
            out.println("BYE");
        }
        closeResources();
        setConnected(false);
    }

    private void closeResources() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            // El cierre del socket no requiere accion adicional en la GUI.
        }
        socket = null;
        out = null;
        in = null;
    }

    private void setConnected(boolean connected) {
        this.connected = connected;
        hostTextField.setDisable(connected);
        portTextField.setDisable(connected);
        nameTextField.setDisable(connected);
        messageTextField.setDisable(!connected);
        sendButton.setDisable(!connected);
        connectionButton.setText(connected ? "Desconectar" : "Conectar");
        messageTextField.requestFocus();
    }

    private void appendMessage(String message) {
        messagesTextArea.appendText(message + System.lineSeparator());
    }

}
