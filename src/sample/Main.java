package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.paint.Paint;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        Scene sc1= new Scene(root, 824, 600);
        sc1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(sc1);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
