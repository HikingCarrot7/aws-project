package me.hikingcarrot7.aws.web.controllers

import me.hikingcarrot7.aws.models.Student
import me.hikingcarrot7.aws.services.StudentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping(StudentController.BASE_URL)
class StudentController constructor(val studentService: StudentService) {
  companion object {
    const val BASE_URL = "/alumnos"
  }

  @GetMapping
  fun getAllStudents(): ResponseEntity<List<Student>> {
    val students = studentService.getAllStudents()
    return ResponseEntity.ok(students)
  }

  @GetMapping("/{studentId}")
  fun getStudent(@PathVariable studentId: Long): ResponseEntity<Student> {
    val student = studentService.getStudentById(studentId)
    return ResponseEntity.ok(student)
  }

  @PostMapping
  fun saveStudent(@RequestBody @Valid student: Student): ResponseEntity<Student> {
    val savedStudent = studentService.saveStudent(student)
    return ResponseEntity
      .created(URI("$BASE_URL/${savedStudent.id}"))
      .body(savedStudent)
  }

  @PutMapping("/{studentId}")
  fun updateStudent(
    @PathVariable studentId: Long, @RequestBody @Valid student: Student
  ): ResponseEntity<Student> {
    val updatedStudent = studentService.updateStudent(studentId, student)
    return ResponseEntity.ok(updatedStudent)
  }

  @DeleteMapping("/{studentId}")
  fun deleteStudent(@PathVariable studentId: Long): ResponseEntity<Student> {
    val deletedStudent = studentService.deleteStudent(studentId)
    return ResponseEntity.ok(deletedStudent)
  }
}