package com.bwxor.piejfxsdk.service;

import com.bwxor.piejfxsdk.factory.ListViewCellFactory;
import com.bwxor.piejfxsdk.state.RepositoryState;
import com.bwxor.piejfxsdk.state.ServiceState;
import com.bwxor.piejfxsdk.state.UIState;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class GitService {
    public void initialize() {
        ServiceState serviceState = ServiceState.instance;
        RepositoryState repositoryState = RepositoryState.instance;

        try {
            Git.init().setDirectory(repositoryState.getOpenedFolder()).call();
        } catch (GitAPIException e) {
            serviceState.getNotificationService().showNotificationOk("Could not initialize Git repository.");
        }

        serviceState.getFileService().openFolder(repositoryState.getOpenedFolder());
    }

    public void resetListViews() {
        ServiceState serviceState = ServiceState.instance;
        UIState uiState = UIState.instance;
        RepositoryState repositoryState = RepositoryState.instance;

        Status status;

        try {
            status = repositoryState.getRepo().status().call();
        } catch (GitAPIException e) {
            serviceState.getNotificationService().showNotificationOk("Could not read staging area.");
            return;
        }

        Platform.runLater(() -> {
            List<String> stagedItems = Stream.of(
                            status.getAdded().stream().map(s -> "✚ " + s).toList(),
                            status.getChanged().stream().map(s -> "✱ " + s).toList(),
                            status.getRemoved().stream().map(s -> "✖ " + s).toList()
                    )
                    .filter(d -> !d.isEmpty())
                    .flatMap(Collection::stream)
                    .toList();

            uiState.getStagedListView().getItems().setAll(stagedItems);
            uiState.getStagedListView().setCellFactory(_ -> new ListViewCellFactory());

            List<String> unstagedItems = Stream.of(
                            status.getModified().stream().map(s -> "✱ " + s).toList(),
                            status.getUntracked().stream().map(s -> "✚ " + s).toList(),
                            status.getMissing().stream().map(s -> "✖ " + s).toList()
                    )
                    .filter(d -> !d.isEmpty())
                    .flatMap(Collection::stream)
                    .toList();

            uiState.getUnstagedListView().getItems().setAll(unstagedItems);
            uiState.getUnstagedListView().setCellFactory(_ -> new ListViewCellFactory());
        });
    }

    public void addFileToStagingArea(String filePattern) {
        ServiceState serviceState = ServiceState.instance;
        RepositoryState repositoryState = RepositoryState.instance;

        try {
            if (filePattern.startsWith("✖")) {
                repositoryState.getRepo().rm().addFilepattern(filePattern.substring(2)).call();
            }
            else {
                repositoryState.getRepo().add().addFilepattern(filePattern.substring(2)).call();
            }
            resetListViews();
        } catch (GitAPIException e) {
            serviceState.getNotificationService().showNotificationOk("Could not add file " + filePattern + " to staging area.");
        }
    }

    public void removeFileFromStagingArea(String filePattern) {
        ServiceState serviceState = ServiceState.instance;
        RepositoryState repositoryState = RepositoryState.instance;

        try {
            repositoryState.getRepo().reset().addPath(filePattern.substring(2)).setRef("HEAD").call();
            resetListViews();
        } catch (GitAPIException e) {
            serviceState.getNotificationService().showNotificationOk("Could not add file " + filePattern + " to staging area.");
        }
    }
}
