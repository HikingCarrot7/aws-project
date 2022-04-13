package me.hikingcarrot7.aws.repositories

import me.hikingcarrot7.aws.models.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentRepository : JpaRepository<Student, Long>