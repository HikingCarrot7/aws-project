package me.hikingcarrot7.aws.models

import com.sun.istack.NotNull
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.Min
import javax.validation.constraints.Size

@Entity
@Table(name = "teachers")
class Teacher {

  @Id
  @GeneratedValue
  val id = Long.MIN_VALUE

  @Column
  @NotNull
  @Min(1)
  var employeeNumber = Long.MIN_VALUE

  @Column
  @NotNull
  @Size(min = 5)
  lateinit var names: String

  @Column
  @NotNull
  @Size(min = 5)
  lateinit var surnames: String

  @Column
  @NotNull
  @Min(1)
  var classHours: Int = Int.MIN_VALUE
}