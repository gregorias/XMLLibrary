package me.gregorias.xmllibrary.interfaces.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import me.gregorias.xmllibrary.Main;
import me.gregorias.xmllibrary.library.LibraryFacade;
import me.gregorias.xmllibrary.library.jaxb.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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

  private Stage mPrimaryStage;

  private Hyperlink mCatalogueOption;
  private Hyperlink mProfileInformationOption;
  private Hyperlink mRentedPositionsOption;

  private Hyperlink mItemsOption;
  private Hyperlink mUsersOption;
  private Hyperlink mMergeOption;

  private BorderPane mMainPane;

  private HBox mTopPane;
  private Label mTopPaneMainLabel;
  private Label mTopPaneLoginInfoLabel;
  private Button mTopPaneLoginButton;
  private Button mTopPaneLogoutButton;
  private Button mTopPaneRegisterButton;

  private VBox mLeftMenu;

  private List<Book> mSelectedBooks;
  private CenterMode mCenterMode = CenterMode.CATALOGUE;


  @Override
  public void start(Stage primaryStage) {
    mPrimaryStage = primaryStage;
    selectAllBooks();

    mMainPane = new BorderPane();
    createTopPane();
    createLeftMenu();
    mMainPane.setTop(mTopPane);
    mMainPane.setLeft(mLeftMenu);
    mMainPane.setMinSize(SCENE_WIDTH, SCENE_HEIGHT);
    mMainPane.setMaxSize(SCENE_WIDTH, SCENE_HEIGHT);

    Scene scene = new Scene(mMainPane);
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

  private static enum CenterMode {
    CATALOGUE,
    PROFILE_INFORMATION,
    RENTED_POSITIONS,
    ALL_ITEMS,
    MANAGE_USERS
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
      Platform.runLater(MainApplication.this::drawScene);
    }
  }

  private void addOptionsToLeftMenu(VBox vbox, String title, Hyperlink... options) {
    Text textTitle = new Text(title);
    textTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    vbox.getChildren().add(textTitle);

    for (Hyperlink option : options) {
      option.getStyleClass().add("option");
      vbox.getChildren().add(option);
    }
  }

  private void createLeftMenu() {
    mLeftMenu = new VBox();
    mLeftMenu.setId("left-menu");
    mLeftMenu.setMinWidth(LEFT_MENU_WIDTH);
    mLeftMenu.setMaxWidth(LEFT_MENU_WIDTH);

    mCatalogueOption = new Hyperlink("Catalogue");
    mCatalogueOption.setOnAction((event) -> {
        selectAllBooks();
        mCenterMode = CenterMode.CATALOGUE;
        updateCenter();
      });

    mProfileInformationOption = new Hyperlink("Profile Information");
    mProfileInformationOption.setOnAction((event) -> {
        mCenterMode = CenterMode.PROFILE_INFORMATION;
        updateCenter();
      });

    mRentedPositionsOption = new Hyperlink("Rented Positions");
    mRentedPositionsOption.setOnAction((event) -> {
        mCenterMode = CenterMode.RENTED_POSITIONS;
        updateCenter();
      });

    mItemsOption = new Hyperlink("All Items");
    mItemsOption.setOnAction((event) -> {
        mCenterMode = CenterMode.ALL_ITEMS;
        updateCenter();
      });

    mUsersOption = new Hyperlink("Manage Users");
    mUsersOption.setOnAction((event) -> {
        mCenterMode = CenterMode.MANAGE_USERS;
        updateCenter();
      });

    mMergeOption = new Hyperlink("Add books");
    mMergeOption.setOnAction((event) -> {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(mPrimaryStage);
        if (file != null) {
          boolean hasSucceeded = FACADE.mergeLibrary(file.toPath());
          if (!hasSucceeded) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Could not merge.");
            alert.showAndWait();
          } else {
            FACADE.setChanged();
            FACADE.notifyObservers();
          }
        }
      });
  }

  private void createTopPane() {
    mTopPane = new HBox();
    mTopPane.setId("top-pane");

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
    RegisterRequestHandler registerHandler = new RegisterRequestHandler(FACADE);
    mTopPaneRegisterButton.setOnAction(registerHandler);

    HBox.setHgrow(mTopPaneMainLabel, Priority.ALWAYS);
  }

  private void updateCenter() {
    FACADE.acquireLock();
    try {
      if (mCenterMode.equals(CenterMode.CATALOGUE)) {
        Pane bookShelf = BookShelf.createBookShelfPane(FACADE, mSelectedBooks);
        mMainPane.setCenter(Utils.wrapNodeInVerticalScrollPane(bookShelf));
      } else if (mCenterMode.equals(CenterMode.PROFILE_INFORMATION)) {
        mMainPane.setCenter(Utils.wrapNodeInVerticalScrollPane(new ProfileInformationPane(FACADE)));
      } else if (mCenterMode.equals(CenterMode.ALL_ITEMS)) {
        mMainPane.setCenter(Utils.wrapNodeInVerticalScrollPane(
            new ItemsPane(FACADE, FACADE.joinAllItemsWithData())));
      } else if (mCenterMode.equals(CenterMode.MANAGE_USERS)) {
        mMainPane.setCenter(Utils.wrapNodeInVerticalScrollPane(
            new ManageUsersPane(FACADE, FACADE.getAllUsers())));

      } else {
        mMainPane.setCenter(Utils.wrapNodeInVerticalScrollPane(new RentedPositionsPane(FACADE)));
      }
    } finally {
      FACADE.releaseLock();
    }
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

    addOptionsToLeftMenu(mLeftMenu, "Library menu", mCatalogueOption);
    try {
      if (FACADE.isLoggedIn()) {
        if (FACADE.getCurrentLoggedInAccount().isLibrarian()) {
          addOptionsToLeftMenu(mLeftMenu, "Librarian Toolbox",
              mItemsOption,
              mUsersOption,
              mMergeOption);
        } else {
          addOptionsToLeftMenu(mLeftMenu, "Profile",
              mProfileInformationOption,
              mRentedPositionsOption);
        }
      }
    } finally {
      FACADE.releaseLock();
    }

  }

  private void drawScene() {
    LOGGER.debug("drawScene()");
    if (!FACADE.isLoggedIn()) {
      mCenterMode = CenterMode.CATALOGUE;
    }
    updateTopPane();
    updateLeftMenu();
    updateCenter();
  }

  private void selectAllBooks() {
    mSelectedBooks = FACADE.getLibrary().getPositions().getBooks();
  }
}
