package com.bwxor.piejfxsdk;

import com.bwxor.piejfxsdk.factory.MenuFactory;
import com.bwxor.piejfxsdk.factory.TabFactory;
import com.bwxor.piejfxsdk.service.ConfigurationService;
import com.bwxor.piejfxsdk.service.GitCheckoutViewService;
import com.bwxor.piejfxsdk.service.GitCloneViewService;
import com.bwxor.piejfxsdk.service.GitCommitFilesViewService;
import com.bwxor.piejfxsdk.service.GitFileDiffViewService;
import com.bwxor.piejfxsdk.service.GitLogViewService;
import com.bwxor.piejfxsdk.service.GitNewBranchViewService;
import com.bwxor.piejfxsdk.service.GitService;
import com.bwxor.piejfxsdk.service.ResourceService;
import com.bwxor.piejfxsdk.state.*;
import com.bwxor.plugin.Plugin;
import com.bwxor.plugin.input.PluginContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import org.eclipse.jgit.api.Git;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class GitPlugin implements Plugin {
    private PluginContext pluginContext;
    private Tab gitTab;

    @Override
    public void onLoad(PluginContext pluginContext) {
        ServiceState serviceState = ServiceState.instance;
        StylesheetState stylesheetState = StylesheetState.instance;
        UIState uiState = UIState.instance;

        serviceState.setGitService(new GitService());
        serviceState.setConfigurationService(new ConfigurationService());
        serviceState.setFileService(pluginContext.getServiceContainer().getFileService());
        serviceState.setNotificationService(pluginContext.getServiceContainer().getNotificationService());
        serviceState.setResourceService(new ResourceService());
        serviceState.setGitCloneViewService(new GitCloneViewService());
        serviceState.setGitLogViewService(new GitLogViewService());
        serviceState.setGitCommitFilesViewService(new GitCommitFilesViewService());
        serviceState.setGitFileDiffViewService(new GitFileDiffViewService());
        serviceState.setGitCheckoutViewService(new GitCheckoutViewService());
        serviceState.setGitNewBranchViewService(new GitNewBranchViewService());

        stylesheetState.setThemeURL(pluginContext.getStylesheets().getThemeURL());
        stylesheetState.setDefaultStylesheetURL(pluginContext.getStylesheets().getDefaultStylesURL());
        stylesheetState.setDefaultMaximizedStylesheetURL(pluginContext.getStylesheets().getDefaultMaximizedURL());

        this.pluginContext = pluginContext;
        ConfigurationState.instance.setConfigurationDirectory(pluginContext.getConfigurationDirectoryPath());

        try {
            serviceState.getConfigurationService().loadCredentials();
        } catch (IOException e) {
            serviceState.getNotificationService().showNotificationOk("Error loading your Git configuration.");
        }

        uiState.setGitMenu(MenuFactory.createGitMenu());
        gitTab = TabFactory.createGitTab();

        pluginContext.getApplicationWindow().getMenuBar().getMenus().add(uiState.getGitMenu());
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
        RepositoryState repositoryState = RepositoryState.instance;
        UIState uiState = UIState.instance;

        repositoryState.setOpenedFolder(file);

        TabPane sidebarTabPane = pluginContext.getApplicationWindow().getSidebarTabPane();

        var gitMenu = uiState.getGitMenu();

        try {
            repositoryState.setRepo(Git.open(file));
            gitMenu.setText("⚡ " + repositoryState.getRepo().getRepository().getBranch());
            gitMenu.getItems().get(0).setVisible(false);
            gitMenu.getItems().get(1).setVisible(true);
            gitMenu.getItems().get(2).setVisible(true);
            gitMenu.getItems().get(3).setVisible(true);
            gitMenu.getItems().get(4).setVisible(true);
            gitMenu.getItems().get(5).setVisible(true);
            gitMenu.getItems().get(6).setVisible(true);

            if (!sidebarTabPane.getTabs().contains(gitTab)) {
                pluginContext.getApplicationWindow().getSidebarTabPane().getTabs().add(gitTab);
            }

            serviceState.getGitService().resetListViews();
        } catch (IOException e) {
            gitMenu.setText("Git");
            gitMenu.getItems().get(0).setVisible(true);
            gitMenu.getItems().get(1).setVisible(true);
            gitMenu.getItems().get(2).setVisible(false);
            gitMenu.getItems().get(3).setVisible(true);
            gitMenu.getItems().get(4).setVisible(false);
            gitMenu.getItems().get(5).setVisible(false);
            gitMenu.getItems().get(6).setVisible(false);

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

    @Override
    public void onThemeChange(URL url) {
        StylesheetState.instance.setThemeURL(url);
    }
}
