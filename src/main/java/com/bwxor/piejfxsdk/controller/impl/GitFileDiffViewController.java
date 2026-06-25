package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class GitFileDiffViewController extends MovableViewController {

    @FXML
    private Label windowTitle;

    @FXML
    private Label oldLabel;

    @FXML
    private Label newLabel;

    @FXML
    private TextArea oldContent;

    @FXML
    private TextArea newContent;

    public void setWindowTitle(String title) {
        this.windowTitle.setText(title);
    }

    public void setOldContent(String label, String content) {
        oldLabel.setText(label);
        oldContent.setText(content);
    }

    public void setNewContent(String label, String content) {
        newLabel.setText(label);
        newContent.setText(content);
    }

    @FXML
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ESCAPE)) {
            ((Stage) closeButton.getScene().getWindow()).close();
        }
    }
}
