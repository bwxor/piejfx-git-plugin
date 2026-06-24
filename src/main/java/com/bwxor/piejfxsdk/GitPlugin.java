package com.bwxor.piejfxsdk;

import com.bwxor.piejfxsdk.factory.MenuFactory;
import com.bwxor.piejfxsdk.factory.TabFactory;
import com.bwxor.piejfxsdk.service.ConfigurationService;
import com.bwxor.piejfxsdk.service.GitService;
import com.bwxor.piejfxsdk.state.ConfigurationState;
import com.bwxor.piejfxsdk.state.RepositoryState;
import com.bwxor.piejfxsdk.state.ServiceState;
import com.bwxor.plugin.Plugin;
import com.bwxor.plugin.input.PluginContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import org.eclipse.jgit.api.Git;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class GitPlugin implements Plugin {
    private PluginContext pluginContext;
    private Menu gitMenu;
    private Tab gitTab;

    @Override
    public void onLoad(PluginContext pluginContext) {
        ServiceState serviceState = ServiceState.instance;

        serviceState.setGitService(new GitService());
        serviceState.setConfigurationService(new ConfigurationService());
        serviceState.setFileService(pluginContext.getServiceContainer().getFileService());
        serviceState.setNotificationService(pluginContext.getServiceContainer().getNotificationService());

        this.pluginContext = pluginContext;
        ConfigurationState.instance.setConfigurationDirectory(pluginContext.getConfigurationDirectoryPath());

        try {
            serviceState.getConfigurationService().loadCredentials();
        } catch (IOException e) {
            serviceState.getNotificationService().showNotificationOk("Error loading your Git configuration.");
        }

        gitMenu = MenuFactory.createGitMenu();
        gitTab = TabFactory.createGitTab();

        pluginContext.getApplicationWindow().getMenuBar().getMenus().add(gitMenu);
    }

    @Override
    public void onKeyPress(KeyEvent keyEvent) {
    }

    @Override
    public void onSaveFile(File file) {
        RepositoryState repositoryState = RepositoryState.instance;
        ConfigurationState configurationState = ConfigurationState.instance;
        ServiceState serviceState = ServiceState.instance;

        if (file.getPath().equals(Paths.get(configurationState.getConfigurationDirectory().toString(), "config.json").toString())) {
            try {
                serviceState.getConfigurationService().loadCredentials();
            } catch (IOException e) {
                serviceState.getNotificationService().showNotificationOk("Error loading your Git configuration.");
            }
        }

        if (repositoryState.getRepo() != null) {
            serviceState.getGitService().resetListViews();
        }
    }

    @Override
    public void onOpenFile(File file) {
    }

    @Override
    public void onOpenFolder(File file) {
        ServiceState serviceState = ServiceState.instance;
        RepositoryState.instance.setOpenedFolder(file);

        TabPane sidebarTabPane = pluginContext.getApplicationWindow().getSidebarTabPane();

        try {
            RepositoryState.instance.setRepo(Git.open(file));
            gitMenu.getItems().get(0).setDisable(true);
            gitMenu.getItems().get(1).setDisable(true);
            gitMenu.getItems().get(2).setDisable(false);
            gitMenu.getItems().get(3).setDisable(false);

            if (!sidebarTabPane.getTabs().contains(gitTab)) {
                pluginContext.getApplicationWindow().getSidebarTabPane().getTabs().add(gitTab);
            }

            serviceState.getGitService().resetListViews();
        } catch (IOException e) {
            gitMenu.getItems().get(0).setDisable(false);
            gitMenu.getItems().get(1).setDisable(false);
            gitMenu.getItems().get(2).setDisable(true);
            gitMenu.getItems().get(3).setDisable(true);

            if (sidebarTabPane.getTabs().contains(gitTab)) {
                pluginContext.getApplicationWindow().getSidebarTabPane().getTabs().remove(gitTab);
            }
        }
    }

    @Override
    public void onCreateFile(File file) {
        RepositoryState repositoryState = RepositoryState.instance;
        ServiceState serviceState = ServiceState.instance;

        if (repositoryState.getRepo() != null) {
            serviceState.getGitService().resetListViews();
        }
    }

    @Override
    public void onCreateFolder(File file) {
    }

    @Override
    public void onRenameFile(File file) {

    }

    @Override
    public void onDeleteFile(File file) {
        RepositoryState repositoryState = RepositoryState.instance;
        ServiceState serviceState = ServiceState.instance;

        if (repositoryState.getRepo() != null) {
            serviceState.getGitService().resetListViews();
        }
    }
}
