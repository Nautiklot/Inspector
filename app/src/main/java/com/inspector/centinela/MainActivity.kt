package com.inspector.centinela

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.inspector.annotations.Capitalize
import com.inspector.annotations.Max
import com.inspector.annotations.Min
import com.inspector.annotations.NotBlank
import com.inspector.annotations.Positive
import com.inspector.annotations.Size
import com.inspector.annotations.Trim
import com.inspector.centinela.ui.theme.InspectorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InspectorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        val model = Moto(marca = "dsdsd", cilindrada = 1, misss= 1001)
        val errores = model.validate()
        model.validateOrThrow()

        errores.forEach { println(it) }


    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InspectorTheme {
        Greeting("Android")
    }
}

data class Moto(
    @NotBlank
    @Trim
    @Capitalize
    var marca: String,

    @Positive
    var cilindrada: Int,

    @Max(5000)
    @Min(1000)
    @Size(1,16)
    val misss: Int
    
)