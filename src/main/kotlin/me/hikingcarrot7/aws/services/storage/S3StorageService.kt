package me.hikingcarrot7.aws.services.storage

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.PutObjectRequest
import me.hikingcarrot7.aws.services.utils.FileUtils.toFile
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
@Primary
class S3StorageService(
  val s3Client: AmazonS3
) : StorageService {
  @Value("\${application.bucket.name}")
  lateinit var bucketName: String
  lateinit var multiFile: MultipartFile

  override fun store(multiFile: MultipartFile): String {
    this.multiFile = multiFile
    val uploadFileName = getUploadFileName()
    uploadFile(uploadFileName)
    return getUploadedFileUrl(uploadFileName)
  }

  private fun uploadFile(uploadFileName: String) {
    val fileObj = multiFile.toFile()
    val objRequest = PutObjectRequest(bucketName, uploadFileName, fileObj)
    s3Client.putObject(objRequest)
    fileObj.delete()
  }

  private fun getUploadFileName(): String {
    return "${System.currentTimeMillis()}_${multiFile.originalFilename}"
  }

  private fun getUploadedFileUrl(uploadFileName: String): String {
    val url = s3Client.getUrl(bucketName, uploadFileName)
    return url.toExternalForm()
  }

}