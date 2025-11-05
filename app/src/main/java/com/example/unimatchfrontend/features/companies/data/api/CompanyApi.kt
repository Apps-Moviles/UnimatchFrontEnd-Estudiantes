package com.example.unimatchfrontend.features.companies.data.api

import com.example.unimatchfrontend.features.companies.domain.model.Company
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CompanyApi {
    @GET("companies")
    suspend fun getCompanyByUserId(@Query("userId") userId: Int): List<Company>

    @GET("companies")
    suspend fun getAllCompanies(): List<Company>

    @GET("companies")
    suspend fun getCompanyById(@Query("id") companyId: Int): List<Company>


    @POST("companies")
    suspend fun createCompany(@Body company: Company): Company

}
