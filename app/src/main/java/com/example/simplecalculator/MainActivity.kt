package com.example.simplecalculator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplecalculator.ui.theme.SimpleCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleCalculatorTheme {
                CombinedCalculatorScreen()
            }
        }
    }
}

@Composable
fun CombinedCalculatorScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CalculatorUI()
        Spacer(modifier = Modifier.height(40.dp))
        TemperatureConverterUI()
    }
}


@Composable
fun CalculatorUI() {
    var num1 by remember { mutableStateOf("0") }
    var num2 by remember { mutableStateOf("0") }
    var result by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Text("Calculator", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = num1,
            onValueChange = { num1 = it },
            label = { Text("Number 1") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = num2,
            onValueChange = { num2 = it },
            label = { Text("Number 2") }
        )

        Row(modifier = Modifier.padding(top = 20.dp)) {
            Button(onClick = {
                val n1 = num1.toDoubleOrNull()
                val n2 = num2.toDoubleOrNull()
                result = if (n1 != null && n2 != null) "Result is ${formatResult(n1 + n2)}" else "Invalid input"
            }) {
                Text("+", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))

            Button(onClick = {
                val n1 = num1.toDoubleOrNull()
                val n2 = num2.toDoubleOrNull()
                result = if (n1 != null && n2 != null) "Result is ${formatResult(n1 - n2)}" else "Invalid input"
            }) {
                Text("–", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))

            Button(onClick = {
                val n1 = num1.toDoubleOrNull()
                val n2 = num2.toDoubleOrNull()
                result = if (n1 != null && n2 != null) "Result is ${formatResult(n1 * n2)}" else "Invalid input"
            }) {
                Text("×", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))

            Button(onClick = {
                val n1 = num1.toDoubleOrNull()
                val n2 = num2.toDoubleOrNull()
                result = when {
                    n1 == null || n2 == null -> "Invalid input"
                    n2 == 0.0 -> "Can't divide by 0"
                    else -> "Result is ${formatResult(n1 / n2)}"
                }
            }) {
                Text("÷", fontSize = 24.sp)
            }
        }

        result?.let {
            Text(
                text = it,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}

fun formatResult(value: Double): String {
    return if (value % 1 == 0.0) {
        value.toLong().toString()  // show as integer if no decimals
    } else {
        "%.8f".format(value).trimEnd('0').trimEnd('.')  // trim trailing zeros
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorUIPreview() {
    SimpleCalculatorTheme {
        CalculatorUI()
    }
}


fun convertTemperature(value: Double, from: String, to: String): Double {
    val celsius = when (from) {
        "Celsius" -> value
        "Fahrenheit" -> (value - 32) * 5 / 9
        "Kelvin" -> value - 273.15
        else -> value
    }

    return when (to) {
        "Celsius" -> celsius
        "Fahrenheit" -> (celsius * 9 / 5) + 32
        "Kelvin" -> celsius + 273.15
        else -> celsius
    }
}

@Composable
fun TemperatureConverterUI() {
    var inputValue by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf("Celsius") }
    var toUnit by remember { mutableStateOf("Fahrenheit") }
    var result by remember { mutableStateOf<String?>(null) }
    var convertedUnit by remember { mutableStateOf<String?>(null) }

//    val units = listOf("Celsius", "Fahrenheit", "Kelvin")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Temperature Converter", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            label = { Text("Enter temperature") },
            singleLine = true
        )

        Row(modifier = Modifier.padding(top = 16.dp)) {
            UnitDropdown(selectedUnit = fromUnit, onUnitSelected = { fromUnit = it }, label = "From")
            Spacer(modifier = Modifier.width(28.dp))
            UnitDropdown(selectedUnit = toUnit, onUnitSelected = { toUnit = it }, label = "To")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            val value = inputValue.toDoubleOrNull()
            if (value != null) {
                val converted = convertTemperature(value, fromUnit, toUnit)
                result = "%.2f".format(converted)
                convertedUnit = toUnit // store the unit at the time of conversion
            } else {
                result = "Invalid input"
                convertedUnit = null
            }
        }) {
            Text("Convert", fontSize = 20.sp)
        }

        result?.let { res ->
            val unitSuffix = when (convertedUnit) {
                "Celsius" -> "°C"
                "Fahrenheit" -> "°F"
                "Kelvin" -> "K"
                else -> ""
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Result: $res $unitSuffix", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun UnitDropdown(selectedUnit: String, onUnitSelected: (String) -> Unit, label: String) {
    var expanded by remember { mutableStateOf(false) }
    val units = listOf("Celsius", "Fahrenheit", "Kelvin")

    Column {
        Text(label)
        Box {
            Button(
                onClick = { expanded = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text(selectedUnit)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                units.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(unit) },
                        onClick = {
                            onUnitSelected(unit)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TemperatureConverterPreview() {
    SimpleCalculatorTheme {
        TemperatureConverterUI()
    }
}

// for development purpose
@Preview(showBackground = true)
@Composable
fun CombinedCalculatorScreenPreview() {
    SimpleCalculatorTheme {
        CombinedCalculatorScreen()
    }
}
