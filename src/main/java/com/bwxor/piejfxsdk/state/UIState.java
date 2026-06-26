package com.bwxor.piejfxsdk.state;

import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.TextArea;

public class UIState {
    private ListView stagedListView;
    private ListView unstagedListView;
    private TextArea commitMessageTextArea;
    private Menu gitMenu;
    public static final UIState instance = new UIState();

    public ListView getStagedListView() {
        return stagedListView;
    }

    public void setStagedListView(ListView stagedListView) {
        this.stagedListView = stagedListView;
    }

    public ListView getUnstagedListView() {
        return unstagedListView;
    }

    public void setUnstagedListView(ListView unstagedListView) {
        this.unstagedListView = unstagedListView;
    }

    public TextArea getCommitMessageTextArea() {
        return commitMessageTextArea;
    }

    public void setCommitMessageTextArea(TextArea commitMessageTextArea) {
        this.commitMessageTextArea = commitMessageTextArea;
    }

    public Menu getGitMenu() {
        return gitMenu;
    }

    public void setGitMenu(Menu gitMenu) {
        this.gitMenu = gitMenu;
    }
}
