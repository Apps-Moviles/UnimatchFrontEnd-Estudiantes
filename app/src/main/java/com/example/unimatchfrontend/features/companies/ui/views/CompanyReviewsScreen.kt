package com.example.unimatchfrontend.features.companies.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.unimatchfrontend.features.companies.ui.components.ReviewCard
import com.example.unimatchfrontend.features.projects.ui.ProjectViewModel
import com.example.unimatchfrontend.features.reputations.ui.ReputationViewModel
import com.example.unimatchfrontend.features.users.ui.UserViewModel
import com.example.unimatchfrontend.navigation.Routes
import com.example.unimatchfrontend.shared.ui.BottomNavigationBar
import kotlinx.coroutines.launch

data class CompanyReviewItem(
    val comment: String,
    val authorName: String,
    val rating: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyReviewsScreen(
    navController: NavController,
    companyId: Int,
    reputationVM: ReputationViewModel,
    projectVM: ProjectViewModel,
    userVM: UserViewModel
) {
    val coroutine = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(true) }
    var reviewItems by remember { mutableStateOf<List<CompanyReviewItem>>(emptyList()) }

    LaunchedEffect(companyId) {
        coroutine.launch {
            // 1. Asegurarnos de tener los proyectos cargados
            projectVM.loadProjects()
            val projects = projectVM.projects.value

            // 2. Traer todas las reputaciones
            val reputations = reputationVM.getAllReputations()

            // 3. Filtrar solo type = 2 y companyId que coincide
            val filtered = reputations.filter { rep ->
                rep.type == 2 &&
                        projects.find { it.id == rep.projectId }?.companyId == companyId
            }

            // 4. Mapear a items de UI con nombre real del estudiante
            val items = filtered.map { rep ->
                val authorName = userVM.getStudentNameByStudentId(rep.studentId)
                CompanyReviewItem(
                    comment = rep.comment,
                    authorName = authorName,
                    rating = rep.rating
                )
            }

            reviewItems = items
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reseñas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF5F0E8),
                    titleContentColor = Color(0xFF1C1F2B),
                    navigationIconContentColor = Color(0xFF1C1F2B)
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController, currentRoute = Routes.OPPORTUNITIES)
        },
        containerColor = Color(0xFFF5F0E8)
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                reviewItems.isEmpty() -> {
                    Text(
                        text = "Aún no hay reseñas para esta empresa.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(reviewItems.size) { index ->
                            val item = reviewItems[index]
                            ReviewCard(
                                comment = item.comment,
                                author = item.authorName,
                                rating = item.rating
                            )
                        }
                    }
                }
            }
        }
    }
}
