package com.mindpalais.android.ui.theme

import android.media.Image
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindpalais.android.R

@Composable
fun SplashScreen(onSplashClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .clickable(onClick = onSplashClick),
        contentAlignment = Alignment.Center
    ) {

        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.stickler_bkg1),
            contentDescription = "background_image",
            contentScale = ContentScale.FillBounds,
//                colorFilter = ColorFilter.tint(Color.White.copy(alpha =  0.2f))

        )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                
                Spacer(modifier = Modifier.height(24.dp))


                Image(
                    painter = painterResource(R.drawable.slickr_app_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(160.dp, 160.dp)
                )

                Text(
                    text = "sticklr", fontFamily = interFontFamily, fontWeight = FontWeight.SemiBold,
                    fontSize = 48.sp, color = darkPrimary, modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "v 1.0-beta", fontFamily = interFontFamily, fontWeight = FontWeight.Light,
                    fontSize = 16.sp, color = darkPrimary, modifier = Modifier.padding(top= 4.dp)
                )

                Spacer(modifier = Modifier.height(120.dp))
                Text(
                    text = "OpenAI : Configured", fontFamily = interFontFamily, fontWeight = FontWeight.Normal,
                    fontSize = 16.sp, color = darkSecondary, modifier = Modifier.padding(4.dp)
                )

                Text(
                    text = "Tap to Start", fontFamily = interFontFamily, fontWeight = FontWeight.Normal,
                    fontSize = 16.sp, color = darkSecondary, modifier = Modifier.padding(4.dp)
                )
            }


        }
    }
//}