package com.example.simplecalculator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
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
                CalculatorUI()
            }
        }
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
