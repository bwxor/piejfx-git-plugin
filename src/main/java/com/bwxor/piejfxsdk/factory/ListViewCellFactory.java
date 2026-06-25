package com.bwxor.piejfxsdk.factory;

import javafx.scene.control.ListCell;

public class ListViewCellFactory extends ListCell<String> {
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
            setStyle("");
        } else {
            setText(item);

            if (item.startsWith("✚ ")) {
                setStyle("-fx-text-fill: #42E3A7;");
            } else if (item.startsWith("✖ ")) {
                setStyle("-fx-text-fill: #E3425A;");
            } else {
                setStyle("");
            }
        }
    }
}
