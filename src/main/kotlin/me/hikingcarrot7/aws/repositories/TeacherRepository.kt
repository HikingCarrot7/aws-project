package me.hikingcarrot7.aws.repositories

import me.hikingcarrot7.aws.models.Teacher
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TeacherRepository : JpaRepository<Teacher, Long>