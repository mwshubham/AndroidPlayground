package com.example.android.playground.core.ui.theme

import androidx.compose.ui.graphics.Color

// Color constants - Light theme palette
private const val PRIMARY_40_COLOR = 0xFF1565C0 // Vibrant blue
private const val PRIMARY_CONTAINER_40_COLOR = 0xFFE3F2FD // Very light blue container
private const val SECONDARY_40_COLOR = 0xFF5E35B1 // Purple accent
private const val SECONDARY_CONTAINER_40_COLOR = 0xFFEDE7F6 // Light purple container
private const val TERTIARY_40_COLOR = 0xFF00695C // Teal accent
private const val TERTIARY_CONTAINER_40_COLOR = 0xFFE0F2F1 // Light teal container

// Color constants - Dark theme palette
private const val PRIMARY_80_COLOR = 0xFF8AB4F8 // Soft periwinkle blue - easier on eyes
private const val PRIMARY_CONTAINER_80_COLOR = 0xFF1A237E // Deep navy blue with purple undertone
private const val SECONDARY_80_COLOR = 0xFFCE93D8 // Soft lavender purple
private const val SECONDARY_CONTAINER_80_COLOR = 0xFF4A148C // Rich deep purple
private const val TERTIARY_80_COLOR = 0xFF81C784 // Soft mint green for accent
private const val TERTIARY_CONTAINER_80_COLOR = 0xFF1B5E20 // Deep forest green

// Color constants - Dark surface colors
private const val SURFACE_DARK_COLOR = 0xFF121212 // True Material dark with slight warmth
private const val SURFACE_VARIANT_DARK_COLOR = 0xFF1E1E1E // Elevated surface with subtle contrast
private const val ON_SURFACE_DARK_COLOR = 0xFFE3E3E3 // Softer white for better readability
private const val ON_SURFACE_VARIANT_DARK_COLOR = 0xFFB0B0B0 // Warmer gray for secondary text

// Color constants - Light theme surface containers
private const val SURFACE_CONTAINER_LIGHT_COLOR = 0xFFF8F9FA // Clean light gray surface container
private const val SURFACE_CONTAINER_HIGH_LIGHT_COLOR = 0xFFF1F3F4 // Slightly darker for emphasis
private const val SURFACE_CONTAINER_LOW_LIGHT_COLOR = 0xFFFCFCFC // Almost white for minimal emphasis
private const val SURFACE_CONTAINER_LOWEST_LIGHT_COLOR = 0xFFFFFFFF // Pure white
private const val SURFACE_CONTAINER_HIGHEST_LIGHT_COLOR = 0xFFE8EAED // More contrast for highest emphasis

// Color constants - Dark theme surface containers
private const val SURFACE_CONTAINER_DARK_COLOR = 0xFF1F1F1F // Subtle elevation with warmth
private const val SURFACE_CONTAINER_HIGH_DARK_COLOR = 0xFF2D2D2D // Mid-level elevation
private const val SURFACE_CONTAINER_LOW_DARK_COLOR = 0xFF181818 // Lower elevation, closer to background
private const val SURFACE_CONTAINER_LOWEST_DARK_COLOR = 0xFF121212 // Matches main dark surface
private const val SURFACE_CONTAINER_HIGHEST_DARK_COLOR = 0xFF3A3A3A // Highest elevation with good contrast

// Color constants - Inverse colors
private const val INVERSE_SURFACE_LIGHT_COLOR = 0xFF2D2D2D
private const val INVERSE_ON_SURFACE_LIGHT_COLOR = 0xFFE3E3E3
private const val INVERSE_PRIMARY_LIGHT_COLOR = 0xFF8AB4F8
private const val INVERSE_SURFACE_DARK_COLOR = 0xFFF8F9FA
private const val INVERSE_ON_SURFACE_DARK_COLOR = 0xFF2D2D2D
private const val INVERSE_PRIMARY_DARK_COLOR = 0xFF1565C0

// Color constants - Dark theme "on" colors
private const val DARK_ON_PRIMARY_COLOR = 0xFF1A237E
private const val DARK_ON_PRIMARY_CONTAINER_COLOR = 0xFFE8EAF6
private const val DARK_ON_SECONDARY_COLOR = 0xFF4A148C
private const val DARK_ON_SECONDARY_CONTAINER_COLOR = 0xFFF3E5F5
private const val DARK_ON_TERTIARY_COLOR = 0xFF1B5E20
private const val DARK_ON_TERTIARY_CONTAINER_COLOR = 0xFFE8F5E8

