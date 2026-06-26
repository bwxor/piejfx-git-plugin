package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.state.ServiceState;
import com.bwxor.piejfxsdk.validator.GitBranchNameValidator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class GitNewBranchViewController extends MovableViewController {

    @FXML
    private Label windowTitle;

    @FXML
    private TextField branchNameTextField;

    @FXML
    private Label existsWarningLabel;

    @FXML
    private Label illegalWarningLabel;

    @FXML
    private Button createButton;

    public void initialize() {
        createButton.setDisable(true);
        existsWarningLabel.setVisible(false);
        illegalWarningLabel.setVisible(false);
        Platform.runLater(() -> branchNameTextField.requestFocus());
    }

    public void setWindowTitle(String title) {
        this.windowTitle.setText(title);
    }

    @FXML
    public void onBranchNameKeyTyped() {
        String name = branchNameTextField.getText().trim();
        boolean empty = name.isEmpty();
        boolean illegal = !empty && !GitBranchNameValidator.isValid(name);
        boolean exists = !empty && !illegal && ServiceState.instance.getGitService().branchExists(name);

        existsWarningLabel.setVisible(exists);
        illegalWarningLabel.setVisible(illegal);
        createButton.setDisable(empty || exists || illegal);
    }

    @FXML
    public void onCreateButtonClick() {
        String name = branchNameTextField.getText().trim();
        if (!name.isEmpty() && GitBranchNameValidator.isValid(name) && !ServiceState.instance.getGitService().branchExists(name)) {
            ServiceState.instance.getGitService().createAndCheckoutBranch(name);
            ((Stage) closeButton.getScene().getWindow()).close();
        }
    }

    @FXML
    public void onCancelButtonClick() {
        ((Stage) closeButton.getScene().getWindow()).close();
    }

    @FXML
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ESCAPE)) {
            ((Stage) closeButton.getScene().getWindow()).close();
        } else if (event.getCode().equals(KeyCode.ENTER)) {
            if (!createButton.isDisabled()) {
                onCreateButtonClick();
            }
        }
    }
}
