package me.hikingcarrot7.aws.web.controllers

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import me.hikingcarrot7.aws.models.Student
import me.hikingcarrot7.aws.services.StudentService
import me.hikingcarrot7.aws.services.exceptions.StudentNotFoundException
import me.hikingcarrot7.aws.web.utils.JsonUtils.asJsonString
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.`is`
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(StudentController::class)
internal class StudentControllerTest(
  @MockkBean private val studentService: StudentService,
  private val mockMvc: MockMvc
) : ShouldSpec() {
  private val studentsWithinDatabase = arrayListOf<Student>()

  init {
    beforeEach {
      studentsWithinDatabase.clear()
      studentsWithinDatabase.addAll(
        listOf(
          Student(1, "Nicolás", "Canul", "15001169", 77.7),
          Student(2, "Fernando", "Uicab", "18341599", 89.5)
        )
      )
    }

    context("GET - Get all students") {
      should("return all students & 200 status code") {
        every { studentService.getAllStudents() } returns studentsWithinDatabase

        mockMvc
          .perform(get(StudentController.BASE_URL))
          .andExpect(status().isOk)
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.length()", `is`(2)))
          .andExpect(jsonPath("$[0].names").value("Nicolás"))
      }
    }

    context("GET - Get student by id") {
      should("return requested student & 200 status code") {
        val requestedStudent = studentsWithinDatabase.first()
        val requestedStudentId = requestedStudent.id
        val url = "${StudentController.BASE_URL}/$requestedStudentId"
        every { studentService.getStudentById(requestedStudentId) } returns requestedStudent

        mockMvc
          .perform(get(url))
          .andExpect(status().isOk)
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath<List<*>>("$.*", hasSize(5)))
          .andExpect(jsonPath("$.names").value("Nicolás"))
      }

      should("return 404 status code if student not found") {
        val requestedStudentId = 4L
        val url = "${StudentController.BASE_URL}/$requestedStudentId"
        every {
          studentService.getStudentById(requestedStudentId)
        } throws StudentNotFoundException(requestedStudentId)

        mockMvc
          .perform(get(url))
          .andExpect(status().isNotFound)
      }
    }

    context("POST - Save student") {
      should("return saved student & 201 status code") {
        val newStudent = Student(3, "Eusebio", "Do Santos", "17348934", 56.34)
        val resourceLocation = "${StudentController.BASE_URL}/${newStudent.id}"
        every { studentService.saveStudent(any()) } returns newStudent

        mockMvc
          .perform(
            post(StudentController.BASE_URL)
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(newStudent))
          )
          .andExpect(status().isCreated)
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.names").value(newStudent.names))
          .andExpect(header().string("Location", resourceLocation))
      }

      xshould("return 400 status code when invalid fields") {
        val newStudent = Student(3, "Eu", "Do Santos", "17348934", -10.34)

        mockMvc
          .perform(
            post(StudentController.BASE_URL)
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(newStudent))
          )
          .andExpect(status().isBadRequest)
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        verify(exactly = 0) { studentService.saveStudent(any()) }
      }
    }

    context("PUT - Update student") {
      should("return updated student & 200 status code") {
        val updatedStudent = Student(3, "Eusebio", "Santos", "1738934", 90.23)
        val updateStudentId = updatedStudent.id
        val url = "${StudentController.BASE_URL}/$updateStudentId"
        val studentIdSlot = slot<Long>()
        every {
          studentService.updateStudent(capture(studentIdSlot), any())
        } returns updatedStudent

        mockMvc
          .perform(
            put(url)
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(updatedStudent))
          )
          .andExpect(status().isOk)
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.names").value("Eusebio"))
          .andExpect(jsonPath("$.surnames").value("Santos"))
        studentIdSlot.captured shouldBe updateStudentId
      }

      xshould("return 400 status code when invalid fields") {
        val updatedStudent = Student(3, "Eu", "Do Santos", "17348934", -10.34)
        val url = "${StudentController.BASE_URL}/${updatedStudent.id}"

        mockMvc
          .perform(
            put(url)
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(updatedStudent))
          )
          .andExpect(status().isBadRequest)
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        verify(exactly = 0) { studentService.updateStudent(any(), any()) }
      }

      should("return 404 status code when student to update not found") {
        val updatedStudent = Student(10, "Eusebio", "Santos", "1738934", 90.23)
        val updatedStudentId = updatedStudent.id
        val url = "${StudentController.BASE_URL}/$updatedStudentId"
        every {
          studentService.updateStudent(any(), any())
        } throws StudentNotFoundException(updatedStudentId)

        mockMvc
          .perform(
            put(url)
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(updatedStudent))
          )
          .andExpect(status().isNotFound)
      }
    }

    context("DELETE - Delete student") {
      should("return deleted student & 200 status code") {
        val studentToDelete = studentsWithinDatabase.first()
        val studentToDeleteId = studentToDelete.id
        val url = "${StudentController.BASE_URL}/$studentToDeleteId"
        every { studentService.deleteStudent(studentToDeleteId) } returns studentToDelete

        mockMvc
          .perform(delete(url))
          .andExpect(status().isOk)
          .andExpect(jsonPath("$.names").value(studentToDelete.names))
          .andExpect(jsonPath("$.surnames").value(studentToDelete.surnames))
      }

      should("return 404 status code when student to delete is not found") {
        val studentToDelete = Student(10, "Eusebio", "Santos", "1738934", 90.23)
        val studentToDeleteId = studentToDelete.id
        val url = "${StudentController.BASE_URL}/$studentToDeleteId"
        every {
          studentService.deleteStudent(studentToDeleteId)
        } throws StudentNotFoundException(studentToDeleteId)

        mockMvc
          .perform(delete(url))
          .andExpect(status().isNotFound)
      }
    }
  }
}