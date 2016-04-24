package com.insano10.go.plugin.fixtures;

import com.insano10.go.plugin.PluginConstants;
import com.thoughtworks.go.plugin.api.request.DefaultGoPluginApiRequest;

public class ApiRequestFixtures
{
    public static DefaultGoPluginApiRequest stageStatusRequestMasterBuilding(final String repositoryUrl)
    {
        final String requestBody = "{" +
                "  \"pipeline\": {" +
                "    \"name\": \"my-test-repo\"," +
                "    \"counter\": \"1\"," +
                "    \"group\": \"pull-requests\"," +
                "    \"build-cause\": [" +
                "      {" +
                "        \"material\": {" +
                "          \"plugin-id\": \"github.pr\"," +
                "          \"scm-configuration\": {" +
                "            \"url\": \"" + repositoryUrl + "\"" +
                "          }," +
                "          \"type\": \"scm\"" +
                "        }," +
                "        \"changed\": true," +
                "        \"modifications\": [" +
                "          {" +
                "            \"revision\": \"ee8c8de6619ad353c8ed922027e58ebeb1c66cc5\"," +
                "            \"modified-time\": \"2016-03-13T15:48:23.000Z\"," +
                "            \"data\": {" +
                "              \"PR_ID\": \"master\"" +
                "            }" +
                "          }" +
                "        ]" +
                "      }" +
                "    ]," +
                "    \"stage\": {" +
                "      \"name\": \"defaultStage\"," +
                "      \"counter\": \"1\"," +
                "      \"approval-type\": \"success\"," +
                "      \"approved-by\": \"changes\"," +
                "      \"state\": \"Building\"," +
                "      \"result\": \"Unknown\"," +
                "      \"create-time\": \"2016-04-22T14:40:39.799Z\"," +
                "      \"last-transition-time\": \"\"," +
                "      \"jobs \": [" +
                "        {" +
                "          \"name\": \"compile\"," +
                "          \"schedule-time\": \"2016-04-22T14:40:39.799Z\"," +
                "          \"complete-time\": \"\"," +
                "          \"state\": \"Scheduled\"," +
                "          \"result\": \"Unknown\"" +
                "        }" +
                "      ]" +
                "    }" +
                "  }" +
                "}";

        return createRequest(requestBody);
    }

    public static DefaultGoPluginApiRequest stageStatusRequestMasterPassed(final String repositoryUrl)
    {
        final String requestBody = "{" +
                "  \"pipeline\": {" +
                "    \"name\": \"my-test-repo\"," +
                "    \"counter\": \"1\"," +
                "    \"group\": \"pull-requests\"," +
                "    \"build-cause\": [" +
                "      {" +
                "        \"material\": {" +
                "          \"plugin-id\": \"github.pr\"," +
                "          \"scm-configuration\": {" +
                "            \"url\": \"" + repositoryUrl + "\"" +
                "          }," +
                "          \"type\": \"scm\"" +
                "        }," +
                "        \"changed\": true," +
                "        \"modifications\": [" +
                "          {" +
                "            \"revision\": \"ee8c8de6619ad353c8ed922027e58ebeb1c66cc5\"," +
                "            \"modified-time\": \"2016-03-13T15:48:23.000Z\"," +
                "            \"data\": {" +
                "              \"PR_ID\": \"master\"" +
                "            }" +
                "          }" +
                "        ]" +
                "      }" +
                "    ]," +
                "    \"stage\": {" +
                "      \"name\": \"defaultStage\"," +
                "      \"counter\": \"1\"," +
                "      \"approval-type\": \"success\"," +
                "      \"approved-by\": \"changes\"," +
                "      \"state\": \"Passed\"," +
                "      \"result\": \"Passed\"," +
                "      \"create-time\": \"2016-04-22T14:40:39.799Z\"," +
                "      \"last-transition-time\": \"2016-04-22T14:44:52.054Z\"," +
                "      \"jobs\": [" +
                "        {" +
                "          \"name\": \"compile\"," +
                "          \"schedule-time\": \"2016-04-22T14:40:39.799Z\"," +
                "          \"complete-time\": \"2016-04-22T14:44:52.054Z\"," +
                "          \"state\": \"Completed\"," +
                "          \"result\": \"Passed\"," +
                "          \"agent-uuid\": \"306af67b-420e-4b8b-8b7b-0eef119670e0\"" +
                "        }" +
                "      ]" +
                "    }" +
                "  }" +
                "}";
        return createRequest(requestBody);
    }

