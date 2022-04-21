package me.hikingcarrot7.aws.services

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import me.hikingcarrot7.aws.errors.TeacherNotFoundException
import me.hikingcarrot7.aws.models.Teacher
import me.hikingcarrot7.aws.repositories.TeacherRepository
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
internal class TeacherServiceTest(
  private val teacherRepository: TeacherRepository
) : ShouldSpec() {
  private lateinit var underTest: TeacherService;
  private var teachersWithinDatabase = listOf(
    Teacher(1, "Jorge", "Montalvo", 40),
    Teacher(2, "Eusebio", "Ajax Santos", 80)
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
      should("return saved teacher with a valid id and original fields") {
        val newTeacher = Teacher(
          names = "Ricardo Nicolás",
          surnames = "Canul Ibarra",
          employeeNumber = 1127318,
          classHours = 40
        )

        val savedTeacher = underTest.saveTeacher(newTeacher)

        savedTeacher.id shouldBeGreaterThan 0L
        savedTeacher.names shouldBe newTeacher.names
        savedTeacher.surnames shouldBe newTeacher.surnames
      }
    }

    context("#updateTeacher") {
      should("return updated names and class hours") {
        val oldTeacher = teachersWithinDatabase.last()
        val oldTeacherId = oldTeacher.id
        val newTeacher = Teacher(
          names = "Víctor",
          surnames = "Ajax Santos",
          employeeNumber = 2,
          classHours = 60
        )

        val updatedTeacher = underTest.updateTeacher(oldTeacherId, newTeacher)

        updatedTeacher.names shouldBe newTeacher.names
        updatedTeacher.classHours shouldBe newTeacher.classHours
      }

      should("throw an exception if teacher to update is not found") {
        val updatedTeacher = Teacher(
          names = "Eusebio",
          surnames = "Do Santos",
          employeeNumber = 5,
          classHours = 60,
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