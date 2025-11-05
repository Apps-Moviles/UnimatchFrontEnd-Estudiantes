package com.example.unimatchfrontend.features.students.ui.views

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import coil.compose.rememberImagePainter
import com.example.unimatchfrontend.features.students.domain.model.Student
import com.example.unimatchfrontend.features.students.ui.StudentViewModel
import com.example.unimatchfrontend.features.users.domain.model.User
import com.example.unimatchfrontend.features.users.ui.UserViewModel
import com.example.unimatchfrontend.navigation.Routes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditStudentProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    studentViewModel: StudentViewModel
) {
    val student = userViewModel.currentStudent
    val user = userViewModel.currentUser
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf(user?.name ?: "") }
    var birthdate by remember { mutableStateOf(student?.birthdate ?: "") }
    var city by remember { mutableStateOf(student?.city ?: "") }
    var country by remember { mutableStateOf(student?.country ?: "") }
    var career by remember { mutableStateOf(student?.career ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var phone by remember { mutableStateOf(student?.phoneNumber ?: "") }
    var profilePicture by remember { mutableStateOf(student?.profilePicture ?: "") }

    // Lanzador de imagen
    val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { profilePicture = it.toString() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar mi perfil", fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF5F0E8))
            )
        },
        containerColor = Color(0xFFF5F0E8)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Botón Subir Foto
            Button(
                onClick = { imageLauncher.launch("image/*") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD479)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Subir foto", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Formulario
            CustomTextField("Nombre", name) { name = it }
            CustomTextField("Fecha de Nacimiento", birthdate) { birthdate = it }
            CustomTextField("Ciudad/País", "$city, $country") {
                val parts = it.split(",").map { part -> part.trim() }
                city = parts.getOrNull(0) ?: ""
                country = parts.getOrNull(1) ?: ""
            }
            CustomTextField("Carrera", career) { career = it }
            CustomTextField("Email", email) { email = it }
            CustomTextField("Celular", phone) { phone = it }

            Spacer(modifier = Modifier.height(24.dp))

            // Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { navController.navigate(Routes.PROFILE) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222222)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Cancelar", color = Color.White)
                }

                Button(
                    onClick = {
                        scope.launch {
                            val updatedStudent = student?.copy(
                                birthdate = birthdate,
                                city = city,
                                country = country,
                                career = career,
                                phoneNumber = phone,
                                profilePicture = profilePicture
                            )
                            val updatedUser = user?.copy(name = name, email = email)
                            if (updatedStudent != null && updatedUser != null) {
                                studentViewModel.updateStudent(updatedStudent)
                                userViewModel.updateUser(updatedUser)


                                updatedStudent.userId?.let { userId ->
                                    val refreshedStudent = studentViewModel.getStudentByUserId(userId)
                                    if (refreshedStudent != null) {
                                        userViewModel.currentStudent = refreshedStudent
                                    }
                                }

                                navController.navigate(Routes.PROFILE)
                            }

                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDB813)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Guardar cambios")
                }
            }
        }
    }
}

@Composable
fun CustomTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text = "$label:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(6.dp)
        )
    }
}
