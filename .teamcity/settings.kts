import jetbrains.buildServer.configs.kotlin.v2019_2.*

version = "2019.2"

project {

    subProject(ProjectA)
    subProject(ProjectB)

    buildType(StartingBuild)

    seq.buildTypes().forEach { buildType(it) }

}

sequential {
    buildType(ProjectA_BuildA)
    buildType(ProjectA_BuildB)
    buildType(ProjectA_BuildC)
}

object StartingBuild : BuildType({
    name = "Starting build"
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
})

object ProjectA_BuildB : BuildType({
    name = "Build B"
})

object ProjectA_BuildC : BuildType({
    name = "Build C"
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

//    buildType(ProjectA_Subproject_BuildA)
//    buildType(ProjectA_Subproject_BuildB)
})

val seq = sequential {
    buildType {
        id("ProjectA_Subproject_BuildA")
        name = "Build A"
    }
    buildType {
        id("ProjectA_Subproject_BuildB")
        name = "Build B"
    }
}
//
//object ProjectA_Subproject_BuildA : BuildType({
//    name = "Build A"
//})
//
//object ProjectA_Subproject_BuildB : BuildType({
//    name = "Build B"
//})
