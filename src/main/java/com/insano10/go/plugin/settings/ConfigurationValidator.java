package com.insano10.go.plugin.settings;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationValidator
{
    public static GoPluginApiResponse validateConfiguration(final GoPluginApiRequest request)
    {
        final Gson GSON = new GsonBuilder().create();
        final Map dataMap = GSON.fromJson(request.requestBody(), Map.class);

        final Map resultFileConfig = (Map) dataMap.get("resultXmlFileLocations");
        final Map artifactLocationConfig = (Map) dataMap.get("artifactXmlFileLocations");

        final String resultFileValue = (String)resultFileConfig.get("value");
        final String artifactLocValue = (String)artifactLocationConfig.get("value");

        int resultFileCount = resultFileValue.split(",").length;
        int artifactLocCount = artifactLocValue.split(",").length;


        //String.split will not add a token for a trailing delimiter but it will for a leading one
        if(artifactLocValue.trim().length() > 1 && artifactLocValue.trim().charAt(artifactLocValue.length()-1) == ',')
        {
            artifactLocCount++;
        }

        final Map<String, String> errors = new HashMap<>();
        if(resultFileValue.trim().length() == 0)
        {
            errors.put("resultXmlFileLocations", "You must specify at least one result file location");
        }
        else if(resultFileCount < artifactLocCount)
        {
            errors.put("resultXmlFileLocations", "You have specified " + artifactLocCount + " artifact locations but only " + resultFileCount + " result files");
        }
        else if(resultFileCount > artifactLocCount)
        {
            errors.put("artifactXmlFileLocations", "You have specified " + resultFileCount + " result files but only " + artifactLocCount + " artifact locations");
        }

        return DefaultGoPluginApiResponse.success(GSON.toJson(ImmutableMap.of("errors", errors)));
    }
}
