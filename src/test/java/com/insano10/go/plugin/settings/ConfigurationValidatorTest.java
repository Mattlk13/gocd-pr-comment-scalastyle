package com.insano10.go.plugin.settings;

import com.insano10.go.plugin.fixtures.ApiRequestFixtures;
import com.thoughtworks.go.plugin.api.request.DefaultGoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Test;

import static com.insano10.go.plugin.settings.ConfigurationValidator.validateConfiguration;
import static org.junit.Assert.*;

public class ConfigurationValidatorTest
{

    @Test
    public void shouldValidateConfigWithOneResultFileAsARootArtifact() throws Exception
    {
        final DefaultGoPluginApiRequest request = ApiRequestFixtures.validateConfigRequest("/target/file.xml", "");

        final GoPluginApiResponse response = validateConfiguration(request);

        assertEquals("{\"errors\":{}}", response.responseBody());
    }

    @Test
    public void shouldValidateConfigWithOneResultFileInAnArtifactFolder() throws Exception
    {
        final DefaultGoPluginApiRequest request = ApiRequestFixtures.validateConfigRequest("/target/file.xml", "analysis/");

        final GoPluginApiResponse response = validateConfiguration(request);

        assertEquals("{\"errors\":{}}", response.responseBody());
    }

    @Test
    public void shouldValidateConfigWithTwoResultFilesWithOneAsARootArtifact() throws Exception
    {
        final DefaultGoPluginApiRequest request1 = ApiRequestFixtures.validateConfigRequest("/server/target/file.xml,/client/target/file.xml", ",analysis/");
        final DefaultGoPluginApiRequest request2 = ApiRequestFixtures.validateConfigRequest("/server/target/file.xml,/client/target/file.xml", "analysis/,");

        assertEquals("{\"errors\":{}}", validateConfiguration(request1).responseBody());
        assertEquals("{\"errors\":{}}", validateConfiguration(request2).responseBody());
    }

    @Test
    public void shouldValidateConfigWithTwoResultFilesInArtifactFolders() throws Exception
    {
        final DefaultGoPluginApiRequest request = ApiRequestFixtures.validateConfigRequest("/server/target/file.xml,/client/target/file.xml", "analysis/server, analysis/client");

        final GoPluginApiResponse response = validateConfiguration(request);

        assertEquals("{\"errors\":{}}", response.responseBody());
    }

    @Test
    public void shouldNotValidateConfigWithEmptyResultFileValue() throws Exception
    {
        final DefaultGoPluginApiRequest request = ApiRequestFixtures.validateConfigRequest("", "");

        final GoPluginApiResponse response = validateConfiguration(request);

        assertEquals("{\"errors\":{\"resultXmlFileLocations\":\"You must specify at least one result file location\"}}", response.responseBody());
    }

    @Test
    public void shouldNotValidateConfigWithAsymmetricNumbersOfResultsAndArtifacts() throws Exception
    {
        final DefaultGoPluginApiRequest request1 = ApiRequestFixtures.validateConfigRequest("/server/target/file.xml, /client/target/file.xml", "analysis/");
        final DefaultGoPluginApiRequest request2 = ApiRequestFixtures.validateConfigRequest("/client/target/file.xml", "analysis/server, analysis/client");

        assertEquals("{\"errors\":{\"artifactXmlFileLocations\":\"You have specified 2 result files but only 1 artifact locations\"}}", validateConfiguration(request1).responseBody());
        assertEquals("{\"errors\":{\"resultXmlFileLocations\":\"You have specified 2 artifact locations but only 1 result files\"}}", validateConfiguration(request2).responseBody());
    }
}