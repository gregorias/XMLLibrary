package me.gregorias.xmllibrary.interfaces.gui;

import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class LoginDialog extends Dialog<LoginDialog.LoginDialogResult> {
  private final GridPane mGrid;
  private final RadioButton mLibrarianButton = new RadioButton("Login as a Librarian");
  private final RadioButton mUserButton = new RadioButton("Login as a user");
  private final Label mIdLabel;
  private final TextField mIdTextField;

  public LoginDialog() {
    final DialogPane dialogPane = getDialogPane();

    final ToggleGroup group = new ToggleGroup();

    mLibrarianButton.setToggleGroup(group);
    mLibrarianButton.setSelected(true);

    mUserButton.setToggleGroup(group);

    mIdTextField = new TextField("");
    mIdTextField.setMaxWidth(Double.MAX_VALUE);
    mIdTextField.setDisable(true);
    GridPane.setHgrow(mIdTextField, Priority.ALWAYS);
    GridPane.setFillWidth(mIdTextField, true);

    mIdLabel = new Label("Id");
    mIdLabel.setMaxHeight(Double.MAX_VALUE);
    mIdLabel.setWrapText(true);
    mIdLabel.setPrefWidth(360);
    mIdLabel.setPrefWidth(Region.USE_COMPUTED_SIZE);

    mGrid = new GridPane();
    mGrid.setHgap(10);
    mGrid.setVgap(10);
    mGrid.setMaxWidth(Double.MAX_VALUE);
    mGrid.setAlignment(Pos.CENTER_LEFT);

    mUserButton.selectedProperty().addListener((obs, oldVal, newVal) ->
        mIdTextField.setDisable(!newVal));

    dialogPane.contentTextProperty().addListener(o -> updateGrid());

    setTitle("Login");
    dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    updateGrid();

    setResultConverter((dialogButton) -> dialogButton == ButtonType.OK
        ? (mLibrarianButton.isSelected()
          ? new LoginDialogResult()
          : new LoginDialogResult(mIdTextField.getText()))
        : null);
  }

  public static class LoginDialogResult {
    public boolean mIsUser;
    public String mId;

    public LoginDialogResult() {
      mIsUser = false;
      mId = "";
    }

    public LoginDialogResult(String id) {
      mIsUser = true;
      mId = id;
    }
  }

  private void updateGrid() {
    mGrid.getChildren().clear();
    mGrid.add(mLibrarianButton, 0, 0, 2, 1);
    mGrid.add(mUserButton, 0, 1, 2, 1);
    mGrid.add(mIdLabel, 0, 2);
    mGrid.add(mIdTextField, 1, 2);
    getDialogPane().setContent(mGrid);
  }
}
