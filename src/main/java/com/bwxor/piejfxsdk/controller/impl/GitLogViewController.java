package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.dto.CommitLogEntry;
import com.bwxor.piejfxsdk.state.ServiceState;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.List;

public class GitLogViewController extends MovableViewController {

    @FXML
    private Label windowTitle;

    @FXML
    private ListView<CommitLogEntry> commitListView;

    public void initialize() {
        commitListView.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(CommitLogEntry item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("[%s] %s — %s  (%s)",
                            item.hash(), item.date(), item.message(), item.author()));
                }
            }
        });
    }

    @FXML
    public void onListMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            CommitLogEntry selected = commitListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                ServiceState.instance.getGitService().showCommitFiles(selected.fullHash());
            }
        }
    }

    public void setWindowTitle(String title) {
        this.windowTitle.setText(title);
    }

    public void setCommits(List<CommitLogEntry> commits) {
        commitListView.setItems(FXCollections.observableArrayList(commits));
    }

    @FXML
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ESCAPE)) {
            ((Stage) closeButton.getScene().getWindow()).close();
        }
    }
}
