package me.gregorias.xmllibrary.interfaces.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import me.gregorias.xmllibrary.library.BookItemUser;
import me.gregorias.xmllibrary.library.LibraryFacade;
import me.gregorias.xmllibrary.library.jaxb.ItemStatus;

import java.util.Collection;

/**
 * Pane for all items.
 */
public class ItemsPane extends VBox {
  private final LibraryFacade mFacade;
  public ItemsPane(LibraryFacade facade, Collection<BookItemUser> triples) {
    mFacade = facade;

    setSpacing(10);

    for (BookItemUser triple : triples) {
      addTriple(triple);
    }

    getStyleClass().add("toplist");
  }

  private static void addFields(GridPane pane, int row, String name, String value) {
    pane.add(new Label(name), 0, row);
    pane.add(new Label(value), 1, row);
  }

  private void addTriple(BookItemUser triple) {
    GridPane pane = new GridPane();
    pane.setHgap(10);
    pane.setVgap(10);
    addFields(pane, 0, "Title", triple.mBook.getTitle());
    addFields(pane, 1, "ISBN-10", triple.mBook.getIsbn10());
    addFields(pane, 2, "ItemId", String.valueOf(triple.mItem.getItemId()));
    pane.add(new Label("Status"), 0, 3);
    Label statusLabel = new Label(triple.getBookStatus().toString());
    pane.add(statusLabel, 1, 3);
    if (triple.getBookStatus().isOverdue()) {
      statusLabel.getStyleClass().add("overdue");
    }
    if (triple.mUser != null) {
      addFields(pane, 4, "User", triple.mUser.getName());
    }
    HBox buttonBar = new HBox();
    buttonBar.setSpacing(10);
    buttonBar.setAlignment(Pos.CENTER_LEFT);
    if (!triple.getBookStatus().isRentable()) {
      Button returnButton = new Button("Return Book");
      returnButton.setOnAction((event) -> {
          mFacade.returnItem(triple.mItem);
          mFacade.setChanged();
          mFacade.notifyObservers();
        });
      buttonBar.getChildren().add(returnButton);
    } else {
      if (triple.mItem.getStatus() == ItemStatus.IN_STORE) {
        Button availableButton = new Button("Mark as available");
        availableButton.setOnAction((event) -> {
            triple.mItem.setStatus(ItemStatus.AVAILABLE);
            mFacade.setChanged();
            mFacade.notifyObservers();
          });
        buttonBar.getChildren().add(availableButton);
      }
      Button removeButton = new Button("Remove item");
      removeButton.setOnAction((event) -> {
          mFacade.getLibrary().getItems().getItems().remove(triple.mItem);
          mFacade.setChanged();
          mFacade.notifyObservers();
        });
      buttonBar.getChildren().add(removeButton);
    }

    pane.add(buttonBar, 0, 5, 2, 1);
    pane.getStyleClass().add("listitem");
    getChildren().add(pane);
  }
}
