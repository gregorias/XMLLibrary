package me.gregorias.xmllibrary.interfaces.gui;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import me.gregorias.xmllibrary.library.LibraryFacade;
import me.gregorias.xmllibrary.library.jaxb.Book;
import me.gregorias.xmllibrary.library.jaxb.HistoryItem;
import me.gregorias.xmllibrary.library.jaxb.Item;
import me.gregorias.xmllibrary.library.jaxb.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Created by grzesiek on 14.12.14.
 */
public class RentedPositionsPane extends VBox {
  public RentedPositionsPane(LibraryFacade facade) {
    User user = facade.getCurrentLoggedInAccount().getUser();
    setSpacing(10);

    facade.acquireLock();
    try {
      List<Item> userItems = facade.getUserItems(user);
      for (Item item : userItems) {
        addItem(facade, item);
      }

    } finally {
      facade.releaseLock();
    }

    getStyleClass().addAll("pane", "rentedpositions");
  }

  private void addItem(LibraryFacade facade, Item item) {
    GridPane pane = new GridPane();
    pane.setHgap(10);
    pane.setVgap(10);

    Optional<Book> optBook = facade.findBook(item.getIsbn10());
    Book book = optBook.get();
    HistoryItem lastRent = new LinkedList<>(item.getHistory().getRents()).getLast();

    Label nameLabel = new Label("Title");
    Label nameFieldLabel = new Label(book.getTitle());

    Label rentedFromLabel = new Label("Rented from");
    Label rentedFromFieldLabel = new Label(lastRent.getRentedFrom().toString());

    Label rentedToLabel = new Label("Rented to");
    Label rentedToFieldLabel = new Label(lastRent.getRentedTo().toString());

    pane.add(nameLabel, 0, 0);
    pane.add(nameFieldLabel, 1, 0);

    pane.add(rentedFromLabel, 0, 1);
    pane.add(rentedFromFieldLabel, 1, 1);

    pane.add(rentedToLabel, 0, 2);
    pane.add(rentedToFieldLabel, 1, 2);

    pane.getStyleClass().add("rentedpositionpane");
    getChildren().add(pane);
  }
}
