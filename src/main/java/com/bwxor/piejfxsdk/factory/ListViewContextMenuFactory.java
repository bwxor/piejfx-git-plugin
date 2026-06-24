package com.bwxor.piejfxsdk.factory;

import com.bwxor.piejfxsdk.state.ServiceState;
import com.bwxor.piejfxsdk.state.UIState;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class ListViewContextMenuFactory {
    public static ContextMenu createStagedListViewContextMenu() {
        ServiceState serviceState = ServiceState.instance;
        UIState uiState = UIState.instance;

        var contextMenu = new ContextMenu();

        var stageMenuItem = new MenuItem("Remove from staging area");
        stageMenuItem.setOnAction(_ -> {
            var selectedItems = uiState.getStagedListView().getSelectionModel().getSelectedItems();

            if (!selectedItems.isEmpty()) {
                selectedItems.forEach(
                        f -> serviceState.getGitService().removeFileFromStagingArea((String)f)
                );
            }
        });
        contextMenu.getItems().add(stageMenuItem);

        var rollbackMenuItem = new MenuItem("Rollback");
        rollbackMenuItem.setOnAction(_ -> {
            var selectedItems = uiState.getStagedListView().getSelectionModel().getSelectedItems();

            if (!selectedItems.isEmpty()) {
                serviceState.getGitService().rollbackFromStaged(selectedItems);
            }
        });
        contextMenu.getItems().add(rollbackMenuItem);

        return contextMenu;
    }

    public static ContextMenu createUnstagedListViewContextMenu() {
        ServiceState serviceState = ServiceState.instance;
        UIState uiState = UIState.instance;

        var contextMenu = new ContextMenu();

        var unstageMenuItem = new MenuItem("Add to staging area");
        unstageMenuItem.setOnAction(_ -> {
            var selectedItems = uiState.getUnstagedListView().getSelectionModel().getSelectedItems();

            if (!selectedItems.isEmpty()) {
                selectedItems.forEach(
                        f -> serviceState.getGitService().addFileToStagingArea((String)f)
                );
            }
        });
        contextMenu.getItems().add(unstageMenuItem);

        var rollbackMenuItem = new MenuItem("Rollback");
        rollbackMenuItem.setOnAction(_ -> {
            var selectedItems = uiState.getUnstagedListView().getSelectionModel().getSelectedItems();

            if (!selectedItems.isEmpty()) {
                serviceState.getGitService().rollbackFromUnstaged(selectedItems);
            }
        });
        contextMenu.getItems().add(rollbackMenuItem);

        return contextMenu;
    }
}
