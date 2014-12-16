package me.gregorias.xmllibrary.library;

import me.gregorias.xmllibrary.library.jaxb.Book;
import me.gregorias.xmllibrary.library.jaxb.HistoryItem;
import me.gregorias.xmllibrary.library.jaxb.Item;
import me.gregorias.xmllibrary.library.jaxb.User;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.LinkedList;

/**
 * Triple for book.
 */
public class BookItemUser {
  public final Book mBook;
  public final Item mItem;
  public final User mUser;
  public BookItemUser(Book book, Item item, User user) {
    mBook = book;
    mItem = item;
    mUser = user;
  }

  public BookStatus getBookStatus() {
    switch (mItem.getStatus()) {
      case AVAILABLE:
        return new BookStatus(BookStatus.Status.AVAILABLE);
      case IN_STORE:
        return new BookStatus(BookStatus.Status.IN_STORE);
      case RENTED:
        LinkedList<HistoryItem> rents = new LinkedList<>(mItem.getHistory().getRents());
        XMLGregorianCalendar rentedTo = rents.getLast().getRentedTo();
        return new BookStatus(BookStatus.Status.RENTED, rentedTo);
      default:
        return new BookStatus(BookStatus.Status.UNAVAILABLE);
    }
  }
}
