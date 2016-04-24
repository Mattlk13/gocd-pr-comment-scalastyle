package com.insano10.go.plugin;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.insano10.go.plugin.analysis.scalastyle.ScalastyleResultsAnalyser;
import com.insano10.go.plugin.comment.GitHubPullRequestCommenter;
import com.insano10.go.plugin.settings.PluginSettingsProvider;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.plugin.api.task.JobConsoleLogger;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonList;

@Extension
public class GithubPRCommentScalastylePlugin implements GoPlugin
{
    private final JobConsoleLogger consoleLogger;
    private final GitHubPullRequestCommenter pullRequestCommenter;
    private final ScalastyleResultsAnalyser scalastyleResultsAnalyser;

    public GithubPRCommentScalastylePlugin()
    {
        this.pullRequestCommenter = new GitHubPullRequestCommenter(PluginSettingsProvider.getPluginSettings());
        this.scalastyleResultsAnalyser = new ScalastyleResultsAnalyser();
        this.consoleLogger = JobConsoleLogger.getConsoleLogger();
    }

    public GithubPRCommentScalastylePlugin(final GitHubPullRequestCommenter pullRequestCommenter,
                                           final ScalastyleResultsAnalyser scalastyleResultsAnalyser,
                                           final JobConsoleLogger consoleLogger)
    {
        this.pullRequestCommenter = pullRequestCommenter;
        this.scalastyleResultsAnalyser = scalastyleResultsAnalyser;
        this.consoleLogger = consoleLogger;
    }

    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor goApplicationAccessor)
    {
    }

    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest request) throws UnhandledRequestTypeException
    {
        switch (request.requestName())
        {
            case PluginConstants.REQUEST_NAME_GET_TASK_CONFIGURATION:
                return handleGetConfigRequest();
            case PluginConstants.REQUEST_NAME_GET_TASK_VIEW:
                return handleTaskView();
            case PluginConstants.REQUEST_NAME_VALIDATE_CONFIGURATION:
                return validateConfiguration(request);
            case PluginConstants.REQUEST_NAME_EXECUTE:
                return executeTask(request);
            default:
                throw new UnhandledRequestTypeException(request.requestName());
        }
    }

    @Override
    public GoPluginIdentifier pluginIdentifier()
    {
        return new GoPluginIdentifier(PluginConstants.TASK_PLUGIN_EXTENSION, singletonList(PluginConstants.TASK_PLUGIN_EXTENSION_VERSION));
    }

    private GoPluginApiResponse handleGetConfigRequest()
    {
        return DefaultGoPluginApiResponse.success("{" +
                                                          "  \"resultXmlFileLocation\": {" +
                                                          "    \"default-value\": \"target/scalastyle-result.xml\"," +
                                                          "    \"secure\": false," +
                                                          "    \"required\": true" +
                                                          "  }" +
                                                          "}");
    }

    private GoPluginApiResponse handleTaskView()
    {
        int responseCode = DefaultGoApiResponse.SUCCESS_RESPONSE_CODE;
        HashMap view = new HashMap();
        view.put("displayValue", "Github PR Comment: Scalastyle");
        try
        {
            view.put("template", IOUtils.toString(getClass().getResourceAsStream("/views/task.template.html"), "UTF-8"));
        }
        catch (Exception e)
        {
            responseCode = DefaultGoApiResponse.INTERNAL_ERROR;
            String errorMessage = "Failed to find template: " + e.getMessage();
            view.put("exception", errorMessage);
            consoleLogger.printLine("[Error]: " + errorMessage + " -> " + e.toString());
        }
        return renderJSON(responseCode, view);
    }

    private GoPluginApiResponse validateConfiguration(final GoPluginApiRequest request)
    {
        return DefaultGoPluginApiResponse.success("{\"errors\": {}}");
    }

    private GoPluginApiResponse executeTask(final GoPluginApiRequest request)
    {
        consoleLogger.printLine("Task executing...");
        consoleLogger.printLine("Request parameters: " + request.requestBody());

        try
        {
            final Gson GSON = new GsonBuilder().create();
            final Map dataMap = GSON.fromJson(request.requestBody(), Map.class);

            Map configData = (Map) dataMap.get("config");
            Map resultsFileData = (Map) configData.get("resultXmlFileLocation");
            String resultXmlFileLocation = (String) resultsFileData.get("value");

            final Path resultsFilePath = Paths.get(resultXmlFileLocation);

            if (Files.exists(resultsFilePath))
            {
                final String summary = scalastyleResultsAnalyser.buildGithubMarkdownSummary(resultsFilePath);

                Map contextData = (Map) dataMap.get("context");
                Map envVarsData = (Map) contextData.get("environmentVariables");

                String url = (String) envVarsData.get(getKeyLike(envVarsData, "^GO_SCM.*PRS_URL$"));
                int pullRequestId = Integer.parseInt((String)envVarsData.get(getKeyLike(envVarsData, "^GO_SCM.*PRS_PR_ID$")));

                pullRequestCommenter.addCommentToPullRequest(url, pullRequestId, summary);

                consoleLogger.printLine("Summary successfully added to " + url + ", PR ID [" + pullRequestId + "]");
            }
            else
            {
                consoleLogger.printLine("No Scalastyle results found at " + resultsFilePath.toAbsolutePath());
            }

            consoleLogger.printLine("Task execution complete.");
            return DefaultGoPluginApiResponse.success("{\"success\": true, \"message\": \"Task execution complete\"}");
        }
        catch (Exception e)
        {
            consoleLogger.printLine("[Error]: Error -> " + e.toString());
            return renderJSON(HttpStatus.SC_INTERNAL_SERVER_ERROR, ImmutableMap.of("status", "failure", "messages", singletonList(e.toString())));
        }
    }

    private String getKeyLike(final Map<String, Object> map, final String regex)
    {
        for (String key : map.keySet())
        {
            if(key.matches(regex))
            {
                return key;
            }
        }
        return null;
    }

    private GoPluginApiResponse renderJSON(final int responseCode, Map response)
    {
        final String responseBody = new GsonBuilder().create().toJson(response);
        final DefaultGoPluginApiResponse apiResponse = new DefaultGoPluginApiResponse(responseCode);

        apiResponse.setResponseBody(responseBody);
        return apiResponse;
    }
}
