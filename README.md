Gradle OSS Plugin
----------------

This plugin can be used to transfer files to and from Aliyun OSS buckets. It is based on the wondeful plugin written by [Suresh Khatri](https://github.com/skhatri/gradle-s3-plugin). Be sure to check his out if you are looking for S3 uploads from your gradle buildscript.

The plugin creates two tasks:

**ossUpload**  
The ossUpload task can be used to upload a local file to a bucket with a given key name.

**ossDownload**  
The ossDownload task can be used to download a given file from a chosen bucket to a local directory.

The plugin requires an Aliyun access and secret key. However, it is recommended that these not be stored directly in the build.grade, but rather injected via a mechanism such as `System.getenv('ALIYUN_ACCESS_KEY')` and `System.getenv('ALIYUN_SECRET_KEY')`.

Please fork if you would like to contribute.

Plugin Configuration in your build.gradle
-----------------------------------------
```
buildscript {
    dependencies {
        classpath 'com.github.kevinmmarlow:gradle-oss-plugin:1.0.2'
    }
    repositories {
        mavenCentral()
    }
}
apply plugin: 'oss'

oss {
    bucket = 'your-bucket'
    accessKey = 'your-access-key' // Preferrably use System.getenv('')
    secretKey = 'your-access-key' // Preferrably use System.getenv('')
    upload {
        //"key" is the name of the target file in oss
        key =  'my_upload_file_name'
        
        // "file" is the name of the local source file
        file = '../path/to/myfile.jar' 
    }
    download {
        key = 'my-uploaded-file-name'
        saveTo = 'my_downloaded_file.jar'
    }
}


```

### Using Upload Task to upload more artifacts


```
task uploadReleaseAPK(type: com.github.kevinmmarlow.ossaliyun.plugin.ossUploadTask) {
    key = 'my_remote_release_file.apk'
    file = 'my_local_release_file.apk'
}

task uploadDebugAPK(type: com.github.kevinmmarlow.ossaliyun.plugin.ossUploadTask) {
    key = 'my_remote_debug_file.apk'
    file = 'my_local_debug_file.apk'
}
```

Then I can call `./gradlew uploadReleaseAPK` to upload my release artifact to oss and `./gradlew uploadDebugAPK` to upload my debug version.

### Downloading more than one artifact ###

Similarly, I can download more artifacts by simply creating ad-hoc Download task.

```
task ossDownload(type: com.github.kevinmmarlow.ossaliyun.plugin.ossDownloadTask) {
    key = 'remote_file_name.apk'
    saveTo = 'path/to/local_file_name.apk'
}
```