package com.github.kevinmmarlow.ossaliyun.plugin;

import com.github.kevinmmarlow.ossaliyun.client.AliyunOSSClient;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

class OSSUploadTask extends DefaultTask {

    @Input
    String bucket
    @Input
    String accessKey
    @Input
    String secretKey
    @Input
    String key
    @Input
    String file

    public OSSUploadTask() {
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

        logger.quiet "oss upload " + bucketName

        String fileName = getFile()
        if (fileName == null || fileName == '') {
            return;
        }

        String keyValue = getKey()

        String region = AliyunOSSClient.getBucketLocation(accessKey, secretKey, bucketName);
        String endpoint = "http://" + region + ".aliyuncs.com";

        AliyunOSSClient client = new AliyunOSSClient(endpoint, accessKey, secretKey)

        String presigned = client.uploadFile(bucketName, keyValue, fileName)
        logger.quiet "Uploaded \"" + fileName + "\" to \"" + keyValue + "\""
        logger.quiet "Downloadable from " + presigned + " within next 30 days"
    }
}
