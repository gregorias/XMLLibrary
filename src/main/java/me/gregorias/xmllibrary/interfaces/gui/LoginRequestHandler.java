package me.gregorias.xmllibrary.interfaces.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import me.gregorias.xmllibrary.library.LibraryFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Handler of login request.
 */
public class LoginRequestHandler implements EventHandler<ActionEvent> {
  private static final Logger LOGGER = LoggerFactory.getLogger(LoginRequestHandler.class);
  private final LibraryFacade mFacade;
  public LoginRequestHandler(LibraryFacade facade) {
    mFacade = facade;
  }

  @Override
  public void handle(ActionEvent event) {
    LOGGER.debug("handle()");
    LoginDialog dialog = new LoginDialog();
    boolean hasDialogBeenProperlyHandledByUser = false;
    while (!hasDialogBeenProperlyHandledByUser) {
      Optional<LoginDialog.LoginDialogResult> optionalResult = dialog.showAndWait();
      if (optionalResult.isPresent()) {
        LoginDialog.LoginDialogResult result = optionalResult.get();
        if (result.mIsUser) {
          try {
            int id = Integer.parseInt(result.mId);
            boolean wasLoginSuccessful = mFacade.loginAsUser(id);
            if (!wasLoginSuccessful) {
              Alert alert = new Alert(Alert.AlertType.WARNING);
              alert.setHeaderText("Unknown user, could not log in.");
              alert.showAndWait();
            }
          } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("User id should be a nonnegative number.");
            alert.showAndWait();
          }
        } else {
          mFacade.loginAsLibrarian();
        }
        mFacade.setChanged();
        mFacade.notifyObservers();
        hasDialogBeenProperlyHandledByUser = true;
      } else {
        hasDialogBeenProperlyHandledByUser = true;
      }
    }
  }
}
