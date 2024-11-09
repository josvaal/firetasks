package com.josval.firetasks.model

import androidx.compose.ui.graphics.Color
import com.josval.firetasks.ui.theme.HighPriorityColor
import com.josval.firetasks.ui.theme.LowPriorityColor
import com.josval.firetasks.ui.theme.MediumPriorityColor
import com.josval.firetasks.ui.theme.NonePriorityColor

enum class Priority(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor);

    companion object {
        fun fromString(value: String): Priority {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: NONE
        }
    }
}