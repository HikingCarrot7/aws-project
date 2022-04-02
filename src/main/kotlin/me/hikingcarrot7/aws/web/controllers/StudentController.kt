package me.hikingcarrot7.aws.web.controllers

import me.hikingcarrot7.aws.models.Student
import me.hikingcarrot7.aws.services.StudentService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/alumnos")
class StudentController constructor(val studentService: StudentService) {

  @GetMapping
  fun getAllStudents(): List<Student> {
    return studentService.getAllStudents()
  }

  @GetMapping("/{studentId}")
  fun getStudent(@PathVariable studentId: Long): Student {
    return studentService.getStudentById(studentId)
  }

  @PostMapping
  fun saveStudent(@RequestBody @Valid student: Student): Student {
    return studentService.saveStudent(student)
  }

  @PutMapping("/{studentId}")
  fun updateStudent(
    @PathVariable studentId: Long, @RequestBody @Valid student: Student
  ): Student {
    return studentService.updateStudent(studentId, student)
  }

  @DeleteMapping("/{studentId}")
  fun deleteStudent(@PathVariable studentId: Long): Student {
    return studentService.deleteStudent(studentId)
  }

}