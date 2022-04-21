package me.hikingcarrot7.aws.services.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Student not found")
class StudentNotFoundException(studentId: Long) :
  RuntimeException("Student with id: $studentId doesn't exists!")
