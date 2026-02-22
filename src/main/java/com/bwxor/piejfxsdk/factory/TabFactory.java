package com.bwxor.piejfxsdk.factory;

import com.bwxor.piejfxsdk.handler.ListViewDoubleClickEventHandler;
import com.bwxor.piejfxsdk.state.ServiceState;
import com.bwxor.piejfxsdk.state.UIState;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TabFactory {
    public static Tab createGitTab() {
        Tab tab = new Tab("Git");

        SplitPane mainSplitPane = new SplitPane();
        mainSplitPane.setOrientation(Orientation.VERTICAL);
        mainSplitPane.setDividerPosition(0, 0.75);
        tab.setContent(mainSplitPane);

        mainSplitPane.getItems().add(createListViewsScrollPane());
        mainSplitPane.getItems().add(createCommitVBox());

        return tab;
    }

    private static VBox createCommitVBox() {
        UIState uiState = UIState.instance;

        VBox commitVBox = new VBox();

        TextArea commitMessageTextArea = new TextArea();
        commitMessageTextArea.setPromptText("Commit message...");
        uiState.setCommitMessageTextArea(commitMessageTextArea);
        commitVBox.getChildren().add(commitMessageTextArea);
        VBox.setVgrow(commitMessageTextArea, Priority.ALWAYS);
        commitVBox.getChildren().add(createCommitHBox());

        return commitVBox;
    }

    private static HBox createCommitHBox() {
        HBox commitHBox = new HBox();

        commitHBox.setPadding(new Insets(5, 10, 5, 10));
        commitHBox.getChildren().add(createCommitButton());
        commitHBox.getChildren().add(createCommitLogButton());

        return commitHBox;
    }

    private static ScrollPane createListViewsScrollPane() {
        ScrollPane listViewsScrollPane = new ScrollPane();

        listViewsScrollPane.setContent(createListViewsVBox());
        listViewsScrollPane.setFitToWidth(true);
        listViewsScrollPane.setFitToHeight(true);

        return listViewsScrollPane;
    }

    private static VBox createListViewsVBox() {
        VBox listViewsVBox = new VBox();

        var stagedVBox = createStagedVBox();
        var unstagedVBox = createUnstagedVBox();

        listViewsVBox.getChildren().add(stagedVBox);
        listViewsVBox.getChildren().add(unstagedVBox);

        VBox.setVgrow(stagedVBox, Priority.ALWAYS);
        VBox.setVgrow(unstagedVBox, Priority.ALWAYS);

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
        unstagedListView.setOnMouseClicked(e -> ListViewDoubleClickEventHandler.handle(e, unstagedListView));
        unstagedListView.setContextMenu(ListViewContextMenuFactory.createUnstagedListViewContextMenu());

        uiState.setUnstagedListView(unstagedListView);

        return unstagedListView;
    }

    private static Button createCommitButton() {
        ServiceState serviceState = ServiceState.instance;
        UIState uiState = UIState.instance;

        Button commitButton = new Button("Commit");

        commitButton.setOnAction(_ -> {
            serviceState.getGitService().commit(uiState.getCommitMessageTextArea().getText());
        });

        return commitButton;
    }

    private static Button createCommitLogButton() {
        Button commitLogButton = new Button("Commit log");

        return commitLogButton;
    }
}
