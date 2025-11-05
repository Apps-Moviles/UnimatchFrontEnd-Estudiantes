package com.example.unimatchfrontend.features.students.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.unimatchfrontend.features.studentpostulations.ui.components.StudentPostulationCard
import com.example.unimatchfrontend.features.studentpostulations.ui.StudentPostulationViewModel
import com.example.unimatchfrontend.features.students.ui.StudentViewModel
import com.example.unimatchfrontend.features.users.ui.UserViewModel
import com.example.unimatchfrontend.navigation.Routes
import com.example.unimatchfrontend.shared.ui.BottomNavigationBar
import kotlinx.coroutines.launch

@Composable
fun PostulationsScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    studentViewModel: StudentViewModel,
    studentPostulationViewModel: StudentPostulationViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val student by studentViewModel.student.collectAsState()
    val postulations by studentPostulationViewModel.studentPostulations.collectAsState()

    // Cargar el estudiante desde el usuario actual
    LaunchedEffect(userViewModel.currentUser?.id) {
        userViewModel.currentUser?.id?.let { userId ->
            studentViewModel.loadStudentByUserId(userId)
        }
    }

    // Cargar las postulaciones del estudiante
    LaunchedEffect(student) {
        student?.id?.let { studentId ->
            studentPostulationViewModel.getPostulationsByStudentId(studentId)
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, currentRoute = Routes.POSTULATIONS) },
        containerColor = Color(0xFFF5F0E8)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "Mis Postulaciones",
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )

            if (postulations.isEmpty()) {
                Text(
                    text = "No tienes postulaciones aún.",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(postulations) { postulation ->
                        val project = studentPostulationViewModel.getProjectById(postulation.projectId)
                        val companyName = studentPostulationViewModel.getCompanyNameByProjectId(postulation.projectId)

                        StudentPostulationCard(
                            projectName = project?.title ?: "Proyecto",
                            companyName = companyName,
                            status = postulation.status,
                            date = postulation.date
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}
