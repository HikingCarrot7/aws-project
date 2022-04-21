package me.hikingcarrot7.aws.models

import com.sun.istack.NotNull
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.Size

@Entity
@Table(name = "teachers")
class Teacher() {

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

  constructor(
    employeeNumber: Long,
    names: String,
    surnames: String,
    classHours: Int
  ) : this() {
    this.employeeNumber = employeeNumber
    this.names = names
    this.surnames = surnames
    this.classHours = classHours
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Teacher

    if (id != other.id) return false
    if (employeeNumber != other.employeeNumber) return false
    if (names != other.names) return false
    if (surnames != other.surnames) return false
    if (classHours != other.classHours) return false

    return true
  }

  override fun hashCode(): Int {
    var result = id.hashCode()
    result = 31 * result + employeeNumber.hashCode()
    result = 31 * result + names.hashCode()
    result = 31 * result + surnames.hashCode()
    result = 31 * result + classHours
    return result
  }


}