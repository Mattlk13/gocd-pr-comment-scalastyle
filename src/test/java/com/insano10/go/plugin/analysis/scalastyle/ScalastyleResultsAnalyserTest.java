package com.insano10.go.plugin.analysis.scalastyle;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ScalastyleResultsAnalyserTest
{
    private static final Path RESULTS_FILE_PATH = Paths.get("src/test/resources/dirA/scalastyle-result.xml");
    private static final String TRACKBACK_LINK = "http://go/here";
    private static final String ARTIFACT_FILE = "artifact/file.xml";

    private final ScalastyleResultsAnalyser analyser = new ScalastyleResultsAnalyser();


    @Test
    public void shouldProduceMarkdownSummaryOfResults() throws Exception
    {
        final String summary = analyser.buildGithubMarkdownSummary(ARTIFACT_FILE, RESULTS_FILE_PATH, TRACKBACK_LINK);

        assertThat(summary, is("### :mag:  Scalastyle - [" + ARTIFACT_FILE + "](" + TRACKBACK_LINK + ")\n" +
                                       "\n" +
                                       "| Severity |  Issues found |\n" +
                                       "| -------- | ------------- |\n" +
                                       "| :exclamation:  **Errors**  | 0  |\n" +
                                       "| :warning:  **Warnings**  | 265 |\n" +
                                       "| :information_source:  **Info**  | 0 |"));
    }
}