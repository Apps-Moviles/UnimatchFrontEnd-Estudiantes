package com.example.unimatchfrontend.features.users.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unimatchfrontend.R
import com.example.unimatchfrontend.features.users.domain.model.User

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onLoginNavigate: () -> Unit,
    viewModel: UserViewModel
) {
    var role by remember { mutableStateOf("student") }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Para estudiantes
    var birthdate by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var career by remember { mutableStateOf("") }

    // Para empresas
    var companyName by remember { mutableStateOf("") }
    var sector by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    val user = viewModel.currentUser
    val error = viewModel.errorMessage
    val isLoading = viewModel.isLoading

    LaunchedEffect(user) {
        if (user != null) {
            onRegisterSuccess()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_unimatch_claro),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(140.dp)
                    .padding(bottom = 12.dp)
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                listOf("student" to "Estudiante", "company" to "Empresa").forEach { (value, label) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .selectable(
                                selected = (role == value),
                                onClick = { role = value }
                            )
                            .padding(end = 16.dp)
                    ) {
                        RadioButton(
                            selected = (role == value),
                            onClick = { role = value }
                        )
                        Text(text = label)
                    }
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            if (role == "student") {
                OutlinedTextField(
                    value = birthdate,
                    onValueChange = { birthdate = it },
                    label = { Text("Fecha de nacimiento") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("Ciudad") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp, top = 8.dp)
                    )
                    OutlinedTextField(
                        value = country,
                        onValueChange = { country = it },
                        label = { Text("País") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp, top = 8.dp)
                    )
                }

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Número de celular") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )

                OutlinedTextField(
                    value = career,
                    onValueChange = { career = it },
                    label = { Text("Carrera") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }

            if (role == "company") {
                OutlinedTextField(
                    value = companyName,
                    onValueChange = { companyName = it },
                    label = { Text("Nombre de la compañía") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = sector,
                        onValueChange = { sector = it },
                        label = { Text("Sector") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp, top = 8.dp)
                    )
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Ubicación") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp, top = 8.dp)
                    )
                }

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Número de celular") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val user = User(
                        id = null,
                        name = name,
                        email = email,
                        password = password,
                        role = role
                    )

                    if (role == "student") {
                        viewModel.registerStudentWithUser(
                            user = user,
                            birthdate = birthdate,
                            city = city,
                            country = country,
                            career = career,
                            phoneNumber = phone
                        )
                    }  else {
                        viewModel.registerCompanyWithUser(
                            user = user,
                            companyName = companyName,
                            sector = sector,
                            location = location,
                            phone = phone
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD479)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                Text("Register", color = Color.Black)
            }


            if (error != null) {
                Text(error, color = Color.Red, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))
            }

            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Ya tienes una cuenta. ")
                Text(
                    text = "Inicia Sesión",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000000),
                    modifier = Modifier.clickable { onLoginNavigate() }
                )
            }
        }
    }
}
