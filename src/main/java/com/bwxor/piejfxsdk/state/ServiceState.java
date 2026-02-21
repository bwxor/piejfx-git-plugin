package com.bwxor.piejfxsdk.state;

import com.bwxor.piejfxsdk.service.ConfigurationService;
import com.bwxor.piejfxsdk.service.GitService;
import com.bwxor.plugin.service.PluginFileService;
import com.bwxor.plugin.service.PluginNotificationService;

public class ServiceState {
    private ConfigurationService configurationService;
    private GitService gitService;
    private PluginFileService fileService;
    private PluginNotificationService notificationService;
    public static final ServiceState instance = new ServiceState();

    private ServiceState() {}

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public GitService getGitService() {
        return gitService;
    }

    public void setGitService(GitService gitService) {
        this.gitService = gitService;
    }

    public PluginFileService getFileService() {
        return fileService;
    }

    public void setFileService(PluginFileService fileService) {
        this.fileService = fileService;
    }

    public PluginNotificationService getNotificationService() {
        return notificationService;
    }

    public void setNotificationService(PluginNotificationService notificationService) {
        this.notificationService = notificationService;
    }
}
