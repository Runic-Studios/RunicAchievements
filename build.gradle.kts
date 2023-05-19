plugins {
    `java-library`
    `maven-publish`
}

group = "com.runicrealms.plugin"
version = "1.0-SNAPSHOT"

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
    compileOnly(project(":Projects:Core"))
    compileOnly(project(":Projects:Guilds"))
    compileOnly(project(":Projects:Items"))
    compileOnly(project(":Projects:Professions"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.runicrealms.plugin"
            artifactId = "achievements"
            version = "1.0-SNAPSHOT"
            from(components["java"])
        }
    }
}

tasks.register("wrapper")
tasks.register("prepareKotlinBuildScriptModel")