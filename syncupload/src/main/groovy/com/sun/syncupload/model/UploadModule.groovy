package com.sun.syncupload.model

class UploadModule implements Comparable<UploadModule>{

    String name
    int order
    String pomGroupId
    String pomArtifactId
    String pomName
    String pomVersion
    String pomPackaging
    boolean noSource

    UploadModule(String name) {
        this.name = name
    }

    void order(int order) {
        this.order = order
    }

    void pomGroupId(String pomGroupId) {
        this.pomGroupId = pomGroupId
    }

    void pomArtifactId(String pomArtifactId) {
        this.pomArtifactId = pomArtifactId
    }

    void pomName(String pomName) {
        this.pomName = pomName
    }

    void pomVersion(String pomVersion) {
        this.pomVersion = pomVersion
    }

    void pomPackaging(String pomPackaging) {
        this.pomPackaging = pomPackaging
    }

    void noSource(boolean noSource) {
        this.noSource = noSource
    }

    @Override
    String toString() {
        return name
    }

    @Override
    int compareTo(UploadModule o) {
        return this.order - o.order
    }
}