package com.example.android.playground.core.ui.preview

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

/**
 * Multi-preview annotation that generates both light and dark theme previews
 * with common device configurations
 */
@Preview(
    name = "Light Theme",
    group = "themes",
    showBackground = true,
    backgroundColor = 0xFFFFFBFE,
)
@Preview(
    name = "Dark Theme",
    group = "themes",
    showBackground = true,
    backgroundColor = 0xFF1C1B1F,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
)
annotation class DualThemePreview

/**
 * Preview annotation for components with phone device configuration
 */
@Preview(
    name = "Light Theme - Phone",
    group = "device-themes",
    showBackground = true,
    device = Devices.PIXEL_4,
    backgroundColor = 0xFFFFFBFE,
)
@Preview(
    name = "Dark Theme - Phone",
    group = "device-themes",
    showBackground = true,
    device = Devices.PIXEL_4,
    backgroundColor = 0xFF1C1B1F,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
)
annotation class PhonePreview

/**
 * Preview annotation for tablet layouts
 */
@Preview(
    name = "Light Theme - Tablet",
    group = "tablet-themes",
    showBackground = true,
    device = Devices.PIXEL_C,
    backgroundColor = 0xFFFFFBFE,
)
@Preview(
    name = "Dark Theme - Tablet",
    group = "tablet-themes",
    showBackground = true,
    device = Devices.PIXEL_C,
    backgroundColor = 0xFF1C1B1F,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
)
annotation class TabletPreview

/**
 * Comprehensive preview with multiple configurations
 */
@DualThemePreview
@PhonePreview
@TabletPreview
annotation class FullPreview

/**
 * Preview for individual components with minimal configurations
 */
@Preview(
    name = "Component Light",
    group = "component",
    showBackground = true,
    backgroundColor = 0xFFFFFBFE,
)
@Preview(
    name = "Component Dark",
    group = "component",
    showBackground = true,
    backgroundColor = 0xFF1C1B1F,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
)
annotation class ComponentPreview

/**
 * Preview for screens with different screen sizes
 */
@Preview(
    name = "Small Screen Light",
    group = "screen-sizes",
    showBackground = true,
    device = "spec:width=360dp,height=640dp,dpi=160",
    backgroundColor = 0xFFFFFBFE,
)
@Preview(
    name = "Small Screen Dark",
    group = "screen-sizes",
    showBackground = true,
    device = "spec:width=360dp,height=640dp,dpi=160",
    backgroundColor = 0xFF1C1B1F,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "Large Screen Light",
    group = "screen-sizes",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420",
    backgroundColor = 0xFFFFFBFE,
)
@Preview(
    name = "Large Screen Dark",
    group = "screen-sizes",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420",
    backgroundColor = 0xFF1C1B1F,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
)
annotation class ScreenSizePreview
