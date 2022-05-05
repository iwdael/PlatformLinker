package com.iwdael.platformlinker

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.api.BaseVariant
import groovy.xml.XmlSlurper
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.reflect.KClass

class PlatformLinkerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.all {
            when (it) {
                is AppPlugin -> {
                    project.extensions.getByType(AppExtension::class.java).run {
                        wxEntryActivityGeneration(project, applicationVariants)
                    }
                }
            }
        }
    }

    private fun getPackageName(variant: BaseVariant): String {
        val slurper = XmlSlurper(false, false)
        val list = variant.sourceSets.map { it.manifestFile }
        val result = slurper.parse(list[0])
        return result.getProperty("@package").toString()
    }

    private fun wxEntryActivityGeneration(
        project: Project,
        variants: DomainObjectSet<out BaseVariant>
    ) {
        variants.all { variant ->
            val useAndroidX =
                (project.findProperty("android.useAndroidX") as String?)?.toBoolean() ?: false
            val outputDir = project.buildDir.resolve("generated/source/wx/${variant.dirName}")

            val task = project.tasks.create("generate${variant.name.capitalize()}WXEntryActivity")
            task.inputs.property("useAndroidX", useAndroidX)
            task.outputs.dir(outputDir)
            variant.registerJavaGeneratingTask(task, outputDir)

            val rPackage = getPackageName(variant)
            val once = AtomicBoolean()
            variant.outputs.all { output ->
                val processResources = output.processResources
                task.dependsOn(processResources)
                if (once.compareAndSet(false, true)) {
                    task.apply {
                        doLast {
                            FinalWxAcClassBuilder.brewJava(
                                outputDir,
                                "${rPackage}.wxapi"
                            )
                        }
                    }
                }
            }
        }
    }

    private operator fun <T : Any> ExtensionContainer.get(type: KClass<T>): T {
        return getByType(type.java)!!
    }
}