// Color constants - Dark theme error colors
private const val DARK_ERROR_COLOR = 0xFFFF8A80 // Coral red, easier on eyes than pure red
private const val DARK_ON_ERROR_COLOR = 0xFF5D1A1A
private const val DARK_ERROR_CONTAINER_COLOR = 0xFF8C1D18
private const val DARK_ON_ERROR_CONTAINER_COLOR = 0xFFFFEDEA

// Color constants - Dark theme outline colors
private const val DARK_OUTLINE_COLOR = 0xFF6A6A6A // Warmer gray for better visual hierarchy
private const val DARK_OUTLINE_VARIANT_COLOR = 0xFF3A3A3A // Subtle variant that works with elevation

// Color constants - Light theme "on" colors
private const val LIGHT_ON_PRIMARY_CONTAINER_COLOR = 0xFF0D47A1
private const val LIGHT_ON_SECONDARY_CONTAINER_COLOR = 0xFF4527A0
private const val LIGHT_ON_TERTIARY_CONTAINER_COLOR = 0xFF004D40

// Color constants - Light theme background and surface colors
private const val LIGHT_BACKGROUND_COLOR = 0xFFFEFBFF
private const val LIGHT_ON_BACKGROUND_COLOR = 0xFF1A1C1E
private const val LIGHT_SURFACE_COLOR = 0xFFFEFBFF
private const val LIGHT_ON_SURFACE_COLOR = 0xFF1A1C1E
private const val LIGHT_SURFACE_VARIANT_COLOR = 0xFFF1F3F4
private const val LIGHT_ON_SURFACE_VARIANT_COLOR = 0xFF44474F

// Color constants - Light theme error colors
private const val LIGHT_ERROR_COLOR = 0xFFB3261E
private const val LIGHT_ERROR_CONTAINER_COLOR = 0xFFF9DEDC
private const val LIGHT_ON_ERROR_CONTAINER_COLOR = 0xFF410E0B

// Color constants - Light theme outline colors
private const val LIGHT_OUTLINE_COLOR = 0xFF74777F
private const val LIGHT_OUTLINE_VARIANT_COLOR = 0xFFC4C7C5

// Light theme colors - Modern Blue/Indigo palette
val Primary40 = Color(PRIMARY_40_COLOR)
val PrimaryContainer40 = Color(PRIMARY_CONTAINER_40_COLOR)
val Secondary40 = Color(SECONDARY_40_COLOR)
val SecondaryContainer40 = Color(SECONDARY_CONTAINER_40_COLOR)
val Tertiary40 = Color(TERTIARY_40_COLOR)
val TertiaryContainer40 = Color(TERTIARY_CONTAINER_40_COLOR)

// Dark theme colors - Modern sophisticated palette with warm undertones
val Primary80 = Color(PRIMARY_80_COLOR)
val PrimaryContainer80 = Color(PRIMARY_CONTAINER_80_COLOR)
val Secondary80 = Color(SECONDARY_80_COLOR)
val SecondaryContainer80 = Color(SECONDARY_CONTAINER_80_COLOR)
val Tertiary80 = Color(TERTIARY_80_COLOR)
val TertiaryContainer80 = Color(TERTIARY_CONTAINER_80_COLOR)

// Surface colors for better dark mode - warmer and more comfortable
val SurfaceDark = Color(SURFACE_DARK_COLOR)
val SurfaceVariantDark = Color(SURFACE_VARIANT_DARK_COLOR)
val OnSurfaceDark = Color(ON_SURFACE_DARK_COLOR)
val OnSurfaceVariantDark = Color(ON_SURFACE_VARIANT_DARK_COLOR)

// Additional Material 3 surface colors for light theme
val SurfaceContainerLight = Color(SURFACE_CONTAINER_LIGHT_COLOR)
val SurfaceContainerHighLight = Color(SURFACE_CONTAINER_HIGH_LIGHT_COLOR)
val SurfaceContainerLowLight = Color(SURFACE_CONTAINER_LOW_LIGHT_COLOR)
val SurfaceContainerLowestLight = Color(SURFACE_CONTAINER_LOWEST_LIGHT_COLOR)
val SurfaceContainerHighestLight = Color(SURFACE_CONTAINER_HIGHEST_LIGHT_COLOR)

