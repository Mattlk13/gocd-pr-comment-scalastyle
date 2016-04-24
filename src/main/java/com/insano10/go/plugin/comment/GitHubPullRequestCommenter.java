package com.insano10.go.plugin.comment;

import com.insano10.go.plugin.settings.PluginSettings;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;

public class GitHubPullRequestCommenter
{
    private PluginSettings pluginSettings;

    public GitHubPullRequestCommenter(final PluginSettings pluginSettings)
    {
        this.pluginSettings = pluginSettings;
    }

    public void addCommentToPullRequest(final String url, final int pullRequestId, final String comment) throws Exception
    {
        final String repository = getRepository(url);
        final String endPointToUse = pluginSettings.getEndPoint();
        final String oauthAccessTokenToUse = pluginSettings.getOauthToken();

        final GitHub github = createGitHubClient(oauthAccessTokenToUse, endPointToUse);
        final GHRepository ghRepository = github.getRepository(repository);
        final GHIssue issue = ghRepository.getIssue(pullRequestId);

        issue.comment(comment);
    }

    private GitHub createGitHubClient(final String oauthAccessTokenToUse, final String endPointToUse) throws IOException
    {
        if (!StringUtils.isBlank(oauthAccessTokenToUse))
        {
            if (!StringUtils.isBlank(endPointToUse))
            {
                return GitHub.connectUsingOAuth(endPointToUse, oauthAccessTokenToUse);
            }
            else
            {
                return GitHub.connectUsingOAuth(oauthAccessTokenToUse);
            }
        }
        else
        {
            return GitHub.connect();
        }
    }

    private String getRepository(final String url)
    {
        String[] urlParts = url.split("/");
        String repo = urlParts[urlParts.length - 1];
        String usernameWithSSHPrefix = urlParts[urlParts.length - 2];
        int positionOfColon = usernameWithSSHPrefix.lastIndexOf(":");
        if (positionOfColon > 0)
        {
            usernameWithSSHPrefix = usernameWithSSHPrefix.substring(positionOfColon + 1);
        }

        String urlWithoutPrefix = String.format("%s/%s", usernameWithSSHPrefix, repo);
        if (urlWithoutPrefix.endsWith(".git")) return urlWithoutPrefix.substring(0, urlWithoutPrefix.length() - 4);
        else return urlWithoutPrefix;
    }
}
