import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script

version = "2019.2"

project {

    subProject(ProjectA)
    subProject(ProjectB)
    subProject(ProjectC)

    buildType(StartingBuild)

}

object ProjectC : Project({
    name = "Project C"

    sequential {
        buildType(StartingBuild)
        parallel {
            buildType(ProjectA_BuildA) {
                produces("fileA.txt")
            }
            buildType(ProjectA_BuildB) {
                produces("fileB.txt")
            }
            buildType(ProjectA_BuildC) {
                produces("fileC.txt")
            }
        }
        buildType(ProjectB_BuildA) {
            consumes(ProjectA_BuildA, "fileA.txt")
            consumes(ProjectA_BuildB, "fileB.txt")
            consumes(ProjectA_BuildC, "fileC.txt")
            produces("fileC.txt")
        }
        buildType(ProjectB_BuildB) {
            dependsOn(StartingBuild)
        }
        parallel {
            buildType(ProjectA_Subproject_BuildA) {
                consumes(ProjectB_BuildA, "fileC.txt") {
                    cleanDestination = true
                    artifactRules = "fileC.txt=>subforlder"
                    sameChainOrLastFinished()
                }
            }
            buildType(ProjectA_Subproject_BuildB)
        }
    }
})

object StartingBuild : BuildType({
    name = "Starting build"
})

object ProjectA : Project({
    name = "Project A"

    subProject(ProjectA_Subproject)

    buildType(ProjectA_BuildA)
    buildType(ProjectA_BuildB)
    buildType(ProjectA_BuildC)

//    sequential {
//        parallel {
//            buildType(ProjectA_Subproject_BuildA)
//            buildType(ProjectA_Subproject_BuildB)
//        }
//        parallel {
//            buildType(ProjectB_BuildA)
//            buildType(ProjectB_BuildB)
//        }
//        parallel {
//            buildType(ProjectA_BuildA)
//            buildType(ProjectA_BuildB)
//            buildType(ProjectA_BuildC)
//        }
//        buildType(StartingBuild)
//    }
})

object ProjectA_BuildA : BuildType({
    name = "Build A"

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
