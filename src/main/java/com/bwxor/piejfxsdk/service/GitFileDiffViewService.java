package com.bwxor.piejfxsdk.service;

import com.bwxor.piejfxsdk.controller.impl.GitFileDiffViewController;
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

public class GitFileDiffViewService {
    public void showFileDiffWindow(String path, String oldLabel, String oldContent, String newLabel, String newContent) {
        ServiceState serviceState = ServiceState.instance;
        StylesheetState stylesheetState = StylesheetState.instance;

        FXMLLoader loader = new FXMLLoader(serviceState.getResourceService().getResourceByName("views/git-file-diff-view.fxml"));
        loader.setClassLoader(GitFileDiffViewController.class.getClassLoader());
        Parent root;

        try {
            root = loader.load();

            GitFileDiffViewController controller = loader.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(stylesheetState.getThemeURL().toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Diff — " + path);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().add(stylesheetState.getDefaultStylesheetURL().toExternalForm());
            controller.setWindowTitle("Diff — " + path);
            controller.setOldContent(oldLabel, oldContent);
            controller.setNewContent(newLabel, newContent);
            stage.showAndWait();

        } catch (IOException e) {
            serviceState.getNotificationService().showNotificationOk("Error while trying to load the diff window.");
            e.printStackTrace();
        }
    }
}
