package com.insano10.go.plugin.fixtures;

import com.insano10.go.plugin.PluginConstants;
import com.thoughtworks.go.plugin.api.request.DefaultGoPluginApiRequest;

public class ApiRequestFixtures
{
    public static DefaultGoPluginApiRequest executeTaskRequest(String repositoryUrl, int pullRequestId, final String resultXmlFileLocation)
    {
        final String requestBody = "{" +
                "  \"context\": {" +
                "    \"workingDirectory\": \"pipelines/my-test-pipeline\"," +
                "    \"environmentVariables\": {" +
                "      \"GO_SCM_MY_TEST_PIPELINE_PRS_PR_BRANCH\": \"testUser:myBranch\"," +
                "      \"GO_SERVER_URL\": \"https://127.0.0.1:8154/go/\"," +
                "      \"GO_SCM_MY_TEST_PIPELINE_PRS_TARGET_BRANCH\": \"testUser:master\"," +
                "      \"GO_PIPELINE_LABEL\": \"19\"," +
                "      \"GO_STAGE_NAME\": \"defaultStage\"," +
                "      \"GO_PIPELINE_NAME\": \"my-test-pipeline\"," +
                "      \"GO_SCM_MY_TEST_PIPELINE_PRS_URL\": \"" + repositoryUrl + "\"," +
                "      \"GO_SCM_MY_TEST_PIPELINE_PRS_LABEL\": \"822b7a86dce336d06e429801e384ea4221c29672\"," +
                "      \"GO_SCM_MY_TEST_PIPELINE_PRS_PR_TITLE\": \"random change\"," +
                "      \"GO_SCM_MY_TEST_PIPELINE_PRS_PR_AUTHOR\": \"Jenny Beckett\"," +
                "      \"GO_STAGE_COUNTER\": \"1\"," +
                "      \"GO_PIPELINE_COUNTER\": \"19\"," +
                "      \"GO_JOB_NAME\": \"myJob\"," +
                "      \"GO_SCM_MY_TEST_PIPELINE_PRS_PR_URL\": \"https://github.com/testUser/my-test-repo/pull/" + pullRequestId + "\"," +
                "      \"GO_TRIGGER_USER\": \"anonymous\"," +
                "      \"GO_SCM_MY_TEST_PIPELINE_PRS_PR_DESCRIPTION\": \"testing testing\"," +
                "      \"GO_SCM_MY_TEST_PIPELINE_PRS_PR_ID\": \"" + pullRequestId + "\"" +
                "    }" +
                "  }," +
                "  \"config\": {" +
                "    \"resultXmlFileLocation\": {" +
                "      \"secure\": false," +
                "      \"value\": \"" + resultXmlFileLocation + "\"," +
                "      \"required\": false" +
                "    }" +
                "  }" +
                "}";

        return createRequest(requestBody);
    }

    private static DefaultGoPluginApiRequest createRequest(String requestBody)
    {
        final DefaultGoPluginApiRequest request = new DefaultGoPluginApiRequest(
                PluginConstants.TASK_PLUGIN_EXTENSION,
                PluginConstants.TASK_PLUGIN_EXTENSION_VERSION,
                PluginConstants.REQUEST_NAME_EXECUTE);
        request.setRequestBody(requestBody);
        return request;
    }
}
