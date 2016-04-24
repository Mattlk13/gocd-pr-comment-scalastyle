package com.insano10.go.plugin.fixtures;

import com.thoughtworks.go.plugin.api.task.JobConsoleLogger;

public class StubJobConsoleLogger extends JobConsoleLogger
{
    @Override
    public void printLine(String line)
    {
        System.out.println(line);
    }
}
