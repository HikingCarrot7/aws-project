package me.hikingcarrot7.aws.web.utils

import com.fasterxml.jackson.databind.ObjectMapper

object JsonUtils {

  fun asJsonString(obj: Any): String {
    return ObjectMapper().writeValueAsString(obj)
  }

}