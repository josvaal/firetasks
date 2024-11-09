package com.josval.firetasks.model

import androidx.compose.ui.graphics.Color
import com.josval.firetasks.ui.theme.HighPriorityColor
import com.josval.firetasks.ui.theme.LowPriorityColor
import com.josval.firetasks.ui.theme.MediumPriorityColor
import com.josval.firetasks.ui.theme.NonePriorityColor

enum class PriorityView(val text: String, val color: Color) {
    HIGH("Alto", HighPriorityColor),
    MEDIUM("Medio", MediumPriorityColor),
    LOW("Bajo", LowPriorityColor),
    NONE("Ninguno", NonePriorityColor)
}
