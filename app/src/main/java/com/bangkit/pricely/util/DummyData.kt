package com.bangkit.pricely.util

import com.bangkit.pricely.domain.product.model.Product

object DummyData {
    val product = Product(
        id = 1,
        name = "Tomato",
        weight = 500,
        unit = "gram/pack",
        price = 13000,
        isRise = true,
        description = "The tomato is the edible berry of the plant Solanum lycopersicum, commonly known as the tomato plant. The species originated in western South America and Central America. The Mexican Nahuatl word tomatl gave rise to the Spanish word tomate, from which the English word tomato derived.",
    )
}