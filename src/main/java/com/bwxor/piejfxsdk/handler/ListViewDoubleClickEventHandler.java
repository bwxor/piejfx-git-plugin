package com.bwxor.piejfxsdk.handler;

import com.bwxor.piejfxsdk.state.RepositoryState;
import com.bwxor.piejfxsdk.state.ServiceState;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.nio.file.Paths;

public class ListViewDoubleClickEventHandler {
    public static void handle(MouseEvent mouseEvent, ListView<String> listView) {
        ServiceState serviceState = ServiceState.instance;
        RepositoryState repositoryState = RepositoryState.instance;

        if (mouseEvent.getClickCount() == 2) {
            String selectedItem = listView.getSelectionModel().getSelectedItem();

            if (selectedItem != null) {
                if (selectedItem.startsWith("✖ ")) {
                    serviceState.getNotificationService().showNotificationOk("You can't open a deleted file.");
                }
                else {
                    serviceState.getFileService().openFile(
                            Paths.get(repositoryState.getOpenedFolder().toPath().toString(), selectedItem.substring(2)).toFile()
                    );
                }
            }
        }
    }
}