    public static DefaultGoPluginApiRequest stageStatusRequestPullRequestBuilding(final String repositoryUrl, final int pullRequestId)
    {
        final String requestBody = "{" +
                "  \"pipeline\": {" +
                "    \"name\": \"my-test-repo\"," +
                "    \"counter\": \"9\"," +
                "    \"group\": \"pull-requests\"," +
                "    \"build-cause\": [" +
                "      {" +
                "        \"material\": {" +
                "          \"plugin-id\": \"github.pr\"," +
                "          \"scm-configuration\": {" +
                "            \"url\": \"" + repositoryUrl + "\"" +
                "          }," +
                "          \"type\": \"scm\"" +
                "        }," +
                "        \"changed\": true," +
                "        \"modifications\": [" +
                "          {" +
                "            \"revision\": \"6e0590979ab748122bdb9df7a0d7a3cb30507ba6\"," +
                "            \"modified-time\": \"2016-04-22T16:31:55.000Z\"," +
                "            \"data\": {" +
                "              \"PR_TITLE\": \"random change\"," +
                "              \"PR_URL\": \"https://github.com/testUser/my-test-repo/pull/" + pullRequestId + "\"," +
                "              \"PR_DESCRIPTION\": \"testing testing\"," +
                "              \"PR_ID\": \"" + pullRequestId + "\"," +
                "              \"PR_BRANCH\": \"testUser:myBranch\"," +
                "              \"PR_AUTHOR\": \"Someone\"," +
                "              \"TARGET_BRANCH\": \"testUser:master\"" +
                "            }" +
                "          }" +
                "        ]" +
                "      }" +
                "    ]," +
                "    \"stage\": {" +
                "      \"name\": \"defaultStage\"," +
                "      \"counter\": \"1\"," +
                "      \"approval-type\": \"success\"," +
                "      \"approved-by\": \"anonymous\"," +
                "      \"state\": \"Building\"," +
                "      \"result\": \"Unknown\"," +
                "      \"create-time\": \"2016-04-22T16:34:54.294Z\"," +
                "      \"last-transition-time\": \"\"," +
                "      \"jobs\": [" +
                "        {" +
                "          \"name\": \"compile\"," +
                "          \"schedule-time\": \"2016-04-22T16:34:54.294Z\"," +
                "          \"complete-time\": \"\"," +
                "          \"state\": \"Scheduled\"," +
                "          \"result\": \"Unknown\"" +
                "        }" +
                "      ]" +
                "    }" +
                "  }" +
                "}";

        return createRequest(requestBody);
    }

    public static DefaultGoPluginApiRequest stageStatusRequestPullRequestPassed(final String repositoryUrl, final int pullRequestId)
    {
        final String requestBody = "{" +
                "  \"pipeline\": {" +
                "    \"name\": \"my-test-repo\"," +
                "    \"counter\": \"9\"," +
                "    \"group\": \"pull-requests\"," +
                "    \"build-cause\": [" +
                "      {" +
                "        \"material\": {" +
                "          \"plugin-id\": \"github.pr\"," +
                "          \"scm-configuration\": {" +
                "            \"url\": \"" + repositoryUrl + "\"" +
                "          }," +
                "          \"type\": \"scm\"" +
                "        }," +
                "        \"changed\": true," +
                "        \"modifications\": [" +
                "          {" +
                "            \"revision\": \"6e0590979ab748122bdb9df7a0d7a3cb30507ba6\"," +
                "            \"modified-time\": \"2016-04-22T16:31:55.000Z\"," +
                "            \"data\": {" +
                "              \"PR_TITLE\": \"random change\"," +
                "              \"PR_URL\": \"https://github.com/testUser/my-test-repo/pull/" + pullRequestId + "\"," +
                "              \"PR_DESCRIPTION\": \"testing testing\"," +
                "              \"PR_ID\": \"" + pullRequestId + "\"," +
                "              \"PR_BRANCH\": \"testUser:myBranch\"," +
                "              \"PR_AUTHOR\": \"Someone\"," +
                "              \"TARGET_BRANCH\": \"testUser:master\"" +
                "            }" +
                "          }" +
                "        ]" +
                "      }" +
                "    ]," +
                "    \"stage\": {" +
                "      \"name\": \"defaultStage\"," +
                "      \"counter\": \"1\"," +
                "      \"approval-type\": \"success\"," +
                "      \"approved-by\": \"anonymous\"," +
                "      \"state\": \"Passed\"," +
                "      \"result\": \"Passed\"," +
                "      \"create-time\": \"2016-04-22T16:34:54.294Z\"," +
                "      \"last-transition-time\": \"2016-04-22T16:35:38.608Z\"," +
                "      \"jobs\": [" +
                "        {" +
                "          \"name\": \"compile\"," +
                "          \"schedule-time\": \"2016-04-22T16:34:54.294Z\"," +
                "          \"complete-time\": \"2016-04-22T16:35:38.608Z\"," +
                "          \"state\": \"Completed\"," +
                "          \"result\": \"Passed\"," +
                "          \"agent-uuid\": \"306af67b-420e-4b8b-8b7b-0eef119670e0\"" +
                "        }" +
                "      ]" +
                "    }" +
                "  }" +
                "}";
        return createRequest(requestBody);
    }

    private static DefaultGoPluginApiRequest createRequest(String requestBody)
    {
        final DefaultGoPluginApiRequest request = new DefaultGoPluginApiRequest(
                PluginConstants.NOTIFICATION_PLUGIN_EXTENSION,
                PluginConstants.NOTIFICATION_PLUGIN_EXTENSION_VERSION,
                PluginConstants.REQUEST_NAME_STAGE_STATUS);
        request.setRequestBody(requestBody);
        return request;
    }
}
