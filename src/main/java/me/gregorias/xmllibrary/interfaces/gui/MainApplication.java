package me.gregorias.xmllibrary.interfaces.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by grzesiek on 08.12.14.
 */
public class MainApplication extends Application {

  @Override
  public void start(Stage primaryStage) {
    BorderPane borderPane = new BorderPane();

    borderPane.getChildren().add(createTopPane());

    Scene scene = new Scene(borderPane, 300, 250);

    primaryStage.setTitle("Hello World!");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }

  private static class HelloWorldEventHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent event) {
      System.out.println("Hello World!");
    }
  }

  private HBox createTopPane() {
    HBox hbox = new HBox();
    hbox.setPadding(new Insets(15, 12, 15, 12));
    hbox.setSpacing(10);
    hbox.setStyle("-fx-background-color: #336699;");
    Text scenetitle = new Text("XMLLibrary");
    scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    Button loginButton = new Button("login");
    loginButton.setAlignment(Pos.CENTER_RIGHT);
    Button registerButton = new Button("register");
    registerButton.setAlignment(Pos.CENTER_RIGHT);
    hbox.getChildren().addAll(scenetitle, loginButton, registerButton);
    hbox.setHgrow(scenetitle, Priority.ALWAYS);
    return hbox;
  }
}
