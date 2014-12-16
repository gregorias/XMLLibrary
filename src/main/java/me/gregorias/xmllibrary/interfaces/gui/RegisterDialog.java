package me.gregorias.xmllibrary.interfaces.gui;

import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import me.gregorias.xmllibrary.library.jaxb.Address;
import me.gregorias.xmllibrary.library.jaxb.Passport;

public class RegisterDialog extends Dialog<RegisterDialog.RegistrationInformation> {
  private final GridPane mGrid;
  private final LabelTextFieldPair mNamePair;
  private final LabelTextFieldPair mBirthdayPair;
  private final LabelTextFieldPair mPESELPair;
  private final LabelTextFieldPair mPassportIdPair;
  private final LabelTextFieldPair mPassportCountryPair;

  private final LabelTextFieldPair mAddressStreetPair;
  private final LabelTextFieldPair mAddressNumberPair;
  private final LabelTextFieldPair mAddressZipCodePair;
  private final LabelTextFieldPair mAddressCityPair;
  private final LabelTextFieldPair mAddressCountryPair;

  public RegisterDialog() {
    final DialogPane dialogPane = getDialogPane();

    mNamePair = createLabelTextFieldPair("Name");
    mBirthdayPair = createLabelTextFieldPair("Birthday");
    mPESELPair = createLabelTextFieldPair("PESEL");
    mPassportIdPair = createLabelTextFieldPair("Passport Id");
    mPassportCountryPair = createLabelTextFieldPair("Passport's country");
    mAddressStreetPair = createLabelTextFieldPair("Street");
    mAddressNumberPair = createLabelTextFieldPair("Street number");
    mAddressZipCodePair = createLabelTextFieldPair("Zip code");
    mAddressCityPair = createLabelTextFieldPair("City");
    mAddressCountryPair = createLabelTextFieldPair("Country");

    mGrid = new GridPane();
    mGrid.setHgap(10);
    mGrid.setVgap(10);
    mGrid.setMaxWidth(Double.MAX_VALUE);
    mGrid.setAlignment(Pos.CENTER_LEFT);

    dialogPane.contentTextProperty().addListener(o -> updateGrid());

    setTitle("User registration");
    dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    updateGrid();

    setResultConverter((dialogButton) -> dialogButton == ButtonType.OK
        ? convertTextFieldsContentToRegistrationInformation()
        : null);
  }

  public static class RegistrationInformation {
    public String mName;
    public String mBirthday;
    public String mPESEL;
    public Passport mPassport;
    public Address mAddress;

  }

  private void addPairToGrid(LabelTextFieldPair pair, int row) {
    mGrid.add(pair.mLabel, 0, row);
    mGrid.add(pair.mTextField, 1, row);
  }

  private RegistrationInformation convertTextFieldsContentToRegistrationInformation() {
    RegistrationInformation information = new RegistrationInformation();
    information.mName = mNamePair.mTextField.getText();
    information.mBirthday = mBirthdayPair.mTextField.getText();
    information.mPESEL = mPESELPair.mTextField.getText();
    information.mPassport = new Passport();
    information.mPassport.setId(mPassportIdPair.mTextField.getText());
    information.mPassport.setCountry(mPassportCountryPair.mTextField.getText());
    information.mAddress = new Address();
    information.mAddress.setStreet(mAddressStreetPair.mTextField.getText());
    information.mAddress.setNumber(mAddressNumberPair.mTextField.getText());
    information.mAddress.setZipCode(mAddressZipCodePair.mTextField.getText());
    information.mAddress.setCity(mAddressCityPair.mTextField.getText());
    information.mAddress.setCountry(mAddressCountryPair.mTextField.getText());
    return information;
  }

  private LabelTextFieldPair createLabelTextFieldPair(String name) {
    LabelTextFieldPair pair = LabelTextFieldPair.create(name);
    GridPane.setHgrow(pair.mTextField, Priority.ALWAYS);
    GridPane.setFillWidth(pair.mTextField, true);
    return pair;
  }

  private void updateGrid() {
    mGrid.getChildren().clear();
    addPairToGrid(mNamePair, 0);
    addPairToGrid(mBirthdayPair, 1);
    mGrid.add(new Label("Identification"), 0, 2);
    addPairToGrid(mPESELPair, 3);
    mGrid.add(new Label("or"), 1, 4);
    addPairToGrid(mPassportIdPair, 5);
    addPairToGrid(mPassportCountryPair, 6);
    mGrid.add(new Label("Address"), 0, 8);
    addPairToGrid(mAddressStreetPair, 9);
    addPairToGrid(mAddressNumberPair, 10);
    addPairToGrid(mAddressZipCodePair, 11);
    addPairToGrid(mAddressCityPair, 12);
    addPairToGrid(mAddressCountryPair, 13);
    getDialogPane().setContent(mGrid);
  }
}

