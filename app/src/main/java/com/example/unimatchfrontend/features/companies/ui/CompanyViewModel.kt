package com.example.unimatchfrontend.features.companies.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unimatchfrontend.features.companies.domain.model.Company
import com.example.unimatchfrontend.features.companies.domain.repository.CompanyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CompanyViewModel(
    private val repository: CompanyRepository
) : ViewModel() {

    private val companyNameCache = mutableMapOf<Int, String>()

    suspend fun getCompanyNameById(companyId: Int): String {
        return companyNameCache[companyId] ?: run {
            val company = repository.getCompanyById(companyId)
            val name = company?.companyName ?: "Empresa"
            companyNameCache[companyId] = name
            name
        }
    }

    suspend fun getCompanyById(userId: Int): Company? {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            repository.getCompanyByUserId(userId)
        }
    }
}
