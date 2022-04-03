package me.hikingcarrot7.aws.models

import javax.persistence.*
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "students")
class Student() {
  @Id
  @GeneratedValue
  var id = Long.MIN_VALUE

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
  lateinit var enrolment: String

  @Column
  @NotNull
  @DecimalMin("0.0", message = "El promedio debe ser mayor a 0")
  @DecimalMax("100.0", message = "El promedio debe ser menor que 100")
  var gradePointAverage = Double.MIN_VALUE

  constructor(
    id: Long,
    names: String,
    surnames: String,
    enrolment: String,
    gradePointAverage: Double
  ) : this() {
    this.id = id
    this.names = names
    this.surnames = surnames
    this.enrolment = enrolment
    this.gradePointAverage = gradePointAverage
  }
}