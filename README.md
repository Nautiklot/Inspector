# 🛡️ Centinela

[![Release](https://jitpack.io/v/Nautiklot/Inspector.svg)](https://jitpack.io/#Nautiklot/Inspector)
[![Kotlin](https://img.shields.io/badge/kotlin-1.9.22-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

*Read this in other languages: [Español](README.es.md)*

A dynamic, annotation-based validation engine for Kotlin focused on **Developer Experience (DX)**. 

Unlike heavy traditional frameworks, Centinela combines strict **compile-time safety** (via Kapt) with an ultra-lightweight **runtime** reflection engine. If you try to validate an incompatible data type, your code simply won't compile. No more unexpected runtime crashes due to misplaced annotations.

## ✨ Features

* **Fail-Fast (Compile-Time Safety):** The annotation processor prevents you from using rules on incompatible types (e.g., using `@NotBlank` on an `Int` will break the build).
* **Data Transformers:** It doesn't just validate; it cleans your data (e.g., `@Trim`, `@Capitalize`) before evaluation.
* **100% Kotlin-First:** Designed to leverage Kotlin's type system without heavy Java EE dependencies.
* **Clean Error Handling:** Aggregates all validation errors into a single exception or returns them through a clean, optional interface.

---

## 📦 Installation

This library is hosted on **JitPack**.

**1. Add the JitPack repository** to your `settings.gradle.kts` or project-level `build.gradle.kts`:

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

**2. Add the dependencies**  in your app module:

```kotlin
  plugins {
      kotlin("kapt")
  }
  
  dependencies {
      val validKtVersion = "1.0.0" // Check the latest version on the badge above
  
      // Core annotations and runtime engine
      implementation("com.github.Nautiklot.Inspector:annotations:$validKtVersion")
      implementation("com.github.Nautiklot.Inspector:centinela:$validKtVersion")
      
      // Compile-time processor for type safety
      kapt("com.github.com.github.Nautiklot.Inspector:processor:$validKtVersion")
  }
```

## 🚀 Quick Start

**1. Define your Model** Use the annotations to set your rules. Use `var` if you are applying Transformers (like `@Capitalize`), or `val` if you are only using pure Validators.:

```kotlin
import com.inspector.annotations.*

data class Motorcycle(
    @NotBlank 
    @Capitalize 
    var brand: String, // If "suzuki" is provided, the engine mutates it to "Suzuki"
    
    @NotBlank 
    var model: String,
    
    @Positive 
    @Min(600) 
    val engineCc: Int, // If 599 is provided, it throws an error
    
    @Regex("^[A-Z0-9-]{5,8}$") 
    val licensePlate: String
)
```

**2 .Execute Validation** Pass your object through the `Centinela`. You can decide whether to throw an exception (`throws = true`) or handle the errors manually via an interface.:

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

## 🛠️ Supported Annotations

The Kapt processor will strictly verify that you use these annotations on their allowed data types.

### Transformers (Data Mutation)
| Annotation | Allowed Types | Description |
| :--- | :--- | :--- |
| `@Capitalize` | `String` | Capitalizes the first letter of the string. |
| `@Trim` | `String` | Removes leading and trailing whitespace. |

### Logical Validators
| Annotation | Allowed Types | Description |
| :--- | :--- | :--- |
| `@NotNull` | `Any` | Fails if the explicit value is null. |
| `@NotBlank` | `String` | Fails if null, empty, or consists only of whitespace. |
| `@NotEmpty` | `String` | Fails if length is 0. |
| `@Regex(pattern)` | `String` | Fails if the string does not match the regular expression. |
| `@Size(min, max)` | `String` | Verifies that the length is within the range. |
| `@Positive` / `@Negative` | `Number` | Verifies the number is strictly greater (or less) than 0. |
| `@NotZero` | `Number` | Fails if the number is exactly 0. |
| `@Min(value)` / `@Max(value)`| `Number` | Sets an inclusive numeric limit. |

---

## 🤝 Contributing
Contributions are always welcome! To add a new validation rule:
1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/NewRule`).
3. Create the annotation and ensure you use `@AllowedType` for compile-time safety.
4. Implement the validator inheriting from `ConstraintValidator`.
5. Open a Pull Request.

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
