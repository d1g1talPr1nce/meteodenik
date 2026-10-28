package cz.utb.meteodenik

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.utb.meteodenik.ui.theme.MetoDenikTheme
import android.util.Log
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.platform.LocalContext
import cz.utb.meteodenik.model.UKAZKOVA_MESTA
import cz.utb.meteodenik.ui.PolohaKarta
import androidx.compose.ui.res.pluralStringResource

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(
            TAG,
            "MainActivity.onCreate (savedInstanceState == null: ${savedInstanceState == null})"
        )

        enableEdgeToEdge()

        setContent {
            MetoDenikTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MeteoObrazovka(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "MainActivity.onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "MainActivity.onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "MainActivity.onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "MainActivity.onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "MainActivity.onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(
            TAG,
            "MainActivity.onDestroy (isFinishing: $isFinishing)"
        )
    }

    companion object {
        private const val TAG = "Cyklus"
    }
}

@Composable
fun MeteoObrazovka(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        val pocetMest = UKAZKOVA_MESTA.size

        Text(
            text = stringResource(
                R.string.seznam_titulek,
                pluralStringResource(
                    R.plurals.pocet_zaznamu,
                    pocetMest,
                    pocetMest
                )
            ),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        PolohaKarta()
        UKAZKOVA_MESTA.forEach { mesto ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val intent = Intent(
                            context,
                            DetailMestaActivity::class.java
                        ).apply {
                            putExtra(
                                DetailMestaActivity.EXTRA_MESTO,
                                mesto.nazev
                            )
                            putExtra(
                                DetailMestaActivity.EXTRA_TEPLOTA,
                                mesto.teplota
                            )
                        }

                        context.startActivity(intent)
                    }
                    .padding(16.dp)
            ) {
                Text(
                    text = mesto.nazev,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = stringResource(
                        R.string.teplota_format,
                        mesto.teplota
                    )
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MeteoObrazovkaPreview() {
    MetoDenikTheme {
        MeteoObrazovka()
    }
}