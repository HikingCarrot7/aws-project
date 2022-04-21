package me.hikingcarrot7.aws.web.controllers

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import me.hikingcarrot7.aws.models.Teacher
import me.hikingcarrot7.aws.services.TeacherService
import me.hikingcarrot7.aws.services.exceptions.StudentNotFoundException
import me.hikingcarrot7.aws.services.exceptions.TeacherNotFoundException
import me.hikingcarrot7.aws.web.utils.JsonUtils.asJsonString
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.`is`
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(TeacherController::class)
internal class TeacherControllerTest(
  @MockkBean private val teacherService: TeacherService,
  private val mockMvc: MockMvc
) : ShouldSpec() {
  private val teachersWithinDatabase = arrayListOf<Teacher>()

  init {
    beforeEach {
      teachersWithinDatabase.clear()
      teachersWithinDatabase.addAll(
        listOf(
          Teacher(1, "Jorge", "Montalvo", 40),
          Teacher(2, "Eusebio", "Ajax Santos", 80)
        )
      )
    }

    context("GET - Get all teachers") {
      should("return all teachers & 200 status code") {
        every { teacherService.getAllTeachers() } returns teachersWithinDatabase

        mockMvc
          .perform(get(TeacherController.BASE_URL))
          .andExpect(status().isOk)
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.length()", `is`(2)))
          .andExpect(jsonPath("$[0].names").value("Jorge"))
      }
    }

    context("GET - Get teacher by id") {
      should("return requested teacher & 200 status code") {
        val requestedTeacher = teachersWithinDatabase.first()
        val requestedTeacherId = requestedTeacher.id
        val url = "${TeacherController.BASE_URL}/$requestedTeacherId"
        every { teacherService.getTeacherById(requestedTeacherId) } returns requestedTeacher

        mockMvc
          .perform(get(url))
          .andExpect(status().isOk)
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath<List<*>>("$.*", hasSize(5)))
          .andExpect(jsonPath("$.names").value("Jorge"))
      }

      should("return 404 status code if teacher not found") {
        val requestedTeacherId = 4L
        val url = "${TeacherController.BASE_URL}/$requestedTeacherId"
        every {
          teacherService.getTeacherById(requestedTeacherId)
        } throws StudentNotFoundException(requestedTeacherId)

        mockMvc
          .perform(get(url))
          .andExpect(status().isNotFound)
      }
    }

    context("POST - Save teacher") {
      should("return saved teacher & 201 status code") {
        val newTeacher = Teacher(3, "Víctor", "Cauich", 40)
        val resourceLocation = "${TeacherController.BASE_URL}/${newTeacher.id}"
        every { teacherService.saveTeacher(any()) } returns newTeacher

        mockMvc
          .perform(
            post(TeacherController.BASE_URL)
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(newTeacher))
          )
          .andExpect(status().isCreated)
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.names").value(newTeacher.names))
          .andExpect(header().string("Location", resourceLocation))
      }

      xshould("return 400 status code when invalid fields") {
        val newTeacher = Teacher(5, "Ni", "Ca", -30)
        mockMvc.perform(
          post(TeacherController.BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(newTeacher))
        )
          .andExpect(status().isBadRequest)
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        verify(exactly = 0) { teacherService.saveTeacher(any()) }
      }
    }

    context("PUT - Update teacher") {
      should("return updated teacher & 200 status code") {
        val updatedTeacher = Teacher(5, "Eusebio", "Ajax Santos", 60)
        val updatedTeacherId = updatedTeacher.id
        val url = "${TeacherController.BASE_URL}/$updatedTeacherId"
        val studentIdSlot = slot<Long>()
        every {
          teacherService.updateTeacher(capture(studentIdSlot), any())
        } returns updatedTeacher

        mockMvc
          .perform(
            put(url)
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(updatedTeacher))
          )
          .andExpect(status().isOk)
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.names").value("Eusebio"))
          .andExpect(jsonPath("$.surnames").value("Ajax Santos"))
        studentIdSlot.captured shouldBe updatedTeacherId
      }

      xshould("return 400 status code when invalid fields") {
        val updatedTeacher = Teacher(5, "Eu", "Ajax Santos", -60)
        val url = "${TeacherController.BASE_URL}/${updatedTeacher.id}"

        mockMvc
          .perform(
            put(url)
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(updatedTeacher))
          )
          .andExpect(status().isBadRequest)
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        verify(exactly = 0) { teacherService.updateTeacher(any(), any()) }
      }

      should("return 400 status code when teacher to update not found") {
        val updatedTeacher = Teacher(5, "Eusebio", "Ajax Santos", 60)
        val updateTeacherId = updatedTeacher.id
        val url = "${TeacherController.BASE_URL}/$updateTeacherId"
        every {
          teacherService.updateTeacher(any(), any())
        } throws TeacherNotFoundException(updateTeacherId)

        mockMvc
          .perform(
            put(url)
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(updatedTeacher))
          )
          .andExpect(status().isNotFound)
      }
    }

    context("DELETE - Delete teacher") {
      should("return deleted teacher & 200 status code") {
        val teacherToDelete = teachersWithinDatabase.first()
        val teacherToDeleteId = teacherToDelete.id
        val url = "${TeacherController.BASE_URL}/$teacherToDeleteId"
        every { teacherService.deleteTeacher(teacherToDeleteId) } returns teacherToDelete

        mockMvc
          .perform(delete(url))
          .andExpect(status().isOk)
          .andExpect(jsonPath("$.names").value(teacherToDelete.names))
          .andExpect(jsonPath("$.surnames").value(teacherToDelete.surnames))
      }

      should("return 404 status code when teacher to delete is not found") {
        val teacherToDelete = Teacher(5, "Eusebio", "Ajax Santos", 60)
        val teacherToDeleteId = teacherToDelete.id
        val url = "${TeacherController.BASE_URL}/$teacherToDeleteId"
        every {
          teacherService.deleteTeacher(teacherToDeleteId)
        } throws StudentNotFoundException(teacherToDeleteId)

        mockMvc
          .perform(delete(url))
          .andExpect(status().isNotFound)
      }
    }
  }
}
