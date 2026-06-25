package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.dto.CloneViewResponse;
import com.bwxor.piejfxsdk.type.CloneViewDialogChoice;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class GitCloneViewController extends MovableViewController {
    @FXML
    private Label windowTitle;
    @FXML
    private TextField repoTextArea;
    @FXML
    private TextField destinationTextArea;
    @FXML
    private Button buttonClone;

    private CloneViewResponse cloneViewResponse;

    public void setWindowTitle(String windowTitle) {
        this.windowTitle.setText(windowTitle);
    }

    public void initialize() {
        buttonClone.setDisable(true);
        Platform.runLater(() -> repoTextArea.requestFocus());
    }

    public CloneViewResponse getCloneViewResponse() {
        return cloneViewResponse;
    }

    public void setCloneViewResponse(CloneViewResponse cloneViewResponse) {
        this.cloneViewResponse = cloneViewResponse;
    }

    @FXML
    public void onCloseButtonClick() {
        cloneViewResponse = new CloneViewResponse(CloneViewDialogChoice.CANCEL, null, null);
        ((Stage) repoTextArea.getScene().getWindow()).close();
    }

    @FXML
    public void onCloneButtonClick() {
        cloneViewResponse = new CloneViewResponse(CloneViewDialogChoice.OK, repoTextArea.getText(), destinationTextArea.getText());
        ((Stage) repoTextArea.getScene().getWindow()).close();
    }

    @FXML
    public void onCancelButtonClick() {
        cloneViewResponse = new CloneViewResponse(CloneViewDialogChoice.CANCEL, null, null);
        ((Stage) repoTextArea.getScene().getWindow()).close();
    }

    @FXML
    public void onTextAreaKeyTyped(KeyEvent event) {
        if (repoTextArea.getText().trim().isEmpty() || destinationTextArea.getText().trim().isEmpty()) {
            buttonClone.setDisable(true);
        }
        else {
            buttonClone.setDisable(false);
        }
    }

    @FXML
    public void onDestinationTextAreaKeyTyped(KeyEvent event) {
        if (repoTextArea.getText().trim().isEmpty() || destinationTextArea.getText().trim().isEmpty()) {
            buttonClone.setDisable(true);
        }
        else {
            buttonClone.setDisable(false);
        }
    }

    @FXML
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            if (!buttonClone.isDisabled()) {
                cloneViewResponse = new CloneViewResponse(CloneViewDialogChoice.OK, repoTextArea.getText(), destinationTextArea.getText());
                ((Stage) repoTextArea.getScene().getWindow()).close();
            }
        }
    }
}
