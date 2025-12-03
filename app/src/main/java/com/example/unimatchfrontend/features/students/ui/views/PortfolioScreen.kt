package com.example.unimatchfrontend.features.students.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.unimatchfrontend.features.students.ui.components.PortfolioCard
import com.example.unimatchfrontend.navigation.Routes
import com.example.unimatchfrontend.shared.ui.BottomNavigationBar
import com.example.unimatchfrontend.features.users.ui.UserViewModel
import com.example.unimatchfrontend.features.projects.ui.ProjectViewModel
import com.example.unimatchfrontend.features.companies.ui.CompanyViewModel
import com.example.unimatchfrontend.features.students.utils.PdfCertificateGenerator
import com.example.unimatchfrontend.features.reputations.ui.ReputationViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

fun calculateWeeksAgo(dateString: String): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val parsedDate = sdf.parse(dateString)
        val now = System.currentTimeMillis()
        val diff = now - (parsedDate?.time ?: now)
        val weeks = TimeUnit.MILLISECONDS.toDays(diff) / 7
        "Finalizado hace $weeks semana(s)"
    } catch (e: Exception) {
        "Fecha desconocida"
    }
}

@Composable
fun PortfolioScreen(
    navController: NavController,
    userVM: UserViewModel,
    projectVM: ProjectViewModel,
    companyVM: CompanyViewModel,
    reputationVM: ReputationViewModel
) {
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current
    val student = userVM.currentStudent

    var portfolioItems by remember { mutableStateOf<List<Triple<String, String, String>>>(emptyList()) }

    var showDialog by remember { mutableStateOf(false) }
    var selectedProjectTitle by remember { mutableStateOf("") }

    // Popup “Dejar Reseña”
    var showReviewDialog by remember { mutableStateOf(false) }
    var rating by remember { mutableStateOf(0f) }
    var comment by remember { mutableStateOf("") }

    LaunchedEffect(student) {
        if (student != null) {
            coroutine.launch {
                projectVM.loadProjects()
                val ended = student.endedProjects ?: emptyList()
                val allProjects = projectVM.projects.value

                val list = ended.mapNotNull { projectId ->
                    val project = allProjects.find { it.id == projectId } ?: return@mapNotNull null
                    val company = companyVM.companyRepository.getCompanyById(project.companyId ?: -1)
                    val companyName = company?.companyName ?: "Empresa desconocida"

                    Triple(
                        project.title ?: "Proyecto",
                        companyName,
                        calculateWeeksAgo(project.createdAt)
                    )
                }

                portfolioItems = list
            }
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, currentRoute = Routes.PORTFOLIO) },
        containerColor = Color(0xFFF5F0E8)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Text(
                text = "Portafolio",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(portfolioItems.size) { index ->
                    val item = portfolioItems[index]
                    PortfolioCard(
                        title = item.first,
                        companyName = item.second,
                        timeAgo = item.third,
                        onClick = {
                            selectedProjectTitle = item.first
                            showDialog = true
                        }
                    )
                }
            }
        }

        // ----------------------------------------------------
        // POPUP PRINCIPAL (Descargar & Dejar reseña)
        // ----------------------------------------------------
        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .padding(20.dp)
                ) {

                    Card(
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(Color.White)
                    ) {

                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = selectedProjectTitle,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(12.dp))

                            //-------------------------
                            // Descargar certificado
                            //-------------------------
                            Button(
                                onClick = {
                                    coroutine.launch {

                                        val studentName = userVM.currentUser?.name ?: "Estudiante"
                                        val project = projectVM.projects.value
                                            .find { it.title == selectedProjectTitle }
                                        val date = project?.createdAt ?: "2024-01-01"

                                        val success = PdfCertificateGenerator.generateCertificate(
                                            context = context,
                                            projectTitle = selectedProjectTitle,
                                            studentName = studentName,
                                            date = date
                                        )

                                        if (success) showDialog = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(Color(0xFFFFD479))
                            ) {
                                Text("Descargar Certificado", color = Color.White)
                            }

                            Spacer(Modifier.height(12.dp))

                            //-------------------------
                            // Dejar reseña
                            //-------------------------
                            Button(
                                onClick = {
                                    showDialog = false
                                    showReviewDialog = true
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(Color(0xFF1C1F2B))
                            ) {
                                Text("Dejar una reseña", color = Color.White)
                            }

                            Spacer(Modifier.height(18.dp))

                            TextButton(onClick = { showDialog = false }) {
                                Text("Cerrar", color = Color.Red)
                            }
                        }
                    }
                }
            }
        }

        // ----------------------------------------------------
        // POPUP PARA DEJAR RESEÑA
        // ----------------------------------------------------
        if (showReviewDialog) {
            Dialog(onDismissRequest = { showReviewDialog = false }) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .padding(20.dp)
                ) {

                    Card(
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(Color.White)
                    ) {

                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "Calificar Proyecto",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(14.dp))

                            Text("Calificación (0 a 5):")
                            Slider(
                                value = rating,
                                onValueChange = { rating = it },
                                valueRange = 0f..5f,
                                steps = 4
                            )

                            Spacer(Modifier.height(10.dp))

                            OutlinedTextField(
                                value = comment,
                                onValueChange = { comment = it },
                                label = { Text("Comentario") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    coroutine.launch {

                                        val studentId = student?.id ?: return@launch
                                        val project = projectVM.projects.value
                                            .find { it.title == selectedProjectTitle }
                                            ?: return@launch

                                        // 1️⃣ Guardar reputación
                                        reputationVM.createReputation(
                                            studentId = studentId,
                                            projectId = project.id,
                                            rating = rating.toDouble(),
                                            comment = comment,
                                            type = 2
                                        ) { success ->

                                            if (success) {
                                                coroutine.launch {

                                                    // 2️⃣ Obtener la empresa
                                                    val companyId = project.companyId ?: return@launch
                                                    val company = companyVM.companyRepository.getCompanyById(companyId)
                                                        ?: return@launch

                                                    // 3️⃣ Promediar el rating
                                                    val newAvg = (company.rating + rating) / 2

                                                    val updatedCompany = company.copy(
                                                        rating = newAvg
                                                    )

                                                    // 4️⃣ Guardar empresa actualizada
                                                    companyVM.updateCompany(updatedCompany) {}

                                                    showReviewDialog = false
                                                }
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(Color(0xFF1C1F2B))
                            ) {
                                Text("Guardar Reseña", color = Color.White)
                            }

                            Spacer(Modifier.height(15.dp))

                            TextButton(onClick = { showReviewDialog = false }) {
                                Text("Cancelar", color = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}
