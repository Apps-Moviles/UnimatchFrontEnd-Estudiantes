package com.example.unimatchfrontend.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.unimatchfrontend.features.users.ui.*
import com.example.unimatchfrontend.features.students.ui.views.*
import com.example.unimatchfrontend.features.students.ui.StudentViewModel
import com.example.unimatchfrontend.features.companies.ui.CompanyViewModel
import com.example.unimatchfrontend.features.companies.ui.views.CompanyDetailScreen
import com.example.unimatchfrontend.features.projects.ui.ProjectViewModel
import com.example.unimatchfrontend.features.projects.ui.views.ProjectDetailScreen

// ✅ Importa el nuevo ViewModel
import com.example.unimatchfrontend.features.studentpostulations.ui.StudentPostulationViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    userViewModel: UserViewModel,
    studentViewModel: StudentViewModel,
    companyViewModel: CompanyViewModel,
    projectViewModel: ProjectViewModel,
    studentPostulationViewModel: StudentPostulationViewModel, // ✅ nuevo
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.WELCOME
    ) {
        composable(Routes.WELCOME) {
            WelcomeScreen(
                onLoginClick = { navController.navigate(Routes.LOGIN) },
                onRegisterClick = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    val user = userViewModel.currentUser
                    if (user?.role == "student") {
                        navController.navigate(Routes.OPPORTUNITIES)
                    }
                },
                onRegisterNavigate = { navController.navigate(Routes.REGISTER) },
                viewModel = userViewModel
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    val user = userViewModel.currentUser
                    if (user?.role == "student") {
                        navController.navigate(Routes.OPPORTUNITIES)
                    } else {
                        navController.popBackStack(Routes.LOGIN, false)
                    }
                },
                onLoginNavigate = { navController.popBackStack(Routes.LOGIN, false) },
                viewModel = userViewModel
            )
        }

        // ✅ Student main screens
        composable(Routes.OPPORTUNITIES) {
            OpportunitiesScreen(
                navController = navController,
                studentViewModel = studentViewModel,
                companyViewModel = companyViewModel,
                projectViewModel = projectViewModel
            )
        }

        composable(Routes.PORTFOLIO) {
            PortfolioScreen(navController = navController)
        }

        composable(Routes.POSTULATIONS) {
            PostulationsScreen(navController = navController,userViewModel = userViewModel,studentViewModel = studentViewModel, studentPostulationViewModel = studentPostulationViewModel )
        }

        composable(Routes.PROFILE) {
            StudentProfileScreen(navController = navController)
        }

        composable("${Routes.PROJECT_DETAIL}/{projectId}") { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")?.toIntOrNull() ?: return@composable
            ProjectDetailScreen(
                navController = navController,
                projectId = projectId,
                projectViewModel = projectViewModel,
                companyViewModel = companyViewModel,
                studentViewModel = studentViewModel,
                userViewModel = userViewModel,
                studentPostulationViewModel = studentPostulationViewModel
            )
        }

        composable("${Routes.COMPANY_DETAIL}/{companyId}") { backStackEntry ->
            val companyId = backStackEntry.arguments?.getString("companyId")?.toIntOrNull() ?: return@composable
            CompanyDetailScreen(
                navController = navController,
                companyId = companyId,
                companyViewModel = companyViewModel,
                userViewModel = userViewModel
            )
        }

    }
}
