# gocd-pr-comment-scalastyle

This plugin will add scalastyle static analysis results to Github pull requests built using a GoCD pipeline

Prerequesites:

- GoCD
- A scala project hosted on Github built using sbt with a dependency on the scalastyle plugin
- A plugin to build pull requests -> https://github.com/ashwanthkumar/gocd-build-github-pull-requests


Plugin installation:

1. mvn clean install
2. copy the generated jar file into the go-server plugins/external folder
3. Set the go server base URL to use in the trackback link


    /etc/default/go-server
    
    export GO_SERVER_SYSTEM_PROPERTIES=-Dgo.plugin.github.pr.comment.go-server=http://mygoserver.com
    
4. restart the go-server
5. ensure you have a .github file in the go user's home directory with your authentication details

e.g.

    ~/.github
    
    login=someUser
    oauth=myGeneratedOauthTokenFromGithub

Usage:

1. create a pipeline with a Github material that will build a repository's pull requests
2. add an 'sbt scalastyle' task to one of the stages
3. save the scalastyle-result.xml file as a pipeline artifact
4. add a 'Github PR Comment: Scalastyle' task
5. point the task to the location of the scalastyle results (this will tend to be target/scalastyle-result.xml)
6. point the task to the folder containing the XML file artifact (this is needed for the trackback link) 

When the task executes, the plugin will examine the scalastyle results XML file and comment on the pull request with a summary of the findings