// Additional Material 3 surface colors for dark theme - sophisticated elevation system
val SurfaceContainerDark = Color(SURFACE_CONTAINER_DARK_COLOR)
val SurfaceContainerHighDark = Color(SURFACE_CONTAINER_HIGH_DARK_COLOR)
val SurfaceContainerLowDark = Color(SURFACE_CONTAINER_LOW_DARK_COLOR)
val SurfaceContainerLowestDark = Color(SURFACE_CONTAINER_LOWEST_DARK_COLOR)
val SurfaceContainerHighestDark = Color(SURFACE_CONTAINER_HIGHEST_DARK_COLOR)

// Inverse colors for better accessibility with updated color scheme
val InverseSurfaceLight = Color(INVERSE_SURFACE_LIGHT_COLOR)
val InverseOnSurfaceLight = Color(INVERSE_ON_SURFACE_LIGHT_COLOR)
val InversePrimaryLight = Color(INVERSE_PRIMARY_LIGHT_COLOR)

val InverseSurfaceDark = Color(INVERSE_SURFACE_DARK_COLOR)
val InverseOnSurfaceDark = Color(INVERSE_ON_SURFACE_DARK_COLOR)
val InversePrimaryDark = Color(INVERSE_PRIMARY_DARK_COLOR)

// Colors used in dark theme scheme - updated for better harmony
val DarkOnPrimary = Color(DARK_ON_PRIMARY_COLOR)
val DarkOnPrimaryContainer = Color(DARK_ON_PRIMARY_CONTAINER_COLOR)
val DarkOnSecondary = Color(DARK_ON_SECONDARY_COLOR)
val DarkOnSecondaryContainer = Color(DARK_ON_SECONDARY_CONTAINER_COLOR)
val DarkOnTertiary = Color(DARK_ON_TERTIARY_COLOR)
val DarkOnTertiaryContainer = Color(DARK_ON_TERTIARY_CONTAINER_COLOR)

// Error colors for dark theme - softer but still attention-grabbing
val DarkError = Color(DARK_ERROR_COLOR)
val DarkOnError = Color(DARK_ON_ERROR_COLOR)
val DarkErrorContainer = Color(DARK_ERROR_CONTAINER_COLOR)
val DarkOnErrorContainer = Color(DARK_ON_ERROR_CONTAINER_COLOR)

// Outline colors for dark theme - subtle and sophisticated
val DarkOutline = Color(DARK_OUTLINE_COLOR)
val DarkOutlineVariant = Color(DARK_OUTLINE_VARIANT_COLOR)

// Colors used in light theme scheme
val LightOnPrimary = Color.White
val LightOnPrimaryContainer = Color(LIGHT_ON_PRIMARY_CONTAINER_COLOR)
val LightOnSecondary = Color.White
val LightOnSecondaryContainer = Color(LIGHT_ON_SECONDARY_CONTAINER_COLOR)
val LightOnTertiary = Color.White
val LightOnTertiaryContainer = Color(LIGHT_ON_TERTIARY_CONTAINER_COLOR)

// Background and surface colors for light theme
val LightBackground = Color(LIGHT_BACKGROUND_COLOR)
val LightOnBackground = Color(LIGHT_ON_BACKGROUND_COLOR)
val LightSurface = Color(LIGHT_SURFACE_COLOR)
val LightOnSurface = Color(LIGHT_ON_SURFACE_COLOR)
val LightSurfaceVariant = Color(LIGHT_SURFACE_VARIANT_COLOR)
val LightOnSurfaceVariant = Color(LIGHT_ON_SURFACE_VARIANT_COLOR)

// Error colors for light theme
val LightError = Color(LIGHT_ERROR_COLOR)
val LightOnError = Color.White
val LightErrorContainer = Color(LIGHT_ERROR_CONTAINER_COLOR)
val LightOnErrorContainer = Color(LIGHT_ON_ERROR_CONTAINER_COLOR)

// Outline colors for light theme
val LightOutline = Color(LIGHT_OUTLINE_COLOR)
val LightOutlineVariant = Color(LIGHT_OUTLINE_VARIANT_COLOR)

