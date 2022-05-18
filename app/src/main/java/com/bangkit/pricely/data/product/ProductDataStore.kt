package com.bangkit.pricely.data.product

import com.bangkit.pricely.data.product.remote.ProductApi

class ProductDataStore(private val webService: ProductApi) : ProductRepository {

}