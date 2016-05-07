package com.insano10.go.plugin;

import com.insano10.go.plugin.analysis.scalastyle.ScalastyleResultsAnalyser;
import com.insano10.go.plugin.comment.GitHubPullRequestCommenter;
import com.insano10.go.plugin.fixtures.StubJobConsoleLogger;
import com.thoughtworks.go.plugin.api.request.DefaultGoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.insano10.go.plugin.fixtures.ApiRequestFixtures.executeTaskFromMasterRequest;
import static com.insano10.go.plugin.fixtures.ApiRequestFixtures.executeTaskFromPRRequest;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class GithubPRCommentScalastylePluginTest
{
    private static final String WORKING_DIRECTORY = "src/test";
    private static final String RESULTS_FILE_1 = "resources/dirA/scalastyle-result.xml";
    private static final String RESULTS_FILE_2 = "resources/dirB/scalastyle-result.xml";
    private static final String UNKNOWN_RESULTS_FILE = "does/not/exist/scalastyle-result.xml";
    private static final String ARTIFACT_DIR_1 = "analysis/dirA";
    private static final String ARTIFACT_FILE_1 = "analysis/dirA/scalastyle-result.xml";
    private static final String ARTIFACT_DIR_2 = "analysis/dirB";
    private static final String ARTIFACT_FILE_2 = "analysis/dirB/scalastyle-result.xml";

    private static final List<String> RESULTS_FILE_LOCATIONS = newArrayList(RESULTS_FILE_1, RESULTS_FILE_2);
    private static final List<String> ARTIFACT_FILE_LOCATIONS = newArrayList(ARTIFACT_DIR_1, ARTIFACT_DIR_2);
    private static final Path RESULTS_FILE_PATH_1 = Paths.get(WORKING_DIRECTORY + "/" + RESULTS_FILE_LOCATIONS.get(0));
    private static final Path RESULTS_FILE_PATH_2 = Paths.get(WORKING_DIRECTORY + "/" + RESULTS_FILE_LOCATIONS.get(1));

    private static final String REPOSITORY_URL = "git@github.com:testUser/my-test-repo.git";
    private static final int PULL_REQUEST_ID = 7;

    private final GitHubPullRequestCommenter pullRequestCommenter = mock(GitHubPullRequestCommenter.class);
    private final ScalastyleResultsAnalyser scalastyleResultsAnalyser = mock(ScalastyleResultsAnalyser.class);
    private final GithubPRCommentScalastylePlugin plugin = new GithubPRCommentScalastylePlugin(pullRequestCommenter, scalastyleResultsAnalyser, new StubJobConsoleLogger());


    @Test
    public void shouldNotCommentIfTaskWasNotTriggeredFromAPullRequest() throws Exception
    {
        final GoPluginApiResponse response = plugin.handle(executeTaskFromMasterRequest(WORKING_DIRECTORY, RESULTS_FILE_LOCATIONS, ARTIFACT_FILE_LOCATIONS));

        verify(pullRequestCommenter, never()).addCommentToPullRequest(any(String.class), anyInt(), any(String.class));
        assertEquals("{\"success\":true,\"message\":\"Task execution complete\"}", response.responseBody());
    }

    @Test
    public void shouldNotCommentIfScalastyleResultsAreNotFound() throws Exception
    {
        final GoPluginApiResponse response = plugin.handle(executeTaskFromPRRequest(REPOSITORY_URL, PULL_REQUEST_ID, WORKING_DIRECTORY,
                                                                                    newArrayList(UNKNOWN_RESULTS_FILE), newArrayList(ARTIFACT_DIR_1)));

        verify(pullRequestCommenter, never()).addCommentToPullRequest(any(String.class), anyInt(), any(String.class));
        assertEquals("{\"success\":true,\"message\":\"Task execution complete\"}", response.responseBody());
    }

    @Test
    public void shouldCommentIfSingleScalastyleResultsAreFound() throws Exception
    {
        final String expectedTrackbackLink = "http://localhost:8153/go/files/my-test-pipeline/19/defaultStage/1/myJob/analysis/dirA/scalastyle-result.xml";
        final String expectedComment = "hello world!";

        when(scalastyleResultsAnalyser.buildGithubMarkdownSummary(ARTIFACT_FILE_1, RESULTS_FILE_PATH_1, expectedTrackbackLink)).thenReturn(expectedComment);

        final GoPluginApiResponse response = plugin.handle(executeTaskFromPRRequest(REPOSITORY_URL, PULL_REQUEST_ID, WORKING_DIRECTORY,
                                                                                    newArrayList(RESULTS_FILE_1), newArrayList(ARTIFACT_DIR_1)));

        verify(pullRequestCommenter).addCommentToPullRequest(eq(REPOSITORY_URL), eq(PULL_REQUEST_ID), eq(expectedComment));
        assertEquals("{\"success\":true,\"message\":\"Task execution complete\"}", response.responseBody());
    }

    @Test
    public void shouldCommentIfMultipleScalastyleResultsAreFound() throws Exception
    {
        final String expectedTrackbackLink1 = "http://localhost:8153/go/files/my-test-pipeline/19/defaultStage/1/myJob/analysis/dirA/scalastyle-result.xml";
        final String expectedTrackbackLink2 = "http://localhost:8153/go/files/my-test-pipeline/19/defaultStage/1/myJob/analysis/dirB/scalastyle-result.xml";
        final String expectedComment1 = "hello world!";
        final String expectedComment2 = "why hello, how are you!";

        when(scalastyleResultsAnalyser.buildGithubMarkdownSummary(ARTIFACT_FILE_1, RESULTS_FILE_PATH_1, expectedTrackbackLink1)).thenReturn(expectedComment1);
        when(scalastyleResultsAnalyser.buildGithubMarkdownSummary(ARTIFACT_FILE_2, RESULTS_FILE_PATH_2, expectedTrackbackLink2)).thenReturn(expectedComment2);

        final GoPluginApiResponse response = plugin.handle(executeTaskFromPRRequest(REPOSITORY_URL, PULL_REQUEST_ID, WORKING_DIRECTORY, RESULTS_FILE_LOCATIONS, ARTIFACT_FILE_LOCATIONS));

        verify(pullRequestCommenter).addCommentToPullRequest(eq(REPOSITORY_URL), eq(PULL_REQUEST_ID), eq(expectedComment1 + "\n\n" + expectedComment2));
        assertEquals("{\"success\":true,\"message\":\"Task execution complete\"}", response.responseBody());
    }

    @Test
    public void shouldReturnFailureMessageIfSomethingUnexpectedGoesWrong() throws Exception
    {
        final DefaultGoPluginApiRequest request = executeTaskFromPRRequest(REPOSITORY_URL, PULL_REQUEST_ID, WORKING_DIRECTORY, RESULTS_FILE_LOCATIONS, ARTIFACT_FILE_LOCATIONS);
        final String requestBodyWithAnInvalidPullRequestId = request.requestBody().replace("\"GO_SCM_MY_TEST_PIPELINE_PRS_PR_ID\": \"" + PULL_REQUEST_ID + "\"",
                                                                                           "\"GO_SCM_MY_TEST_PIPELINE_PRS_PR_ID\": \"NotAnInteger\"");
        request.setRequestBody(requestBodyWithAnInvalidPullRequestId);

        final GoPluginApiResponse response = plugin.handle(request);


        verify(pullRequestCommenter, never()).addCommentToPullRequest(any(String.class), anyInt(), any(String.class));
        assertEquals("{\"success\":false,\"message\":\"java.lang.NumberFormatException: For input string: \\\"NotAnInteger\\\"\"}", response.responseBody());
    }
}