package me.hikingcarrot7.aws.models

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

@Entity
@Table(name = "teachers")
class Teacher() {
  @Id
  @Positive(message = "Debe especificarse un identificador válido para el maestro")
  var id = Long.MIN_VALUE

  @Column
  @Min(1, message = "El número del empleado debe ser mayor a 1")
  var numeroEmpleado = Long.MIN_VALUE

  @Column
  @NotEmpty
  @Size(
    min = 5,
    message = "Los nombres deben tener un mínimo de 5 caracteres"
  )
  lateinit var nombres: String

  @Column
  @NotEmpty
  @Size(
    min = 5,
    message = "Los apellidos deben tener un mínimo de 5 caracteres"
  )
  lateinit var apellidos: String

  @Column
  @Min(0, message = "El número de horas de clases debe ser positivo")
  var horasClase: Int = Int.MIN_VALUE

  constructor(
    id: Long,
    numeroEmpleado: Long,
    nombres: String,
    apellidos: String,
    horasClase: Int
  ) : this() {
    this.id = id
    this.numeroEmpleado = numeroEmpleado
    this.nombres = nombres
    this.apellidos = apellidos
    this.horasClase = horasClase
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Teacher

    if (id != other.id) return false
    if (numeroEmpleado != other.numeroEmpleado) return false
    if (nombres != other.nombres) return false
    if (apellidos != other.apellidos) return false
    if (horasClase != other.horasClase) return false

    return true
  }

  override fun hashCode(): Int {
    var result = id.hashCode()
    result = 31 * result + numeroEmpleado.hashCode()
    result = 31 * result + nombres.hashCode()
    result = 31 * result + apellidos.hashCode()
    result = 31 * result + horasClase
    return result
  }
}