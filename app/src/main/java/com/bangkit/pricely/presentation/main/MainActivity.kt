package com.bangkit.pricely.presentation.main

import android.content.Intent
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.pricely.R
import com.bangkit.pricely.base.BaseActivity
import com.bangkit.pricely.databinding.ActivityMainBinding
import com.bangkit.pricely.domain.product.model.Category
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.presentation.viewmodel.ProductViewModel
import com.bangkit.pricely.util.dp
import com.bangkit.pricely.util.observe
import com.bangkit.pricely.util.recyclerview.PricelyGridLayoutItemDecoration
import com.bangkit.pricely.util.setupToolbar
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val productViewModel: ProductViewModel by inject()

    private lateinit var categoryAdapter: CategoryAdapter

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
            viewAllProductSection.setOnViewAllButtonClicked {
                // TODO: Change this category model to the newest category
                val allProductCategory = Category(CategoryDetailActivity.ALL_PRODUCT,getString(R.string.label_description_allproduct),
                    1,"")
                CategoryDetailActivity.start(this@MainActivity, allProductCategory)
            }
            viewRecommendationSection.setOnViewAllButtonClicked {
                // TODO: Change this category model to the newest category
                val recommendationCategory = Category(CategoryDetailActivity.RECOMMENDATION, getString(R.string.label_description_recommendation),
                    1,"")
                CategoryDetailActivity.start(this@MainActivity, recommendationCategory)
            }

            categoryAdapter.setOnClickedItem { category, position ->
                when(category.type) {
                    0 -> {
                        categoryAdapter.selectCategory(position)
                    }
                    1 -> {
                        categoryAdapter.selectCategory(position)
                    }
                    2 -> {
                        CategoryBottomSheet.newInstance(ArrayList(getDummyBottomSheetCategory())){ newCategory ->
                            CategoryDetailActivity.start(this@MainActivity, newCategory)
                        }.showDialog(supportFragmentManager)
                    }
                }
            }
        }
    }

    override fun setupProcess() {
        getRecommendation()
        getAllProducts()
        categoryAdapter.submitList(getDummyCategory())
    }

    override fun setupObserver() {
        productViewModel.listRecommendation.observe(this,
            onLoading = {
                showLoading()
            },
            onError = {
                dismissLoading()
                showErrorDialog(it, ::getRecommendation)
            },
            onSuccess = {
                dismissLoading()
                if (it.size > 3) setRecommendation(it.take(3)) else setRecommendation(it)
            }
        )

        productViewModel.listAllProduct.observe(this,
            onLoading = {
                showLoading()
            },
            onError = {
                dismissLoading()
                showErrorDialog(it, ::getAllProducts)
            },
            onSuccess = {
                dismissLoading()
                if (it.size > 3) setAllProduct(it.take(3)) else setAllProduct(it)
            }
        )
    }

    private fun getAllProducts() {
        productViewModel.getListAllProduct()
    }

    private fun getRecommendation() {
        productViewModel.getListRecommendation(true)
    }

    private fun setAllProduct(list: List<Product>) {
        binding.viewAllProductSection.setProducts(list)
    }

    private fun setRecommendation(list: List<Product>) {
        binding.viewRecommendationSection.setProducts(list)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> SearchActivity.start(this)
            R.id.menu_language -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        with(binding) {
            categoryAdapter = CategoryAdapter()

            rvCategory.apply {
                adapter = categoryAdapter
                layoutManager = GridLayoutManager(this@MainActivity, 4)
                addItemDecoration(PricelyGridLayoutItemDecoration(4, 8.dp))
            }
        }
    }

    /* Type 0 for Unclickable, 1 for Intent, 2 for Show Bottom Sheet */
    private fun getDummyBottomSheetCategory(): List<Category> {
        return listOf(
            Category(resources.getString(R.string.label_fruit), "Fruits are the means by which flowering plants (also known as angiosperms) disseminate their seeds. Edible fruits in particular have long propagated using the movements of humans and animals in a symbiotic relationship that is the means for seed dispersal for the one group and nutrition for the other; in fact, humans and many animals have become dependent on fruits as a source of food. Consequently, fruits account for a substantial fraction of the world's agricultural output, and some (such as the apple and the pomegranate) have acquired extensive cultural and symbolic meanings.",
                1, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
            Category(resources.getString(R.string.label_meat), "Meat is animal flesh that is eaten as food. Humans have hunted, farmed, and scavenged animals for meat since prehistoric times. The establishment of settlements in the Neolithic Revolution allowed the domestication of animals such as chickens, sheep, rabbits, pigs and cattle. This eventually led to their use in meat production on an industrial scale in slaughterhouses.",
                1, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
            Category(resources.getString(R.string.label_vegetable), "Vegetables are parts of plants that are consumed by humans or other animals as food. The original meaning is still commonly used and is applied to plants collectively to refer to all edible plant matter, including the flowers, fruits, stems, leaves, roots, and seeds.",
                1, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
            Category(resources.getString(R.string.label_food), "Food is any substance consumed to provide nutritional support for an organism. Food is usually of plant, animal, or fungal origin, and contains essential nutrients, such as carbohydrates, fats, proteins, vitamins, or minerals. The substance is ingested by an organism and assimilated by the organism's cells to provide energy, maintain life, or stimulate growth. Different species of animals have different feeding behaviours that satisfy the needs of their unique metabolisms, often evolved to fill a specific ecological niche within specific geographical contexts.",
                1, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
            Category(resources.getString(R.string.label_drink), "A drink (or beverage) is a liquid intended for human consumption. In addition to their basic function of satisfying thirst, drinks play important roles in human culture. Common types of drinks include plain drinking water, milk, juice, smoothies and soft drinks. Traditionally warm beverages include coffee, tea, and hot chocolate. Caffeinated drinks that contain the stimulant caffeine have a long history.",
                1, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
            Category(resources.getString(R.string.label_grocery), "A grocery store (AE), grocery shop (BE) or simply grocery is a store that primarily retails a general range of food products, which may be fresh or packaged. In everyday U.S. usage, however, \"grocery store\" is a synonym for supermarket, and is not used to refer to other types of stores that sell groceries. In the UK, shops that sell food are distinguished as grocers or grocery shops (though in everyday use, people usually use either the term \"supermarket\" or a \"corner shop\" or \"convenience shop\").",
                1, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
            Category(resources.getString(R.string.label_seafood), "Seafood is any form of sea life regarded as food by humans, prominently including fish and shellfish. Shellfish include various species of molluscs (e.g. bivalve molluscs such as clams, oysters and mussels, and cephalopods such as octopus and squid), crustaceans (e.g. shrimp, crabs, and lobster), and echinoderms (e.g. sea cucumbers and sea urchins). Historically, marine mammals such as cetaceans (whales and dolphins) as well as seals have been eaten as food, though that happens to a lesser extent in modern times. Edible sea plants such as some seaweeds and microalgae are widely eaten as sea vegetables around the world, especially in Asia.",
                1, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
            Category(resources.getString(R.string.label_seasoning), "In general use, herbs are a widely distributed and widespread group of plants, excluding vegetables and other plants consumed for macronutrients, with savory or aromatic properties that are used for flavoring and garnishing food, for medicinal purposes, or for fragrances. Culinary use typically distinguishes herbs from spices. Herbs generally refers to the leafy green or flowering parts of a plant (either fresh or dried), while spices are usually dried and produced from other parts of the plant, including seeds, bark, roots and fruits.",
                1, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
        )
    }

    /* Type 0 for Unclickable, 1 for Intent, 2 for Show Bottom Sheet */
    private fun getDummyCategory(): List<Category> {
        return listOf(
            Category(resources.getString(R.string.label_all_product), "-", 0, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
            Category(resources.getString(R.string.label_fruit), "Fruits are the means by which flowering plants (also known as angiosperms) disseminate their seeds. Edible fruits in particular have long propagated using the movements of humans and animals in a symbiotic relationship that is the means for seed dispersal for the one group and nutrition for the other; in fact, humans and many animals have become dependent on fruits as a source of food. Consequently, fruits account for a substantial fraction of the world's agricultural output, and some (such as the apple and the pomegranate) have acquired extensive cultural and symbolic meanings.",
                1, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
            Category(resources.getString(R.string.label_meat), "Meat is animal flesh that is eaten as food. Humans have hunted, farmed, and scavenged animals for meat since prehistoric times. The establishment of settlements in the Neolithic Revolution allowed the domestication of animals such as chickens, sheep, rabbits, pigs and cattle. This eventually led to their use in meat production on an industrial scale in slaughterhouses.",
                1, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
            Category(resources.getString(R.string.label_vegetable), "Vegetables are parts of plants that are consumed by humans or other animals as food. The original meaning is still commonly used and is applied to plants collectively to refer to all edible plant matter, including the flowers, fruits, stems, leaves, roots, and seeds.",
                1, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
            Category(resources.getString(R.string.label_food), "Food is any substance consumed to provide nutritional support for an organism. Food is usually of plant, animal, or fungal origin, and contains essential nutrients, such as carbohydrates, fats, proteins, vitamins, or minerals. The substance is ingested by an organism and assimilated by the organism's cells to provide energy, maintain life, or stimulate growth. Different species of animals have different feeding behaviours that satisfy the needs of their unique metabolisms, often evolved to fill a specific ecological niche within specific geographical contexts.",
                1, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
            Category(resources.getString(R.string.label_drink), "A drink (or beverage) is a liquid intended for human consumption. In addition to their basic function of satisfying thirst, drinks play important roles in human culture. Common types of drinks include plain drinking water, milk, juice, smoothies and soft drinks. Traditionally warm beverages include coffee, tea, and hot chocolate. Caffeinated drinks that contain the stimulant caffeine have a long history.",
                1, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
            Category(resources.getString(R.string.label_grocery), "A grocery store (AE), grocery shop (BE) or simply grocery is a store that primarily retails a general range of food products, which may be fresh or packaged. In everyday U.S. usage, however, \"grocery store\" is a synonym for supermarket, and is not used to refer to other types of stores that sell groceries. In the UK, shops that sell food are distinguished as grocers or grocery shops (though in everyday use, people usually use either the term \"supermarket\" or a \"corner shop\" or \"convenience shop\").",
                1, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
            Category(resources.getString(R.string.label_other), "-", 2, "https://cdn-icons-png.flaticon.com/512/291/291893.png"),
        )
    }
}