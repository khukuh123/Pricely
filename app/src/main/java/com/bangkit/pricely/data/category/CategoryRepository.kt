package com.bangkit.pricely.data.category

import com.bangkit.pricely.domain.category.model.Category
import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun getCategoryDetail(categoryId: Int): Flow<Resource<Category>>

    suspend fun getCategoryList(): Flow<Resource<List<Category>>>

}