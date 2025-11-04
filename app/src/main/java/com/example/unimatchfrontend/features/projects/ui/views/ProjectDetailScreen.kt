package com.example.unimatchfrontend.features.projects.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.unimatchfrontend.features.companies.ui.CompanyViewModel
import com.example.unimatchfrontend.features.projects.domain.model.Project
import com.example.unimatchfrontend.features.projects.ui.ProjectViewModel
import com.example.unimatchfrontend.navigation.Routes
import com.example.unimatchfrontend.shared.ui.BottomNavigationBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    navController: NavController,
    projectId: Int,
    projectViewModel: ProjectViewModel,
    companyViewModel: CompanyViewModel
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    var project by remember { mutableStateOf<Project?>(null) }
    var companyName by remember { mutableStateOf("Empresa desconocida") }

    LaunchedEffect(projectId) {
        project = projectViewModel.projects.value.find { it.id == projectId }
        project?.let {
            val company = companyViewModel.getCompanyById(it.companyId)
            companyName = company?.companyName ?: companyName
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Proyecto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF5F0E8), // Fondo
                    titleContentColor = Color(0xFF1C1F2B),    // Color del título
                    navigationIconContentColor = Color(0xFF1C1F2B) // Color del ícono
                )

            )
        },
        bottomBar = {
            BottomNavigationBar(navController, Routes.OPPORTUNITIES)
        },
        containerColor = Color(0xFFF5F0E8)
    ) { innerPadding ->

        project?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
            ) {

                // Scrollable content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(top = 16.dp)
                ) {
                    Text(
                        text = it.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "by $companyName",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Descripción:", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(it.description)

                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Requisitos:", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    it.requirements.forEach { req ->
                        Text("• $req")
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Pago:", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("${it.budget.toInt()} dólares")
                }

                // Buttons (fuera del scroll)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            project?.companyId?.let {
                                navController.navigate("company_detail/$it")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1C1F2B),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Text("Ver empresa")
                    }

                    Button(
                        onClick = {
                            // Aquí va la lógica de postulación
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFD479),
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        Text("Postularme")
                    }
                }
            }
        }
    }
}

