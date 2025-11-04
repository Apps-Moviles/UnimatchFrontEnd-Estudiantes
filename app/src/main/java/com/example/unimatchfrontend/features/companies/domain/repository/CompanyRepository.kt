package com.example.unimatchfrontend.features.companies.domain.repository

import com.example.unimatchfrontend.features.companies.domain.model.Company

interface CompanyRepository {
    suspend fun getCompanyByUserId(userId: Int): Company?

    suspend fun getCompanyById(companyId: Int): Company?
}
