package me.hikingcarrot7.aws.services

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import me.hikingcarrot7.aws.models.Student
import me.hikingcarrot7.aws.repositories.StudentRepository
import me.hikingcarrot7.aws.services.exceptions.StudentNotFoundException
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
internal class StudentServiceTest(
  private val studentRepository: StudentRepository
) : ShouldSpec() {
  private lateinit var underTest: StudentService
  private var studentsWithinDatabase = listOf(
    Student(1, "Nicolás", "Canul", "15001169", 77.7),
    Student(2, "Fernando", "Uicab", "18341599", 89.5)
  )

  override fun extensions() = listOf(SpringExtension)

  init {
    beforeEach {
      studentRepository.deleteAll()
      studentsWithinDatabase = studentRepository.saveAll(studentsWithinDatabase)
      underTest = StudentService(studentRepository)
    }

    context("#getAllStudents") {
      should("return all students within database") {
        val students = underTest.getAllStudents()

        students shouldContainExactly studentsWithinDatabase
      }
    }

    context("#getStudentById") {
      should("return student by id") {
        val expectedStudent = studentsWithinDatabase.last()

        val student = underTest.getStudentById(expectedStudent.id)

        student shouldBe expectedStudent
      }

      should("throw an exception if student is not found") {
        val studentId = Long.MAX_VALUE

        assertThrows<StudentNotFoundException> {
          underTest.getStudentById(studentId)
        }
      }
    }

    context("#saveStudent") {
      should("return saved student with a valid id and original fields") {
        val newStudent = Student(
          id = 3,
          nombres = "Eusebio",
          apellidos = "Do Santos",
          matricula = "17005634",
          promedio = 78.3
        )

        val savedStudent = underTest.saveStudent(newStudent)

        savedStudent.nombres shouldBe newStudent.nombres
        savedStudent.apellidos shouldBe newStudent.apellidos
      }
    }

    context("#updateStudent") {
      should("return updated surnames and grade point average") {
        val oldStudent = studentsWithinDatabase.last()
        val oldStudentId = oldStudent.id
        val newStudent = Student(
          id = oldStudentId,
          nombres = "Nicolás",
          apellidos = "Canul Ibarra",
          matricula = "15001169",
          promedio = 80.3
        )

        val updatedStudent = underTest.updateStudent(oldStudentId, newStudent)

        updatedStudent.apellidos shouldBe newStudent.apellidos
        updatedStudent.promedio shouldBe newStudent.promedio
      }

      should("throw an exception if student to update is not found") {
        val updatedStudent = Student(
          id = Long.MAX_VALUE,
          nombres = "Eusebio",
          apellidos = "Do Santos",
          matricula = "15001189",
          promedio = 89.34
        )
        val studentId = updatedStudent.id

        assertThrows<StudentNotFoundException> {
          underTest.updateStudent(studentId, updatedStudent)
        }
      }
    }

    context("#deleteStudent") {
      should("delete student from database") {
        val totalStudents = studentRepository.count()
        val studentToDelete = studentsWithinDatabase.first()
        val studentToDeleteId = studentToDelete.id

        val deletedStudent = underTest.deleteStudent(studentToDeleteId)

        deletedStudent shouldBe studentToDelete
        studentRepository.count() shouldBe totalStudents - 1
      }

      should("throw an exception if student to delete is not found") {
        val studentToDeleteId = Long.MAX_VALUE

        assertThrows<StudentNotFoundException> {
          underTest.deleteStudent(studentToDeleteId)
        }
      }
    }
  }
}