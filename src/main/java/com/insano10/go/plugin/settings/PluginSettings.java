package com.insano10.go.plugin.settings;

import org.apache.commons.lang3.StringUtils;

public class PluginSettings
{
    private final String endPoint;
    private final String oauthToken;

    public PluginSettings(String endPoint, String oauthToken)
    {
        this.endPoint = endPoint;
        this.oauthToken = oauthToken;
    }

    public String getEndPoint()
    {
        return StringUtils.isBlank(endPoint) ? System.getProperty("go.plugin.build.status.github.endpoint") : endPoint;
    }

    public String getOauthToken()
    {
        return StringUtils.isBlank(oauthToken) ? System.getProperty("go.plugin.build.status.github.oauth") : oauthToken;
    }
}
