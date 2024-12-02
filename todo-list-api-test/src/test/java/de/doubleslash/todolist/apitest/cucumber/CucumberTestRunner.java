package de.doubleslash.todolist.apitest.cucumber;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("de/doubleslash/todolist/apitest/cucumber/features")
public class CucumberTestRunner {
}
