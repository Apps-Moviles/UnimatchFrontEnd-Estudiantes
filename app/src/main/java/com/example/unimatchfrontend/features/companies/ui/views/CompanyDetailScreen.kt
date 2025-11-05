package com.example.unimatchfrontend.features.companies.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.unimatchfrontend.features.companies.domain.model.Company
import com.example.unimatchfrontend.features.companies.ui.CompanyViewModel
import com.example.unimatchfrontend.features.users.ui.UserViewModel
import com.example.unimatchfrontend.shared.ui.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyDetailScreen(
    navController: NavController,
    companyId: Int,
    companyViewModel: CompanyViewModel,
    userViewModel: UserViewModel
) {
    var company by remember { mutableStateOf<Company?>(null) }
    var managerName by remember { mutableStateOf<String>("Cargando...") }

    LaunchedEffect(companyId) {
        println("🔍 Buscando empresa con ID: $companyId")
        val result = companyViewModel.getCompanyByCompanyId(companyId)
        if (result != null) {
            println("✅ Empresa encontrada: ${result.companyName}")
            company = result

            // Obtener nombre del encargado
            val user = userViewModel.getUserById(result.userId)
            managerName = user?.name ?: "No especificado"
        } else {
            println("❌ Empresa no encontrada para ID: $companyId")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles de la Empresa") },
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
            BottomNavigationBar(navController, currentRoute = "")
        },
        containerColor = Color(0xFFF5F0E8)
    ) { innerPadding ->
        company?.let { companyData ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 32.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo de la empresa
                Image(
                    painter = rememberImagePainter(
                        data = companyData.profilePicture.ifBlank {
                            "https://cdn-icons-png.flaticon.com/512/4086/4086679.png"
                        }
                    ),
                    contentDescription = "Logo de la empresa",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Nombre y rating
                Text(companyData.companyName, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text("⭐ ${companyData.rating}", style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(24.dp))

                // Campos espaciados uniformemente
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    InfoText("Encargado", managerName)
                    InfoText("Ciudad/País", companyData.location)
                    InfoText("Enfoque", companyData.sector)
                    InfoText("Email", companyData.email)
                    InfoText("Celular", companyData.phone)
                }

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = { /* Ver Reseñas */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD479),
                        contentColor = Color.Black
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Ver Reseñas")
                }
            }
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun InfoText(label: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}



