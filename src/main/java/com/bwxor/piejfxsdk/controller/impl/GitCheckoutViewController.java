package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.state.ServiceState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.List;

public class GitCheckoutViewController extends MovableViewController {

    @FXML
    private Label windowTitle;

    @FXML
    private TextField filterTextField;

    @FXML
    private ListView<String> branchListView;

    private ObservableList<String> allBranches;

    public void initialize() {
        allBranches = FXCollections.observableArrayList();
        branchListView.setItems(allBranches);

        filterTextField.textProperty().addListener((_, _, newValue) -> {
            if (newValue == null || newValue.isBlank()) {
                branchListView.setItems(allBranches);
            } else {
                String lower = newValue.toLowerCase();
                ObservableList<String> filtered = FXCollections.observableArrayList(
                        allBranches.stream()
                                .filter(b -> b.toLowerCase().contains(lower))
                                .toList()
                );
                branchListView.setItems(filtered);
            }
        });
    }

    public void setWindowTitle(String title) {
        this.windowTitle.setText(title);
    }

    public void setBranches(List<String> branches) {
        allBranches.setAll(branches.stream().filter(e -> !e.trim().equalsIgnoreCase("HEAD") && !e.trim().equalsIgnoreCase("remotes/origin/HEAD")).toList());
    }

    @FXML
    public void onListMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            String selected = branchListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                ServiceState.instance.getGitService().checkoutBranch(selected);
                ((Stage) closeButton.getScene().getWindow()).close();
            }
        }
    }

    @FXML
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ESCAPE)) {
            ((Stage) closeButton.getScene().getWindow()).close();
        }
    }
}
