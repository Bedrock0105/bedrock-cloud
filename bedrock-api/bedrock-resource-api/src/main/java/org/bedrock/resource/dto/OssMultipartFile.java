package org.bedrock.resource.dto;

import org.bedrock.common.code.constant.StringPool;
import org.bedrock.common.code.util.FileUtil;
import org.bedrock.common.code.util.IoUtil;
import org.bedrock.common.code.util.MediaTypeUtil;
import org.bedrock.common.code.util.StringUtil;
import org.bedrock.resource.feign.IOssClient;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * 基于字节数组的 {@link MultipartFile} 实现，供跨模块 Feign 调用文件上传使用。
 * <p>
 * 典型场景：其他业务模块调用 {@link IOssClient#uploadFile(MultipartFile)} 时，
 * 本地只有 {@code byte[]} / {@link InputStream} / {@link File}，无法直接得到 Servlet 上传的 MultipartFile，
 * 可通过本类静态工厂方法快速封装后再发起 Feign 请求。
 * </p>
 * <p>
 * 表单字段名默认 {@link #DEFAULT_FORM_FIELD}（{@code file}），与 {@link IOssClient} 的 {@code @RequestPart("file")} 对齐。
 * contentType 未显式传入时，会按文件名通过 {@link MediaTypeUtil#getMimeType(String, String)} 自动推断。
 * </p>
 *
 * <pre>{@code
 * // byte[] 上传
 * R<OssResultFile> result = ossClient.uploadFile(OssMultipartFile.of(bytes, "avatar.png"));
 *
 * // InputStream 上传
 * R<OssResultFile> result = ossClient.uploadFile(OssMultipartFile.of(inputStream, "report.pdf"));
 *
 * // File 上传
 * R<OssResultFile> result = ossClient.uploadFile(OssMultipartFile.of(new File("/tmp/a.jpg")));
 *
 * // 指定 contentType / 表单字段名
 * MultipartFile file = OssMultipartFile.of("file", "data.bin", "application/octet-stream", bytes);
 * }</pre>
 *
 * @see IOssClient#uploadFile(MultipartFile)
 * @see MultipartFile
 */
public class OssMultipartFile implements MultipartFile, Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 默认表单字段名，与 {@link IOssClient#UPLOAD_FILE} 接口的 {@code @RequestPart("file")} 保持一致
	 */
	public static final String DEFAULT_FORM_FIELD = "file";

	/**
	 * 表单字段名（multipart 中的 name）
	 */
	private final String name;

	/**
	 * 原始文件名（客户端侧文件名）
	 */
	private final String originalFilename;

	/**
	 * MIME 类型，可为 null
	 */
	@Nullable
	private final String contentType;

	/**
	 * 文件内容（内存持有，适合 Feign 编码；大文件请评估内存占用）
	 */
	private final byte[] content;

	/**
	 * 全参构造
	 *
	 * @param name             表单字段名（不可为空）
	 * @param originalFilename 原始文件名（可为 null，将兜底为空字符串）
	 * @param contentType      MIME 类型（可为 null，将按文件名推断）
	 * @param content          文件字节（可为 null，视为空内容）
	 */
	public OssMultipartFile(String name,
							@Nullable String originalFilename,
							@Nullable String contentType,
							@Nullable byte[] content) {
		if (StringUtil.isBlank(name)) {
			throw new IllegalArgumentException("表单字段名 name 不能为空");
		}
		this.name = name;
		this.originalFilename = originalFilename != null ? originalFilename : StringPool.EMPTY;
		this.content = content != null ? content : IoUtil.EMPTY_CONTENT;
		// 未指定 contentType 时，按文件名推断；仍无法识别则使用通用二进制类型
		this.contentType = StringUtil.isNotBlank(contentType)
			? contentType
			: MediaTypeUtil.getMimeType(this.originalFilename, MediaTypeUtil.APPLICATION_OCTET_STREAM_VALUE);
	}

	// ==================== 静态工厂：byte[] ====================

	/**
	 * 由字节数组构建（表单字段名默认 {@code file}，contentType 自动推断）
	 *
	 * @param content          文件内容
	 * @param originalFilename 原始文件名（建议带扩展名，便于推断 MIME）
	 * @return OssMultipartFile
	 */
	public static OssMultipartFile of(byte[] content, String originalFilename) {
		return of(DEFAULT_FORM_FIELD, originalFilename, null, content);
	}

	/**
	 * 由字节数组构建（指定 contentType）
	 *
	 * @param content          文件内容
	 * @param originalFilename 原始文件名
	 * @param contentType      MIME 类型
	 * @return OssMultipartFile
	 */
	public static OssMultipartFile of(byte[] content, String originalFilename, @Nullable String contentType) {
		return of(DEFAULT_FORM_FIELD, originalFilename, contentType, content);
	}

	/**
	 * 由字节数组构建（完整参数）
	 *
	 * @param name             表单字段名
	 * @param originalFilename 原始文件名
	 * @param contentType      MIME 类型
	 * @param content          文件内容
	 * @return OssMultipartFile
	 */
	public static OssMultipartFile of(String name,
									  @Nullable String originalFilename,
									  @Nullable String contentType,
									  @Nullable byte[] content) {
		return new OssMultipartFile(name, originalFilename, contentType, content);
	}

	// ==================== 静态工厂：InputStream ====================

	/**
	 * 由输入流构建（读取全部字节后关闭由调用方负责；本方法不关闭流）
	 *
	 * @param inputStream      输入流
	 * @param originalFilename 原始文件名
	 * @return OssMultipartFile
	 */
	public static OssMultipartFile of(InputStream inputStream, String originalFilename) {
		return of(inputStream, originalFilename, null);
	}

	/**
	 * 由输入流构建（指定 contentType）
	 *
	 * @param inputStream      输入流
	 * @param originalFilename 原始文件名
	 * @param contentType      MIME 类型
	 * @return OssMultipartFile
	 */
	public static OssMultipartFile of(InputStream inputStream,
									  String originalFilename,
									  @Nullable String contentType) {
		return of(DEFAULT_FORM_FIELD, originalFilename, contentType, IoUtil.copyToByteArray(inputStream));
	}

	/**
	 * 由输入流构建（完整参数）
	 *
	 * @param name             表单字段名
	 * @param inputStream      输入流
	 * @param originalFilename 原始文件名
	 * @param contentType      MIME 类型
	 * @return OssMultipartFile
	 */
	public static OssMultipartFile of(String name,
									  InputStream inputStream,
									  String originalFilename,
									  @Nullable String contentType) {
		return of(name, originalFilename, contentType, IoUtil.copyToByteArray(inputStream));
	}

	// ==================== 静态工厂：File / Path ====================

	/**
	 * 由本地文件构建（文件名取 {@link File#getName()}，contentType 自动推断）
	 *
	 * @param file 本地文件
	 * @return OssMultipartFile
	 * @throws IllegalArgumentException 文件不存在或读取失败
	 */
	public static OssMultipartFile of(File file) {
		Objects.requireNonNull(file, "file 不能为 null");
		return of(file.toPath());
	}

	/**
	 * 由本地文件构建（指定 contentType）
	 *
	 * @param file        本地文件
	 * @param contentType MIME 类型
	 * @return OssMultipartFile
	 */
	public static OssMultipartFile of(File file, @Nullable String contentType) {
		Objects.requireNonNull(file, "file 不能为 null");
		return of(DEFAULT_FORM_FIELD, file.getName(), contentType, readFileBytes(file.toPath()));
	}

	/**
	 * 由 Path 构建
	 *
	 * @param path 文件路径
	 * @return OssMultipartFile
	 */
	public static OssMultipartFile of(Path path) {
		Objects.requireNonNull(path, "path 不能为 null");
		String filename = path.getFileName() != null ? path.getFileName().toString() : StringPool.EMPTY;
		return of(DEFAULT_FORM_FIELD, filename, null, readFileBytes(path));
	}

	/**
	 * 由 Path 构建（完整参数）
	 *
	 * @param name        表单字段名
	 * @param path        文件路径
	 * @param contentType MIME 类型
	 * @return OssMultipartFile
	 */
	public static OssMultipartFile of(String name, Path path, @Nullable String contentType) {
		Objects.requireNonNull(path, "path 不能为 null");
		String filename = path.getFileName() != null ? path.getFileName().toString() : StringPool.EMPTY;
		return of(name, filename, contentType, readFileBytes(path));
	}

	// ==================== 静态工厂：已有 MultipartFile / 其他 ====================

	/**
	 * 从已有 {@link MultipartFile} 复制内容构建（深拷贝字节，独立于原对象）
	 *
	 * @param multipartFile 源文件
	 * @return OssMultipartFile
	 * @throws IllegalStateException 读取源文件失败
	 */
	public static OssMultipartFile of(MultipartFile multipartFile) {
		Objects.requireNonNull(multipartFile, "multipartFile 不能为 null");
		if (multipartFile instanceof OssMultipartFile ossMultipartFile) {
			return ossMultipartFile;
		}
		try {
			return of(
				StringUtil.isNotBlank(multipartFile.getName()) ? multipartFile.getName() : DEFAULT_FORM_FIELD,
				multipartFile.getOriginalFilename(),
				multipartFile.getContentType(),
				multipartFile.getBytes()
			);
		} catch (IOException e) {
			throw new IllegalStateException("读取 MultipartFile 内容失败: " + multipartFile.getOriginalFilename(), e);
		}
	}

	/**
	 * 仅指定原始文件名与内容（表单字段默认 {@code file}）
	 *
	 * @param originalFilename 原始文件名
	 * @param content          文件内容
	 * @return OssMultipartFile
	 */
	public static OssMultipartFile ofFilename(String originalFilename, byte[] content) {
		return of(content, originalFilename);
	}

	/**
	 * 空文件（占位，size=0）
	 *
	 * @param originalFilename 原始文件名
	 * @return 空内容的 OssMultipartFile
	 */
	public static OssMultipartFile empty(String originalFilename) {
		return of(IoUtil.EMPTY_CONTENT, originalFilename);
	}

	// ==================== MultipartFile 接口实现 ====================

	@Override
	@NonNull
	public String getName() {
		return this.name;
	}

	@Override
	@NonNull
	public String getOriginalFilename() {
		return this.originalFilename;
	}

	@Override
	@Nullable
	public String getContentType() {
		return this.contentType;
	}

	@Override
	public boolean isEmpty() {
		return this.content.length == 0;
	}

	@Override
	public long getSize() {
		return this.content.length;
	}

	@Override
	@NonNull
	public byte[] getBytes() {
		return this.content;
	}

	@Override
	@NonNull
	public InputStream getInputStream() {
		return new ByteArrayInputStream(this.content);
	}

	@Override
	public void transferTo(@NonNull File dest) throws IOException, IllegalStateException {
		File parent = dest.getParentFile();
		if (parent != null && !parent.exists() && !parent.mkdirs()) {
			throw new IOException("无法创建目标目录: " + parent.getAbsolutePath());
		}
		Files.write(dest.toPath(), this.content);
	}

	@Override
	public void transferTo(@NonNull Path dest) throws IOException, IllegalStateException {
		Path parent = dest.getParent();
		if (parent != null && !Files.exists(parent)) {
			Files.createDirectories(parent);
		}
		Files.write(dest, this.content);
	}

	// ==================== 便捷方法 ====================

	/**
	 * 复制并替换表单字段名
	 *
	 * @param name 新表单字段名
	 * @return 新实例
	 */
	public OssMultipartFile withName(String name) {
		return of(name, this.originalFilename, this.contentType, this.content);
	}

	/**
	 * 复制并替换原始文件名（会按需重新推断 contentType：仅当当前为通用二进制类型时）
	 *
	 * @param originalFilename 新文件名
	 * @return 新实例
	 */
	public OssMultipartFile withOriginalFilename(String originalFilename) {
		String type = this.contentType;
		if (StringUtil.isBlank(type) || MediaTypeUtil.APPLICATION_OCTET_STREAM_VALUE.equals(type)) {
			type = null;
		}
		return of(this.name, originalFilename, type, this.content);
	}

	/**
	 * 复制并替换 MIME 类型
	 *
	 * @param contentType 新 MIME
	 * @return 新实例
	 */
	public OssMultipartFile withContentType(@Nullable String contentType) {
		return of(this.name, this.originalFilename, contentType, this.content);
	}

	/**
	 * 获取文件扩展名（不含点）
	 *
	 * @return 扩展名，无则空字符串
	 */
	public String getExtension() {
		return FileUtil.getExtension(this.originalFilename);
	}

	@Override
	public String toString() {
		return "OssMultipartFile{"
			+ "name='" + name + '\''
			+ ", originalFilename='" + originalFilename + '\''
			+ ", contentType='" + contentType + '\''
			+ ", size=" + content.length
			+ '}';
	}

	// ==================== 内部方法 ====================

	private static byte[] readFileBytes(Path path) {
		try {
			if (!Files.exists(path) || !Files.isRegularFile(path)) {
				throw new IllegalArgumentException("文件不存在或不是普通文件: " + path);
			}
			return Files.readAllBytes(path);
		} catch (IOException e) {
			throw new IllegalStateException("读取文件失败: " + path, e);
		}
	}

}
