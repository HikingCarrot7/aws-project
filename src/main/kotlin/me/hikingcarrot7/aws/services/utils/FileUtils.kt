package me.hikingcarrot7.aws.services.utils

import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream

object FileUtils {

  fun MultipartFile.toFile(): File {
    val convertedFile = File(originalFilename)
    val fos = FileOutputStream(convertedFile)
    fos.use {
      fos.write(bytes)
    }
    return convertedFile
  }

}