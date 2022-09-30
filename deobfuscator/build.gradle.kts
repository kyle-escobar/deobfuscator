plugins {
    application
}

dependencies {
    implementation(project(":asm"))
    implementation("org.tinylog:tinylog-api-kotlin:_")
    implementation("org.tinylog:tinylog-impl:_")
}

application {
    mainClass.set("io.rsbox.deobfuscator.Deobfuscator")
}

tasks.named<JavaExec>("run") {
    workingDir = rootProject.projectDir
}