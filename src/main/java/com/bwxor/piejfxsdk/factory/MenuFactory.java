package com.bwxor.piejfxsdk.factory;

import com.bwxor.piejfxsdk.state.ConfigurationState;
import com.bwxor.piejfxsdk.state.RepositoryState;
import com.bwxor.piejfxsdk.state.ServiceState;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.nio.file.Paths;

public class MenuFactory {
    public static Menu createGitMenu() {
        ServiceState serviceState = ServiceState.instance;
        ConfigurationState configurationState = ConfigurationState.instance;

        Menu menu = new Menu("Git");
        MenuItem initializeRepositoryMenuItem = new MenuItem("Initialize");
        initializeRepositoryMenuItem.setDisable(true);
        initializeRepositoryMenuItem.setOnAction(_ -> {
            serviceState.getGitService().initialize();
        });
        menu.getItems().add(initializeRepositoryMenuItem);

        MenuItem cloneRepositoryMenuItem = new MenuItem("Clone...");
        cloneRepositoryMenuItem.setDisable(true);
        menu.getItems().add(cloneRepositoryMenuItem);

        MenuItem pushMenuItem = new MenuItem("Push...");
        pushMenuItem.setDisable(true);
        menu.getItems().add(pushMenuItem);

        MenuItem pullMenuItem = new MenuItem("Pull...");
        pullMenuItem.setDisable(true);
        menu.getItems().add(pullMenuItem);

        MenuItem settingsMenuItem = new MenuItem("Settings");
        settingsMenuItem.setOnAction(_ -> serviceState.getFileService().openFile(Paths.get(configurationState.getConfigurationDirectory().toString(), "config.json").toFile()));
        menu.getItems().add(settingsMenuItem);


        return menu;
    }
}
