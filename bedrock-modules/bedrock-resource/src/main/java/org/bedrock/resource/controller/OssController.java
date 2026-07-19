package org.bedrock.resource.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.code.util.FileUtil;
import org.bedrock.common.resource.model.oss.OssResultFile;
import org.bedrock.resource.support.OssSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Tag(name = "文件上传")
@Slf4j
@RestController
@RequestMapping("/oss")
@RequiredArgsConstructor
public class OssController extends BaseController {

    private final OssSupport ossSupport;

    @PostMapping("/mkdir-bucket")
    @Operation(summary = "创建bucket")
    @ApiOperationSupport(order = 1)
    public R<Void> mkdirBucket(@Parameter(description = "bucket名称", required = true, name = "bucketName", in = ParameterIn.QUERY) String bucketName) {
        return R.status(ossSupport.ossTemplate().mkdirBucket(bucketName));
    }

    @DeleteMapping("/remove-bucket")
    @Operation(summary = "删除bucket")
    @ApiOperationSupport(order = 2)
    public R<Void> removeBucket(@Parameter(description = "bucket名称", required = true, name = "bucketName", in = ParameterIn.QUERY) String bucketName) {
        return R.status(ossSupport.ossTemplate().deleteBucket(bucketName));
    }

    @PostMapping("/copy-file")
    @Operation(summary = "复制文件")
    @ApiOperationSupport(order = 3)
    @Parameters({
            @Parameter(description = "源bucket名称", required = true, name = "sourceBucketName", in = ParameterIn.QUERY),
            @Parameter(description = "源文件名称", required = true, name = "sourceFileName", in = ParameterIn.QUERY),
            @Parameter(description = "目标bucket名称", required = true, name = "targetBucketName", in = ParameterIn.QUERY),
            @Parameter(description = "目标文件名称", required = true, name = "targetFileName", in = ParameterIn.QUERY)
    })
    public R<Void> copyFile(String sourceBucketName,
                            String sourceFileName,
                            String targetBucketName,
                            String targetFileName) {
        return R.status(ossSupport.ossTemplate().copyFile(sourceBucketName, sourceFileName, targetBucketName, targetFileName));
    }

    @DeleteMapping("/remove-file")
    @Operation(summary = "删除文件")
    @ApiOperationSupport(order = 4)
    @Parameters({
            @Parameter(description = "文件名称", required = true, name = "filepath", in = ParameterIn.QUERY)
    })
    public R<Void> removeFile(String filepath) {
        return R.status(ossSupport.ossTemplate().deleteFile(filepath));
    }

    /**
     * 下载文件
     */
    @GetMapping(value = "/download-file")
    @Operation(summary = "下载文件")
    @ApiOperationSupport(order = 5)
    @Parameters({
            @Parameter(description = "文件路径", required = true, name = "filepath", in = ParameterIn.QUERY)
    })
    public void downloadFile(String filepath) throws IOException {
        log.info("下载文件：filepath: {}", filepath);
        InputStream inputStream = ossSupport.ossTemplate().downloadFile(filepath);
        downloadFile(inputStream, FileUtil.getFileName(filepath));
    }

    /**
     * 下载文件
     */
    @GetMapping(value = "/download-file/bucket")
    @Operation(summary = "下载文件")
    @ApiOperationSupport(order = 6)
    @Parameters({
            @Parameter(description = "bucket名称", required = true, name = "bucketName", in = ParameterIn.QUERY),
            @Parameter(description = "文件路径", required = true, name = "filepath", in = ParameterIn.QUERY),
    })
    public void downloadFileBucket(String bucketName, String filepath) throws IOException {
        log.info("下载文件：bucketName: {},filepath: {}", bucketName, filepath);
        InputStream inputStream = ossSupport.ossTemplate().downloadFile(bucketName, filepath);
        downloadFile(inputStream, FileUtil.getFileName(filepath));
    }

    /**
     * 下载文件
     */
    @GetMapping(value = "/download-file/bucket/custom-name")
    @Operation(summary = "下载文件")
    @ApiOperationSupport(order = 7)
    @Parameters({
            @Parameter(description = "bucket名称", required = true, name = "bucketName", in = ParameterIn.QUERY),
            @Parameter(description = "文件路径", required = true, name = "filepath", in = ParameterIn.QUERY),
            @Parameter(description = "文件名称 携带后缀", required = true, name = "fileName", in = ParameterIn.QUERY)
    })
    public void downloadFileBucket(String bucketName, String filepath, String fileName) throws IOException {
        log.info("下载文件：bucketName: {},filepath: {},fileName: {}", bucketName, filepath, fileName);
        InputStream inputStream = ossSupport.ossTemplate().downloadFile(bucketName, filepath);
        downloadFile(inputStream, fileName);
    }

    /**
     * 下载文件
     */
    @GetMapping(value = "/download-file/custom-name")
    @Operation(summary = "下载文件")
    @ApiOperationSupport(order = 8)
    @Parameters({
            @Parameter(description = "文件路径", required = true, name = "filepath", in = ParameterIn.QUERY),
            @Parameter(description = "文件名称 携带后缀", required = true, name = "fileName", in = ParameterIn.QUERY)
    })
    public void downloadFileCustomName(String filepath, String fileName) throws IOException {
        log.info("下载文件：filepath: {},fileName: {}", filepath, fileName);
        InputStream inputStream = ossSupport.ossTemplate().downloadFile(filepath);
        downloadFile(inputStream, fileName);
    }

    @PostMapping(value = "/upload-file")
    @Operation(summary = "上传文件")
    @ApiOperationSupport(order = 9)
    public R<OssResultFile> uploadFile(@RequestPart("file") MultipartFile file) throws IOException {
        return R.success(ossSupport.ossTemplate().uploadFile(file.getOriginalFilename(), file.getInputStream(), file.getContentType()));
    }

}
