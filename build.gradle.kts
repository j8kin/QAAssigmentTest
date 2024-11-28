plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "org.qatest.plugin"
version = "0.1.0-SNAPSHOT"
var remoteRobotVersion = "0.11.23"

repositories {
    mavenCentral()
    maven("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies")
}

dependencies {
    api("com.squareup.okhttp3:okhttp:4.12.0")
    testImplementation("com.intellij.remoterobot:remote-robot:$remoteRobotVersion")
    testImplementation("com.intellij.remoterobot:remote-fixtures:$remoteRobotVersion")
    testImplementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.2")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.2.8")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf(/* Plugin Dependencies */))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("242.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    runIdeForUiTests {
//    In case your Idea is launched on remote machine you can enable public port and enable encryption of JS calls
//    systemProperty "robot-server.host.public", "true"
//    systemProperty "robot.encryption.enabled", "true"
//    systemProperty "robot.encryption.password", "my super secret"

        systemProperty("robot-server.port", "8082")
//        systemProperty "ide.mac.message.dialogs.as.sheets", "false"
        systemProperty("jb.privacy.policy.text", "<!--999.999-->")
        systemProperty("jb.consents.confirmation.enabled", "false")
//        systemProperty "ide.mac.file.chooser.native", "false"
//        systemProperty "jbScreenMenuBar.enabled", "false"
//        systemProperty "apple.laf.useScreenMenuBar", "false"
        systemProperty("idea.trust.all.projects", "true")
        systemProperty("ide.show.tips.on.startup.default.value", "false")
//    systemProperty "eap.require.license", "true"
    }

    test {
        // enable here nad in runIdeForUiTests block - to log the retrofit HTTP calls
        // systemProperty "debug-retrofit", "enable"

        // enable encryption on test side when use remote machine
        // systemProperty "robot.encryption.password", "my super secret"
        useJUnitPlatform()
    }
}
