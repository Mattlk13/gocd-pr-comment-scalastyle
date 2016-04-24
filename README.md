# gocd-scalastyle-notifier

This plugin will add scalastyle static analysis results to Github pull requests built using a GoCD pipeline

Prerequesites:

- GoCD
- A scala project hosted on Github built using sbt with a dependency on the scalastyle plugin
- A plugin to build pull requests -> https://github.com/ashwanthkumar/gocd-build-github-pull-requests


Plugin installation:

1. mvn clean install
2. copy the generated jar file into the go-server plugins/external folder
3. restart the go-server
4. ensure you have a .github file in the go user's home directory with your authentication details

e.g.

    ~/.github
    
    login=someUser
    oauth=myGeneratedOauthTokenFromGithub

Usage:

1. create a pipeline with a Github material that will build a repository's pull requests
2. add an 'sbt scalastyle' task to one of the stages

When the pipeline completes successfully, the plugin will examine the scalastyle results XML file and comment on the pull request with a summary of the findings

