package com.insano10.go.plugin.analysis.scalastyle;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ScalastyleResultsAnalyser
{
    public String buildGithubMarkdownSummary(String artifactFile, final Path resultsFilePath, String trackbackLink)
    {
        final List<String> lines = getLinesFromFile(resultsFilePath);

        int errors = 0;
        int warnings = 0;
        int info = 0;

        for (final String line : lines)
        {
            errors += line.contains("severity=\"error\"") ? 1 : 0;
            warnings += line.contains("severity=\"warning\"") ? 1 : 0;
            info += line.contains("severity=\"info\"") ? 1 : 0;
        }

        return String.format("### :mag:  Scalastyle - [%s](%s)\n" +
                              "\n" +
                              "| Severity |  Issues found |\n" +
                              "| -------- | ------------- |\n" +
                              "| :exclamation:  **Errors**  | %d  |\n" +
                              "| :warning:  **Warnings**  | %d |\n" +
                              "| :information_source:  **Info**  | %d |", artifactFile, trackbackLink, errors, warnings, info);
    }

    private List<String> getLinesFromFile(final Path filePath)
    {
        try
        {
            return Files.readAllLines(filePath, Charset.forName("UTF-8"));
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to read scalastyle results file", e);
        }
    }
}
