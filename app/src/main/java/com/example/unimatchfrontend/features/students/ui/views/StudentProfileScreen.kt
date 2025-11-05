package com.example.unimatchfrontend.features.students.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.unimatchfrontend.features.users.ui.UserViewModel
import com.example.unimatchfrontend.navigation.Routes
import com.example.unimatchfrontend.shared.ui.BottomNavigationBar

@Composable
fun StudentProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {
    val student = userViewModel.currentStudent
    val user = userViewModel.currentUser

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, currentRoute = Routes.PROFILE) },
        containerColor = Color(0xFFF5F0E8)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mi Perfil",
                fontSize = 24.sp,
                color = Color(0xFF222222),
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Imagen de perfil con imagen por defecto si está vacía
            Image(
                painter = rememberImagePainter(
                    data = student?.profilePicture?.ifBlank {
                        "https://cdn-icons-png.flaticon.com/512/4086/4086679.png"
                    }
                ),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre y rating
            Text(user?.name ?: "", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = "Estrella", tint = Color(0xFFFDB813))
                Text(
                    text = "${student?.rating ?: 0.0}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.padding(top = 12.dp)
            ) {
                ProfileDetail(label = "Fecha de Nacimiento", value = student?.birthdate ?: "-")
                ProfileDetail(label = "Ciudad/País", value = "${student?.city ?: "-"}, ${student?.country ?: "-"}")
                ProfileDetail(label = "Carrera", value = student?.career ?: "-")
                ProfileDetail(label = "Email", value = user?.email ?: "-")
                ProfileDetail(label = "Celular", value = student?.phoneNumber ?: "-")
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = { /* TODO: Navegar a editar perfil */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD479),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Modificar perfil")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { /* TODO: Cerrar sesión */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF222222)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Cerrar Sesión", color = Color.White)
            }
        }
    }
}

@Composable
fun ProfileDetail(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
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
