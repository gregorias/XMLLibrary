package me.gregorias.xmllibrary.interfaces.gui;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

/**
 * Created by grzesiek on 13.12.14.
 */
public class LabelTextFieldPair {
  public Label mLabel;
  public TextField mTextField;

  public LabelTextFieldPair(Label label, TextField textField) {
    mLabel = label;
    mTextField = textField;
  }

  public static LabelTextFieldPair create(String name) {
    Label label = new  Label(name);

    label.setMaxHeight(Double.MAX_VALUE);
    label.setMaxWidth(Double.MAX_VALUE);
    label.setWrapText(true);
    label.setPrefWidth(Region.USE_COMPUTED_SIZE);

    TextField textField = new  TextField("");
    textField.setMaxWidth(Double.MAX_VALUE);
    return new LabelTextFieldPair(label, textField);
  }
}
