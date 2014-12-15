package me.gregorias.xmllibrary.interfaces.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import me.gregorias.xmllibrary.library.LibraryFacade;
import me.gregorias.xmllibrary.library.jaxb.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Registration request handler.
 */
public class RegisterRequestHandler implements EventHandler<ActionEvent> {
  private static final Logger LOGGER = LoggerFactory.getLogger(RegisterRequestHandler.class);
  private final LibraryFacade mFacade;

  public RegisterRequestHandler(LibraryFacade facade) {
    mFacade = facade;
  }

  @Override
  public void handle(ActionEvent event) {
    LOGGER.debug("handle()");
    RegisterDialog dialog = new RegisterDialog();
    boolean hasDialogBeenProperlyHandledByUser = false;
    while (!hasDialogBeenProperlyHandledByUser) {
      Optional<RegisterDialog.RegistrationInformation> optionalResult = dialog.showAndWait();
      if (optionalResult.isPresent()) {
        RegisterDialog.RegistrationInformation information = optionalResult.get();
        User user = parseRegistrationInformation(information);
        if (user == null) {
          continue;
        }

        mFacade.acquireLock();
        try {
          mFacade.registerNewUser(user);
          showRegistrationSuccessDialog(user.getId());
        } finally {
          mFacade.releaseLock();
        }
        hasDialogBeenProperlyHandledByUser = true;
      } else {
        hasDialogBeenProperlyHandledByUser = true;
      }
    }
  }

  private static User parseRegistrationInformation(
      RegisterDialog.RegistrationInformation information) {
    User user = new User();
    if (information.mName.length() == 0) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setHeaderText("Name needs to be provided.");
      alert.showAndWait();
      return null;
    }
    user.setName(information.mName);

    DatatypeFactory factory;
    try {
      factory = DatatypeFactory.newInstance();
    } catch (DatatypeConfigurationException e) {
      LOGGER.error("parseRegistrationInformation(): Unexpected exception.", e);
      return null;
    }
    XMLGregorianCalendar  calendar;
    try {
      calendar = factory.newXMLGregorianCalendar(information.mBirthday);
    } catch (IllegalArgumentException e) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setHeaderText("Invalid birthday provided.");
      alert.showAndWait();
      return null;
    }
    user.setBirthday(calendar);

    if (information.mPESEL.length() == 0) {
      if (information.mPassport.getId().length() == 0
          || information.mPassport.getCountry().length() == 0) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Provide valid PESEL or passport.");
        alert.showAndWait();
        return null;
      }
      user.setPassport(information.mPassport);
    } else {
      boolean matches = Pattern.matches("\\d{11}", information.mPESEL);
      if (!matches) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Invalid PESEL provided.");
        alert.showAndWait();
        return null;
      }
      user.setPESEL(information.mPESEL);
    }

    if (information.mAddress.getCountry().length() == 0
        || information.mAddress.getNumber().length() == 0
        || information.mAddress.getStreet().length() == 0
        || information.mAddress.getZipCode().length() == 0
        || information.mAddress.getCity().length() == 0) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setHeaderText("Invalid Address provided.");
      alert.showAndWait();
      return null;
    }
    user.setAddress(information.mAddress);

    return user;
  }

  private void showRegistrationSuccessDialog(int id) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setHeaderText("Registration was successful. Your new id is: " + id);
    alert.showAndWait();
  }
}
