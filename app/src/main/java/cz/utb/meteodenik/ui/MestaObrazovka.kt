package cz.utb.meteodenik.ui

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.utb.meteodenik.DetailMestaActivity
import cz.utb.meteodenik.R
import cz.utb.meteodenik.model.Mesto
import cz.utb.meteodenik.model.UKAZKOVA_MESTA
import cz.utb.meteodenik.ui.theme.MetoDenikTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel


// STAVOVÁ funkce
@Composable
fun MestaObrazovka(
    modifier: Modifier = Modifier,
    vm: MestaViewModel = viewModel()          // NIKDY MestaViewModel() – viz níže
) {
    val context = LocalContext.current
    val stav by vm.stav.collectAsStateWithLifecycle()

    // Rozepsaný text je stav ovládacího prvku, ne data obrazovky: rememberSaveable ho uloží
    // do Bundle a přežije otočení i smrt procesu, kdežto ViewModel by smrt procesu nepřežil.
    var novyNazev by rememberSaveable { mutableStateOf("") }

    MestaObsah(
        mesta = stav.mesta,
        novyNazev = novyNazev,
        jeDuplikat = stav.jeDuplikat,
        onNovyNazevChange = {
            novyNazev = it
            vm.zrusChybu()
        },
        onPridatMesto = {
            if (vm.pridej(novyNazev)) novyNazev = ""
        },
        onSmazatMesto = vm::smaz,
        onOtevritDetail = { mesto -> otevriDetail(context, mesto) },
        modifier = modifier,
        horniObsah = { PolohaKarta() }
    )
}
private fun otevriDetail(context: Context, mesto: Mesto) {
    val intent = Intent(context, DetailMestaActivity::class.java).apply {
        putExtra(DetailMestaActivity.EXTRA_MESTO, mesto.nazev)
        mesto.teplota?.let { putExtra(DetailMestaActivity.EXTRA_TEPLOTA, it) }
    }
    context.startActivity(intent)
}


// BEZSTAVOVÉ funkce – data parametrem dolů, události lambdou nahoru

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MestaObsah(
    mesta: List<Mesto>,
    novyNazev: String,
    jeDuplikat: Boolean,
    onNovyNazevChange: (String) -> Unit,
    onPridatMesto: () -> Unit,
    onSmazatMesto: (Int) -> Unit,
    onOtevritDetail: (Mesto) -> Unit,
    modifier: Modifier = Modifier,
    horniObsah: @Composable () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(
                            R.string.seznam_titulek,
                            pluralStringResource(R.plurals.pocet_zaznamu, mesta.size, mesta.size)
                        )
                    )
                }
            )
        }
    ) { odsazeni ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(odsazeni)
                .padding(horizontal = 16.dp)
        ) {
            horniObsah()

            PridatMestoRadek(
                nazev = novyNazev,
                jeChyba = jeDuplikat,
                onNazevChange = onNovyNazevChange,
                onPridat = onPridatMesto
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = pluralStringResource(R.plurals.mesta_pocet, mesta.size, mesta.size),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            MestaSeznam(
                mesta = mesta,
                onSmazatMesto = onSmazatMesto,
                onOtevritDetail = onOtevritDetail,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun PridatMestoRadek(
    nazev: String,
    jeChyba: Boolean,
    onNazevChange: (String) -> Unit,
    onPridat: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        OutlinedTextField(
            value = nazev,
            onValueChange = onNazevChange,
            label = { Text(stringResource(R.string.mesta_novy_nazev)) },
            singleLine = true,
            isError = jeChyba,
            supportingText = if (jeChyba) {
                { Text(stringResource(R.string.mesto_duplikat)) }
            } else {
                null
            },
            modifier = Modifier.weight(1f)
        )

        Spacer(Modifier.width(8.dp))

        Button(
            onClick = onPridat,
            enabled = nazev.isNotBlank(),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(stringResource(R.string.mesta_pridat))
        }
    }
}

@Composable
fun MestaSeznam(
    mesta: List<Mesto>,
    onSmazatMesto: (Int) -> Unit,
    onOtevritDetail: (Mesto) -> Unit,
    modifier: Modifier = Modifier
) {
    if (mesta.isEmpty()) {
        Text(
            text = stringResource(R.string.mesta_prazdny),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier.padding(vertical = 16.dp)
        )
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(mesta, key = { it.id }) { mesto ->
                MestoKarta(
                    mesto = mesto,
                    onSmazat = { onSmazatMesto(mesto.id) },   // id doplní SEZNAM, ne karta
                    onClick = { onOtevritDetail(mesto) }
                )
            }
        }
    }
}

@Composable
fun MestoKarta(
    mesto: Mesto,
    onSmazat: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val smazatPopis = stringResource(R.string.mesto_smazat_popis, mesto.nazev)
    val teplotaText = if (mesto.teplota != null) {
        stringResource(R.string.teplota_format, mesto.teplota)
    } else {
        stringResource(R.string.mesto_teplota_neznama)
    }

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mesto.nazev,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = teplotaText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            TextButton(
                onClick = onSmazat,
                modifier = Modifier.semantics { contentDescription = smazatPopis }
            ) {
                Text(stringResource(R.string.mesto_smazat))
            }
        }
    }
}


// NÁHLEDY

private val NAHLED_MESTA = UKAZKOVA_MESTA + Mesto(4, "Praha", null)

@Composable
private fun NahledObsahu(
    mesta: List<Mesto>,
    novyNazev: String = "",
    jeDuplikat: Boolean = false
) {
    MetoDenikTheme {
        MestaObsah(
            mesta = mesta,
            novyNazev = novyNazev,
            jeDuplikat = jeDuplikat,
            onNovyNazevChange = {},
            onPridatMesto = {},
            onSmazatMesto = {},
            onOtevritDetail = {}
        )
    }
}

@Preview(name = "Světlý", showSystemUi = true)
@Preview(name = "Tmavý", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MestaObsahPreview() = NahledObsahu(NAHLED_MESTA)

@Preview(name = "Prázdný seznam", showSystemUi = true)
@Composable
private fun MestaObsahPrazdnyPreview() = NahledObsahu(emptyList())

@Preview(name = "Chyba – duplikát", showSystemUi = true)
@Composable
private fun MestaObsahChybaPreview() = NahledObsahu(NAHLED_MESTA, novyNazev = "zlín", jeDuplikat = true)