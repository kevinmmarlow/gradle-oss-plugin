package com.github.kevinmmarlow.ossaliyun.plugin;

class OSSExtension {
    String bucket
    String accessKey
    String secretKey
    OSSDownloadExtension download = new OSSDownloadExtension()
    OSSUploadExtension upload = new OSSUploadExtension()
}
