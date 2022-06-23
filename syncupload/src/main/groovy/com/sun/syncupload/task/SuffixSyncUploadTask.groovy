package com.sun.syncupload.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import javax.inject.Inject

class SuffixSyncUploadTask extends DefaultTask{

    /**
     * 这里的名称是真正我们需要执行的task名称
     */
    static final String NAME = "syncUpload"

    @Inject
    SuffixSyncUploadTask() {}

    @TaskAction
    void start() {
        println("============= SyncUpload End ===========")
    }

}