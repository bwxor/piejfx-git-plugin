package com.bwxor.piejfxsdk.service;

import com.bwxor.piejfxsdk.controller.impl.GitLogViewController;
import com.bwxor.piejfxsdk.dto.CommitLogEntry;
import com.bwxor.piejfxsdk.state.ServiceState;
import com.bwxor.piejfxsdk.state.StylesheetState;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

public class GitLogViewService {
    public void showGitLogWindow(List<CommitLogEntry> commits) {
        ServiceState serviceState = ServiceState.instance;
        StylesheetState stylesheetState = StylesheetState.instance;

        FXMLLoader loader = new FXMLLoader(serviceState.getResourceService().getResourceByName("views/git-log-view.fxml"));
        loader.setClassLoader(GitLogViewController.class.getClassLoader());
        Parent root;

        try {
            root = loader.load();

            GitLogViewController controller = loader.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(stylesheetState.getThemeURL().toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Commit Log");
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().add(stylesheetState.getDefaultStylesheetURL().toExternalForm());
            controller.setWindowTitle("Commit Log");
            controller.setCommits(commits);
            stage.showAndWait();

        } catch (IOException e) {
            serviceState.getNotificationService().showNotificationOk("Error while trying to load the commit log window.");
            e.printStackTrace();
        }
    }
}
