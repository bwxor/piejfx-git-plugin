package com.bwxor.piejfxsdk.state;

import javafx.scene.control.ListView;

public class UIState {
    private ListView stagedListView;
    private ListView unstagedListView;
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
}
