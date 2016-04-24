package com.insano10.go.plugin;

import com.insano10.go.plugin.analysis.scalastyle.ScalastyleResultsAnalyser;
import com.insano10.go.plugin.comment.GitHubPullRequestCommenter;
import com.insano10.go.plugin.fixtures.ApiRequestFixtures;
import com.insano10.go.plugin.fixtures.StubJobConsoleLogger;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class GithubPRCommentScalastylePluginTest
{
    private static final String RESULTS_FILE_LOCATION = "src/test/resources/scalastyle-result.xml";
    private static final Path RESULTS_FILE_PATH = Paths.get(RESULTS_FILE_LOCATION);

    private static final String REPOSITORY_URL = "git@github.com:testUser/my-test-repo.git";
    private static final int PULL_REQUEST_ID = 7;

    private final GitHubPullRequestCommenter pullRequestCommenter = mock(GitHubPullRequestCommenter.class);
    private final ScalastyleResultsAnalyser scalastyleResultsAnalyser = mock(ScalastyleResultsAnalyser.class);
    private final GithubPRCommentScalastylePlugin plugin = new GithubPRCommentScalastylePlugin(pullRequestCommenter, scalastyleResultsAnalyser, new StubJobConsoleLogger());


//    @Test
//    public void shouldNotCommentIfStageStatusIsPassedOnPullRequestAndScalastyleResultsAreNotFound() throws Exception
//    {
//        when(scalastyleResultsAnalyser.hasResults()).thenReturn(false);
//
//        final GoPluginApiResponse response = plugin.handle(stageStatusRequestPullRequestPassed(REPOSITORY_URL, PULL_REQUEST_ID));
//
//        verify(pullRequestCommenter, never()).addCommentToPullRequest(any(String.class), anyInt(), any(String.class));
//        assertThat(response.responseBody(), is("{\"status\":\"success\",\"messages\":[\"Status change processing completed\"]}"));
//    }

    @Test
    public void shouldCommentIfScalastyleResultsAreFound() throws Exception
    {
        final String expectedComment = "hello world!";

        when(scalastyleResultsAnalyser.buildGithubMarkdownSummary(RESULTS_FILE_PATH)).thenReturn(expectedComment);

        final GoPluginApiResponse response = plugin.handle(ApiRequestFixtures.executeTaskRequest(REPOSITORY_URL, PULL_REQUEST_ID, RESULTS_FILE_LOCATION));

        verify(pullRequestCommenter).addCommentToPullRequest(eq(REPOSITORY_URL), eq(PULL_REQUEST_ID), eq(expectedComment));
        assertEquals("{\"success\": true, \"message\": \"Task execution complete\"}", response.responseBody());
    }

//    @Test
//    public void shouldReturnFailureMessageIfSomethingUnexpectedGoesWrong() throws Exception
//    {
//        final DefaultGoPluginApiRequest request = stageStatusRequestPullRequestPassed(REPOSITORY_URL, PULL_REQUEST_ID);
//        final String requestBodyWithAnInvalidPullRequestId = request.requestBody().replace("\"PR_ID\": \"" + PULL_REQUEST_ID + "\"", "\"PR_ID\": \"NotAnInteger\"");
//        request.setRequestBody(requestBodyWithAnInvalidPullRequestId);
//
//        final GoPluginApiResponse response = plugin.handle(request);
//
//        verify(pullRequestCommenter, never()).addCommentToPullRequest(any(String.class), anyInt(), any(String.class));
//        assertThat(response.responseBody(), is("{\"status\":\"failure\",\"messages\":[\"java.lang.NumberFormatException: For input string: \\\"NotAnInteger\\\"\"]}"));
//    }
}