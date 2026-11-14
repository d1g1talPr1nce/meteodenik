package cz.utb.meteodenik.navigace

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.utb.meteodenik.ui.MestaObrazovka
import cz.utb.meteodenik.ui.detail.DetailObrazovka

@Composable
fun MeteoDenikNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Seznam,
        modifier = modifier
    ) {
        composable<Seznam> { zaznam ->
            MestaObrazovka(
                onMesto = { id ->
                    if (zaznam.jeAktivni()) {
                        navController.navigate(Detail(id)) { launchSingleTop = true }
                    }
                }
            )
        }

        composable<Detail> { zaznam ->
            DetailObrazovka(
                onZpet = {
                    if (zaznam.jeAktivni()) navController.popBackStack()
                }
            )
        }
    }
}

/** Obrazovka je plně na popředí – ne uprostřed přechodu na jinou. */
private fun NavBackStackEntry.jeAktivni(): Boolean =
    lifecycle.currentState == Lifecycle.State.RESUMED