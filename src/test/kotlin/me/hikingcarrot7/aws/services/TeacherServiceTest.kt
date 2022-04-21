package me.hikingcarrot7.aws.services

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import me.hikingcarrot7.aws.models.Teacher
import me.hikingcarrot7.aws.repositories.TeacherRepository
import me.hikingcarrot7.aws.services.exceptions.TeacherNotFoundException
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
internal class TeacherServiceTest(
  private val teacherRepository: TeacherRepository
) : ShouldSpec() {
  private lateinit var underTest: TeacherService
  private var teachersWithinDatabase = listOf(
    Teacher(1, 100, "Jorge", "Montalvo", 40),
    Teacher(2, 200, "Eusebio", "Ajax Santos", 80)
  )

  override fun extensions() = listOf(SpringExtension)

  init {
    beforeEach {
      teacherRepository.deleteAll()
      teachersWithinDatabase = teacherRepository.saveAll(teachersWithinDatabase)
      underTest = TeacherService(teacherRepository)
    }

    context("#getAllTeachers") {
      should("return all teachers within database") {
        val teachers = underTest.getAllTeachers()

        teachers shouldContainExactly teachersWithinDatabase
      }
    }

    context("#getTeacherById") {
      should("return teacher by id") {
        val expectedTeacher = teachersWithinDatabase.first()

        val teacher = underTest.getTeacherById(expectedTeacher.id)

        teacher shouldBe expectedTeacher
      }

      should("throw an exception if teacher is not found") {
        val teacherId = Long.MAX_VALUE

        assertThrows<TeacherNotFoundException> {
          underTest.getTeacherById(teacherId)
        }
      }
    }

    context("#saveTeacher") {
      should("return saved teacher with original fields") {
        val newTeacher = Teacher(
          id = 3,
          numeroEmpleado = 1127318,
          nombres = "Ricardo Nicolás",
          apellidos = "Canul Ibarra",
          horasClase = 40
        )

        val savedTeacher = underTest.saveTeacher(newTeacher)

        savedTeacher.id shouldBeGreaterThan 0L
        savedTeacher.nombres shouldBe newTeacher.nombres
        savedTeacher.apellidos shouldBe newTeacher.apellidos
      }
    }

    context("#updateTeacher") {
      should("return updated names and class hours") {
        val oldTeacher = teachersWithinDatabase.last()
        val oldTeacherId = oldTeacher.id
        val newTeacher = Teacher(
          id = 2,
          numeroEmpleado = 200,
          nombres = "Víctor",
          apellidos = "Ajax Santos",
          horasClase = 60
        )

        val updatedTeacher = underTest.updateTeacher(oldTeacherId, newTeacher)

        updatedTeacher.nombres shouldBe newTeacher.nombres
        updatedTeacher.horasClase shouldBe newTeacher.horasClase
      }

      should("throw an exception if teacher to update is not found") {
        val updatedTeacher = Teacher(
          id = 5,
          numeroEmpleado = 500,
          nombres = "Eusebio",
          apellidos = "Do Santos",
          horasClase = 60,
        )
        val updatedTeacherId = Long.MAX_VALUE
        assertThrows<TeacherNotFoundException> {
          underTest.updateTeacher(updatedTeacherId, updatedTeacher)
        }
      }
    }

    context("#deleteTeacher") {
      should("delete teacher from database") {
        val totalTeachers = teacherRepository.count()
        val teacherToDelete = teachersWithinDatabase.first()
        val teacherToDeleteId = teacherToDelete.id

        val deletedTeacher = underTest.deleteTeacher(teacherToDeleteId)

        deletedTeacher shouldBe teacherToDelete
        teacherRepository.count() shouldBe totalTeachers - 1
      }

      should("throw an exception if teacher to delete if not found") {
        val teacherToDeleteId = Long.MAX_VALUE

        assertThrows<TeacherNotFoundException> {
          underTest.deleteTeacher(teacherToDeleteId)
        }
      }
    }
  }
}