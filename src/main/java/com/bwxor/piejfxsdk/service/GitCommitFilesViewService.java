package com.bwxor.piejfxsdk.service;

import com.bwxor.piejfxsdk.controller.impl.GitCommitFilesViewController;
import com.bwxor.piejfxsdk.dto.CommitFilesEntry;
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

public class GitCommitFilesViewService {
    public void showCommitFilesWindow(String commitHash, List<CommitFilesEntry> files) {
        ServiceState serviceState = ServiceState.instance;
        StylesheetState stylesheetState = StylesheetState.instance;

        FXMLLoader loader = new FXMLLoader(serviceState.getResourceService().getResourceByName("views/git-commit-files-view.fxml"));
        loader.setClassLoader(GitCommitFilesViewController.class.getClassLoader());
        Parent root;

        try {
            root = loader.load();

            GitCommitFilesViewController controller = loader.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(stylesheetState.getThemeURL().toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Changed Files — " + commitHash);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().add(stylesheetState.getDefaultStylesheetURL().toExternalForm());
            controller.setWindowTitle("Changed Files — " + commitHash);
            controller.setFiles(files);
            stage.showAndWait();

        } catch (IOException e) {
            serviceState.getNotificationService().showNotificationOk("Error while trying to load the commit files window.");
            e.printStackTrace();
        }
    }
}
