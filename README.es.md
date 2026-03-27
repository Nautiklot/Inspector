# 🛡️ Centinela

[![Release](https://jitpack.io/v/Nautiklot/Inspector.svg)](https://jitpack.io/#Nautiklot/Inspector)
[![Kotlin](https://img.shields.io/badge/kotlin-1.9.22-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

*Read this in other languages: [English](README.md)*

Un motor de validación dinámico basado en anotaciones para Kotlin, enfocado en la **Experiencia del Desarrollador (DX)**.

A diferencia de los pesados ​​frameworks tradicionales, Centinela combina una estricta **seguridad en tiempo de compilación** (mediante Kapt) con un motor de reflexión ultraligero en **tiempo de ejecución**. Si intentas validar un tipo de dato incompatible, tu código simplemente no compilará. Se acabaron los fallos inesperados en tiempo de ejecución debido a anotaciones mal colocadas.

## ✨ Características

* **Fallo rápido (Seguridad en tiempo de compilación):** El procesador de anotaciones impide el uso de reglas en tipos incompatibles (por ejemplo, usar `@NotBlank` en un `Int` provocará un error de compilación).
* **Transformadores de datos:** No solo valida, sino que limpia los datos (por ejemplo, `@Trim`, `@Capitalize`) antes de la evaluación.
* **100% Kotlin-First:** Diseñado para aprovechar el sistema de tipos de Kotlin sin dependencias pesadas de Java EE.
* **Gestión de errores limpia:** Agrupa todos los errores de validación en una única excepción o los devuelve a través de una interfaz limpia y opcional.

---

## 📦 Instalación

Esta biblioteca está alojada en **JitPack**.

**1. Agrega el repositorio de JitPack** a tu archivo `settings.gradle.kts` o al archivo `build.gradle.kts` de tu proyecto:

```kotlin
    dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
      google()
      mavenCentral()
      maven { url = uri("[https://jitpack.io](https://jitpack.io)") }
    }
}
```

**2. Agrega las dependencias** en el módulo de tu aplicación:

```kotlin

plugins {
  kotlin("kapt")
}

dependencies {
  val validKtVersion = "1.0.0" // Consulta la última versión en la insignia de arriba

  // Anotaciones principales y motor de ejecución
  implementation("com.github.Nautiklot.Inspector:annotations:$validKtVersion")
  implementation("com.github.Nautiklot.Inspector:centinela:$validKtVersion")

  // Procesador en tiempo de compilación para seguridad de tipos
  kapt("com.github.com.github.Nautiklot.Inspector:processor:$validKtVersion")
}
```

## 🚀 Inicio rápido

**1. Define tu modelo** Usa las anotaciones para establecer tus reglas. Usa `var` si aplicas transformadores (como `@Capitalize`), o `val` si solo usas validadores puros:

```kotlin
import com.inspector.annotations.*

data class Motorcycle(
    @NotBlank
    @Capitalize
    var brand: String, // Si se proporciona "suzuki", el motor lo transforma en "Suzuki"
    
    @NotBlank
    var model: String,
    
    @Positive
    @Min(600)
    val engineCc: Int, // Si se proporciona 599, se produce un error
    
    @Regex("^[A-Z0-9-]{5,8}$")
    val licensePlate: String
)
```

**2. Ejecutar validación** Pasa tu objeto a través de `Centinela`. Puedes decidir si lanzar una excepción (`throws = true`) o gestionar los errores manualmente mediante una interfaz:

```kotlin
fun main() {
    val myBike = Motorcycle(
        brand = "suzuki", 
        model = "GSX-R", 
        engineCc = 600, 
        licensePlate = "bad-plate!"
    )

    try {
        Centinela.engine(myBike, throws = true)
        println("Motorcycle registered: ${myBike.brand}") 
    } catch (e: ValidationAggregatorException) {
        println("Validation failed:")
        e.errors.forEach { println("- ${it.message}") }
    }
}
```

## 🛠️ Anotaciones compatibles

El procesador Kapt verificará estrictamente que uses estas anotaciones en los tipos de datos permitidos.

### Transformadores (Mutación de datos)
| Anotación | Tipos permitidos | Descripción |
| :--- | :--- | :--- |
| `@Capitalize` | `String` | Pon en mayúscula la primera letra de la cadena. |
| `@Trim` | `String` | Elimina los espacios en blanco iniciales y finales. |

### Validadores lógicos
| Anotación | Tipos permitidos | Descripción |
| :--- | :--- | :--- |
| `@NotNull` | `Any` | Falla si el valor explícito es nulo. |
| `@NotBlank` | `String` | Falla si es nulo, está vacío o solo contiene espacios en blanco. |
| `@NotEmpty` | `String` | Falla si la longitud es 0. |
| `@Regex(pattern)` | `String` | Falla si la cadena no coincide con la expresión regular. |
| `@Size(min, max)` | `String` | Verifica que la longitud esté dentro del rango. |
| `@Positive` / `@Negative` | `Number` | Verifica que el número sea estrictamente mayor (o menor) que 0. |
| `@NoZero` | `Number` | Falla si el número es exactamente 0. |
| `@Min(value)` / `@Max(value)` | `Number` | Establece un límite numérico inclusivo. |

---

## 🤝 Contribuciones
¡Las contribuciones son siempre bienvenidas! Para agregar una nueva regla de validación:
1. Bifurca el repositorio.
2. Crea una rama de características (`git checkout -b feature/NewRule`).
3. Crea la anotación y asegúrate de usar `@AllowedType` para garantizar la seguridad en tiempo de compilación.
4. Implementa el validador que hereda de `ConstraintValidator`.
5. Abre una solicitud de extracción (Pull Request).

---

## 📄 Licencia
Este proyecto está bajo la licencia MIT. Consulte el archivo [LICENSE](LICENSE) para obtener más detalles.
