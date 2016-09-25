package com.github.kevinmmarlow.ossaliyun.client

import com.aliyun.oss.OSSClient
import com.aliyun.oss.model.OSSObject
import com.aliyun.oss.model.ObjectMetadata
import com.aliyun.oss.model.PutObjectRequest
import org.gradle.api.GradleException
import org.gradle.api.GradleScriptException
import org.gradle.api.InvalidUserDataException
import org.joda.time.LocalDateTime;

public class AliyunOSSClient {
	private OSSClient ossClient;

	public AliyunOSSClient(final String endpoint, final String accessKey, final String secretKey) {
        ossClient = new OSSClient(endpoint, accessKey, secretKey);
	}

	public static void validateAliyunAccount(final String accessKey, final String secretKey) throws InvalidUserDataException {
		try {
            OSSClient client = new OSSClient(accessKey, secretKey);
			client.listBuckets();
		} catch (Exception e) {
            throw new InvalidUserDataException("Unable to validate Aliyun account：" + e.getMessage())
		}
	}


	public static String getBucketLocation(String accessKey,
                                           String secretKey, String bucketName) throws InvalidUserDataException{
		try {
            OSSClient client = new OSSClient(accessKey, secretKey);
            return client.getBucketLocation(bucketName);
		} catch (Exception e) {
            throw new InvalidUserDataException("Bucket not found with name：" + bucketName, e)
		}
	}
    
     public String uploadFile(String bucketName, String key, String fileName) {

         try {

             File file = new File(fileName);
             ObjectMetadata meta = new ObjectMetadata()
             meta.setContentLength(file.length())
             PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file, meta)
             ossClient.putObject(putObjectRequest)

             return ossClient.generatePresignedUrl(bucketName, key, new LocalDateTime().plusDays(30).toDate())

         } catch (Exception e) {
             throw new GradleScriptException("OSS Upload Exception", e)
         }
     }

     public void downloadFile(String bucketName, String key, String saveTo) {
         try {
             OSSObject object = ossClient.getObject(bucketName, key);
             byte[] bytes = object.getObjectContent().getBytes();
             def fs = new FileOutputStream(new File(saveTo));
             fs.write(bytes);
             fs.close();
         } catch (Exception e) {
             throw new GradleScriptException("OSS Download Exception ", e);
         }
     }
}