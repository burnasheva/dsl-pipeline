import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script

version = "2019.2"

project {

    subProject(ProjectA)
    subProject(ProjectB)
    subProject(ProjectC)

    buildType(StartingBuild)
    buildType(AnotherBuild)
}

object ProjectC : Project({
    name = "Project C"

    sequential {
        parallel {
            buildType(ProjectA_BuildA)
            buildType(ProjectA_BuildB)
            buildType(ProjectA_BuildC)
        }
        buildType(ProjectB_BuildA)
        sequential {
            dependsOn(AnotherBuild)
            buildType(ProjectB_BuildB)
            parallel {
                buildType(ProjectA_Subproject_BuildA)
                buildType(ProjectA_Subproject_BuildB)
            }
        }
        buildType(StartingBuild)
    }
})

object StartingBuild : BuildType({
    name = "Starting build"
    dependencies {
        artifacts(AnotherBuild) {
            cleanDestination = true
            artifactRules = "something.txt"
        }
    }
})

object AnotherBuild : BuildType({
    name = "Another build"
    artifactRules = "something.txt"
})

object ProjectA : Project({
    name = "Project A"

    subProject(ProjectA_Subproject)

    buildType(ProjectA_BuildA)
    buildType(ProjectA_BuildB)
    buildType(ProjectA_BuildC)

})

object ProjectA_BuildA : BuildType({
    name = "Build A"
    artifactRules = "fileA.txt"

    vcs {
        cleanCheckout = true
    }

    steps {
        script {
            scriptContent = "touch fileA.txt"
        }
    }
})

object ProjectA_BuildB : BuildType({
    name = "Build B"
    artifactRules = "fileB.txt"

    vcs {
        cleanCheckout = true
    }

    steps {
        script {
            scriptContent = "touch fileB.txt"
        }
    }
})

object ProjectA_BuildC : BuildType({
    name = "Build C"
    artifactRules = "fileC.txt"

    vcs {
        cleanCheckout = true
    }

    steps {
        script {
            scriptContent = "touch fileC.txt"
        }
    }
})

object ProjectB : Project({
    name = "Project B"

    buildType(ProjectB_BuildA)
    buildType(ProjectB_BuildB)
})

object ProjectB_BuildA : BuildType({
    name = "Build A"
    artifactRules = "fileC.txt"

    dependencies {
        artifacts(ProjectA_BuildA) {
            artifactRules = "fileA.txt"
        }
        artifacts(ProjectA_BuildB) {
            artifactRules = "fileB.txt"
        }
        artifacts(ProjectA_BuildC) {
            artifactRules = "fileC.txt"
        }
    }
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

    dependencies {
        artifacts(ProjectB_BuildA) {
            artifactRules = "fileC.txt=>subforlder"
            cleanDestination = true
            sameChainOrLastFinished()
        }
    }
})

object ProjectA_Subproject_BuildB : BuildType({
    name = "Build B"
})
