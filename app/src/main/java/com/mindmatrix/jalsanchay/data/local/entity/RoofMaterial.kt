package com.mindmatrix.jalsanchay.data.local.entity

enum class RoofMaterial(val displayName: String, val runoffCoefficient: Double) {
    TILE("Tile", 0.85),
    METAL("Metal", 0.95),
    CONCRETE("Concrete", 0.80),
    PLASTIC_SHEET("Plastic sheet", 0.90)
}
