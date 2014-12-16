package me.gregorias.xmllibrary.interfaces.gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import me.gregorias.xmllibrary.library.LibraryFacade;
import me.gregorias.xmllibrary.library.jaxb.User;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;
import java.util.Optional;

/**
 * Pane for managing users.
 */
public class ManageUsersPane extends VBox {
  private final LibraryFacade mFacade;
  public ManageUsersPane(LibraryFacade facade, List<User> users) {
    mFacade = facade;
    setSpacing(10);
    for (User user : users) {
      addUserPane(user);
    }
    getStyleClass().add("toplist");
  }

  private void addUserPane(User user) {
    GridPane pane = new GridPane();
    pane.setHgap(10);
    pane.setVgap(10);
    Label nameLabel = new Label("Name");
    Label nameFieldLabel = new Label(user.getName());

    Label validToLabel = new Label("Valid To");
    Label validToFieldLabel = new Label(user.getValidTo());

    Label rentedItemsLabel = new Label("Rented items");
    int rentedItemsCount = mFacade.getUserItems(user).size();
    Label rentedItemsFieldLabel = new Label(String.valueOf(rentedItemsCount));

    HBox buttonBar = new HBox();
    buttonBar.setSpacing(10);
    Button setValidButton = new Button("Change account validity");
    setValidButton.setOnAction((event) -> {
        Optional<String> validTo = getValidToFromUser();
        if (validTo.isPresent()) {
          user.setValidTo(validTo.get());
          mFacade.setChanged();
          mFacade.notifyObservers();
        }
      });
    buttonBar.getChildren().add(setValidButton);
    if (rentedItemsCount == 0) {
      Button deleteAccountButton = new Button("Delete account");
      deleteAccountButton.setOnAction((event) -> {
          mFacade.deleteUser(user);
          mFacade.setChanged();
          mFacade.notifyObservers();
        });
      buttonBar.getChildren().add(deleteAccountButton);
    }

    pane.add(nameLabel, 0, 0);
    pane.add(nameFieldLabel, 1, 0);

    pane.add(validToLabel, 0, 1);
    pane.add(validToFieldLabel, 1, 1);

    pane.add(rentedItemsLabel, 0, 2);
    pane.add(rentedItemsFieldLabel, 1, 2);

    pane.add(buttonBar, 0, 3, 2, 1);
    pane.getStyleClass().add("listitem");

    getChildren().add(pane);
  }

  private static Optional<String> getValidToFromUser() {
    TextInputDialog dialog = new TextInputDialog("walter");
    dialog.setTitle("Valid to field");
    dialog.setHeaderText("Valid to field indicates till when user account is active.");
    dialog.setContentText("Provide valid to date or indefinite:");
    boolean hasInputBeenHandled = false;

    while (!hasInputBeenHandled) {
      Optional<String> validToInput = dialog.showAndWait();
      if (validToInput.isPresent()) {
        if (validToInput.get().equals("indefinite")) {
          return Optional.of("indefinite");
        } else {
          Optional<XMLGregorianCalendar> calendar =
              me.gregorias.xmllibrary.library.Utils.getDateFromString(validToInput.get());
          if (calendar.isPresent()) {
            return validToInput;
          }
        }
      } else {
        hasInputBeenHandled = true;
      }

    }
    return Optional.empty();
  }
}
