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

/**
 * Unit tests for the [Centinela] validation engine.
 */
class CentinelaTest {

    private val factory = CentinelaFactory(
        transformerRegistry = true,
        validatorRegistry = true
    )

    /** Model for testing text-based annotations like [Trim], [Capitalize], and [Regex]. */
    data class TextModel(
        @Trim
        @Capitalize
        @NotBlank
        @Size(min = 3, max = 10)
        var username: String,

        @Regex(regex = "^[0-9]+$")
        var pinCode: String
    )

    /** Model for testing numeric annotations like [Positive], [Negative], [Max], and [NotZero]. */
    data class NumberModel(
        @Positive
        @Max(max = 100)
        val score: Int = 0,

        @Negative
        val debt: Double = 0.0,

        @NotZero
        val multiplier: Float = 0.0f
    )

    /** Complex model for testing various mixed constraints. */
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

    /** Model to test massive error aggregation. */
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

    /** Model to test the execution order of transformers vs validators. */
    data class UserInput(
        @Trim
        @Size(min = 5)
        var searchString: String
    )

    /** Model without any annotations to test engine safety. */
    data class PlainObject(
        val id: Int,
        val description: String
    )

    /** Model with private properties to test reflection capabilities. */
    class EngineSpecs(
        @NotZero
        private val compressionRatio: Double,
        @Positive
        private val cylinders: Int
    )

    /** Test implementation of [ValidationResultHandler] to capture errors. */
    class TestValidationHandler : ValidationResultHandler {
        val capturedErrors = mutableListOf<Exception>()
        override fun onValidationFailed(errors: List<Exception>) {
            capturedErrors.addAll(errors)
        }
    }

    @Test
    fun `Trim and Capitalize should clean and format text correctly before validation`() {
        val model = TextModel(username = "   juanito   ", pinCode = "1234")
        Centinela(factory).engine(model, throws = true)
        assertEquals("Juanito", model.username)
    }

    @Test
    fun `Should throw exception if string is blank or Regex fails`() {
        val model = TextModel(username = "   ", pinCode = "ABC")
        val exception = assertThrows<ValidationAggregatorException> {
            Centinela(factory).engine(model, throws = true)
        }
        val errorMessages = exception.errors.map { it.message }
        assertTrue(errorMessages.any { it!!.contains("NotBlank") })
        assertTrue(errorMessages.any { it!!.contains("Regex") })
    }

    @Test
    fun `Should throw exception if Size constraint is not met`() {
        val modelShort = TextModel(username = "al", pinCode = "1234")
        val exception = assertThrows<ValidationAggregatorException> {
            Centinela(factory).engine(modelShort, throws = true)
        }
        assertTrue(exception.errors.any { it.message!!.contains("Size") })
    }

    @Test
    fun `Valid numbers should not throw any exceptions`() {
        val validModel = NumberModel(score = 85, debt = -50.5, multiplier = 1.5f)
        Centinela(factory).engine(validModel, throws = true)
    }

    @Test
    fun `Should fail if Positive receives zero or negative value`() {
        val model = NumberModel(score = 0, debt = -10.0, multiplier = 1f)
        val exception = assertThrows<ValidationAggregatorException> {
            Centinela(factory).engine(model, throws = true)
        }
        assertTrue(exception.errors.any { it.message!!.contains("Positive") })
    }

    @Test
    fun `Should fail if Negative receives zero or positive value`() {
        val model = NumberModel(score = 10, debt = 0.0, multiplier = 1f)
        val exception = assertThrows<ValidationAggregatorException> {
            Centinela(factory).engine(model, throws = true)
        }
        assertTrue(exception.errors.any { it.message!!.contains("Negative") })
    }

    @Test
    fun `Should fail if NotZero receives zero value`() {
        val model = NumberModel(score = 10, debt = -5.0, multiplier = 0.0f)
        val exception = assertThrows<ValidationAggregatorException> {
            Centinela(factory).engine(model, throws = true)
        }
        assertTrue(exception.errors.any { it.message!!.contains("NotZero") })
    }

    @Test
    fun `Should fail if numeric value exceeds Max constraint`() {
        val model = NumberModel(score = 150, debt = -5.0, multiplier = 1f)
        val exception = assertThrows<ValidationAggregatorException> {
            Centinela(factory).engine(model, throws = true)
        }
        assertTrue(exception.errors.any { it.message!!.contains("Max") })
    }

    @Test
    fun `NotEmpty should allow strings with only spaces unlike NotBlank`() {
        val bike = Motorcycle(owner = "Juan", licenseType = "   ", customName = "Susy", modelCode = "GSX", plateNumber = "ABC-1234", engineCc = 600)
        val validatorOnlyFactory = CentinelaFactory(validatorRegistry = true)
        Centinela(validatorOnlyFactory).engine(bike, throws = true)
    }

    @Test
    fun `Trim followed by NotEmpty should fail if user only sends spaces`() {
        val bike = Motorcycle(owner = "Juan", licenseType = "A", customName = "   ", modelCode = "GSX", plateNumber = "ABC-1234", engineCc = 600)
        val exception = assertThrows<ValidationAggregatorException> {
            Centinela(factory).engine(bike, throws = true)
        }
        assertTrue(exception.errors.any { it.message!!.contains("NotEmpty") })
    }

