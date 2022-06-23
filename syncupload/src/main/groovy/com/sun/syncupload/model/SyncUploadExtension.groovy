package com.sun.syncupload.model

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

/**
 * 模板代码：函数名定义
 */
class SyncUploadExtension {
    /**
     * 第一层级的函数名
     */
    static final String NAME = "SyncUploadPluginExtension"

    /**
     * 第二层级的函数名
     */
    NamedDomainObjectContainer<UploadModule> modules

    SyncUploadExtension(Project project) {
        modules = project.container(UploadModule)
    }

    void modules(Action<NamedDomainObjectContainer<UploadModule>> action) {
        action.execute(modules)
    }
}