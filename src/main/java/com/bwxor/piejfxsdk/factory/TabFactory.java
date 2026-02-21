package com.bwxor.piejfxsdk.factory;

import com.bwxor.piejfxsdk.state.UIState;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

public class TabFactory {
    public static Tab createGitTab() {
        UIState uiState = UIState.instance;

        Tab tab = new Tab("Git");

        VBox vBox = new VBox();
        tab.setContent(vBox);

        ListView stagedListView = new ListView();
        uiState.setStagedListView(stagedListView);
        vBox.getChildren().add(stagedListView);

        ListView unstagedListView = new ListView();
        uiState.setUnstagedListView(unstagedListView);
        vBox.getChildren().add(unstagedListView);

        return tab;
    }
}
