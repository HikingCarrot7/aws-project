package me.hikingcarrot7.aws.web.controllers

import me.hikingcarrot7.aws.models.Teacher
import me.hikingcarrot7.aws.services.TeacherService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping(TeacherController.BASE_URL)
class TeacherController constructor(val teacherService: TeacherService) {
  companion object {
    const val BASE_URL = "/profesores"
  }

  @GetMapping
  fun getAllTeachers(): ResponseEntity<List<Teacher>> {
    val teachers = teacherService.getAllTeachers()
    return ResponseEntity.ok(teachers)
  }

  @GetMapping("/{teacherId}")
  fun getTeacher(@PathVariable teacherId: Long): ResponseEntity<Teacher> {
    val teacher = teacherService.getTeacherById(teacherId)
    return ResponseEntity.ok(teacher)
  }

  @PostMapping
  fun saveTeacher(@RequestBody @Valid teacher: Teacher): ResponseEntity<Teacher> {
    println(teacher)
    val savedTeacher = teacherService.saveTeacher(teacher)
    return ResponseEntity
      .created(URI("${BASE_URL}/${savedTeacher.id}"))
      .body(savedTeacher)
  }

  @PutMapping("/{teacherId}")
  fun updateTeacher(
    @PathVariable teacherId: Long, @RequestBody @Valid teacher: Teacher
  ): ResponseEntity<Teacher> {
    val updatedTeacher = teacherService.updateTeacher(teacherId, teacher)
    return ResponseEntity.ok(updatedTeacher)
  }

  @DeleteMapping("/{teacherId}")
  fun deleteTeacher(@PathVariable teacherId: Long): ResponseEntity<Teacher> {
    val deletedTeacher = teacherService.deleteTeacher(teacherId)
    return ResponseEntity.ok(deletedTeacher)
  }
}