package me.gregorias.xmllibrary.interfaces.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import me.gregorias.xmllibrary.Main;
import me.gregorias.xmllibrary.library.LibraryFacade;

/**
 * Main starting point for JavaFX GUI.
 */
public class MainApplication extends Application {
  private static final String CSS_PATH = "/main.css";
  private static final int SCENE_WIDTH = 800;
  private static final int SCENE_HEIGHT = 600;
  private static LibraryFacade FACADE;

  @Override
  public void start(Stage primaryStage) {
    BorderPane borderPane = new BorderPane();
    borderPane.setTop(createTopPane());
    borderPane.setLeft(createLeftMenu());
    borderPane.setMinSize(SCENE_WIDTH, SCENE_HEIGHT);
    borderPane.setMaxSize(SCENE_WIDTH, SCENE_HEIGHT);

    Scene scene = new Scene(borderPane);
    scene.getStylesheets().add(MainApplication.class.getResource(CSS_PATH).toExternalForm());

    primaryStage.setTitle(Main.APPLICATION_NAME);
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  public static void main(LibraryFacade facade) {
    FACADE = facade;
    launch();
  }

  private HBox createTopPane() {
    HBox hbox = new HBox();
    hbox.setId("top-pane");

    Label mainLabel = new Label(Main.APPLICATION_NAME);
    mainLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    mainLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    mainLabel.setTextFill(Color.web("#FFFFFF"));

    Button loginButton = new Button("login");
    Button registerButton = new Button("register");
    hbox.getChildren().addAll(mainLabel, loginButton, registerButton);

    HBox.setHgrow(mainLabel, Priority.ALWAYS);
    return hbox;
  }

  private static VBox createLeftMenu() {
    VBox vbox = new VBox();
    vbox.setId("left-menu");

    addOptionsToLeftMenu(vbox, "Library menu", new Hyperlink[]{
      new Hyperlink("Catalogue"),
      new Hyperlink("Find books")});

    addOptionsToLeftMenu(vbox, "Profile", new Hyperlink[]{
      new Hyperlink("Profile information"),
      new Hyperlink("Rented positions")});

    addOptionsToLeftMenu(vbox, "LibrarianToolbox", new Hyperlink[]{
      new Hyperlink("Overdue rents"),
      new Hyperlink("Add positions")});

    return vbox;
  }

  private static void addOptionsToLeftMenu(VBox vbox, String title, Hyperlink... options) {
    Text textTitle = new Text(title);
    textTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    vbox.getChildren().add(textTitle);

    for (int index = 0; index < options.length; index++) {
      options[index].getStyleClass().add("option");
      vbox.getChildren().add(options[index]);
    }
  }
}
