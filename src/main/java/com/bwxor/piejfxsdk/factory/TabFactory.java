package com.bwxor.piejfxsdk.factory;

import com.bwxor.piejfxsdk.handler.ListViewDoubleClickEventHandler;
import com.bwxor.piejfxsdk.state.UIState;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TabFactory {
    public static Tab createGitTab() {
        Tab tab = new Tab("Git");

        SplitPane mainSplitPane = new SplitPane();
        mainSplitPane.setOrientation(Orientation.VERTICAL);
        tab.setContent(mainSplitPane);

        mainSplitPane.getItems().add(createListViewsScrollPane());


        VBox commitVBox = new VBox();
        TextArea commitMessageTextArea = new TextArea();
        commitMessageTextArea.setPromptText("Commit message...");
        commitVBox.getChildren().add(commitMessageTextArea);

        HBox commitHBox = new HBox();
        Button commitButton = new Button("Commit");
        commitHBox.getChildren().add(commitButton);
        commitVBox.getChildren().add(commitHBox);

        mainSplitPane.getItems().add(commitVBox);

        return tab;
    }

    private static ScrollPane createListViewsScrollPane() {
        ScrollPane listViewsScrollPane = new ScrollPane();

        listViewsScrollPane.setContent(createListViewsVBox());
        listViewsScrollPane.setFitToWidth(true);

        return listViewsScrollPane;
    }

    private static VBox createListViewsVBox() {
        VBox listViewsVBox = new VBox();

        listViewsVBox.getChildren().add(createStagedVBox());
        listViewsVBox.getChildren().add(createUnstagedVBox());

        return listViewsVBox;
    }

    private static VBox createStagedVBox() {
        VBox stagedVBox = new VBox();
        stagedVBox.setPadding(new Insets(5));

        Label stagedLabel = new Label("Staging Area (to be committed)");
        stagedLabel.setPadding(new Insets(0, 0, 3, 0));

        stagedVBox.getChildren().add(stagedLabel);
        stagedVBox.getChildren().add(createStagedListView());

        return stagedVBox;
    }

    private static ListView createStagedListView() {
        UIState uiState = UIState.instance;

        ListView stagedListView = new ListView();

        stagedListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        stagedListView.setMaxHeight(7 * 24 + 2);
        stagedListView.setPrefHeight(7 * 24 + 2);
        stagedListView.setOnMouseClicked(e -> ListViewDoubleClickEventHandler.handle(e, stagedListView));
        stagedListView.setContextMenu(ListViewContextMenuFactory.createStagedListViewContextMenu());

        uiState.setStagedListView(stagedListView);

        return stagedListView;
    }

    private static VBox createUnstagedVBox() {
        VBox unstagedVBox = new VBox();
        unstagedVBox.setPadding(new Insets(5));

        Label unstagedLabel = new Label("Unstaged");
        unstagedLabel.setPadding(new Insets(0, 0, 3, 0));
        unstagedVBox.getChildren().add(unstagedLabel);
        unstagedVBox.getChildren().add(createUnstagedListView());

        return unstagedVBox;
    }

    private static ListView createUnstagedListView() {
        UIState uiState = UIState.instance;

        ListView unstagedListView = new ListView();
        unstagedListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        unstagedListView.setMaxHeight(7 * 24 + 2);
        unstagedListView.setPrefHeight(7 * 24 + 2);
        unstagedListView.setOnMouseClicked(e -> ListViewDoubleClickEventHandler.handle(e, unstagedListView));
        unstagedListView.setContextMenu(ListViewContextMenuFactory.createUnstagedListViewContextMenu());

        uiState.setUnstagedListView(unstagedListView);

        return unstagedListView;
    }
}
