Gradle OSS Plugin
----------------

This plugin can be used to transfer files to and from Aliyun OSS buckets.

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
task uploadAppJar(type: com.github.kevinmmarlow.ossaliyun.plugin.ossUploadTask) {
    key = 'some_other_upload_file_name.jar'
    file = 'my_local_file.jar'
}
```
Then I can call "gradle uploadAppJar" to upload yet another artifact to oss.

This can be useful, if uploading hash checksums of the artifacts. For instance, I can upload sha1 value of the artifact so my automated artifact deployment task could download checksum file first to decide whether downloading the big artifact is worth it.

```
task writeHash() {
    String hashValue = computeHash('../build/libs/gradle-oss-plugin-master-1.0.1-SNAPSHOT.jar')
    file('../build/libs/gradle-oss-plugin-master-1.0.1-SNAPSHOT.sha1').write(hashValue)
}

task uploadHash(type: com.github.skhatri.ossaws.plugin.ossUploadTask) {
    key = 'gradle-oss-plugin-1.0.0-something.sha1'
    file = '../build/libs/gradle-oss-plugin-master-1.0.1-SNAPSHOT.sha1'
    link = 'latest/gradle-plugin.sha1'
}

task ossUpload(dependsOn:['uploadAppJar', 'writeHash', 'uploadHash']) {
}
```
Now, calling ossUpload will upload jar and the hash file.

### Downloading more than one artifact ###

Similarly, I can download more artifacts by simply creating ad-hoc Download task. I am downloading the previously uploaded sha1 file of the artifact using the task below.

```
task ossDownload(type: com.github.kevinmmarlow.ossaliyun.plugin.ossDownloadTask) {
    key = 'latest/gradle-plugin.sha1'
    saveTo = 'gradle-plugin.txt'
}
```