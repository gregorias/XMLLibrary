package me.gregorias.xmllibrary.interfaces.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import me.gregorias.xmllibrary.library.LibraryFacade;
import me.gregorias.xmllibrary.library.jaxb.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by grzesiek on 14.12.14.
 */
public class RentBookRequestHandler implements EventHandler<ActionEvent> {
  private static final Logger LOGGER = LoggerFactory.getLogger(RentBookRequestHandler.class);
  private final LibraryFacade mFacade;
  private final Book mBook;
  public RentBookRequestHandler(LibraryFacade facade, Book book) {
    mFacade = facade;
    mBook = book;
  }

  @Override
  public void handle(ActionEvent event) {
    LOGGER.debug("handle()");
    mFacade.acquireLock();
    try {
      mFacade.rentABook(mBook);
      mFacade.setChanged();
      mFacade.notifyObservers();

    } finally {
      mFacade.releaseLock();
    }

  }
}
