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

3) save the scalastyle-result.xml file as a pipeline artifact

![Set Artifact Location][5]

4) add a 'Github PR Comment: Scalastyle' task

5) point the task to the location of the scalastyle results (this will tend to be target/scalastyle-result.xml)

6) point the task to the folder containing the XML file artifact (this is needed for the trackback link) 

![Scalastyle Plugin Configuration][6]


## Output:

When the task executes, the plugin will examine the scalastyle results XML file and comment on the pull request with a summary of the findings. 
You can click on the 'details' link to view the raw XML results behind the summary.

![Pull Request Comment][7]


[1]: images/example_go_agent_properties.png  "Go Agent Properties"
[2]: images/example_github_properties.png  "Github Properties"
[3]: images/example_github_material_pipeline.png  "Github Material Pipeline"
[4]: images/example_pipeline_tasks.png  "Pipeline Tasks"
[5]: images/example_artifact_location.png  "Artifact Location"
[6]: images/example_task_configuration.png  "Task Configuration"
[7]: images/example_summary_comment.png  "Summary Comment"