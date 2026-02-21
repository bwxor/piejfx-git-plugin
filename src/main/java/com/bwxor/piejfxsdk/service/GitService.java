package com.bwxor.piejfxsdk.service;

import com.bwxor.piejfxsdk.state.RepositoryState;
import com.bwxor.piejfxsdk.state.ServiceState;
import com.bwxor.piejfxsdk.state.UIState;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;

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
                            status.getAdded(),
                            status.getChanged(),
                            status.getRemoved()
                    )
                    .filter(d -> !d.isEmpty())
                    .flatMap(Collection::stream)
                    .toList();

            uiState.getStagedListView().getItems().setAll(stagedItems);

            List<String> unstagedItems = Stream.of(
                            status.getModified(),
                            status.getUntracked(),
                            status.getMissing()
                    )
                    .filter(d -> !d.isEmpty())
                    .flatMap(Collection::stream)
                    .toList();
            ;
            uiState.getUnstagedListView().getItems().setAll(unstagedItems);
        });

    }
}
