package com.inspector.centinela

import com.inspector.annotations.*
import com.inspector.centinela.controller.Centinela
import com.inspector.centinela.controller.CentinelaFactory
import com.inspector.centinela.model.exceptions.ValidationAggregatorException
import org.junit.Test
import org.junit.Assert.*
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class CentinelaTest {

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
    
    val factory = CentinelaFactory(
        transformerRegistry = true,
        validatorRegistry = true
    )

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

}

