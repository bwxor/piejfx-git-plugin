package com.bwxor.piejfxsdk.factory;

import com.bwxor.piejfxsdk.state.ConfigurationState;
import com.bwxor.piejfxsdk.state.ServiceState;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.nio.file.Paths;

public class MenuFactory {
    public static Menu createGitMenu() {
        ServiceState serviceState = ServiceState.instance;
        ConfigurationState configurationState = ConfigurationState.instance;

        Menu menu = new Menu("Git");
        MenuItem initializeRepositoryMenuItem = new MenuItem("Initialize");
        initializeRepositoryMenuItem.setVisible(true);
        initializeRepositoryMenuItem.setOnAction(_ -> serviceState.getGitService().initialize());
        menu.getItems().add(initializeRepositoryMenuItem);

        MenuItem cloneRepositoryMenuItem = new MenuItem("Clone...");
        cloneRepositoryMenuItem.setVisible(true);
        cloneRepositoryMenuItem.setOnAction(_ -> serviceState.getGitService().performClone());
        menu.getItems().add(cloneRepositoryMenuItem);

        MenuItem pushMenuItem = new MenuItem("Push...");
        pushMenuItem.setVisible(false);
        pushMenuItem.setOnAction(_ -> serviceState.getGitService().push());
        menu.getItems().add(pushMenuItem);

        MenuItem pullMenuItem = new MenuItem("Pull...");
        pullMenuItem.setVisible(false);
        pullMenuItem.setOnAction(_ -> serviceState.getGitService().pull());
        menu.getItems().add(pullMenuItem);

        MenuItem settingsMenuItem = new MenuItem("Settings");
        settingsMenuItem.setVisible(true);
        settingsMenuItem.setOnAction(_ -> serviceState.getFileService().openFile(Paths.get(configurationState.getConfigurationDirectory().toString(), "config.json").toFile()));
        menu.getItems().add(settingsMenuItem);

        MenuItem checkoutMenuItem = new MenuItem("Checkout...");
        checkoutMenuItem.setVisible(false);
        checkoutMenuItem.setOnAction(_ -> {
            java.util.List<String> branches = serviceState.getGitService().listBranches();
            serviceState.getGitCheckoutViewService().showCheckoutWindow(branches);
        });
        menu.getItems().add(checkoutMenuItem);

        MenuItem newBranchMenuItem = new MenuItem("New branch...");
        newBranchMenuItem.setVisible(false);
        newBranchMenuItem.setOnAction(_ -> serviceState.getGitNewBranchViewService().showNewBranchWindow());
        menu.getItems().add(newBranchMenuItem);

        return menu;
    }
}
