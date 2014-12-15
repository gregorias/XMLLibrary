package me.gregorias.xmllibrary.interfaces.gui;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import me.gregorias.xmllibrary.library.LibraryFacade;
import me.gregorias.xmllibrary.library.jaxb.Address;
import me.gregorias.xmllibrary.library.jaxb.User;

/**
 * Created by grzesiek on 14.12.14.
 */
public class ProfileInformationPane extends GridPane {
  public ProfileInformationPane(LibraryFacade facade) {
    User user = facade.getCurrentLoggedInAccount().getUser();
    setHgap(10);
    setVgap(10);

    Label nameLabel = new Label("Name");
    Label nameFieldLabel = new Label(user.getName());

    Label birthdayLabel = new Label("Birthday");
    Label birthdayFieldLabel = new Label(user.getBirthday().toString());

    Address address = user.getAddress();
    Label addressLabel = new Label("Address");
    Label addressFieldLabel = new Label(String.format("%s %s%n%s %s%n%s",
        address.getStreet(),
        address.getNumber(),
        address.getZipCode(),
        address.getCity(),
        address.getCountry()));

    Label accountValidToLabel = new Label("Account valid to");
    Label accountValidToFieldLabel = new Label(user.getValidTo());

    add(nameLabel, 0, 0);
    add(nameFieldLabel, 1, 0);
    add(birthdayLabel, 0, 1);
    add(birthdayFieldLabel, 1, 1);
    add(addressLabel, 0, 2);
    add(addressFieldLabel, 1, 2);
    add(accountValidToLabel, 0, 3);
    add(accountValidToFieldLabel, 1, 3);

    getStyleClass().add("profileinfo");
  }
}