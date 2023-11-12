package com.upmgeoinfo.culturamad.ui.composables

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.upmgeoinfo.culturamad.ui.composables.prefab.NavBackButton
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme

@Composable
fun StatementScreen(
    navController: NavHostController? = null
){
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Spacer(modifier = Modifier.height(45.dp))
            Row {
                NavBackButton(
                    color = MaterialTheme.colorScheme.primary,
                    onClick = { navController?.popBackStack() }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "# Data Safety and Privacy Statement\n" +
                            "\n" +
                            "## User Data Collection\n" +
                            "\n" +
                            "CulturaMAD respects the privacy of its users and is committed to protecting their personal information. The app collects the following data for the specified purposes:\n" +
                            "\n" +
                            "- **Geolocation data:** We collect Geolocation Data to provide location-specific services, like mapping, directions and local recommendations.\n" +
                            "    * Mapping and Navigation: geolocation data is used to provide users with accurate maps, locations, and directions.\n" +
                            "    * User's location: is used to display nearby events.\n" +
                            "- **Personal data: email, password and username:** This is the only personal data collected by the app with the only purpose of uniquely identify the users in case of registration. \n" +
                            "    * Email Address and User Name: this data is used to uniquely identify individuals within the platform. In future versions, we will use the email account for double authentication, important communications, and account recovery, but these features are still not implemented.\n" +
                            "    * Email Address and Password: are fundamentals for authentication. When a user logs in, the system checks the provided credentials against stored information to verify the user's identity.\n" +
                            "    \n" +
                            "## Data Security\n" +
                            "\n" +
                            "We prioritize the security of your data and have implemented measures to prevent unauthorized access, disclosure, alteration, and destruction of your information.\n" +
                            "\n" +
                            "## Third-Party Services\n" +
                            "\n" +
                            "CulturaMAD may utilize third-party services for certain functionalities. These services may have their own privacy policies, and we encourage users to review them for a complete understanding of how their data is handled.\n" +
                            "\n" +
                            "## Consent\n" +
                            "\n" +
                            "By using CulturaMAD, you consent to the collection and use of your information as outlined in this Data Safety and Privacy Statement.\n" +
                            "\n" +
                            "## Contact Us\n" +
                            "\n" +
                            "If you have any questions or concerns about our data safety practices, please contact us at [your_contact_email@example.com].\n" +
                            "\n" +
                            "## Changes to This Statement\n" +
                            "\n" +
                            "We may update this Data Safety and Privacy Statement periodically to reflect changes in our practices. Please review this statement regularly for any updates.\n" +
                            "\n" +
                            "Thank you for trusting CulturaMAD with your data.",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    //textAlign = TextAlign.Justify,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 4.dp)
                        .verticalScroll(
                            state = rememberScrollState(),
                            enabled = true,
                            reverseScrolling = false
                        )
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StatementScreenPreview(){
    CulturaMADTheme {
        StatementScreen()
    }
}