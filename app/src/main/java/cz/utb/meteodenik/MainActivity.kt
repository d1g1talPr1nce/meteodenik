package cz.utb.meteodenik

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cz.utb.meteodenik.ui.MestaObrazovka
import cz.utb.meteodenik.ui.theme.MetoDenikTheme

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
                MestaObrazovka()
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