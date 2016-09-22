package com.github.kevinmmarlow.ossaliyun.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class OSSPlugin implements Plugin<Project> {
	
	void apply(Project project) {
        def ossExt = project.extensions.create('oss', OSSExtension)
        ossExt.extensions.create('upload', OSSUploadExtension)
        ossExt.extensions.create('download', OSSDownloadExtension)
        createUploadTask(project)
        createDownloadTask(project)
    }

    void createUploadTask(Project project) {
        project.tasks.create(name: 'ossUpload', type: OSSUploadTask) {

        }
        project.tasks.withType(OSSUploadTask) {
            def ossExt = project.extensions.getByName('oss')
            def ossUploadExt = ossExt.extensions.getByName('upload')

            conventionMapping.bucket = { ossExt.bucket }
            conventionMapping.accessKey = { ossExt.accessKey }
            conventionMapping.secretKey = { ossExt.secretKey }
            conventionMapping.key = { ossUploadExt.key }
            conventionMapping.file = { ossUploadExt.file }
        }
    }

    void createDownloadTask(Project project) {
        project.tasks.create(name: 'ossDownload', type: OSSDownloadTask) {
        }

        project.tasks.withType(OSSDownloadTask) {
            def ossExt = project.extensions.getByName('oss')
            def ossDownloadExt = ossExt.extensions.getByName('download')

            conventionMapping.bucket = { ossExt.bucket }
            conventionMapping.accessKey = { ossExt.accessKey }
            conventionMapping.secretKey = { ossExt.secretKey }
            conventionMapping.key = { ossDownloadExt.key }
            conventionMapping.saveTo = { ossDownloadExt.saveTo }
        }
    }
}