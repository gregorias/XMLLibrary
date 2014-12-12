package me.gregorias.xmllibrary.interfaces.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import me.gregorias.xmllibrary.library.LibraryFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler of logout request.
 */
public class LogoutRequestHandler implements EventHandler<ActionEvent> {
  private static final Logger LOGGER = LoggerFactory.getLogger(LogoutRequestHandler.class);
  private final LibraryFacade mFacade;

  public LogoutRequestHandler(LibraryFacade facade) {
    mFacade = facade;
  }

  @Override
  public void handle(ActionEvent event) {
    LOGGER.debug("handle()");
    mFacade.logout();
    mFacade.setChanged();
    mFacade.notifyObservers();
  }
}

