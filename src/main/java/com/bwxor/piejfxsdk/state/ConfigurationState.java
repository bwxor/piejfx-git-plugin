package com.bwxor.piejfxsdk.state;

import java.nio.file.Path;

public class ConfigurationState {
    public static class LocalCredentials {
        private String authorName;
        private String authorEmail;

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public String getAuthorEmail() {
            return authorEmail;
        }

        public void setAuthorEmail(String authorEmail) {
            this.authorEmail = authorEmail;
        }
    }

    public static class RemoteCredentials {
        private String username;
        private String passkey;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPasskey() {
            return passkey;
        }

        public void setPasskey(String passkey) {
            this.passkey = passkey;
        }
    }

    private final LocalCredentials localCredentials = new LocalCredentials();
    private final RemoteCredentials remoteCredentials = new RemoteCredentials();
    private Path configurationDirectory;
    public static final ConfigurationState instance = new ConfigurationState();
    private ConfigurationState() {}

    public LocalCredentials getLocalCredentials() {
        return localCredentials;
    }

    public RemoteCredentials getRemoteCredentials() {
        return remoteCredentials;
    }
    public Path getConfigurationDirectory() {
        return configurationDirectory;
    }

    public void setConfigurationDirectory(Path configurationDirectory) {
        this.configurationDirectory = configurationDirectory;
    }
}
