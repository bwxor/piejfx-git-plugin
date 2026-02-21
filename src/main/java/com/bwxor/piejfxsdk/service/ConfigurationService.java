package com.bwxor.piejfxsdk.service;

import com.bwxor.piejfxsdk.state.ConfigurationState;
import com.bwxor.piejfxsdk.type.Credential;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Paths;

public class ConfigurationService {
    private static final String EMPTY_STRING = "";

    public void loadCredentials() throws IOException {
        ConfigurationState configurationState = ConfigurationState.instance;
        File confFile = new File(Paths.get(ConfigurationState.instance.getConfigurationDirectory().toString(), "config.json").toUri());

        createDirectoryAndFile();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(confFile))) {
            String confContent = bufferedReader.readAllAsString();

            JSONObject jsonObject = new JSONObject(confContent);
            JSONObject config = jsonObject.getJSONObject("config");
            JSONObject credentials = config.getJSONObject("credentials");

            configurationState.getLocalCredentials().setAuthorName(credentials.getString(Credential.AUTHOR_NAME.getValue()));
            configurationState.getLocalCredentials().setAuthorEmail(credentials.getString(Credential.AUTHOR_EMAIL.getValue()));
            configurationState.getRemoteCredentials().setUsername(credentials.getString(Credential.USERNAME.getValue()));
            configurationState.getRemoteCredentials().setPasskey(credentials.getString(Credential.PASSKEY.getValue()));
        }
    }

    private void createDirectoryAndFile() {
        File confDir = new File(Paths.get(ConfigurationState.instance.getConfigurationDirectory().toString()).toUri());
        File confFile = new File(Paths.get(confDir.toString(), "config.json").toUri());

        if (!confDir.exists()) {
            confDir.mkdirs();
            createFile();
        } else if (!confFile.exists()) {
            createFile();
        }
    }

    private void createFile() {
        File confDir = new File(Paths.get(ConfigurationState.instance.getConfigurationDirectory().toString()).toUri());
        File confFile = new File(Paths.get(confDir.toString(), "config.json").toUri());

        try {
            confFile.createNewFile();

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(confFile));

            JSONObject root = new JSONObject();
            JSONObject config = new JSONObject();
            root.put("config", config);

            JSONObject credentials = new JSONObject();
            credentials.put(Credential.AUTHOR_NAME.getValue(), EMPTY_STRING);
            credentials.put(Credential.AUTHOR_EMAIL.getValue(), EMPTY_STRING);
            credentials.put(Credential.USERNAME.getValue(), EMPTY_STRING);
            credentials.put(Credential.PASSKEY.getValue(), EMPTY_STRING);

            config.put("credentials", credentials);

            bufferedWriter.write(root.toString());
            bufferedWriter.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}