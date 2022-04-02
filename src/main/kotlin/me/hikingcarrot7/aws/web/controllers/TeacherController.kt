package me.hikingcarrot7.aws.web.controllers

import me.hikingcarrot7.aws.models.Teacher
import me.hikingcarrot7.aws.services.TeacherService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/profesores")
class TeacherController constructor(val teacherService: TeacherService) {

  @GetMapping
  fun getAllTeachers(): List<Teacher> {
    return teacherService.getAllTeachers()
  }

  @GetMapping("/{teacherId}")
  fun getTeacher(@PathVariable teacherId: Long): Teacher {
    return teacherService.getTeacherById(teacherId)
  }

  @PostMapping
  fun saveTeacher(@RequestBody @Valid teacher: Teacher): Teacher {
    return teacherService.saveTeacher(teacher)
  }

  @PutMapping("/{teacherId}")
  fun updateTeacher(
    @PathVariable teacherId: Long, @RequestBody @Valid teacher: Teacher
  ): Teacher {
    return teacherService.updateTeacher(teacherId, teacher)
  }

  @DeleteMapping("/{teacherId}")
  fun deleteTeacher(@PathVariable teacherId: Long): Teacher {
    return teacherService.deleteTeacher(teacherId)
  }

}