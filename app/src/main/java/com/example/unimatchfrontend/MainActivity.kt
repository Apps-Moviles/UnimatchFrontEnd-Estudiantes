package com.example.unimatchfrontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.unimatchfrontend.features.users.domain.usecase.LoginUseCase
import com.example.unimatchfrontend.features.users.domain.usecase.RegisterUseCase
import com.example.unimatchfrontend.features.users.ui.UserViewModel
import com.example.unimatchfrontend.features.students.ui.StudentViewModel
import com.example.unimatchfrontend.features.companies.ui.CompanyViewModel
import com.example.unimatchfrontend.features.projects.ui.ProjectViewModel
import com.example.unimatchfrontend.features.users.data.api.UserApi
import com.example.unimatchfrontend.features.users.data.repository.UserRepositoryImpl
import com.example.unimatchfrontend.features.students.data.api.StudentApi
import com.example.unimatchfrontend.features.students.data.repository.StudentRepositoryImpl
import com.example.unimatchfrontend.features.companies.data.api.CompanyApi
import com.example.unimatchfrontend.features.companies.data.repository.CompanyRepositoryImpl
import com.example.unimatchfrontend.features.projects.data.api.ProjectApi
import com.example.unimatchfrontend.features.projects.data.repository.ProjectRepositoryImpl
import com.example.unimatchfrontend.features.projects.domain.usecase.GetAllProjectsUseCase
import com.example.unimatchfrontend.shared.network.RetrofitInstance
import com.example.unimatchfrontend.navigation.AppNavigation

// 👇 importaciones nuevas para postulación
import com.example.unimatchfrontend.features.studentpostulations.data.api.StudentPostulationApi
import com.example.unimatchfrontend.features.studentpostulations.data.repository.StudentPostulationRepositoryImpl
import com.example.unimatchfrontend.features.studentpostulations.ui.StudentPostulationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Retrofit instance
        val retrofit = RetrofitInstance.retrofit

        // APIs
        val userApi = retrofit.create(UserApi::class.java)
        val studentApi = retrofit.create(StudentApi::class.java)
        val companyApi = retrofit.create(CompanyApi::class.java)
        val projectApi = retrofit.create(ProjectApi::class.java)
        val studentPostulationApi = retrofit.create(StudentPostulationApi::class.java) // ✅ nuevo

        // Repositories
        val userRepo = UserRepositoryImpl(userApi)
        val studentRepo = StudentRepositoryImpl(studentApi)
        val companyRepo = CompanyRepositoryImpl(companyApi)
        val projectRepo = ProjectRepositoryImpl(projectApi)
        val studentPostulationRepo = StudentPostulationRepositoryImpl(studentPostulationApi) // ✅ nuevo

        // UseCases
        val loginUseCase = LoginUseCase(userRepo)
        val registerUseCase = RegisterUseCase(userRepo)
        val getAllProjectsUseCase = GetAllProjectsUseCase(projectRepo)

        // ViewModels
        val userViewModel = UserViewModel(
            loginUseCase = loginUseCase,
            registerUseCase = registerUseCase,
            studentRepository = studentRepo,
            companyRepository = companyRepo,
            userRepository = userRepo
        )

        val studentViewModel = StudentViewModel(repository = studentRepo)
        val companyViewModel = CompanyViewModel(repository = companyRepo)
        val projectViewModel = ProjectViewModel(
            getAllProjectsUseCase = getAllProjectsUseCase,
            repository = projectRepo
        )

        val studentPostulationViewModel = StudentPostulationViewModel(studentPostulationRepo, projectRepo, companyRepo) // ✅ nuevo

        // UI Content
        setContent {
            val navController = rememberNavController()

            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                AppNavigation(
                    navController = navController,
                    userViewModel = userViewModel,
                    studentViewModel = studentViewModel,
                    companyViewModel = companyViewModel,
                    projectViewModel = projectViewModel,
                    studentPostulationViewModel = studentPostulationViewModel, // ✅ nuevo
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
