package com.goto_deliveryl.pgoto.ui.utils.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

/* Creates a button with a brand logo */
@ExperimentalMaterial3Api
@Composable
fun ThirdPartyAuthenticationMethod(
    onClick: () -> Unit,
    imagePainter: Painter,
    contentDescription: String? = null
) {
    ElevatedCard(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable { onClick() },
    ) {
        Image(
            painter = imagePainter,
            contentDescription = contentDescription,
            modifier = Modifier.padding(8.dp),
        )
    }
}
