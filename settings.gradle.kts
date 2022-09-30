plugins {
    id("de.fayard.refreshVersions") version "0.50.2"
}

rootProject.name = "deobfuscator"

include(":asm")
include(":updater")
include(":deobfuscator")
include(":util")
