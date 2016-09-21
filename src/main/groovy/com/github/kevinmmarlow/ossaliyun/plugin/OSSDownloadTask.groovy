package com.github.kevinmmarlow.ossaliyun.plugin;

import com.github.kevinmmarlow.ossaliyun.client.AliyunOSSClient;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

class OSSDownloadTask extends DefaultTask {

    @Input
    String bucket
    @Input
    String accessKey
    @Input
    String secretKey
    @Input
    String key
    @Input
    String saveTo

    public OSSDownloadTask() {
        bucket = ''
        accessKey = ''
        secretKey = ''
    }

    @TaskAction
    public void perform() {
        String bucketName = getBucket()
        String accessKey = getAccessKey()
        String secretKey = getSecretKey()

        AliyunOSSClient.validateAliyunAccount(accessKey, secretKey);

        logger.quiet "oss download " + bucketName
        String keyValue = getKey()
        if (keyValue == null || keyValue == '') {
            return;
        }

        String region = AliyunOSSClient.getBucketLocation(accessKey, secretKey, bucketName);
        String endpoint = "http://" + region + ".aliyuncs.com";

        AliyunOSSClient client = new AliyunOSSClient(endpoint, accessKey, secretKey)

        String saveTo = getSaveTo()
        client.downloadFile(bucketName, keyValue, saveTo)
        logger.quiet "Downloaded \"" + keyValue + "\" to \"" + saveTo + "\""
    }
}
