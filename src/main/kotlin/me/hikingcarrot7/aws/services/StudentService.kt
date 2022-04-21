package me.hikingcarrot7.aws.services

import me.hikingcarrot7.aws.models.Student
import me.hikingcarrot7.aws.repositories.StudentRepository
import me.hikingcarrot7.aws.services.exceptions.StudentNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class StudentService constructor(
  val studentRepository: StudentRepository
) {

  fun getAllStudents(): List<Student> {
    return studentRepository.findAll()
  }

  fun getStudentById(studentId: Long): Student {
    return studentRepository
      .findByIdOrNull(studentId)
      ?: throw StudentNotFoundException(studentId)
  }

  fun saveStudent(student: Student): Student {
    return studentRepository.save(student)
  }

  fun updateStudent(studentId: Long, updatedStudent: Student): Student {
    val oldStudent = getStudentById(studentId)
    oldStudent.nombres = updatedStudent.nombres
    oldStudent.apellidos = updatedStudent.apellidos
    oldStudent.matricula = updatedStudent.matricula
    oldStudent.promedio = updatedStudent.promedio
    return studentRepository.save(oldStudent)
  }

  fun deleteStudent(studentId: Long): Student {
    val student = getStudentById(studentId)
    studentRepository.delete(student)
    return student
  }
}