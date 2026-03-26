package com.inspector.centinela

import com.inspector.annotations.*
import com.inspector.centinela.controller.Centinela
import com.inspector.centinela.controller.CentinelaFactory
import com.inspector.centinela.handler.ValidationResultHandler
import com.inspector.centinela.model.exceptions.ValidationAggregatorException
import com.inspector.centinela.model.rules.operative.CapitalizeTransformer
import com.inspector.centinela.model.rules.operative.MinValidator
import com.inspector.centinela.model.rules.operative.NotZeroValidator
import com.inspector.centinela.model.rules.operative.RegexValidator
import org.junit.Test
import org.junit.Assert.*
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class CentinelaTest {

    val factory = CentinelaFactory(
        transformerRegistry = true,
        validatorRegistry = true
    )

    data class TextModel(
        @Trim
        @Capitalize
        @NotBlank
        @Size(min = 3, max = 10)
        var username: String,

        @Regex(regex = "^[0-9]+$")
        var pinCode: String
    )

    data class NumberModel(
        @Positive
        @Max(max = 100)
        val score: Int = 0,

        @Negative
        val debt: Double = 0.0,

        @NotZero
        val multiplier: Float = 0.0f
    )

    data class Motorcycle(
        @NotNull
        val owner: String?,

        @NotEmpty
        val licenseType: String,

        @Trim
        @NotEmpty
        var customName: String,

        @Size(min = 3, max = 5)
        val modelCode: String,

        @Regex(regex = "^[A-Z]{3}-\\d{4}$")
        val plateNumber: String,

        @Min(min = 600)
        @Max(max = 1000)
        val engineCc: Int
    )

    // 1. Modelo para probar la agregación masiva
    data class Musician(
        @NotBlank
        @Size(min = 2, max = 20)
        val name: String,
        @Positive
        @Max(100)
        val age: Int,
        @NotEmpty
        val band: String
    )

    // 2. Modelo para probar el orden de los Transformadores
    data class UserInput(
        @Trim
        @Size(min = 5)
        var searchString: String
    )

    // 3. Modelo sin anotaciones
    data class PlainObject(
        val id: Int,
        val description: String
    )

    // 4. Modelo con propiedades privadas (¡El gran reto de la reflexión!)
    class EngineSpecs(
        @NotZero
        private val compressionRatio: Double,
        @Positive
        private val cylinders: Int
    )

    // 5. Interfaz de prueba para el Handler
    class TestValidationHandler : ValidationResultHandler {
        val capturedErrors = mutableListOf<Exception>()
        override fun onValidationFailed(errors: List<Exception>) {
            capturedErrors.addAll(errors)
        }
    }

    @Test
    fun `Trim y Capitalize deben limpiar y formatear el texto correctamente antes de validar`() {
        // Texto con espacios al inicio/final y en minúsculas
        val model = TextModel(username = "   juanito   ", pinCode = "1234")

        // Ejecutamos el motor (sin que lance error)

        Centinela(factory).engine(model, throws = true)

        // Verificamos que el motor haya modificado las variables
        assertEquals("Juanito", model.username)
    }

    @Test
    fun `Debe lanzar excepcion si el String esta en blanco o falla el Regex`() {
        val model = TextModel(username = "   ", pinCode = "ABC") // pinCode debería ser solo números

        val exception = assertThrows<ValidationAggregatorException> {
            Centinela(factory).engine(model, throws = true)
        }

        val errorMessages = exception.errors.map { it.message }
        assertTrue(errorMessages.any { it!!.contains("NotBlank") })
        assertTrue(errorMessages.any { it!!.contains("Regex") })
    }

    @Test
    fun `Debe lanzar excepcion si no cumple el Size`() {
        val modelShort = TextModel(username = "al", pinCode = "1234") // min = 3
        val exception = assertThrows<ValidationAggregatorException> {
            Centinela(factory).engine(modelShort, throws = true)
        }
        assertTrue(exception.errors.any { it.message!!.contains("Size") })
    }

    // --- 4. PRUEBAS DE VALIDACIÓN NUMÉRICA ---

    @Test
    fun `Numeros validos no deben lanzar excepciones`() {
        val validModel = NumberModel(score = 85, debt = -50.5, multiplier = 1.5f)
        // Si lanza excepción, la prueba fallará automáticamente
        Centinela(factory).engine(validModel, throws = true)
    }

    @Test
    fun `Debe fallar si Positive recibe cero o negativo`() {
        val model = NumberModel(score = 0, debt = -10.0, multiplier = 1f)
        val exception = assertThrows<ValidationAggregatorException> {
            Centinela(factory).engine(model, throws = true)
        }
        assertTrue(exception.errors.any { it.message!!.contains("Positive") })
    }

    @Test
    fun `Debe fallar si Negative recibe cero o positivo`() {
        val model = NumberModel(score = 10, debt = 0.0, multiplier = 1f)
        val exception = assertThrows<ValidationAggregatorException> {
            Centinela(factory).engine(model, throws = true)
        }
        assertTrue(exception.errors.any { it.message!!.contains("Negative") })
    }

    @Test
    fun `Debe fallar si NotZero recibe cero`() {
        val model = NumberModel(score = 10, debt = -5.0, multiplier = 0.0f)
        val exception = assertThrows<ValidationAggregatorException> {
            Centinela(factory).engine(model, throws = true)
        }
        assertTrue(exception.errors.any { it.message!!.contains("NotZero") })
    }

    @Test
    fun `Debe fallar si excede el Max`() {
        val model = NumberModel(score = 150, debt = -5.0, multiplier = 1f) // max = 100
        val exception = assertThrows<ValidationAggregatorException> {
            Centinela(factory).engine(model, throws = true)
        }
        assertTrue(exception.errors.any { it.message!!.contains("Max") })
    }

    @Test
    fun `NotEmpty debe permitir cadenas con solo espacios, a diferencia de NotBlank`() {
        val bike = Motorcycle(owner = "Juan", licenseType = "   ", customName = "Susy", modelCode = "GSX", plateNumber = "ABC-1234", engineCc = 600)
        // No debe lanzar excepción porque "   " no está vacío (tiene longitud 3)
        val factory = CentinelaFactory(
            validatorRegistry = true
        )
        Centinela(factory).engine(bike, throws = true)
    }

    @Test
    fun `Trim seguido de NotEmpty debe fallar si el usuario solo envia espacios`() {
        val bike = Motorcycle(owner = "Juan", licenseType = "A", customName = "   ", modelCode = "GSX", plateNumber = "ABC-1234", engineCc = 600)

        val exception = assertThrows<ValidationAggregatorException> {
            Centinela(factory).engine(bike, throws = true)
        }
        assertTrue(exception.errors.any { it.message!!.contains("NotEmpty") })
    }

    @Test
    fun `Min y Max deben permitir los valores limite exactos`() {
        val bikeLimitMin = Motorcycle(owner = "Juan", licenseType = "A", customName = "Susy", modelCode = "GSX", plateNumber = "ABC-1234", engineCc = 600) // Límite inferior
        val bikeLimitMax = Motorcycle(owner = "Juan", licenseType = "A", customName = "Susy", modelCode = "GSX", plateNumber = "ABC-1234", engineCc = 1000) // Límite superior

        Centinela(factory).engine(bikeLimitMin, throws = true)
        Centinela(factory).engine(bikeLimitMax, throws = true)
    }

    @Test
    fun `Debe fallar si el valor es menor al Min o mayor al Max`() {
        val bikeUnder = Motorcycle(owner = "Juan", licenseType = "A", customName = "Susy", modelCode = "GSX", plateNumber = "ABC-1234", engineCc = 599)
        val bikeOver = Motorcycle(owner = "Juan", licenseType = "A", customName = "Susy", modelCode = "GSX", plateNumber = "ABC-1234", engineCc = 1001)

        assertThrows<ValidationAggregatorException> {  Centinela(factory).engine(bikeUnder, throws = true) }
        assertThrows<ValidationAggregatorException> {  Centinela(factory).engine(bikeOver, throws = true) }
    }
    
    @Test
    fun `NotNull debe fallar si recibe un valor nulo explicito`() {
        val bike = Motorcycle(owner = null, licenseType = "A", customName = "Susy", modelCode = "GSX", plateNumber = "ABC-1234", engineCc = 600)
        val exception = assertThrows<ValidationAggregatorException> {
             Centinela(factory).engine(bike, throws = true)
        }
        assertTrue(exception.errors.any { it.message!!.contains("NotNull") })
    }

    @Test
    fun `Regex debe fallar si el formato no coincide exactamente`() {
        // Placa inválida (falta el guion)
        val bike = Motorcycle(owner = "Juan", licenseType = "A", customName = "Susy", modelCode = "GSX", plateNumber = "ABC1234", engineCc = 600)
        val exception = assertThrows<ValidationAggregatorException> {
             Centinela(factory).engine(bike, throws = true)
        }
        assertTrue(exception.errors.any { it.message!!.contains("Regex") })
    }

    @Test
    fun `El motor no debe hacer nada si el objeto no tiene anotaciones`() {
        val plain = PlainObject(1, "Sin reglas")
        // assertDoesNotThrow asegura que el motor no crashea al leer un objeto limpio
        assertDoesNotThrow {
            Centinela(factory).engine(plain, throws = true)
        }
    }
    @Test
    fun `El ValidationResultHandler debe capturar errores sin lanzar Excepciones`() {
        // Datos inválidos (edad negativa, banda vacía)
        val tom = Musician(name = "Tom Araya", age = -5, band = "")
        val handler = TestValidationHandler()

        // throws = false, pasamos el handler
        assertDoesNotThrow {
            Centinela(factory).engine(tom, throws = false, handler = handler)
        }

        // Verificamos que el handler guardó los errores internamente
        assertEquals(2, handler.capturedErrors.size)
        assertTrue(handler.capturedErrors.any { it.message!!.contains("Positive") })
        assertTrue(handler.capturedErrors.any { it.message!!.contains("NotEmpty") })
    }

    @Test
    fun `Debe agregar errores de multiples propiedades en una sola excepcion`() {
        // Todo está mal en este objeto
        val badMusician = Musician(name = "", age = 150, band = "")

        val exception = assertThrows<ValidationAggregatorException> {
            Centinela(factory).engine(badMusician, throws = true)
        }

        // Debería tener 4 errores: NotBlank, Size (por el nombre), Max (por la edad), NotEmpty (banda)
        assertEquals(4, exception.errors.size)
    }

    @Test
    fun `Los transformadores (Trim) se ejecutan ANTES que los validadores (Size)`() {
        // La longitud original es 7, pero con @Trim bajará a 3. 
        // El @Size(min=5) debería fallar sobre el texto ya cortado.
        val input = UserInput(searchString = "  ABC  ")

        val exception = assertThrows<ValidationAggregatorException> {
            Centinela(factory).engine(input, throws = true)
        }

        assertTrue(exception.errors.any { it.message!!.contains("Size") })
        assertEquals("ABC", input.searchString) // Confirmamos que sí lo mutó
    }

    @Test
    fun `Capitalize debe ignorar strings vacios o nulos sin crashear`() {
        // Simulamos un validador manual para Capitalize solo para probar su lógica interna
        val transformer = CapitalizeTransformer()
        assertEquals("", transformer.transform(Capitalize(), ""))
        assertEquals(null, transformer.transform(Capitalize(), null))
    }

    @Test
    fun `Capitalize debe manejar correctamente textos que ya estan en mayusculas`() {
        val transformer = CapitalizeTransformer()
        assertEquals("SLAYER", transformer.transform(Capitalize(), "SLAYER"))
        assertEquals("Slayer", transformer.transform(Capitalize(), "slayer"))
    }

    @Test
    fun `NotZero debe manejar la precision de Double y Float correctamente`() {
        val validator = NotZeroValidator()

        // 0.0000001 no es cero, debe ser válido
        assertTrue(validator.isValid(NotZero(), 0.0000001))

        // 0.0 estricto es inválido
        assertTrue(!validator.isValid(NotZero(), 0.0))
    }

    @Test
    fun `Regex debe funcionar para validar estructuras complejas como correos`() {
        // Reutilizamos el motor pero validamos la lógica pura del validador de Regex
        val validator = RegexValidator()
        val emailRegex = Regex(regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")

        assertTrue(validator.isValid(emailRegex, "usuario@correo.com"))
        assertTrue(!validator.isValid(emailRegex, "usuario@.com")) // Inválido
    }

    @Test
    fun `Min y Max deben hacer casting automatico para soportar Ints aunque la regla sea Long`() {
        val validator = MinValidator()
        val minRule = Min(min = 600) // Regla de 600 (Long por defecto en tu anotación)

        // Pasamos un Int (600cc). El validador debe convertirlo a Long internamente sin crashear.
        assertTrue(validator.isValid(minRule, 600))
        assertTrue(!validator.isValid(minRule, 599))
    }

    @Test
    fun `El motor debe poder leer y validar propiedades PRIVADAS`() {
        val specs = EngineSpecs(compressionRatio = 0.0, cylinders = 4)

        // El motor va a intentar leer "compressionRatio" que es private.
        val exception = assertThrows<ValidationAggregatorException> {
            Centinela(factory).engine(specs, throws = true)
        }

        assertTrue(exception.errors.any { it.message!!.contains("NotZero") })
    }

}

