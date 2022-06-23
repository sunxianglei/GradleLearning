package com.sun.syncupload.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import javax.inject.Inject

class PrefixSyncUploadTask extends DefaultTask{

    static final String NAME = "PrefixSyncUploadTask"

    @Inject
    PrefixSyncUploadTask() {}

    @TaskAction
    void start() {
        println(" ================ SyncUpload Start ==============")
    }
}