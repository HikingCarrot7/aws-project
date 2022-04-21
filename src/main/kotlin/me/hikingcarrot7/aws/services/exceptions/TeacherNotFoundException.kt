package me.hikingcarrot7.aws.services.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Teacher not found")
class TeacherNotFoundException(teacherId: Long) :
  RuntimeException("Teacher with id: $teacherId doesn't exist!")