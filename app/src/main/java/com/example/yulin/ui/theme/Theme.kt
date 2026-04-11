package com.example.yulin.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = OrangePrimary,
    secondary = SlatePrimary,
    background = PaperBg,
    surface = PaperBg,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    onBackground = SlatePrimary,
    onSurface = SlatePrimary,
)

@Immutable
private object NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = Color.Transparent

    @Composable
    override fun rippleAlpha() = androidx.compose.material.ripple.RippleAlpha(
        draggedAlpha = 0.0f,
        focusedAlpha = 0.0f,
        hoveredAlpha = 0.0f,
        pressedAlpha = 0.0f
    )
}

private object NoIndication : Indication {
    private object NoIndicationInstance : IndicationInstance {
        override fun ContentDrawScope.drawIndication() {
            drawContent()
        }
    }

    @Composable
    override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
        return NoIndicationInstance
    }
}

@Composable
fun CiLinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography
    ) {
        CompositionLocalProvider(
            LocalIndication provides NoIndication,
            LocalRippleTheme provides NoRippleTheme,
            content = content
        )
    }
}
