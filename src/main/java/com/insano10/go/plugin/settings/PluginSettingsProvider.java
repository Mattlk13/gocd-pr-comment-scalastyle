package com.insano10.go.plugin.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PluginSettingsProvider
{
    public static PluginSettings getPluginSettings()
    {
        try
        {
            final Properties githubProperties = readPropertyFile(".github");

            return new PluginSettings(githubProperties.getProperty("endpoint"), githubProperties.getProperty("oauth"));
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to read plugin settings", e);
        }
    }

    private static Properties readPropertyFile(final String filename) throws IOException
    {
        final File propertyFile = new File(System.getProperty("user.home"), filename);
        final Properties props = new Properties();

        try(FileInputStream in = new FileInputStream(propertyFile))
        {
            props.load(in);
        }

        return props;
    }
}
