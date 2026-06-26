package com.bwxor.piejfxsdk.service;

import com.bwxor.piejfxsdk.controller.impl.GitCheckoutViewController;
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

public class GitCheckoutViewService {
    public void showCheckoutWindow(List<String> branches) {
        ServiceState serviceState = ServiceState.instance;
        StylesheetState stylesheetState = StylesheetState.instance;

        FXMLLoader loader = new FXMLLoader(serviceState.getResourceService().getResourceByName("views/git-checkout-view.fxml"));
        loader.setClassLoader(GitCheckoutViewController.class.getClassLoader());
        Parent root;

        try {
            root = loader.load();

            GitCheckoutViewController controller = loader.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(stylesheetState.getThemeURL().toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Checkout Branch");
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().add(stylesheetState.getDefaultStylesheetURL().toExternalForm());
            controller.setWindowTitle("Checkout Branch");
            controller.setBranches(branches);
            stage.showAndWait();

        } catch (IOException e) {
            serviceState.getNotificationService().showNotificationOk("Error while trying to load the checkout window.");
            e.printStackTrace();
        }
    }
}
