package com.insano10.go.plugin.analysis.scalastyle;

import com.thoughtworks.go.plugin.api.logging.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ScalastyleResultsAnalyser
{
    private static final Logger LOGGER = Logger.getLoggerFor(ScalastyleResultsAnalyser.class);

    public String buildGithubMarkdownSummary(final Path resultsFilePath)
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

        return String.format("%d errors\n%d warnings\n%d info", errors, warnings, info);
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
