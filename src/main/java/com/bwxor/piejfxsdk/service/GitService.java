package com.bwxor.piejfxsdk.service;

import com.bwxor.piejfxsdk.dto.CloneViewResponse;
import com.bwxor.piejfxsdk.factory.ListViewCellFactory;
import com.bwxor.piejfxsdk.state.ConfigurationState;
import com.bwxor.piejfxsdk.state.RepositoryState;
import com.bwxor.piejfxsdk.state.ServiceState;
import com.bwxor.piejfxsdk.state.UIState;
import com.bwxor.piejfxsdk.type.CloneViewDialogChoice;
import com.bwxor.piejfxsdk.util.StringUtil;
import javafx.application.Platform;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

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
            } else {
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

    public void commit(String message) {
        ServiceState serviceState = ServiceState.instance;
        ConfigurationState configurationState = ConfigurationState.instance;
        RepositoryState repositoryState = RepositoryState.instance;
        UIState uiState = UIState.instance;

        var creds = configurationState.getLocalCredentials();

        if (message.trim().isEmpty()) {
            serviceState.getNotificationService().showNotificationOk("Cannot commit with an empty message.");
            return;
        }

        if (StringUtil.isNullOrEmpty(creds.getAuthorEmail()) || StringUtil.isNullOrEmpty(creds.getAuthorName())) {
            serviceState.getNotificationService().showNotificationOk("Please fill in your local credentials before doing a commit.");
            return;
        }

        if (uiState.getStagedListView().getItems().isEmpty()) {
            serviceState.getNotificationService().showNotificationOk("You don't have any staged changes to commit.");
            return;
        }

        try {
            var personIdent = new PersonIdent(creds.getAuthorName(), creds.getAuthorEmail());
            repositoryState.getRepo().commit().setMessage(message).setAuthor(personIdent).call();
            serviceState.getNotificationService().showNotificationOk("Commit successful.");
            resetListViews();
            uiState.getCommitMessageTextArea().clear();
        } catch (GitAPIException e) {
            serviceState.getNotificationService().showNotificationOk("Could not commit changes.");
        }
    }

    public void push() {
        ServiceState serviceState = ServiceState.instance;
        ConfigurationState configurationState = ConfigurationState.instance;
        RepositoryState repositoryState = RepositoryState.instance;

        var creds = configurationState.getRemoteCredentials();

        if (StringUtil.isNullOrEmpty(creds.getUsername())
                || StringUtil.isNullOrEmpty(creds.getPasskey())) {

            serviceState.getNotificationService()
                    .showNotificationOk("Please fill in your remote credentials before pushing.");

            return;
        }

        try {
            Iterable<PushResult> results = repositoryState.getRepo()
                    .push()
                    .setCredentialsProvider(
                            new UsernamePasswordCredentialsProvider(
                                    creds.getUsername(),
                                    creds.getPasskey()))
                    .call();

            String failureReason = determinePushFailureReason(results);

            if (failureReason == null) {
                serviceState.getNotificationService()
                        .showNotificationOk("Push successful.");
                resetListViews();
            } else {
                serviceState.getNotificationService()
                        .showNotificationOk("Push failed: " + failureReason);
            }

        } catch (TransportException e) {
            serviceState.getNotificationService()
                    .showNotificationOk("Authentication or network error: " + safeMessage(e));

        } catch (InvalidRemoteException e) {
            serviceState.getNotificationService()
                    .showNotificationOk("Remote repository is invalid or unreachable.");

        } catch (GitAPIException e) {
            serviceState.getNotificationService()
                    .showNotificationOk("Push failed: " + safeMessage(e));

        } catch (Exception e) {
            serviceState.getNotificationService()
                    .showNotificationOk("Unexpected error while pushing.");

            e.printStackTrace();
        }
    }

    private String determinePushFailureReason(Iterable<PushResult> results) {

        for (PushResult pushResult : results) {

            for (RemoteRefUpdate update : pushResult.getRemoteUpdates()) {

                switch (update.getStatus()) {

                    case OK:
                    case UP_TO_DATE:
                        continue;

                    case NON_EXISTING:
                        return "Remote branch does not exist.";

                    case REJECTED_NONFASTFORWARD:
                        return "Remote contains commits you do not have. Pull first.";

                    case REJECTED_REMOTE_CHANGED:
                        return "Remote branch changed during push.";

                    case REJECTED_NODELETE:
                        return "Remote rejected branch deletion.";

                    case REJECTED_OTHER_REASON:
                        return update.getMessage() != null
                                ? update.getMessage()
                                : "Remote rejected push.";

                    case AWAITING_REPORT:
                        return "Push status is unavailable.";

                    case NOT_ATTEMPTED:
                        return "Push was not attempted.";

                    default:
                        return update.getStatus().name();
                }
            }
        }

        return null;
    }

    public void pull() {
        ServiceState serviceState = ServiceState.instance;
        ConfigurationState configurationState = ConfigurationState.instance;
        RepositoryState repositoryState = RepositoryState.instance;

        try {
            var result = repositoryState.getRepo()
                    .pull()
                    .setCredentialsProvider(
                            new UsernamePasswordCredentialsProvider(
                                    configurationState.getRemoteCredentials().getUsername(),
                                    configurationState.getRemoteCredentials().getPasskey()))
                    .call();

            if (result.isSuccessful()) {
                serviceState.getNotificationService()
                        .showNotificationOk("Pull successful.");
                resetListViews();
                return;
            }

            String reason = determineFailureReason(result);

            serviceState.getNotificationService()
                    .showNotificationOk("Pull failed: " + reason);

        } catch (TransportException e) {
            serviceState.getNotificationService()
                    .showNotificationOk("Authentication or network error: " + safeMessage(e));

        } catch (CheckoutConflictException e) {
            serviceState.getNotificationService()
                    .showNotificationOk("Local changes conflict with incoming changes.");

        } catch (WrongRepositoryStateException e) {
            serviceState.getNotificationService()
                    .showNotificationOk("Repository is not in a valid state for pull.");

        } catch (InvalidRemoteException e) {
            serviceState.getNotificationService()
                    .showNotificationOk("Remote repository is invalid or unreachable.");

        } catch (GitAPIException e) {
            serviceState.getNotificationService()
                    .showNotificationOk("Pull failed: " + safeMessage(e));

        } catch (Exception e) {
            serviceState.getNotificationService()
                    .showNotificationOk("Unexpected error while pulling.");

            e.printStackTrace();
        }
    }

    private String determineFailureReason(PullResult result) {
        if (result.getMergeResult() != null) {
            var mergeResult = result.getMergeResult();

            if (mergeResult.getMergeStatus() != null) {
                return switch (mergeResult.getMergeStatus()) {
                    case CONFLICTING -> "There were merge conflicts detected. Fix them before pulling.";
                    case FAILED -> "Please commit your changes or stash them before you merge.";
                    case ABORTED -> "Merge has been aborted.";
                    default -> mergeResult.getMergeStatus().name();
                };
            }
        }

        if (result.getRebaseResult() != null) {
            var rebaseResult = result.getRebaseResult();

            if (rebaseResult.getStatus() != null) {
                return switch (rebaseResult.getStatus()) {
                    case CONFLICTS -> "rebase conflicts detected";
                    case STOPPED -> "rebase stopped due to conflicts";
                    case FAILED -> "rebase failed";
                    case ABORTED -> "rebase aborted";
                    default -> rebaseResult.getStatus().name();
                };
            }
        }

        if (result.getFetchResult() != null
                && !result.getFetchResult().getTrackingRefUpdates().isEmpty()) {

            return result.getFetchResult()
                    .getTrackingRefUpdates()
                    .stream()
                    .map(update -> update.getLocalName() + ": " + update.getResult())
                    .findFirst()
                    .orElse("Fetch failed.");
        }

        return "Unknown reason.";
    }

    private String safeMessage(Exception e) {
        return e.getMessage() != null
                ? e.getMessage()
                : e.getClass().getSimpleName();
    }

    public void rollbackFromStaged(List<String> files) {
        ServiceState serviceState = ServiceState.instance;
        RepositoryState repositoryState = RepositoryState.instance;

        var resetCmd = repositoryState.getRepo()
                .reset()
                .setRef("HEAD");
        var checkoutCmd = repositoryState.getRepo()
                .checkout()
                .setStartPoint("HEAD")
                .setForced(true);
        for (String f : files) {
            resetCmd.addPath(f.substring(2));
            checkoutCmd.addPath(f.substring(2));
        }

        try {
            resetCmd.call();
            checkoutCmd.call();
            serviceState.getNotificationService().showNotificationOk("Successfully rolled back file(s).");
            resetListViews();
        } catch (GitAPIException e) {
            serviceState.getNotificationService()
                    .showNotificationOk("Could not rollback file(s).");
        }
    }

    public void rollbackFromUnstaged(List<String> files) {
        ServiceState serviceState = ServiceState.instance;
        RepositoryState repositoryState = RepositoryState.instance;

        var checkoutCmd = repositoryState
                .getRepo()
                .checkout()
                .setStartPoint("HEAD")
                .setForced(true);
        for (String f : files) {
            checkoutCmd.addPath(f.substring(2));
        }

        try {
            checkoutCmd.call();
            serviceState.getNotificationService().showNotificationOk("Successfully rolled back file(s).");
            resetListViews();
        } catch (GitAPIException e) {
            serviceState.getNotificationService()
                    .showNotificationOk("Could not rollback file(s).");
        }
    }

    public void performClone() {
        ServiceState serviceState = ServiceState.instance;
        RepositoryState repositoryState = RepositoryState.instance;

        var response = serviceState.getGitCloneViewService().showGitCloneWindow();

        if (response.choice() != CloneViewDialogChoice.OK) {
            return;
        }

        String repositoryUrl = response.repoUrl();
        String destinationPath = response.destinationUrl();

        if (repositoryUrl == null || repositoryUrl.isBlank()) {
            serviceState.getNotificationService()
                    .showNotificationOk("Repository URL is required.");
            return;
        }

        if (destinationPath == null || destinationPath.isBlank()) {
            serviceState.getNotificationService()
                    .showNotificationOk("Destination directory is required.");
            return;
        }

        File destination = new File(destinationPath);

        if (destination.exists() && destination.isFile()) {
            serviceState.getNotificationService()
                    .showNotificationOk("Destination must be a directory.");
            return;
        }

        if (destination.exists() && destination.listFiles() != null
                && destination.listFiles().length > 0) {
            serviceState.getNotificationService()
                    .showNotificationOk("Destination directory is not empty.");
            return;
        }

        try (Git _ = Git.cloneRepository()
                .setURI(repositoryUrl)
                .setDirectory(destination)
                .call()) {

            var repo = Git.open(destination);
            repositoryState.setRepo(repo);
            resetListViews();

            serviceState.getFileService().openFolder(destination);

            serviceState.getNotificationService()
                    .showNotificationOk("Repository cloned successfully.");

        } catch (InvalidRemoteException e) {
            serviceState.getNotificationService()
                    .showNotificationOk("Invalid repository URL.");

        } catch (TransportException e) {
            serviceState.getNotificationService()
                    .showNotificationOk("Could not connect to the remote repository.\n" + e.getMessage());

        } catch (GitAPIException e) {
            serviceState.getNotificationService()
                    .showNotificationOk("Failed to clone repository.\n" + e.getMessage());

        } catch (Exception e) {
            serviceState.getNotificationService()
                    .showNotificationOk("Unexpected error.\n" + e.getMessage());
        }
    }
}
