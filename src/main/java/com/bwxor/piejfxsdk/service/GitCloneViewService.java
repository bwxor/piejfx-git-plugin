package com.bwxor.piejfxsdk.service;

import com.bwxor.piejfxsdk.controller.impl.GitCloneViewController;
import com.bwxor.piejfxsdk.dto.CloneViewResponse;
import com.bwxor.piejfxsdk.state.ServiceState;
import com.bwxor.piejfxsdk.state.StylesheetState;
import com.bwxor.piejfxsdk.type.CloneViewDialogChoice;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class GitCloneViewService {
    public CloneViewResponse showGitCloneWindow() {
        ServiceState serviceState = ServiceState.instance;
        StylesheetState stylesheetState = StylesheetState.instance;

        FXMLLoader loader = new FXMLLoader(serviceState.getResourceService().getResourceByName("views/git-clone-view.fxml"));
        loader.setClassLoader(GitCloneViewController.class.getClassLoader());
        Parent root;

        try {
            root = loader.load();

            GitCloneViewController controller = loader.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(stylesheetState.getThemeURL().toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Git Clone");
            stage.setOnCloseRequest(e -> {
                controller.setCloneViewResponse(new CloneViewResponse(CloneViewDialogChoice.CANCEL, null, null));
            });
//            stage.getIcons().add(new Image(Objects.requireNonNull(serviceState.getResourceService().getResourceByNameAsStream("img/icons/icon.png"))));
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
//            stage.initOwner(stageState.getStage());
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().add(stylesheetState.getDefaultStylesheetURL().toExternalForm());
            controller.setWindowTitle("Git Clone");
            stage.showAndWait();

            return controller.getCloneViewResponse();

        } catch (IOException e) {
            serviceState.getNotificationService().showNotificationOk("Error while trying to load the window.");
            e.printStackTrace();
            return null;
        }
    }
}
