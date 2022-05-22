package com.bangkit.pricely.presentation.main

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.pricely.base.BaseActivity
import com.bangkit.pricely.databinding.ActivityMainBinding
import com.bangkit.pricely.domain.product.model.Category
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.util.dp
import com.bangkit.pricely.util.recyclerview.PricelyGridLayoutItemDecoration
import com.bangkit.pricely.util.setupToolbar
import com.bangkit.pricely.util.showToast

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val categoryAdapter by lazy {
        CategoryAdapter(
            onItemClicked = { category ->
                when(category.type) {
                    1 -> {
                        showToast("Terpilih: ${category.name} dengan deskripsi ${category.description}")
                        // TODO: Go to CategoryDetailActivity(category.name)
                    }
                    2 -> {
                        showToast("Open Other Category")
                        // TODO: Open Category BottomSheetDialog
                    }
                }
            }
        )
    }

    override fun getViewBinding(): ActivityMainBinding {
        installSplashScreen()
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun setupIntent() {}

    override fun setupUI() {
        setupToolbar(
            binding.toolbar.toolbar,
            "",
            false
        )
        setupRecyclerView()
    }

    override fun setupAction() {
        with(binding) {
//            toolbar.btnSearch.setOnClickListener {
//                showToast("Search")
//                // TODO: Go to SearchActivity
//            }
            viewAllProductSection.setOnViewAllButtonClicked {
                showToast("Go to all product page")
            }
            viewRecommendationSection.setOnViewAllButtonClicked {
                showToast("Go to recommendation page")
            }
        }
    }

    override fun setupProcess() {
        with(binding){
            viewAllProductSection.setProducts(getDummyProducts())
            viewRecommendationSection.setProducts(getDummyProducts())
        }
        categoryAdapter.submitList(getDummyCategory())
    }

    override fun setupObserver() {}

    private fun setupRecyclerView() {
        with(binding) {
            rvCategory.apply {
                adapter = categoryAdapter
                layoutManager = GridLayoutManager(this@MainActivity, 4)
                addItemDecoration(PricelyGridLayoutItemDecoration(4, 8.dp))
            }
        }
    }

    private fun getDummyProducts() =
        Array(6){
            val rand = (300..500).random()
            val price = "${rand}00".toInt()
            Product(
                id = it.toString(),
                "Product #$it",
                price,
                "Kg / Kg"
            )
        }.toList()

    /* Type 0 for Unclickable, 1 for Intent, 2 for Show Bottom Sheet */
    private fun getDummyCategory(): List<Category> {
        return listOf(
            Category("All Product", "-", 0, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
            Category("Fruit", "Fruits are the means by which flowering plants (also known as angiosperms) disseminate their seeds. Edible fruits in particular have long propagated using the movements of humans and animals in a symbiotic relationship that is the means for seed dispersal for the one group and nutrition for the other; in fact, humans and many animals have become dependent on fruits as a source of food. Consequently, fruits account for a substantial fraction of the world's agricultural output, and some (such as the apple and the pomegranate) have acquired extensive cultural and symbolic meanings.",
                1, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
            Category("Meat", "Meat is animal flesh that is eaten as food. Humans have hunted, farmed, and scavenged animals for meat since prehistoric times. The establishment of settlements in the Neolithic Revolution allowed the domestication of animals such as chickens, sheep, rabbits, pigs and cattle. This eventually led to their use in meat production on an industrial scale in slaughterhouses.",
                1, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
            Category("Vegetable", "Vegetables are parts of plants that are consumed by humans or other animals as food. The original meaning is still commonly used and is applied to plants collectively to refer to all edible plant matter, including the flowers, fruits, stems, leaves, roots, and seeds.",
                1, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
            Category("Food", "Food is any substance consumed to provide nutritional support for an organism. Food is usually of plant, animal, or fungal origin, and contains essential nutrients, such as carbohydrates, fats, proteins, vitamins, or minerals. The substance is ingested by an organism and assimilated by the organism's cells to provide energy, maintain life, or stimulate growth. Different species of animals have different feeding behaviours that satisfy the needs of their unique metabolisms, often evolved to fill a specific ecological niche within specific geographical contexts.",
                1, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
            Category("Drink", "A drink (or beverage) is a liquid intended for human consumption. In addition to their basic function of satisfying thirst, drinks play important roles in human culture. Common types of drinks include plain drinking water, milk, juice, smoothies and soft drinks. Traditionally warm beverages include coffee, tea, and hot chocolate. Caffeinated drinks that contain the stimulant caffeine have a long history.",
                1, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
            Category("Grocery", "A grocery store (AE), grocery shop (BE) or simply grocery is a store that primarily retails a general range of food products, which may be fresh or packaged. In everyday U.S. usage, however, \"grocery store\" is a synonym for supermarket, and is not used to refer to other types of stores that sell groceries. In the UK, shops that sell food are distinguished as grocers or grocery shops (though in everyday use, people usually use either the term \"supermarket\" or a \"corner shop\" or \"convenience shop\").",
                1, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
            Category("Other", "-", 2, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
        )
    }
}