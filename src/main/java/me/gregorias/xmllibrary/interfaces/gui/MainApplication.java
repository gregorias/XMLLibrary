package me.gregorias.xmllibrary.interfaces.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Main starting point for JavaFX GUI.
 */
public class MainApplication extends Application {
  private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);
  private static final String CSS_PATH = "/main.css";
  private static final String LOGGED_IN_ACCOUNT_FORMAT_STRING = "Logged in(%s)";
  private static final String LIBRARIAN_STRING = "Librarian";
  private static final int SCENE_WIDTH = 800;
  private static final int SCENE_HEIGHT = 600;
  private static final int LEFT_MENU_WIDTH = 160;
  private static LibraryFacade FACADE;

  private HBox mTopPane;
  private List<Node> mTopPaneNodeList;
  private Label mTopPaneMainLabel;
  private Label mTopPaneLoginInfoLabel;
  private Button mTopPaneLoginButton;
  private Button mTopPaneLogoutButton;
  private Button mTopPaneRegisterButton;

  private VBox mLeftMenu;

  @Override
  public void start(Stage primaryStage) {
    BorderPane borderPane = new BorderPane();
    createTopPane();
    createLeftMenu();
    borderPane.setTop(mTopPane);
    borderPane.setLeft(mLeftMenu);
    borderPane.setMinSize(SCENE_WIDTH, SCENE_HEIGHT);
    borderPane.setMaxSize(SCENE_WIDTH, SCENE_HEIGHT);

    Scene scene = new Scene(borderPane);
    scene.getStylesheets().add(MainApplication.class.getResource(CSS_PATH).toExternalForm());

    primaryStage.setTitle(Main.APPLICATION_NAME);
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);

    FACADE.addObserver(new LibraryObserver());

    drawScene();
    primaryStage.show();
  }

  public static void main(LibraryFacade facade) {
    FACADE = facade;
    launch();
  }

  private class LibraryObserver implements Observer {
    @Override
    public void update(Observable observable, Object arg) {
      LOGGER.debug("LibraryObserver.update()");
      FACADE.acquireLock();
      try {
        FACADE.getCurrentLoggedInAccount();
      } finally {
        FACADE.releaseLock();
      }
      Platform.runLater(() -> drawScene());
    }
  }

  private void addOptionsToLeftMenu(VBox vbox, String title, Hyperlink... options) {
    Text textTitle = new Text(title);
    textTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    vbox.getChildren().add(textTitle);

    for (int index = 0; index < options.length; index++) {
      options[index].getStyleClass().add("option");
      vbox.getChildren().add(options[index]);
    }
  }


  private void createLeftMenu() {
    mLeftMenu = new VBox();
    mLeftMenu.setId("left-menu");
    mLeftMenu.setMinWidth(LEFT_MENU_WIDTH);
    mLeftMenu.setMaxWidth(LEFT_MENU_WIDTH);
  }

  private void createTopPane() {
    mTopPane = new HBox();
    mTopPane.setId("top-pane");

    mTopPaneNodeList = new ArrayList<>();

    mTopPaneMainLabel = new Label(Main.APPLICATION_NAME);
    mTopPaneMainLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    mTopPaneMainLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    mTopPaneMainLabel.setTextFill(Color.web("#FFFFFF"));

    mTopPaneLoginInfoLabel = new Label("");
    mTopPaneLoginInfoLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    mTopPaneLoginInfoLabel.setTextFill(Color.web("#FFFFFF"));

    mTopPaneLoginButton = new Button("login");
    LoginRequestHandler loginHandler = new LoginRequestHandler(FACADE);
    mTopPaneLoginButton.setOnAction(loginHandler);
    mTopPaneLogoutButton = new Button("logout");
    LogoutRequestHandler logoutHandler = new LogoutRequestHandler(FACADE);
    mTopPaneLogoutButton.setOnAction(logoutHandler);

    mTopPaneRegisterButton = new Button("register");

    mTopPane.setHgrow(mTopPaneMainLabel, Priority.ALWAYS);
  }

  private void updateTopPane() {
    mTopPane.getChildren().clear();
    mTopPane.getChildren().add(mTopPaneMainLabel);
    FACADE.acquireLock();
    try {
      if (!FACADE.isLoggedIn()) {
        mTopPane.getChildren().add(mTopPaneLoginButton);
        mTopPane.getChildren().add(mTopPaneRegisterButton);
      } else {
        mTopPane.getChildren().add(mTopPaneLoginInfoLabel);
        mTopPane.getChildren().add(mTopPaneLogoutButton);

        LibraryFacade.Account account = FACADE.getCurrentLoggedInAccount();
        String accountName;
        if (account.isLibrarian()) {
          accountName = LIBRARIAN_STRING;
        } else {
          accountName = account.getUser().getName();
        }
        mTopPaneLoginInfoLabel.setText(String.format(LOGGED_IN_ACCOUNT_FORMAT_STRING, accountName));
      }
    } finally {
      FACADE.releaseLock();
    }
  }

  private void updateLeftMenu() {
    FACADE.acquireLock();
    mLeftMenu.getChildren().clear();
    addOptionsToLeftMenu(mLeftMenu, "Library menu",
        new Hyperlink("Catalogue"),
        new Hyperlink("Find books"));
    try {
      if (FACADE.isLoggedIn()) {
        if (FACADE.getCurrentLoggedInAccount().isLibrarian()) {
          addOptionsToLeftMenu(mLeftMenu, "Librarian Toolbox",
              new Hyperlink("Overdue rents"),
              new Hyperlink("Manage users"),
              new Hyperlink("Add positions"));
        } else {
          addOptionsToLeftMenu(mLeftMenu, "Profile",
              new Hyperlink("Profile information"),
              new Hyperlink("Rented positions"));
        }
      }
    } finally {
      FACADE.releaseLock();
    }

  }

  private void drawScene() {
    LOGGER.debug("drawScene()");
    updateTopPane();
    updateLeftMenu();
  }
}
