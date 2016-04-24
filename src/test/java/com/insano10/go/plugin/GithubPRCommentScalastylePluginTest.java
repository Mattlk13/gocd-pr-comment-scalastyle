package com.insano10.go.plugin;

import com.insano10.go.plugin.analysis.scalastyle.ScalastyleResultsAnalyser;
import com.insano10.go.plugin.comment.GitHubPullRequestCommenter;
import com.thoughtworks.go.plugin.api.request.DefaultGoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Test;

import static com.insano10.go.plugin.fixtures.ApiRequestFixtures.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class GithubPRCommentScalastylePluginTest
{
    private static final String REPOSITORY_URL = "git@github.com:testUser/my-test-repo.git";
    private static final int PULL_REQUEST_ID = 7;

    private final GitHubPullRequestCommenter pullRequestCommenter = mock(GitHubPullRequestCommenter.class);
    private final ScalastyleResultsAnalyser scalastyleResultsAnalyser = mock(ScalastyleResultsAnalyser.class);
    private final GithubPRCommentScalastylePlugin plugin = new GithubPRCommentScalastylePlugin(pullRequestCommenter, scalastyleResultsAnalyser);

    @Test
    public void shouldRegisterForStageStatusChangeNotifications() throws Exception
    {
        final DefaultGoPluginApiRequest requestNotificationsInterestedIn = new DefaultGoPluginApiRequest(PluginConstants.NOTIFICATION_PLUGIN_EXTENSION,
                                                                                                         PluginConstants.NOTIFICATION_PLUGIN_EXTENSION_VERSION,
                                                                                                         PluginConstants.REQUEST_NAME_NOTIFICATIONS_INTERESTED_IN);

        assertThat(plugin.handle(requestNotificationsInterestedIn).responseBody(), is("{\"notifications\":[\"stage-status\"]}"));
    }

    @Test
    public void shouldNotCommentIfStageStatusIsBuildingOnMaster() throws Exception
    {
        final GoPluginApiResponse response = plugin.handle(stageStatusRequestMasterBuilding(REPOSITORY_URL));

        verify(pullRequestCommenter, never()).addCommentToPullRequest(any(String.class), anyInt(), any(String.class));
        assertThat(response.responseBody(), is("{\"status\":\"success\",\"messages\":[\"Status change processing completed\"]}"));
    }

    @Test
    public void shouldNotCommentIfStageStatusIsPassedOnMaster() throws Exception
    {
        final GoPluginApiResponse response = plugin.handle(stageStatusRequestMasterPassed(REPOSITORY_URL));

        verify(pullRequestCommenter, never()).addCommentToPullRequest(any(String.class), anyInt(), any(String.class));
        assertThat(response.responseBody(), is("{\"status\":\"success\",\"messages\":[\"Status change processing completed\"]}"));
    }

    @Test
    public void shouldNotCommentIfStageStatusIsBuildingOnPullRequest() throws Exception
    {
        final GoPluginApiResponse response = plugin.handle(stageStatusRequestPullRequestBuilding(REPOSITORY_URL, PULL_REQUEST_ID));

        verify(pullRequestCommenter, never()).addCommentToPullRequest(any(String.class), anyInt(), any(String.class));
        assertThat(response.responseBody(), is("{\"status\":\"success\",\"messages\":[\"Status change processing completed\"]}"));
    }

    @Test
    public void shouldNotCommentIfStageStatusIsPassedOnPullRequestAndScalastyleResultsAreNotFound() throws Exception
    {
        when(scalastyleResultsAnalyser.hasResults()).thenReturn(false);

        final GoPluginApiResponse response = plugin.handle(stageStatusRequestPullRequestPassed(REPOSITORY_URL, PULL_REQUEST_ID));

        verify(pullRequestCommenter, never()).addCommentToPullRequest(any(String.class), anyInt(), any(String.class));
        assertThat(response.responseBody(), is("{\"status\":\"success\",\"messages\":[\"Status change processing completed\"]}"));
    }

    @Test
    public void shouldCommentIfStageStatusIsPassedOnPullRequestAndScalastyleResultsAreFound() throws Exception
    {
        final String expectedComment = "hello world!";

        when(scalastyleResultsAnalyser.hasResults()).thenReturn(true);
        when(scalastyleResultsAnalyser.buildGithubMarkdownSummary()).thenReturn(expectedComment);

        final GoPluginApiResponse response = plugin.handle(stageStatusRequestPullRequestPassed(REPOSITORY_URL, PULL_REQUEST_ID));

        verify(pullRequestCommenter).addCommentToPullRequest(eq(REPOSITORY_URL), eq(PULL_REQUEST_ID), eq(expectedComment));
        assertThat(response.responseBody(), is("{\"status\":\"success\",\"messages\":[\"Status change processing completed\"]}"));
    }

    @Test
    public void shouldReturnFailureMessageIfSomethingUnexpectedGoesWrong() throws Exception
    {
        final DefaultGoPluginApiRequest request = stageStatusRequestPullRequestPassed(REPOSITORY_URL, PULL_REQUEST_ID);
        final String requestBodyWithAnInvalidPullRequestId = request.requestBody().replace("\"PR_ID\": \"" + PULL_REQUEST_ID + "\"", "\"PR_ID\": \"NotAnInteger\"");
        request.setRequestBody(requestBodyWithAnInvalidPullRequestId);

        final GoPluginApiResponse response = plugin.handle(request);

        verify(pullRequestCommenter, never()).addCommentToPullRequest(any(String.class), anyInt(), any(String.class));
        assertThat(response.responseBody(), is("{\"status\":\"failure\",\"messages\":[\"java.lang.NumberFormatException: For input string: \\\"NotAnInteger\\\"\"]}"));
    }
}