package me.hikingcarrot7.aws.services.storage

import org.springframework.web.multipart.MultipartFile

interface StorageService {

  fun store(multiFile: MultipartFile): String

}