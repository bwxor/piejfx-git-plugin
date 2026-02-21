package com.bwxor.piejfxsdk.state;

import org.eclipse.jgit.api.Git;

import java.io.File;

public class RepositoryState {
    private File openedFolder;
    private Git repo;
    public static final RepositoryState instance = new RepositoryState();
    private RepositoryState() {}

    public File getOpenedFolder() {
        return openedFolder;
    }

    public void setOpenedFolder(File openedFolder) {
        this.openedFolder = openedFolder;
    }

    public Git getRepo() {
        return repo;
    }

    public void setRepo(Git repo) {
        this.repo = repo;
    }
}
