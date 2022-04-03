package me.hikingcarrot7.aws.services

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import me.hikingcarrot7.aws.errors.StudentNotFoundException
import me.hikingcarrot7.aws.models.Student
import me.hikingcarrot7.aws.repositories.StudentRepository
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
internal class StudentServiceTest(
  @MockkBean private val studentRepository: StudentRepository
) : StringSpec() {
  private lateinit var underTest: StudentService
  private val studentsWithinDatabase = arrayListOf<Student>()

  override fun extensions() = listOf(SpringExtension)

  init {
    beforeEach {
      underTest = StudentService(studentRepository)
      studentsWithinDatabase.clear()
      studentsWithinDatabase.addAll(
        listOf(
          Student(1, "Nicolás", "Canul", "15001169", 77.7),
          Student(2, "Fernando", "Uicab", "18341599", 89.5)
        )
      )
    }

    "should return all students within database" {
      val expectedStudents = studentsWithinDatabase
      every { studentRepository.findAll() } returns expectedStudents

      val students = underTest.getAllStudents()

      students shouldBe expectedStudents
    }

    "should return student by id" {
      val expectedStudent = studentsWithinDatabase.last()
      every { studentRepository.findByIdOrNull(any()) } returns expectedStudent

      val student = underTest.getStudentById(2)

      student shouldBe expectedStudent
    }

    "should throw exception if student is not found" {
      val studentId = 5L
      every { studentRepository.findByIdOrNull(any()) } returns null

      assertThrows<StudentNotFoundException> {
        underTest.getStudentById(studentId)
      }
    }

    "should save student" {
      val newStudent = Student(3, "Eusebio", "Do Santos", "17005634", 84.3)
      val studentSlot = slot<Student>()
      every { studentRepository.save(capture(studentSlot)) } returns newStudent

      underTest.saveStudent(newStudent)

      studentSlot.captured shouldBe newStudent
    }

    "should update student" {
      val oldStudent = studentsWithinDatabase.last()
      val oldStudentId = oldStudent.id
      val newStudent = Student(
        id = oldStudentId,
        names = "Nicolás",
        surnames = "Canul Ibarra",
        enrolment = "15001169",
        gradePointAverage = 80.3
      )
      every { studentRepository.findByIdOrNull(oldStudentId) } returns oldStudent
      every { studentRepository.save(oldStudent) } returns newStudent

      val updatedStudent = underTest.updateStudent(oldStudentId, newStudent)

      updatedStudent shouldBe newStudent
    }

    "should delete student from database" {
      val studentToDelete = studentsWithinDatabase.first()
      val studentToDeleteId = studentToDelete.id
      every { studentRepository.findByIdOrNull(studentToDeleteId) } returns studentToDelete
      every { studentRepository.delete(studentToDelete) } returns Unit

      val deletedStudent = underTest.deleteStudent(studentToDeleteId)

      verify(exactly = 1) { studentRepository.delete(studentToDelete) }
      deletedStudent shouldBe studentToDelete
    }

  }
}