package com.bangkit.pricely.domain.category

import com.bangkit.pricely.data.category.CategoryRepository
import com.bangkit.pricely.domain.category.model.Category
import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class CategoryInteractor(private val categoryRepository: CategoryRepository): CategoryUseCase {
    override suspend fun getCategoryDetail(categoryId: Int): Flow<Resource<Category>> =
        categoryRepository.getCategoryDetail(categoryId)

    override suspend fun getCategoryList(): Flow<Resource<List<Category>>> =
        categoryRepository.getCategoryList()

}