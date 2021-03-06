package me.gregorias.xmllibrary.interfaces.gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import me.gregorias.xmllibrary.library.BookStatus;
import me.gregorias.xmllibrary.library.LibraryFacade;
import me.gregorias.xmllibrary.library.jaxb.Book;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * Created by grzesiek on 14.12.14.
 */
public class BookShelf extends GridPane {
  private static final ColumnConstraints FIRST_COLUMN_CONSTRAINTS =
      new ColumnConstraints(100, 100, 100);
  public static GridPane createBookPane(LibraryFacade facade, Book book) {
    GridPane pane = new GridPane();
    pane.setHgap(10);
    pane.setVgap(10);

    pane.getColumnConstraints().add(FIRST_COLUMN_CONSTRAINTS);

    Label titleLabel = new Label("Title");
    Label titleFieldLabel = new Label(book.getTitle());
    titleFieldLabel.setWrapText(true);

    Label descriptionLabel = new Label("Description");
    Label descriptionFieldLabel = new Label(book.getDescription().trim());
    descriptionFieldLabel.setWrapText(true);

    Label authorsLabel = new Label("Authors");
    Label authorsFieldLabel = new Label(calculateAuthorsString(book));

    Label isbn10Label = new Label("ISBN10");
    Label isbn10FieldLabel = new Label(book.getIsbn10());

    Label editionLabel = new Label("Edition");
    Label editionFieldLabel = new Label(book.getEdition());

    Label publisherLabel = new Label("Publisher");
    Label publishedFieldLabel = new Label(book.getPublisher());

    Label statusLabel = new Label("Status");
    BookStatus bookStatus = getBookStatus(facade, book);
    Label statusFieldLabel = new Label(bookStatus.toString());

    Button rentButton = new Button("Rent this book");
    rentButton.setOnAction(new RentBookRequestHandler(facade, book));

    pane.add(titleLabel, 0, 0);
    pane.add(titleFieldLabel, 1, 0);
    pane.add(descriptionLabel, 0, 1);
    pane.add(descriptionFieldLabel, 1, 1);
    pane.add(authorsLabel, 0, 2);
    pane.add(authorsFieldLabel, 1, 2);
    pane.add(isbn10Label, 0, 3);
    pane.add(isbn10FieldLabel, 1, 3);
    pane.add(editionLabel, 0, 4);
    pane.add(editionFieldLabel, 1, 4);
    pane.add(publisherLabel, 0, 5);
    pane.add(publishedFieldLabel, 1, 5);
    pane.add(statusLabel, 0, 6);
    pane.add(statusFieldLabel, 1, 6);

    if (facade.isLoggedIn()
        && facade.getCurrentLoggedInAccount().isNormalUser()
        && bookStatus.isRentable()
        && !facade.isBookRentedByUser(facade.getCurrentLoggedInAccount().getUser(), book)) {
      pane.add(rentButton, 0, 7, 2, 1);
    }


    pane.getStyleClass().addAll("listitem", "bookpane");

    return pane;
  }

  public static Pane createBookShelfPane(LibraryFacade facade, Collection<Book> books) {
    VBox bookShelf = new VBox(10);

    for (Book book : books) {
      bookShelf.getChildren().add(createBookPane(facade, book));
    }
    bookShelf.getStyleClass().add("toplist");
    return bookShelf;
  }

  private static String calculateAuthorsString(Book book) {
    List<String> authors = book.getAuthors().getAuthors();
    return StringUtils.join(authors, ',');
  }

  private static BookStatus getBookStatus(LibraryFacade facade, Book book) {
    facade.acquireLock();
    try {
      return facade.getBookStatus(book);
    } finally {
      facade.releaseLock();
    }
  }
}
