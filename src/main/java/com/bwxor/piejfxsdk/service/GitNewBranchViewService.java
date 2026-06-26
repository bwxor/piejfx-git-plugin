package com.bwxor.piejfxsdk.service;

import com.bwxor.piejfxsdk.controller.impl.GitNewBranchViewController;
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

public class GitNewBranchViewService {
    public void showNewBranchWindow() {
        ServiceState serviceState = ServiceState.instance;
        StylesheetState stylesheetState = StylesheetState.instance;

        FXMLLoader loader = new FXMLLoader(serviceState.getResourceService().getResourceByName("views/git-new-branch-view.fxml"));
        loader.setClassLoader(GitNewBranchViewController.class.getClassLoader());
        Parent root;

        try {
            root = loader.load();

            GitNewBranchViewController controller = loader.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(stylesheetState.getThemeURL().toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("New Branch");
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().add(stylesheetState.getDefaultStylesheetURL().toExternalForm());
            controller.setWindowTitle("New Branch");
            stage.showAndWait();

        } catch (IOException e) {
            serviceState.getNotificationService().showNotificationOk("Error while trying to load the new branch window.");
            e.printStackTrace();
        }
    }
}
