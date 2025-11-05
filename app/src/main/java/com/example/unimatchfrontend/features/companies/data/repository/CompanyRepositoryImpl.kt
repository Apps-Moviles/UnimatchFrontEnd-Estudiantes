package com.example.unimatchfrontend.features.companies.data.repository

import com.example.unimatchfrontend.features.companies.data.api.CompanyApi
import com.example.unimatchfrontend.features.companies.domain.model.Company
import com.example.unimatchfrontend.features.companies.domain.repository.CompanyRepository

class CompanyRepositoryImpl(
    private val api: CompanyApi
) : CompanyRepository {

    override suspend fun getCompanyByUserId(userId: Int): Company? {
        return api.getCompanyByUserId(userId).firstOrNull()
    }

    override suspend fun getCompanyById(companyId: Int): Company? {
        return api.getCompanyById(companyId).firstOrNull()
    }

    override suspend fun getAllCompanies(): List<Company> {
        return api.getAllCompanies()
    }

    override suspend fun createCompany(company: Company): Company {
        return api.createCompany(company)
    }
}
