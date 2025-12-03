package com.example.unimatchfrontend.features.companies.domain.repository

import com.example.unimatchfrontend.features.companies.domain.model.Company

interface CompanyRepository {
    suspend fun getCompanyByUserId(userId: Int): Company?

    suspend fun getCompanyById(companyId: Int): Company?

    suspend fun getAllCompanies(): List<Company>
    suspend fun createCompany(company: Company): Company

    suspend fun updateCompany(company: Company): Company?


}
