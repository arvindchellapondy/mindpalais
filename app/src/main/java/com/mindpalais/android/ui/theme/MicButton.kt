package com.mindpalais.android.ui.theme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun MicTextButton(
    onClick: () -> Unit,
    text: String,
    icon: ImageVector = Icons.Default.Mic,
    iconTint: Color = Color.White,
    contentColor: Color = Color.White
) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            contentColor = contentColor,
            backgroundColor = Color.Transparent,
            disabledContentColor = Color.Gray
        ),
        shape = CircleShape,
        modifier = Modifier.padding(16.dp)
    ) {
        Row {
            Image(
                imageVector = icon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = text)
        }
    }
}