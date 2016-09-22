package com.github.kevinmmarlow.ossaliyun.client

import com.aliyun.oss.OSSClient
import com.aliyun.oss.model.OSSObject
import com.aliyun.oss.model.ObjectMetadata
import com.aliyun.oss.model.PutObjectRequest
import org.joda.time.LocalDateTime;

public class AliyunOSSClient {
	private OSSClient ossClient;

	public AliyunOSSClient(final String endpoint, final String accessKey, final String secretKey) {
        ossClient = new OSSClient(endpoint, accessKey, secretKey);
	}

	public static void validateAliyunAccount(final String accessKey, final String secretKey) {
		try {
            OSSClient client = new OSSClient(accessKey, secretKey);
			client.listBuckets();
		} catch (Exception e) {
			throw new AliyunOSSException("Unable to validate Aliyun account：" + e.getMessage());
		}
	}


	public static String getBucketLocation(String accessKey,
                                           String secretKey, String bucketName) throws AliyunOSSException{
		try {
            OSSClient client = new OSSClient(accessKey, secretKey);
            return client.getBucketLocation(bucketName);
		} catch (Exception ignored) {
			throw new AliyunOSSException("Bucket not found with name：" + bucketName);
		}
	}

    private static final String AMZ_REDIRECT_LINK = "x-amz-website-redirect-location";

    private String createLinkObject(String link, String key, String bucketName) {
        if (link != null && !link.isEmpty()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setHeader(AMZ_REDIRECT_LINK, createRedirectKey(key));
            metadata.setContentLength(0);
            InputStream inputStream = new ByteArrayInputStream(new byte[0]);
            PutObjectRequest linkPutRequest = new PutObjectRequest(bucketName, link, inputStream, metadata)
            ossClient.putObject(linkPutRequest);
            return ossClient.generatePresignedUrl(bucketName, link, new LocalDateTime().plusDays(30).toDate());
        }
    }

    private String createRedirectKey(String key) {
        key.startsWith("/") || key.startsWith("http") ? key : "/" + key
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
             throw new AliyunOSSException("Aliyun Upload Exception ", e)
         }
     }

     public void downloadFile(String bucketName, String key, String saveTo) {
         try {
             OSSObject object = ossClient.getObject(bucketName, key);
             Map<String, Object> redirect = object.getObjectMetadata().getRawMetadata() //.get(AMZ_REDIRECT_LINK)
             print redirect.toMapString()
//             if (redirect != null && !redirect.isEmpty()) {
//                 println "getting redirect resource = $redirect"
//                 object = ossClient.getObject(bucketName, redirect.substring(1))
//             }
             byte[] bytes = object.getObjectContent().getBytes();
             def fs = new FileOutputStream(new File(saveTo));
             fs.write(bytes);
             fs.close();
         } catch (Exception e) {
             throw new AliyunOSSException("OSS Download Exception ", e);
         }
     }
}