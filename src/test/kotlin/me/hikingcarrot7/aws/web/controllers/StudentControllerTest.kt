package me.hikingcarrot7.aws.web.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import me.hikingcarrot7.aws.errors.StudentNotFoundException
import me.hikingcarrot7.aws.models.Student
import me.hikingcarrot7.aws.services.StudentService
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.hasSize
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(StudentController::class)
internal class StudentControllerTest(
  @MockkBean private val studentService: StudentService,
  private val mockMvc: MockMvc
) : StringSpec() {
  companion object {
    fun asJsonString(obj: Any): String {
      return ObjectMapper().writeValueAsString(obj)
    }
  }

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

    "should return all students and 200 status code when getAllStudents" {
      every { studentService.getAllStudents() } returns studentsWithinDatabase

      mockMvc
        .perform(get(StudentController.BASE_URL))
        .andExpect(status().isOk)
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()", `is`(2)))
        .andExpect(jsonPath("$[0].names").value("Nicolás"))
    }

    "should return student and 200 status when getStudentById" {
      val requestedStudent = studentsWithinDatabase.first()
      val requestedStudentId = requestedStudent.id
      val url = "${StudentController.BASE_URL}/${requestedStudentId}"
      every { studentService.getStudentById(requestedStudentId) } returns requestedStudent

      mockMvc
        .perform(get(url))
        .andExpect(status().isOk)
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath<List<*>>("$.*", hasSize(5)))
        .andExpect(jsonPath("$.names").value("Nicolás"))
    }

    "should return 404 when student not found" {
      val requestedStudentId = 4L
      val url = "${StudentController.BASE_URL}/${requestedStudentId}"
      every {
        studentService.getStudentById(requestedStudentId)
      } throws StudentNotFoundException(requestedStudentId)

      mockMvc
        .perform(get(url))
        .andExpect(status().isNotFound)
    }

    "should return created student and 200 status code" {
      val newStudent = Student(3, "Eusebio", "Do Santos", "17348934", 56.34)
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
    }
  }
}