package com.insano10.go.plugin.analysis.scalastyle;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ScalastyleResultsAnalyserTest
{
    private static final Path RESULTS_FILE_PATH = Paths.get("src/test/resources/scalastyle-result.xml");

    private final ScalastyleResultsAnalyser analyser = new ScalastyleResultsAnalyser(RESULTS_FILE_PATH);

    @Test
    public void shouldIndicateWhenResultsAreFound() throws Exception
    {
        assertTrue(analyser.hasResults());
    }

    @Test
    public void shouldIndicateWhenResultsAreNotFound() throws Exception
    {
        final ScalastyleResultsAnalyser analyser = new ScalastyleResultsAnalyser(Paths.get("this/does/not/exist/scalastyle-result.xml"));

        assertFalse(analyser.hasResults());
    }

    @Test
    public void shouldProduceMarkdownSummaryOfResults() throws Exception
    {
        final String summary = analyser.buildGithubMarkdownSummary();

        assertThat(summary, is("0 errors\n265 warnings\n0 info"));
    }
}