package com.example.unimatchfrontend.features.projects.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.unimatchfrontend.features.companies.ui.CompanyViewModel
import com.example.unimatchfrontend.features.projects.domain.model.Project
import com.example.unimatchfrontend.features.projects.ui.ProjectViewModel
import com.example.unimatchfrontend.features.studentpostulations.ui.StudentPostulationViewModel
import com.example.unimatchfrontend.features.students.ui.StudentViewModel
import com.example.unimatchfrontend.features.users.ui.UserViewModel
import com.example.unimatchfrontend.navigation.Routes
import com.example.unimatchfrontend.shared.ui.BottomNavigationBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    navController: NavController,
    projectId: Int,
    projectViewModel: ProjectViewModel,
    companyViewModel: CompanyViewModel,
    studentViewModel: StudentViewModel,
    userViewModel: UserViewModel,
    studentPostulationViewModel: StudentPostulationViewModel
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var project by remember { mutableStateOf<Project?>(null) }
    var companyName by remember { mutableStateOf("Empresa desconocida") }
    var alreadyPostulated by remember { mutableStateOf(false) }
    var showPostulationDialog by remember { mutableStateOf(false) }

    LaunchedEffect(projectId) {
        project = projectViewModel.projects.value.find { it.id == projectId }
        project?.let {
            companyName = companyViewModel.getCompanyNameById(it.companyId)
        }

        userViewModel.currentUser?.id?.let { userId ->
            studentViewModel.loadStudentByUserId(userId)
        }
    }

    val student = studentViewModel.student.collectAsState().value

    // Verificar si el alumno ya está postulado al proyecto
    LaunchedEffect(student, project) {
        if (student != null && project != null) {
            studentPostulationViewModel.getPostulationByStudentAndProject(
                studentId = student.id!!,
                projectId = project!!.id
            ) { postulation ->
                alreadyPostulated = postulation != null
            }
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
                    containerColor = Color(0xFFF5F0E8),
                    titleContentColor = Color(0xFF1C1F2B),
                    navigationIconContentColor = Color(0xFF1C1F2B)
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController, Routes.OPPORTUNITIES)
        },
        containerColor = Color(0xFFF5F0E8),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
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

                // Botones
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
                            if (alreadyPostulated) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Ya estás postulado a este proyecto")
                                }
                            } else {
                                showPostulationDialog = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (alreadyPostulated) Color.Gray else Color(0xFFFFD479),
                            contentColor = Color.Black
                        ),
                        enabled = !alreadyPostulated,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        Text(if (alreadyPostulated) "Postulado" else "Postularme")
                    }
                }
            }
        }
    }

    // Popup de confirmación de postulación
    if (showPostulationDialog && student != null && project != null) {
        AlertDialog(
            onDismissRequest = { showPostulationDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showPostulationDialog = false
                    coroutineScope.launch {
                        studentPostulationViewModel.postulate(
                            studentId = student.id!!,
                            projectId = project!!.id
                        ) { success, message ->

                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(message)

                                if (success) {
                                    projectViewModel.postulateToProject(
                                        project = project!!,
                                        studentId = student.id!!
                                    ) { updated, msg ->
                                        coroutineScope.launch {
                                            if (!updated) {
                                                snackbarHostState.showSnackbar("⚠️ $msg")
                                            }
                                        }
                                    }

                                    navController.navigate(Routes.OPPORTUNITIES)
                                }
                            }
                        }

                    }
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPostulationDialog = false
                }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Confirmar Postulación") },
            text = { Text("¿Estás seguro de que deseas postular a este proyecto?") }
        )
    }
}
