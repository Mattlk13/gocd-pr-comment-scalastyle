# gocd-pr-comment-scalastyle

This plugin will add scalastyle static analysis results to Github pull requests built using a GoCD pipeline

## Prerequesites:

- GoCD
- A scala project hosted on Github built using sbt with a dependency on the scalastyle plugin
- A plugin to build pull requests -> https://github.com/ashwanthkumar/gocd-build-github-pull-requests


## Plugin installation:

1. Either build the plugin with 'mvn clean install' or download the latest release
2. copy the plugin jar file into the go-server plugins/external folder
3. Set the go server base URL to use in the trackback link (in /etc/default/go-agent)

![Go Agent Properties][1]

4. restart the go-server and go-agents
5. ensure you have a .github file in the go user's home directory with your authentication details

![Github Properties][2]


## Usage:

1) create a pipeline with a Github material that will build a repository's pull requests. 

![Github Material Pipeline][3]

2) add an 'sbt scalastyle' task to one of the stages

![Add Pipeline Tasks][4]

3) save all scalastyle-result.xml files as a pipeline artifacts. You will only have 1 unless you have a multi-project sbt build

![Set Artifact Location][5]

4) add a 'Github PR Comment: Scalastyle' task

5) point the task to the locations of the scalastyle results. If you have a single project build this will tend to be target/scalastyle-result.xml. Otherwise provide a comma
separated list of locations. (e.g. server/target/scalastyle-result.xml, client/target/scalastyle-result.xml)

6) point the task to the folders containing the XML file artifacts (this is needed for the trackback link). For each result file you specified in step 5, specify the matching
artifact location as a comma separated list.

![Scalastyle Plugin Configuration][6]


## Output:

When the task executes, the plugin will examine the scalastyle results XML files and comment on the pull request if it found any errors, warning or infos.<br/>
You can click on the artifact link to view the raw XML results behind the file summary.

![Pull Request Comment][7]


[1]: images/example_go_agent_properties.png  "Go Agent Properties"
[2]: images/example_github_properties.png  "Github Properties"
[3]: images/example_github_material_pipeline.png  "Github Material Pipeline"
[4]: images/example_pipeline_tasks.png  "Pipeline Tasks"
[5]: images/example_artifact_location.png  "Artifact Location"
[6]: images/example_task_configuration.png  "Task Configuration"
[7]: images/example_summary_comment.png  "Summary Comment"