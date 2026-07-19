package org.bedrock.resource.feign;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.code.api.R;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.resource.model.oss.OssResultFile;
import org.bedrock.resource.support.OssSupport;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * OSS Feign 服务端实现
 */
@Hidden
@RestController
@RequiredArgsConstructor
public class OssClient implements IOssClient {

    private final OssSupport ossSupport;

    @Override
    public R<Void> mkdirBucket(String bucketName) {
        return R.status(ossSupport.ossTemplate().mkdirBucket(bucketName));
    }

    @Override
    public R<Void> removeBucket(String bucketName) {
        return R.status(ossSupport.ossTemplate().deleteBucket(bucketName));
    }

    @Override
    public R<Void> copyFile(String sourceBucketName, String sourceFileName,
                             String targetBucketName, String targetFileName) {
        return R.status(ossSupport.ossTemplate().copyFile(sourceBucketName, sourceFileName, targetBucketName, targetFileName));
    }

    @Override
    public R<Void> removeFile(String filepath) {
        return R.status(ossSupport.ossTemplate().deleteFile(filepath));
    }

    @Override
    public R<byte[]> downloadFile(String filepath) {
        try (InputStream inputStream = ossSupport.ossTemplate().downloadFile(filepath)) {
            return R.success(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new ServiceException("下载文件失败");
        }
    }

    @Override
    public R<OssResultFile> uploadFile(MultipartFile file) {
        try {
            return R.success(ossSupport.ossTemplate().uploadFile(file.getOriginalFilename(), file.getInputStream(), file.getContentType()));
        } catch (IOException e) {
            throw new ServiceException("上传文件失败");
        }
    }

}
