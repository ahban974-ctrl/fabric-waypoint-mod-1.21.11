plugins {
	id("fabric-loom") version "1.6.+"
	id("maven-publish")
}

version = project.property("mod_version")!!
group = project.property("maven_group")!!

base {
	archivesName = project.property("archives_base_name")!!
}

repositories {
	mavenCentral()
	maven("https://api.modrinth.com/maven")
	maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
	minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
	mappings(loom.officialMojangMappings())
	modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
}

tasks.processResources {
	inputs.property("version", project.version)
	inputs.property("group", project.group)
	filteringCharacterEncoding = "UTF-8"

	filesMatching("fabric.mod.json") {
		expand(mapOf(
			"version" to project.version,
			"group" to project.group
		))
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 21
}

java {
	withSourcesJar()
	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}

	repositories {
		maven("file://${System.getProperty("user.home")}/.m2/repository")
	}
}
