import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}


// Add this line to define version
version = "0.0.4-snapshot"
val iOSBinaryName = "KmmSharedSpm"
/**
 * This task prepares Release Artifact for iOS XCFramework
 * Updates Podspec, Package.swift, Carthage contents with version and checksum
 */
tasks.register("prepareReleaseOfiOSXCFramework") {
    description = "Prepare release artifacts for iOS framework distribution"

    dependsOn("assembleXCFramework", "packageDistribution")

    doLast {
        val distributionDir = File(rootDir, "XCFramework")
        val zipFile = File(distributionDir, "${iOSBinaryName}.xcframework.zip")

        // Compute checksum using Swift Package Manager
        val outputStream = org.apache.commons.io.output.ByteArrayOutputStream()
        exec {
            workingDir = distributionDir
            commandLine("swift", "package", "compute-checksum", zipFile.name)
            standardOutput = outputStream
        }
        val checksumValue = outputStream.toString().trim()
        println("Checksum for ${zipFile.name}: $checksumValue")

        // Update Package.swift with new version and checksum
        val packageSwift = File(rootDir, "Package.swift")
        val tempFile = File(rootDir, "Package.swift.tmp")

        packageSwift.bufferedReader().use { reader ->
            tempFile.bufferedWriter().use { writer ->
                reader.forEachLine { line ->
                    when {
                        line.trim().startsWith("url:") -> writer.write("            url: \"https://github.com/ankush-keka/kmm-sample/releases/download/${version}/${iOSBinaryName}.xcframework.zip\",\n")
                        line.trim().startsWith("checksum:") -> writer.write("            checksum: \"$checksumValue\"\n")
                        else -> writer.write(line + "\n")
                    }
                }
            }
        }
        if (!packageSwift.delete()) {
            throw GradleException("Failed to delete old Package.swift")
        }
        if (!tempFile.renameTo(packageSwift)) {
            throw GradleException("Failed to rename temp Package.swift")
        }
    }
}

/**
 * Task to create zip for XCFramework
 * To be used by distribution as Binary
 */
tasks.register<Zip>("packageDistribution") {
    description = "Package the XCFramework into a ZIP file"

    // Explicitly depend on assembleSharedReleaseXCFramework
    dependsOn("assembleKmmSharedSpmReleaseXCFramework")

    // Source XCFramework directory from the build output
    val xcfSourceDir = layout.buildDirectory.dir("XCFrameworks/release").get().asFile

    // Create a dedicated directory for distribution files
    val distributionDir = File(rootDir, "XCFramework")
    distributionDir.mkdirs()

    // Create the ZIP file in the XCFramework directory
    archiveFileName.set("${iOSBinaryName}.xcframework.zip")
    destinationDirectory.set(distributionDir)

    // Zip the content of the XCFramework directory
    from(xcfSourceDir)

    doFirst {
        logger.lifecycle("Creating ZIP from: ${xcfSourceDir.absolutePath}")
        logger.lifecycle("Output zip will be: ${archiveFile.get().asFile.absolutePath}")

        if (!xcfSourceDir.exists()) {
            throw GradleException("XCFramework source directory not found at: ${xcfSourceDir.absolutePath}")
        }
    }
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                }
            }
        }
    }

    val xcf = XCFramework()
    val iosTargets = listOf(iosArm64(), iosSimulatorArm64())

    iosTargets.forEach {
        it.binaries.framework {
            baseName = "KmmSharedSpm"
            isStatic = true
            xcf.add(this)
        }
    }

    sourceSets {
        commonMain.dependencies {
            //put your multiplatform dependencies here
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.protuts.xcframework"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
