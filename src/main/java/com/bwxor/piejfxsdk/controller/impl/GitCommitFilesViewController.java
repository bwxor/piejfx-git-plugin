package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.dto.CommitFilesEntry;
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
import org.eclipse.jgit.diff.DiffEntry;

import java.util.List;

public class GitCommitFilesViewController extends MovableViewController {

    @FXML
    private Label windowTitle;

    @FXML
    private ListView<CommitFilesEntry> filesListView;

    public void initialize() {
        filesListView.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(CommitFilesEntry item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("[%s]  %s", item.status(), item.path()));
                }
            }
        });

        filesListView.setOnMouseClicked(this::onListMouseClicked);
    }

    private void onListMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            CommitFilesEntry selected = filesListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                DiffEntry.ChangeType changeType = DiffEntry.ChangeType.valueOf(selected.status());
                ServiceState.instance.getGitService()
                        .showFileDiff(selected.commitFullHash(), selected.path(), changeType);
            }
        }
    }

    public void setWindowTitle(String title) {
        this.windowTitle.setText(title);
    }

    public void setFiles(List<CommitFilesEntry> files) {
        filesListView.setItems(FXCollections.observableArrayList(files));
    }

    @FXML
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ESCAPE)) {
            ((Stage) closeButton.getScene().getWindow()).close();
        }
    }
}
