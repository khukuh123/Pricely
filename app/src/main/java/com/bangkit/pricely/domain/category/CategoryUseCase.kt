package com.bangkit.pricely.domain.category

import com.bangkit.pricely.domain.category.model.Category
import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface CategoryUseCase {

    suspend fun getCategoryDetail(categoryId: Int): Flow<Resource<Category>>

    suspend fun getCategoryList(): Flow<Resource<List<Category>>>

}