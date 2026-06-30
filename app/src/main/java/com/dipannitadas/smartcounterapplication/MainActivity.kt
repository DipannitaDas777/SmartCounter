package com.dipannitadas.smartcounterapplication

import androidx.compose.material3.*
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            val context = LocalContext.current

            val prefs = context.getSharedPreferences(
                "SmartCounter",
                MODE_PRIVATE
            )

            val darkMode = prefs.getBoolean("darkMode", false)

            MaterialTheme(

                colorScheme =
                    if (darkMode)
                        darkColorScheme()
                    else
                        lightColorScheme()

            ) {

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {

                    CounterScreen()

                }

            }

        }
    }
}
@Composable
fun CounterScreen() {

    val context = LocalContext.current

    val prefs = context.getSharedPreferences(
        "SmartCounter",
        android.content.Context.MODE_PRIVATE
    )
    var darkMode by remember {
        mutableStateOf(prefs.getBoolean("darkMode", false))
    }

    var counter by remember {
        mutableStateOf(prefs.getInt("counter", 0))
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    var history by remember {
        mutableStateOf(listOf<String>())
    }

    fun addHistory(operation: String) {
        history = (listOf(operation) + history).take(10)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Smart Counter",
            fontSize = 32.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = counter.toString(),
            fontSize = 70.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Button(
                onClick = {
                    counter++
                    addHistory("+1")
                }
            ) {
                Text("+")
            }

            Button(
                onClick = {

                    if(counter>0){

                        counter--
                        addHistory("-1")

                    }else{

                        Toast.makeText(
                            context,
                            "Counter cannot be negative",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }
            ) {
                Text("-")
            }

        }
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Button(
                onClick = {

                    showDialog = true

                }
            ) {

                Text("Reset")

            }

            Button(
                onClick = {

                    prefs.edit()
                        .putInt("counter", counter)
                        .apply()
                    addHistory("Saved")
                    Toast.makeText(
                        context,
                        "Saved",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            ) {

                Text("Save")

            }

        }
        Spacer(modifier = Modifier.height(30.dp))
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                "Dark Mode",
                fontSize = 18.sp
            )

            Switch(
                checked = darkMode,
                onCheckedChange = {

                    darkMode = it

                    prefs.edit()
                        .putBoolean("darkMode", it)
                        .apply()

                    (context as? ComponentActivity)?.recreate()

                }
            )

        }
        Text(
            text = "Recent Activity",
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            items(history) { item ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {

                    Text(
                        text = item,
                        modifier = Modifier.padding(16.dp)
                    )

                }

            }

        }

    }
    if(showDialog){

        AlertDialog(

            onDismissRequest = {

                showDialog = false

            },

            title = {

                Text("Reset Counter")

            },

            text = {

                Text("Reset counter to zero?")

            },

            confirmButton = {

                TextButton(

                    onClick = {

                        counter = 0
                        addHistory("Reset")

                        showDialog = false

                    }

                ) {

                    Text("Yes")

                }

            },

            dismissButton = {

                TextButton(

                    onClick = {

                        showDialog = false

                    }

                ) {

                    Text("No")

                }

            }

        )

    }
}