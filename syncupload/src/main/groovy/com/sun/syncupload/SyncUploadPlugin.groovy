package com.sun.syncupload

import com.sun.syncupload.task.*
import com.sun.syncupload.model.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.bundling.Jar

class SyncUploadPlugin implements Plugin<Project>{

    /**
     * 这里的GROUP_NAME会出现在Gradle列表的Tasks内
     */
    private static final String GROUP_NAME = "syncUpload"

    private projectsWithName = new HashMap<String, Project>()

    @Override
    void apply(Project project) {
        project.getExtensions().create(SyncUploadExtension.NAME, SyncUploadExtension.class, project)
        def suffixTask = project.tasks.create(SuffixSyncUploadTask.NAME, SuffixSyncUploadTask.class)
        suffixTask.setGroup(GROUP_NAME)
        def preTask = project.tasks.create(PrefixSyncUploadTask.NAME, PrefixSyncUploadTask.class)

        project.afterEvaluate {
            // 真正添加了自定义扩展的代码
            def extension = project.extensions.getByName(SyncUploadExtension.NAME)
            def artifactIds = new ArrayList<String>()
            def modules = new ArrayList<UploadModule>()
            extension.modules.each {
                modules.add(it)
                artifactIds.add(it.pomGroupId + it.pomArtifactId)
            }
            // 保证发布顺序
            Collections.sort(modules)
            Collections.reverse(modules)

            // 添加上传任务和脚本执行任务
            projectsWithName.clear()
            project.subprojects { Project subProject ->
                projectsWithName.put(subProject.getName(), subProject)
            }
            project.subprojects { Project subProject ->
                modules.each { UploadModule module ->
                    if(projectsWithName.get(module.name).name == subProject.name) {
                        // 添加uploadArchives任务
                        subProject.apply([plugin: 'maven'])
                        subProject.uploadArchives {
                            doFirst {
                                println("uploadArchives doFirst")
                            }
                            doLast {
                                println("====== 发布成功 =======")
                                println("pomGroupId: " + module.pomGroupId)
                                println("pomArtifactId: " + module.pomArtifactId)
                                println("pomName: " + module.pomName)
                                println("pomVersion: " + module.pomVersion)
                                println("pomPackaging: " + module.pomPackaging)
                                println("====== 发布成功 =======")
                            }
                            repositories {
                                mavenDeployer {
                                    pom.project {
                                        pom.groupId = module.pomGroupId
                                        pom.artifactId = module.pomArtifactId
                                        pom.packaging = module.pomPackaging
                                        pom.version = module.pomVersion
                                        pom.name = module.pomName
                                    }
                                    if(pom.version.toString().endsWith("-SNAPSHOT")) {
                                        repository(url: 'http://localhost:8081/repository/sun-snapshot') {
                                            authentication(
                                                    userName: 'admin',
                                                    password: 'admin123'
                                            )
                                        }
                                    } else {
                                        repository(url: 'http://localhost:8081/repository/sun-snapshot') {
                                            authentication(
                                                    userName: 'admin',
                                                    password: 'admin123'
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        // 添加execUpload任务
                        subProject.tasks.create("execUpload", Exec.class, {
                            def argList = new ArrayList<String>()
                            argList.add(":${subProject.name}:clean")
                            argList.add(":${subProject.name}:aR")
                            argList.add(":${subProject.name}:uploadArchives")
                            argList.add("-PBY_SYNC_UPLOAD=true")

                            workingDir '../'
                            executable './gradlew'
                            args argList
                        })
                        subProject.afterEvaluate {
                            // 添加源码打包任务
                            subProject.tasks.create("androidSourcesJar", Jar.class, {
                                classifier = 'sources'
                                from subProject.android.sourceSets.main.java.srcDirs
                            })
                            subProject.artifacts {
                                archives subProject.androidSourcesJar
                            }
                        }
                    }
                }
            }

            // 建立任务依赖关系
            for(int i=0;i<modules.size();i++) {
                def module = modules.get(i)
                if(i == 0) {
                    def moduleProject = projectsWithName.get(module.name)
                    suffixTask.dependsOn(moduleProject.getTasks().getByName("execUpload"))
                }else {
                    def preModule = modules.get(i-1)
                    def preModuleProject = projectsWithName.get(preModule.name)
                    def moduleProject = projectsWithName.get(module.name)
                    preModuleProject.getTasks().getByName("execUpload").dependsOn(moduleProject.getTasks().getByName("execUpload"))
                }
                if(i == modules.size() - 1) {
                    def preModule = modules.get(i)
                    def preModuleProject = projectsWithName.get(preModule.name)
                    preModuleProject.getTasks().getByName("execUpload").dependsOn(preTask)
                }
            }
        }
    }

}