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
  @Min(1, message = "El número del empleado debe ser mayor a 1")
  var employeeNumber = Long.MIN_VALUE

  @Column
  @NotNull
  @Size(
    min = 5,
    message = "Los nombres deben tener un mínimo de 5 caracteres"
  )
  lateinit var names: String

  @Column
  @NotNull
  @Size(
    min = 5,
    message = "Los apellidos deben tener un mínimo de 5 caracteres"
  )
  lateinit var surnames: String

  @Column
  @NotNull
  @Min(1, message = "El número de horas de clases debe ser mayor a 1")
  var classHours: Int = Int.MIN_VALUE
}