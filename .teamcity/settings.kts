import jetbrains.buildServer.configs.kotlin.v2019_2.*

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2019.2"

project {

    project(ProjectA)
    project(ProjectB)

    buildType(StartingBuild)
}

object StartingBuild : BuildType({
    name = "Starting build"
})

object ProjectA : Project({
    name = "Project A"

    project(ProjectA_Subproject)

    buildType(ProjectA_BuildA)
    buildType(ProjectA_BuildB)
})

object ProjectA_BuildA : BuildType({
    name = "Build A"
})

object ProjectA_BuildB : BuildType({
    name = "Build B"
})

object ProjectB : Project({
    name = "Project B"

    buildType(ProjectB_BuildA)
    buildType(ProjectB_BuildB)
})

object ProjectB_BuildA : BuildType({
    name = "Build A"
})

object ProjectB_BuildB : BuildType({
    name = "Build B"
})

object ProjectA_Subproject : Project({
    name = "Subproject"

    buildType(ProjectA_Subproject_BuildA)
    buildType(ProjectA_Subproject_BuildB)
})

object ProjectA_Subproject_BuildA : BuildType({
    name = "Build A"
})

object ProjectA_Subproject_BuildB : BuildType({
    name = "Build B"
})