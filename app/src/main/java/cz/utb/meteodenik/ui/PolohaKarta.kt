
package cz.utb.meteodenik.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings

import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

import cz.utb.meteodenik.R

private const val OPRAVNENI_POLOHA =
    Manifest.permission.ACCESS_COARSE_LOCATION

private const val KLIC_ODMITNUTI = "pocet_odmitnuti"


enum class StavOpravneni {
    NEZNAMY,
    ODMITNUTO,
    ODMITNUTO_NATRVALO,
    UDELENO
}


fun maOpravneniKPolohy(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        OPRAVNENI_POLOHA
    ) == PackageManager.PERMISSION_GRANTED
}



fun nactiPosledniPolohu(context: Context): Location? {

    val spravce = context.getSystemService(
        Context.LOCATION_SERVICE
    ) as? LocationManager ?: return null

    return try {
        if (maOpravneniKPolohy(context)) {
            spravce.getLastKnownLocation(
                LocationManager.NETWORK_PROVIDER
            )
        } else {
            null
        }
    } catch (e: SecurityException) {
        null
    }
}


@Composable
fun PolohaKarta() {

    val context = LocalContext.current
    val activity = LocalActivity.current


    val predvolby = remember(context) {
        context.getSharedPreferences(
            "poloha_opravneni",
            Context.MODE_PRIVATE
        )
    }

    fun zjistiStav(): StavOpravneni {

        if (maOpravneniKPolohy(context)) {
            return StavOpravneni.UDELENO
        }

        val pocet = predvolby.getInt(KLIC_ODMITNUTI, 0)

        val lzeVysvetlit = activity != null &&
                ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    OPRAVNENI_POLOHA
                )

        return when {
            pocet >= 2 && !lzeVysvetlit ->
                StavOpravneni.ODMITNUTO_NATRVALO

            pocet > 0 ->
                StavOpravneni.ODMITNUTO

            else ->
                StavOpravneni.NEZNAMY
        }
    }

    var stav by remember {
        mutableStateOf(zjistiStav())
    }

    var poloha by remember {
        mutableStateOf<Location?>(null)
    }

    var zobrazitVysvetleni by remember {
        mutableStateOf(false)
    }

    val spoustec = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { povoleno ->

        zobrazitVysvetleni = false

        if (povoleno) {

            predvolby.edit()
                .putInt(KLIC_ODMITNUTI, 0)
                .apply()

            stav = StavOpravneni.UDELENO
            poloha = nactiPosledniPolohu(context)

        } else {

            val pocet = (
                    predvolby.getInt(KLIC_ODMITNUTI, 0) + 1
                    ).coerceAtMost(2)

            predvolby.edit()
                .putInt(KLIC_ODMITNUTI, pocet)
                .apply()

            val lzeVysvetlit = activity != null &&
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        OPRAVNENI_POLOHA
                    )

            stav = if (pocet >= 2 && !lzeVysvetlit) {
                StavOpravneni.ODMITNUTO_NATRVALO
            } else {
                StavOpravneni.ODMITNUTO
            }

            poloha = null
        }
    }

    val lifecycle = (activity as? LifecycleOwner)?.lifecycle

    DisposableEffect(lifecycle, context) {

        val pozorovatel = LifecycleEventObserver { _, udalost ->

            if (udalost == Lifecycle.Event.ON_RESUME) {

                stav = zjistiStav()

                if (stav == StavOpravneni.UDELENO) {

                    predvolby.edit()
                        .putInt(KLIC_ODMITNUTI, 0)
                        .apply()

                    poloha = nactiPosledniPolohu(context)

                } else {
                    poloha = null
                }
            }
        }

        lifecycle?.addObserver(pozorovatel)

        onDispose {
            lifecycle?.removeObserver(pozorovatel)
        }
    }


    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {

        when (stav) {

            StavOpravneni.NEZNAMY -> {

                Button(
                    onClick = {
                        spoustec.launch(OPRAVNENI_POLOHA)
                    }
                ) {
                    Text(stringResource(R.string.poloha_zjistit))
                }
            }


            StavOpravneni.ODMITNUTO -> {

                if (zobrazitVysvetleni) {

                    Text(
                        stringResource(R.string.poloha_vysvetleni)
                    )

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = {
                            zobrazitVysvetleni = false
                            spoustec.launch(OPRAVNENI_POLOHA)
                        }
                    ) {
                        Text(
                            stringResource(R.string.poloha_pokracovat)
                        )
                    }

                } else {

                    Button(
                        onClick = {
                            zobrazitVysvetleni = true
                        }
                    ) {
                        Text(
                            stringResource(R.string.poloha_zjistit)
                        )
                    }
                }
            }


            StavOpravneni.ODMITNUTO_NATRVALO -> {

                Text(
                    stringResource(R.string.poloha_bez_opravneni)
                )

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts(
                                "package",
                                context.packageName,
                                null
                            )
                        )

                        context.startActivity(intent)
                    }
                ) {
                    Text(
                        stringResource(R.string.otevrit_nastaveni)
                    )
                }
            }

            StavOpravneni.UDELENO -> {

                Text(
                    stringResource(R.string.poloha_povolena)
                )

                val aktualniPoloha = poloha

                if (aktualniPoloha != null) {

                    Text(
                        stringResource(
                            R.string.poloha_souradnice,
                            aktualniPoloha.latitude,
                            aktualniPoloha.longitude
                        )
                    )

                } else {

                    Text(
                        stringResource(R.string.poloha_nedostupna)
                    )
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        poloha = nactiPosledniPolohu(context)
                    }
                ) {
                    Text(
                        stringResource(R.string.poloha_obnovit)
                    )
                }
            }
        }
    }
}
