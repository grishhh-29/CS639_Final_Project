package com.example.finalproject.dietplan

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.finalproject.R

class HeightScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeightHeightEntryScreen()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val context = this
        navigateToSexSelection(context)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WeightHeightEntryScreen() {
    var weight by remember { mutableStateOf(TextFieldValue()) }
    var isKgSelected by remember { mutableStateOf(true) }
    var isCmSelected by remember { mutableStateOf(true) }
    var heightCm by remember { mutableStateOf(TextFieldValue()) }
    val context = LocalContext.current
    var isNextButtonEnabled by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.weight),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.TopStart
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Enter Weight and Height") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = weight,
                        onValueChange = { newValue ->
                            if (newValue.text.isEmpty() || newValue.text.toDoubleOrNull() != null) {
                                if (newValue.text.indexOf('.') != 0) { // Dot should not be at first place
                                    weight = newValue
                                }
                            }
                            isNextButtonEnabled = newValue.text.isNotEmpty() && heightCm.text.isNotEmpty()
                        },
                        label = { Text("Weight") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(
                            onClick = { isKgSelected = true },
                            modifier = androidx.compose.ui.Modifier.padding(end = 8.dp),
                            enabled = !isKgSelected
                        ) {
                            Text(text = "KG")
                        }

                        TextButton(
                            onClick = { isKgSelected = false },
                            modifier = androidx.compose.ui.Modifier.padding(start = 8.dp),
                            enabled = isKgSelected
                        ) {
                            Text(text = "LB")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = heightCm,
                        onValueChange = { newValue ->
                            if (newValue.text.isEmpty() || newValue.text.toDoubleOrNull() != null) {
                                if (newValue.text.indexOf('.') != 0) { // Dot should not be at first place
                                    heightCm = newValue
                                }
                            }
                            isNextButtonEnabled = newValue.text.isNotEmpty() && heightCm.text.isNotEmpty()
                        },
                        label = { Text("Height (cm)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(
                            onClick = { isCmSelected = true },
                            modifier = Modifier.padding(end = 8.dp),
                            enabled = !isKgSelected
                        ) {
                            Text(text = "CM")
                        }

                        TextButton(
                            onClick = { isCmSelected = false },
                            modifier = Modifier.padding(start = 8.dp),
                            enabled = isCmSelected
                        ) {
                            Text(text = "Feet")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    FilledTonalButton(
                        onClick = {
                            val weightValue = weight.text.toDoubleOrNull() ?: 0.0
                            val heightCmValue = heightCm.text.toDoubleOrNull() ?: 0.0
                            val heightInches = heightCmValue * 0.393701
                            val heightFeet = heightInches / 12
                            val heightFeetValue = heightFeet.toInt().toDouble()
                            val weightVal = if (isKgSelected) weightValue else weightValue * 0.453592
                            val height = if (isCmSelected) heightCmValue else heightFeetValue
                            navigateToDietScreen(context, weightVal, height)
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        enabled = isNextButtonEnabled
                    ) {
                        Text(text = "Next")
                    }
                }
            }
        )
    }
}

private fun navigateToSexSelection(context: Context) {
    val intent = Intent(context, SexSelectionActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
}
private fun navigateToDietScreen(context: Context, weightVal: Double, height: Double) {
    val intent = Intent(context, DietActivity::class.java).apply {
        putExtra("WEIGHT", weightVal)
        putExtra("HEIGHT", height)
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}