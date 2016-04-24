package com.insano10.go.plugin;

import com.google.gson.GsonBuilder;
import com.insano10.go.plugin.analysis.scalastyle.ScalastyleResultsAnalyser;
import com.insano10.go.plugin.comment.GitHubPullRequestCommenter;
import com.insano10.go.plugin.settings.PluginSettingsProvider;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.plugin.api.task.JobConsoleLogger;
import org.apache.commons.io.IOUtils;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonList;

@Extension
public class GithubPRCommentScalastylePlugin implements GoPlugin
{
    private static final Logger LOGGER = Logger.getLoggerFor(GithubPRCommentScalastylePlugin.class);
    private static final String DEFAULT_SCALASTYLE_RESULT_FILE_PATH = "target/scalastyle-result.xml";

    private final GitHubPullRequestCommenter pullRequestCommenter;
    private final ScalastyleResultsAnalyser scalastyleResultsAnalyser;

    public GithubPRCommentScalastylePlugin()
    {
        this.pullRequestCommenter = new GitHubPullRequestCommenter(PluginSettingsProvider.getPluginSettings());
        this.scalastyleResultsAnalyser = new ScalastyleResultsAnalyser(Paths.get(DEFAULT_SCALASTYLE_RESULT_FILE_PATH));
    }

    public GithubPRCommentScalastylePlugin(final GitHubPullRequestCommenter pullRequestCommenter, final ScalastyleResultsAnalyser scalastyleResultsAnalyser)
    {
        this.pullRequestCommenter = pullRequestCommenter;
        this.scalastyleResultsAnalyser = scalastyleResultsAnalyser;
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
        return new GoPluginIdentifier(PluginConstants.TASK_PLUGIN_EXTENSION, singletonList(PluginConstants.NOTIFICATION_PLUGIN_EXTENSION_VERSION));
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
        try {
            view.put("template", IOUtils.toString(getClass().getResourceAsStream("/views/task.template.html"), "UTF-8"));
        } catch (Exception e) {
            responseCode = DefaultGoApiResponse.INTERNAL_ERROR;
            String errorMessage = "Failed to find template: " + e.getMessage();
            view.put("exception", errorMessage);
            LOGGER.error(errorMessage, e);
        }
        return renderJSON(responseCode, view);
    }

    private GoPluginApiResponse validateConfiguration(final GoPluginApiRequest request)
    {
        return DefaultGoPluginApiResponse.success("{\"errors\": {}}");
    }

    private GoPluginApiResponse executeTask(final GoPluginApiRequest request)
    {
        final JobConsoleLogger consoleLogger = JobConsoleLogger.getConsoleLogger();
        consoleLogger.printLine("Executing request!");
        consoleLogger.printLine(request.requestName());
        consoleLogger.printLine(request.requestHeaders().toString());
        consoleLogger.printLine(request.requestParameters().toString());
        consoleLogger.printLine(request.requestBody());

        return DefaultGoPluginApiResponse.success("{\"success\": true, \"message\": \"Finished executing task\"}");
    }

    private GoPluginApiResponse renderJSON(final int responseCode, Map response)
    {
        final String responseBody = new GsonBuilder().create().toJson(response);
        final DefaultGoPluginApiResponse apiResponse = new DefaultGoPluginApiResponse(responseCode);

        apiResponse.setResponseBody(responseBody);
        return apiResponse;
    }
}
