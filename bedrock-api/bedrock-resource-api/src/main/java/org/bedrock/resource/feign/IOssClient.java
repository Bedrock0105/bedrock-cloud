package org.bedrock.resource.feign;

import org.bedrock.common.code.api.R;
import org.bedrock.common.constant.ApplicationConstant;
import org.bedrock.common.resource.model.oss.OssResultFile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * OSS Feign 客户端
 */
@FeignClient(value = ApplicationConstant.APPLICATION_RESOURCE_NAME)
public interface IOssClient {

    String MKDIR_BUCKET = "/feign/oss/mkdir-bucket";
    String REMOVE_BUCKET = "/feign/oss/remove-bucket";
    String COPY_FILE = "/feign/oss/copy-file";
    String REMOVE_FILE = "/feign/oss/remove-file";
    String DOWNLOAD_FILE = "/feign/oss/download-file";
    String UPLOAD_FILE = "/feign/oss/upload-file";

    /**
     * 创建 bucket
     */
    @PostMapping(MKDIR_BUCKET)
    R<Void> mkdirBucket(@RequestParam("bucketName") String bucketName);

    /**
     * 删除 bucket
     */
    @DeleteMapping(REMOVE_BUCKET)
    R<Void> removeBucket(@RequestParam("bucketName") String bucketName);

    /**
     * 复制文件
     */
    @PostMapping(COPY_FILE)
    R<Void> copyFile(@RequestParam("sourceBucketName") String sourceBucketName,
                     @RequestParam("sourceFileName") String sourceFileName,
                     @RequestParam("targetBucketName") String targetBucketName,
                     @RequestParam("targetFileName") String targetFileName);

    /**
     * 删除文件
     */
    @DeleteMapping(REMOVE_FILE)
    R<Void> removeFile(@RequestParam("filepath") String filepath);

    /**
     * 下载文件
     */
    @GetMapping(DOWNLOAD_FILE)
    R<byte[]> downloadFile(@RequestParam("filepath") String filepath);

    /**
     * 上传文件
     * <p>
     * 其他模块本地只有 byte[] / InputStream / File 时，可使用
     * {@link org.bedrock.resource.dto.OssMultipartFile} 静态工厂封装后再调用，例如：
     * {@code ossClient.uploadFile(OssMultipartFile.of(bytes, "avatar.png"))}
     * </p>
     *
     * @param file 上传文件（表单字段名必须为 {@code file}）
     * @return 上传结果
     * @see org.bedrock.resource.dto.OssMultipartFile
     */
    @PostMapping(value = UPLOAD_FILE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<OssResultFile> uploadFile(@RequestPart("file") MultipartFile file);

}