    @Test
    fun `Min and Max should allow exact boundary values`() {
        val bikeLimitMin = Motorcycle(owner = "Juan", licenseType = "A", customName = "Susy", modelCode = "GSX", plateNumber = "ABC-1234", engineCc = 600)
        val bikeLimitMax = Motorcycle(owner = "Juan", licenseType = "A", customName = "Susy", modelCode = "GSX", plateNumber = "ABC-1234", engineCc = 1000)

        Centinela(factory).engine(bikeLimitMin, throws = true)
        Centinela(factory).engine(bikeLimitMax, throws = true)
    }

    @Test
    fun `Should fail if value is below Min or above Max`() {
        val bikeUnder = Motorcycle(owner = "Juan", licenseType = "A", customName = "Susy", modelCode = "GSX", plateNumber = "ABC-1234", engineCc = 599)
        val bikeOver = Motorcycle(owner = "Juan", licenseType = "A", customName = "Susy", modelCode = "GSX", plateNumber = "ABC-1234", engineCc = 1001)

        assertThrows<ValidationAggregatorException> { Centinela(factory).engine(bikeUnder, throws = true) }
        assertThrows<ValidationAggregatorException> { Centinela(factory).engine(bikeOver, throws = true) }
    }

    @Test
    fun `NotNull should fail if it receives an explicit null value`() {
        val bike = Motorcycle(owner = null, licenseType = "A", customName = "Susy", modelCode = "GSX", plateNumber = "ABC-1234", engineCc = 600)
        val exception = assertThrows<ValidationAggregatorException> {
            Centinela(factory).engine(bike, throws = true)
        }
        assertTrue(exception.errors.any { it.message!!.contains("NotNull") })
    }

    @Test
    fun `Regex should fail if the format does not match exactly`() {
        val bike = Motorcycle(owner = "Juan", licenseType = "A", customName = "Susy", modelCode = "GSX", plateNumber = "ABC1234", engineCc = 600)
        val exception = assertThrows<ValidationAggregatorException> {
            Centinela(factory).engine(bike, throws = true)
        }
        assertTrue(exception.errors.any { it.message!!.contains("Regex") })
    }

    @Test
    fun `Engine should do nothing if the object has no annotations`() {
        val plain = PlainObject(1, "No rules")
        assertDoesNotThrow {
            Centinela(factory).engine(plain, throws = true)
        }
    }

    @Test
    fun `ValidationResultHandler should capture errors without throwing exceptions`() {
        val tom = Musician(name = "Tom Araya", age = -5, band = "")
        val handler = TestValidationHandler()

        assertDoesNotThrow {
            Centinela(factory).engine(tom, throws = false, handler = handler)
        }

        assertEquals(2, handler.capturedErrors.size)
        assertTrue(handler.capturedErrors.any { it.message!!.contains("Positive") })
        assertTrue(handler.capturedErrors.any { it.message!!.contains("NotEmpty") })
    }

    @Test
    fun `Should aggregate errors from multiple properties into a single exception`() {
        val badMusician = Musician(name = "", age = 150, band = "")
        val exception = assertThrows<ValidationAggregatorException> {
            Centinela(factory).engine(badMusician, throws = true)
        }
        assertEquals(4, exception.errors.size)
    }

    @Test
    fun `Transformers like Trim should execute BEFORE validators like Size`() {
        val input = UserInput(searchString = "  ABC  ")
        val exception = assertThrows<ValidationAggregatorException> {
            Centinela(factory).engine(input, throws = true)
        }
        assertTrue(exception.errors.any { it.message!!.contains("Size") })
        assertEquals("ABC", input.searchString)
    }

    @Test
    fun `Capitalize should ignore empty or null strings without crashing`() {
        val transformer = CapitalizeTransformer()
        assertEquals("", transformer.transform(Capitalize(), ""))
        assertEquals(null, transformer.transform(Capitalize(), null))
    }

    @Test
    fun `Capitalize should correctly handle strings that are already uppercase`() {
        val transformer = CapitalizeTransformer()
        assertEquals("SLAYER", transformer.transform(Capitalize(), "SLAYER"))
        assertEquals("Slayer", transformer.transform(Capitalize(), "slayer"))
    }

    @Test
    fun `NotZero should handle Double and Float precision correctly`() {
        val validator = NotZeroValidator()
        assertTrue(validator.isValid(NotZero(), 0.0000001))
        assertTrue(!validator.isValid(NotZero(), 0.0))
    }

    @Test
    fun `Regex should work for validating complex structures like emails`() {
        val validator = RegexValidator()
        val emailRegex = Regex(regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
        assertTrue(validator.isValid(emailRegex, "user@mail.com"))
        assertTrue(!validator.isValid(emailRegex, "user@.com"))
    }

    @Test
    fun `Min and Max should perform automatic casting to support Ints even if the rule is Long`() {
        val validator = MinValidator()
        val minRule = Min(min = 600)
        assertTrue(validator.isValid(minRule, 600))
        assertTrue(!validator.isValid(minRule, 599))
    }

    @Test
    fun `Engine should be able to read and validate PRIVATE properties`() {
        val specs = EngineSpecs(compressionRatio = 0.0, cylinders = 4)
        val exception = assertThrows<ValidationAggregatorException> {
            Centinela(factory).engine(specs, throws = true)
        }
        assertTrue(exception.errors.any { it.message!!.contains("NotZero") })
    }
}
