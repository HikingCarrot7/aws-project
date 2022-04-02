package me.hikingcarrot7.aws.services

import me.hikingcarrot7.aws.errors.TeacherNotFoundException
import me.hikingcarrot7.aws.models.Teacher
import me.hikingcarrot7.aws.repositories.TeacherRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TeacherService constructor(val teacherRepository: TeacherRepository) {

  fun getAllTeachers(): List<Teacher> {
    return teacherRepository.findAll()
  }

  fun getTeacherById(teacherId: Long): Teacher {
    return teacherRepository
      .findByIdOrNull(teacherId)
      ?: throw TeacherNotFoundException()
  }

  fun saveTeacher(teacher: Teacher): Teacher {
    return teacherRepository.save(teacher)
  }

  fun updateTeacher(teacherId: Long, newTeacher: Teacher): Teacher {
    val oldTeacher = getTeacherById(teacherId)
    oldTeacher.employeeNumber = newTeacher.employeeNumber
    oldTeacher.names = newTeacher.names
    oldTeacher.surnames = newTeacher.surnames
    oldTeacher.classHours = newTeacher.classHours
    return teacherRepository.save(oldTeacher)
  }

  fun deleteTeacher(teacherId: Long): Teacher {
    val teacher = getTeacherById(teacherId)
    teacherRepository.delete(teacher)
    return teacher
  }

}