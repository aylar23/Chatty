package com.aylar.chatty.ui.screens.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aylar.chatty.R
import com.aylar.chatty.ui.components.CountryPhoneCode
import com.aylar.chatty.ui.components.countryPhoneCodes

@Composable
fun CountrySelectionDialog(
    countries: List<CountryPhoneCode>,
    onCountrySelected: (CountryPhoneCode) -> Unit
) {

    Surface(shape = MaterialTheme.shapes.medium) {
        LazyColumn(contentPadding = PaddingValues(vertical = 10.dp)) {

            item {
                Text(
                    modifier = Modifier.padding(20.dp, 10.dp),
                    text = stringResource(R.string.select_country),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            items(countries) { country ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCountrySelected(country) }
                        .padding(vertical = 8.dp, horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = country.flagEmoji,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(modifier = Modifier.weight(1f),
                        text = country.countryName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(text = country.code)
                }
            }
        }
    }
}