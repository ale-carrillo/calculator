package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.ui.theme.CalculatorTheme
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
data class Campo(
    var valor: String = "",
    var error: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val formulas = listOf("Formula General", "Pitágoras", "Área de un cilindro", "Ley de Gravitación")
    var expanded by remember { mutableStateOf(false) }
    var selectedFormula by remember { mutableStateOf(formulas[0]) }

    var campoA by remember { mutableStateOf(Campo()) }
    var campoB by remember { mutableStateOf(Campo()) }
    var campoC by remember { mutableStateOf(Campo()) }
    var result by remember { mutableStateOf("") }
    var mostrarResultado by remember { mutableStateOf(false) }


    fun validarCampo(campo: Campo): Boolean {
        return campo.valor.isNotEmpty() && campo.valor.toFloatOrNull() != null
    }

    fun obtenerImagenSegunFormula(formula: String): Int {
        return when (formula) {
            "Formula General" -> R.drawable.formulageneral2
            "Pitágoras" -> R.drawable.pitagoras
            "Área de un cilindro" -> R.drawable.cilindro
            "Ley de Gravitación" -> R.drawable.universal
            else -> R.drawable.backg
        }
    }

    fun calcular() {
        campoA = campoA.copy(error = false)
        campoB = campoB.copy(error = false)
        campoC = campoC.copy(error = false)

        when (selectedFormula) {
            "Formula General" -> {
                val validoA = validarCampo(campoA)
                val validoB = validarCampo(campoB)
                val validoC = validarCampo(campoC)

                campoA = campoA.copy(error = !validoA)
                campoB = campoB.copy(error = !validoB)
                campoC = campoC.copy(error = !validoC)

                if (validoA && validoB && validoC) {
                    val a = campoA.valor.toFloat()
                    val b = campoB.valor.toFloat()
                    val c = campoC.valor.toFloat()
                    val d = b * b - 4 * a * c
                    result = if (d < 0) {
                        "No hay solución real"
                    } else {
                        val x1 = (-b + sqrt(d)) / (2 * a)
                        val x2 = (-b - sqrt(d)) / (2 * a)
                        "x1 = $x1, x2 = $x2"
                    }
                    mostrarResultado = true
                } else {
                    mostrarResultado = false
                }
            }

            "Pitágoras" -> {
                val validoA = validarCampo(campoA)
                val validoB = validarCampo(campoB)
                campoA = campoA.copy(error = !validoA)
                campoB = campoB.copy(error = !validoB)

                if (validoA && validoB) {
                    val a = campoA.valor.toFloat()
                    val b = campoB.valor.toFloat()
                    result = "c = ${sqrt(a * a + b * b)}"
                    mostrarResultado = true
                } else {
                    mostrarResultado = false
                }
            }

            "Área de un cilindro" -> {
                val validoR = validarCampo(campoA)
                val validoH = validarCampo(campoB)
                campoA = campoA.copy(error = !validoR)
                campoB = campoB.copy(error = !validoH)

                if (validoR && validoH) {
                    val r = campoA.valor.toFloat()
                    val h = campoB.valor.toFloat()
                    result = "Área = ${2 * 3.1416 * r * (r + h)}"
                    mostrarResultado = true
                } else {
                    mostrarResultado = false
                }
            }

            "Ley de Gravitación" -> {
                val validoM1 = validarCampo(campoA)
                val validoM2 = validarCampo(campoB)
                val validoD = validarCampo(campoC)

                campoA = campoA.copy(error = !validoM1)
                campoB = campoB.copy(error = !validoM2)
                campoC = campoC.copy(error = !validoD)

                if (validoM1 && validoM2 && validoD) {
                    val G = 6.67430e-11
                    val m1 = campoA.valor.toFloat()
                    val m2 = campoB.valor.toFloat()
                    val d = campoC.valor.toFloat()
                    result = "F = ${G * m1 * m2 / (d * d)} N"
                    mostrarResultado = true
                } else {
                    mostrarResultado = false
                }
            }
        }
    }


    Box(
        modifier = modifier
        .fillMaxSize()
    ){
        Image(
            painter = painterResource(R.drawable.backg),
            contentDescription = "Imagen de fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Calculadora",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(30.dp))
                Image(
                    painter = painterResource(id = R.drawable.simbolos),
                    contentDescription = "Logo calculadora",
                    modifier = Modifier
                        .size(70.dp)
                        .padding(end = 8.dp)
                )
            }

            Text(
                "Selecciona una fórmula:",
                fontSize = 20.sp,
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                OutlinedTextField(
                    value = selectedFormula,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Selecciona una fórmula") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    formulas.forEach { formula ->
                        DropdownMenuItem(
                            text = { Text(formula) },
                            onClick = {
                                selectedFormula = formula
                                campoA = Campo()
                                campoB = Campo()
                                campoC = Campo()
                                result = ""
                                expanded = false
                            }
                        )
                    }
                }
            }

            Image(
                painter = painterResource(id = obtenerImagenSegunFormula(selectedFormula)),
                contentDescription = "Imagen de la fórmula",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 10.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            InputField(
                label = when (selectedFormula) {
                    "Área de un cilindro" -> "Radio"
                    "Ley de Gravitación" -> "Masa 1"
                    else -> "Valor A"
                },
                campo = campoA,
                onChange = { campoA = campoA.copy(valor = it) },
                permitirNegativos = selectedFormula == "Formula General"
            )

            if (selectedFormula in listOf("Formula General", "Pitágoras", "Área de un cilindro", "Ley de Gravitación")) {
                InputField(
                    label = when (selectedFormula) {
                        "Área de un cilindro" -> "Altura"
                        "Ley de Gravitación" -> "Masa 2"
                        else -> "Valor B"
                    },
                    campo = campoB,
                    onChange = { campoB = campoB.copy(valor = it) },
                    permitirNegativos = selectedFormula == "Formula General"
                )
            }

            if (selectedFormula in listOf("Formula General", "Ley de Gravitación")) {
                InputField(
                    label = when (selectedFormula) {
                        "Ley de Gravitación" -> "Distancia"
                        else -> "Valor C"
                    },
                    campo = campoC,
                    onChange = { campoC = campoC.copy(valor = it) },
                    permitirNegativos = selectedFormula == "Formula General"
                )
            }

            if (mostrarResultado) {
                Text(
                    "$result",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(top = 16.dp)
                )
            }

            Button(
                onClick = { calcular() },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text("Calcular")
            }

            Text(
                "by Alejandra Carrillo",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Blue,
                modifier = Modifier
                    .padding(top = 16.dp)
            )

        }
    }
}

@Composable
fun InputField(
    label: String,
    campo: Campo,
    onChange: (String) -> Unit,
    permitirNegativos: Boolean
) {
    OutlinedTextField(
        value = campo.valor,
        onValueChange = {
            val nuevoValor = it
            val esValido = if (permitirNegativos) {
                nuevoValor.matches(Regex("-?\\d*\\.?\\d*"))
            } else {
                nuevoValor.matches(Regex("\\d*\\.?\\d*"))
            }
            if (esValido) {
                onChange(nuevoValor)
            }
        },
        label = { Text(label) },
        isError = campo.error,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        supportingText = {
            if (campo.error) {
                Text("Ingresa un número válido", color = MaterialTheme.colorScheme.error)
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            errorContainerColor = Color.White
        )
    )
}

