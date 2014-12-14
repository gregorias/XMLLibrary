package me.gregorias.xmllibrary.interfaces.gui;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

/**
 * Created by grzesiek on 14.12.14.
 */
public class Utils {
  public static ScrollPane wrapNodeInVerticalScrollPane(Node node) {
    ScrollPane sp = new ScrollPane();
    sp.setContent(node);
    sp.setFitToWidth(true);
    sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    return sp;
  }
}