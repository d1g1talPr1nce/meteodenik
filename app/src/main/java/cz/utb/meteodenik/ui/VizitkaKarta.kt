package cz.utb.meteodenik.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.utb.meteodenik.R
import cz.utb.meteodenik.ui.theme.MetoDenikTheme

@Composable
fun VizitkaKarta(
    jmeno: String,
    obor: String,
    email: String,
    modifier: Modifier = Modifier
) {
    val avatarPopis = stringResource(R.string.vizitka_avatar_popis, jmeno)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar: nejdřív ořez do kruhu, pak teprve barva pozadí
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .semantics { contentDescription = avatarPopis },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = inicialy(jmeno),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(Modifier.width(16.dp))

            // weight(1f): sloupec dostane jen zbytek místa, takže text nevyleze z karty
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = jmeno,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = obor,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(R.string.vizitka_email_popis, email),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private fun inicialy(jmeno: String): String =t.xml b/app/src/main/AndroidManifest.xml
        index b65c206..f6175f9 100644
--- a/app/src/main/AndroidManifest.xml
+++ b/app/src/main/AndroidManifest.xml
@@ -6,6 +6,7 @@
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-feature android:name="android.hardware.location.gps" android:required="false" />

+
<application
android:allowBackup="true"
android:dataExtractionRules="@xml/data_extraction_rules"
~
~
~
~
    jmeno.trim()
        .split(Regex("\\s+"))
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

@Preview(name = "Světlý")
@Preview(name = "Tmavý", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Velké písmo", fontScale = 1.5f)
@Composable
private fun VizitkaKartaPreview() {
    MetoDenikTheme {
        Surface {
            VizitkaKarta(
                jmeno = "Jan Novák",
                obor = "Informační technologie, 2. ročník",
                email = "j_novak@utb.cz",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}