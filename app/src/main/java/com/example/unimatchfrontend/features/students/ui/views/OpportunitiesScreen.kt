package com.example.unimatchfrontend.features.students.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.unimatchfrontend.features.companies.ui.CompanyViewModel
import com.example.unimatchfrontend.features.projects.ui.ProjectViewModel
import com.example.unimatchfrontend.features.students.ui.components.OpportunityCard
import com.example.unimatchfrontend.features.students.ui.StudentViewModel
import com.example.unimatchfrontend.navigation.Routes
import com.example.unimatchfrontend.shared.ui.BottomNavigationBar
import kotlinx.coroutines.launch

@Composable
fun OpportunitiesScreen(
    navController: NavHostController,
    studentViewModel: StudentViewModel,
    companyViewModel: CompanyViewModel,
    projectViewModel: ProjectViewModel
) {
    val projects by projectViewModel.projects.collectAsState()
    val companyNames = remember { mutableStateMapOf<Int, String>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) { projectViewModel.loadProjects() }

    LaunchedEffect(projects) {
        projects.forEach { project ->
            if (!companyNames.containsKey(project.companyId)) {
                coroutineScope.launch {
                    val name = companyViewModel.getCompanyNameById(project.companyId)
                    companyNames[project.companyId] = name
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentRoute = Routes.OPPORTUNITIES
            )
        },
        containerColor = Color(0xFFF5F0E8)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text("Oportunidades", fontSize = 24.sp, modifier = Modifier.padding(16.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                projects
                    .filter {
                        it.status.equals("en revisión", ignoreCase = true) ||
                                it.status.equals("en revision", ignoreCase = true)
                    }
                    .forEach { project ->
                        val companyName = companyNames[project.companyId] ?: "Empresa"
                        OpportunityCard(
                            project = project,
                            companyName = companyName,
                            onClick = {
                                navController.navigate("project_detail/${project.id}")
                            }
                        )
                    }

            }
        }
    }
}
