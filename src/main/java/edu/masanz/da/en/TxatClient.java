package edu.masanz.da.en;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TxatClient extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(TxatClient.class.getResource("txat-client.fxml"));
        Scene scene = new Scene(loader.load(), 1073, 528);
        stage.setTitle("Txat v7");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
