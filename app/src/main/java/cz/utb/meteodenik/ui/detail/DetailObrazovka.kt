package cz.utb.meteodenik.ui.detail

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.utb.meteodenik.R
import cz.utb.meteodenik.model.Mesto
import cz.utb.meteodenik.ui.theme.MetoDenikTheme

// STAVOVÁ obálka – zná ViewModel. O navigaci neví nic, dostane jen lambdu onZpet.
@Composable
fun DetailObrazovka(
    onZpet: () -> Unit,
    modifier: Modifier = Modifier,
    vm: DetailViewModel = viewModel()
) {
    val mesto by vm.mesto.collectAsStateWithLifecycle()
    DetailObsah(mesto = mesto, onZpet = onZpet, modifier = modifier)
}

// BEZSTAVOVÝ obsah – dostane město (nebo null) a lambdu.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailObsah(
    mesto: Mesto?,
    onZpet: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(mesto?.nazev ?: stringResource(R.string.mesto_nezname)) })
        }
    ) { odsazeni ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(odsazeni)
                .padding(16.dp)
        ) {
            if (mesto == null) {
                Text(
                    text = stringResource(R.string.detail_nenalezeno),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Text(
                    text = mesto.nazev,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(8.dp))
                if (mesto.teplota != null) {
                    Text(
                        text = stringResource(R.string.teplota_format, mesto.teplota),
                        style = MaterialTheme.typography.displaySmall
                    )
                    Text(
                        text = stringResource(R.string.detail_souhrn, mesto.nazev, mesto.teplota),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    Text(
                        text = stringResource(R.string.mesto_teplota_neznama),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(onClick = onZpet) {
                Text(stringResource(R.string.detail_zpet))
            }
        }
    }
}

@Preview(name = "Detail – světlý", showSystemUi = true)
@Preview(name = "Detail – tmavý", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DetailObsahPreview() {
    MetoDenikTheme { DetailObsah(mesto = Mesto(1, "Zlín", 21.4), onZpet = {}) }
}

@Preview(name = "Detail – neexistuje", showSystemUi = true)
@Composable
private fun DetailObsahNenalezenoPreview() {
    MetoDenikTheme { DetailObsah(mesto = null, onZpet = {}) }
}

