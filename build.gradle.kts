val artifactName = "achievements"
val rrGroup: String by rootProject.extra
val rrVersion: String by rootProject.extra

plugins {
    `java-library`
    `maven-publish`
}

version = rrVersion
group = rrGroup

dependencies {
    compileOnly(commonLibs.paper)
    compileOnly(commonLibs.spigot)
    compileOnly(commonLibs.jedis)
    compileOnly(commonLibs.mythicmobs)
    compileOnly(commonLibs.craftbukkit)
    compileOnly(commonLibs.worldguardevents)
    compileOnly(commonLibs.acf)
    compileOnly(commonLibs.taskchain)
    compileOnly(commonLibs.springdatamongodb)
    compileOnly(commonLibs.mongodbdrivercore)
    compileOnly(commonLibs.mongodbdriversync)
    compileOnly(commonLibs.holographicdisplays)
    compileOnly(commonLibs.placeholderapi)
    compileOnly(commonLibs.luckperms)
    compileOnly(project(":Projects:Core"))
    compileOnly(project(":Projects:Guilds"))
    compileOnly(project(":Projects:Items"))
    compileOnly(project(":Projects:Professions"))
    compileOnly(project(":Projects:Database"))
    compileOnly(project(":Projects:Common"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = rrGroup
            artifactId = artifactName
            version = rrVersion
            from(components["java"])
        }
    }
}