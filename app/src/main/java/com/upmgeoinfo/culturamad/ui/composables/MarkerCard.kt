package com.upmgeoinfo.culturamad.ui.composables

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.datamodel.CulturalEventMadrid

/**
 * Creates a card with a cultural event information for display as marker info.
 */
@Composable
fun MarkerCard(
    culturalEvent: CulturalEventMadrid,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    Surface(
        color = MaterialTheme.colors.surface,
        elevation = 5.dp,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(8.dp)
    ){
        Column(){
            Card(//Title
                backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
                shape = androidx.compose.material3.MaterialTheme.shapes.extraSmall,
                elevation = 2.dp,
                modifier = Modifier
                    .padding(top = 4.dp, end = 4.dp, start = 4.dp, bottom = 2.dp)
                    .fillMaxWidth()
            ){
                Text(//event Title
                    text = culturalEvent.title,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    style = androidx.compose.material3.MaterialTheme.typography.titleSmall,
                    //fontWeight = FontWeight(600),
                    modifier = Modifier
                        .padding(4.dp)
                )
            }
            if(culturalEvent.description != ""){
                Card(//Description
                    backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                    shape = androidx.compose.material3.MaterialTheme.shapes.extraSmall,
                    elevation = 1.dp,
                    modifier = Modifier
                        .padding(top = 1.dp, end = 4.dp, start = 4.dp, bottom = 2.dp)
                        .fillMaxWidth()
                ) {
                    Text(//event Description.
                        text = culturalEvent.description,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                        //fontWeight = FontWeight(200),
                        maxLines = 5,
                        modifier = Modifier
                            .padding(4.dp)
                    )
                }
            }
            if(culturalEvent.address != null){
                Row() {
                    Card(//Location and Address
                        backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.outlineVariant,
                        shape = androidx.compose.material3.MaterialTheme.shapes.extraSmall,
                        elevation = 0.dp,
                        modifier = Modifier
                            .padding(top = 1.dp, end = 4.dp, start = 4.dp, bottom = 2.dp)
                            .fillMaxWidth()
                    ) {
                        Column() {
                            Text(//Location
                                text = culturalEvent.eventLocation,
                                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                                style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                                //fontWeight = FontWeight(200),
                                modifier = Modifier
                                    .padding(4.dp)
                            )
                            Text(//Address
                                text = culturalEvent.address.area.streetAddress,
                                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                                style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                                //fontWeight = FontWeight(200),
                                modifier = Modifier
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }
            Row() {
                Card(//start Date
                    backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer,
                    shape = androidx.compose.material3.MaterialTheme.shapes.extraSmall,
                    elevation = 0.dp,
                    modifier = Modifier
                        .padding(top = 1.dp, end = 1.dp, start = 4.dp, bottom = 2.dp)
                    //.fillMaxWidth()
                ){
                    Row(){
                        Text(//text Date.
                            text = stringResource(id = R.string.event_start_date),
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
                            style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                            //fontWeight = FontWeight(200),
                            modifier = Modifier
                                .padding(top = 4.dp, bottom = 4.dp, start = 4.dp, end = 1.dp)
                        )
                        Text(//value Date.
                            text = culturalEvent.dtstart.substringBefore(' '),
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
                            style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                            //fontWeight = FontWeight(200),
                            modifier = Modifier
                                .padding(top = 4.dp, bottom = 4.dp, start = 0.dp, end = 4.dp)
                        )
                    }
                }
                Card(//End Date
                    backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer,
                    shape = androidx.compose.material3.MaterialTheme.shapes.extraSmall,
                    elevation = 0.dp,
                    modifier = Modifier
                        .padding(top = 1.dp, end = 2.dp, start = 1.dp, bottom = 2.dp)
                    //.fillMaxWidth()
                ){
                    Row(){
                        Text(//text Date.
                            text = stringResource(id = R.string.event_end_date),
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
                            style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                            //fontWeight = FontWeight(200),
                            modifier = Modifier
                                .padding(top = 4.dp, bottom = 4.dp, start = 4.dp, end = 1.dp)
                        )
                        Text(//value Date.
                            text = culturalEvent.dtend.substringBefore(' '),
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
                            style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                            //fontWeight = FontWeight(200),
                            modifier = Modifier
                                .padding(top = 4.dp, bottom = 4.dp, start = 0.dp, end = 4.dp)
                        )
                    }
                }
                Card(//Hours
                    backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer,
                    shape = androidx.compose.material3.MaterialTheme.shapes.extraSmall,
                    elevation = 0.dp,
                    modifier = Modifier
                        .padding(top = 1.dp, end = 2.dp, start = 1.dp, bottom = 2.dp)
                    //.fillMaxWidth()
                ){
                    Row(){
                        Text(//text Hours.
                            text = stringResource(id = R.string.event_hours),
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
                            style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                            //fontWeight = FontWeight(200),
                            modifier = Modifier
                                .padding(top = 4.dp, bottom = 4.dp, start = 4.dp, end = 1.dp)
                        )
                        Text(//value Hours.
                            text = culturalEvent.time,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
                            style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                            //fontWeight = FontWeight(200),
                            modifier = Modifier
                                .padding(top = 4.dp, bottom = 4.dp, start = 0.dp, end = 4.dp)
                        )
                    }
                }
                Card(//Price
                    backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.errorContainer,
                    shape = androidx.compose.material3.MaterialTheme.shapes.extraSmall,
                    elevation = 0.dp,
                    modifier = Modifier
                        .padding(top = 1.dp, end = 4.dp, start = 1.dp, bottom = 2.dp)
                ){
                    val value = culturalEvent.price
                    Text(
                        text = if(value == "") stringResource(id = R.string.event_free) else "3.50",
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onErrorContainer,
                        style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                        //fontWeight = FontWeight(200),
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                    )
                }
            }
            if(culturalEvent.recurrence != null){
                Row() {
                    Card(//event Days
                        backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                        shape = androidx.compose.material3.MaterialTheme.shapes.extraSmall,
                        elevation = 1.dp,
                        modifier = Modifier
                            .padding(top = 1.dp, end = 4.dp, start = 4.dp, bottom = 4.dp)
                            .fillMaxWidth()
                    ) {
                        Row() {
                            Text(//text Days.
                                text = stringResource(id = R.string.event_days),
                                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                                style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                                //fontWeight = FontWeight(200),
                                maxLines = 2,
                                modifier = Modifier
                                    .padding(top = 4.dp, bottom = 4.dp, start = 4.dp, end = 1.dp)
                            )
                            Text(//value Days.
                                text = culturalEvent.recurrence.days,
                                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                                style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                                //fontWeight = FontWeight(200),
                                maxLines = 2,
                                modifier = Modifier
                                    .padding(top = 4.dp, bottom = 4.dp, start = 0.dp, end = 1.dp)
                            )
                            Text(//text Excluded Days.
                                text = stringResource(id = R.string.event_excluded_days),
                                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                                style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                                //fontWeight = FontWeight(200),
                                maxLines = 2,
                                modifier = Modifier
                                    .padding(top = 4.dp, bottom = 4.dp, start = 4.dp, end = 1.dp)
                            )
                            Text(//value Excluded Days.
                                text = culturalEvent.excludedDays,
                                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                                style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                                //fontWeight = FontWeight(200),
                                maxLines = 2,
                                modifier = Modifier
                                    .padding(top = 4.dp, bottom = 4.dp, start = 0.dp, end = 1.dp)
                            )
                        }
                    }
                }
            }
            Text(
                text = stringResource(id = R.string.tip),
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(4.dp)
            )
        }
    }
}