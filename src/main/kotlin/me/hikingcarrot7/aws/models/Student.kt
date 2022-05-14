package me.hikingcarrot7.aws.models

import javax.persistence.*
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@Entity
@Table(name = "students")
class Student() {
  @Id
  @GeneratedValue
  var id = Long.MIN_VALUE

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
  var apellidos: String = ""

  @Column
  var fotoPerfilUrl: String = ""

  @Column
  @NotEmpty
  lateinit var matricula: String

  @Column
  @DecimalMin("0.0", message = "El promedio debe ser mayor a 0")
  @DecimalMax("100.0", message = "El promedio debe ser menor que 100")
  var promedio = Double.MIN_VALUE

  constructor(
    id: Long,
    nombres: String,
    apellidos: String,
    matricula: String,
    promedio: Double
  ) : this() {
    this.id = id
    this.nombres = nombres
    this.apellidos = apellidos
    this.matricula = matricula
    this.promedio = promedio
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Student

    if (id != other.id) return false
    if (nombres != other.nombres) return false
    if (apellidos != other.apellidos) return false
    if (matricula != other.matricula) return false
    if (promedio != other.promedio) return false

    return true
  }

  override fun hashCode(): Int {
    var result = id.hashCode()
    result = 31 * result + nombres.hashCode()
    result = 31 * result + apellidos.hashCode()
    result = 31 * result + matricula.hashCode()
    result = 31 * result + promedio.hashCode()
    return result
  }

}