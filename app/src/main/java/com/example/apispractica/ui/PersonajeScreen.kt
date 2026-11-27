package com.example.apispractica.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.apispractica.model.PersonajeConPerro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonajeScreen(
    modifier: Modifier = Modifier,
    viewModel: PersonajeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Apis Combinadas",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            when (val state = uiState) {
                is PersonajeUiState.Cargando -> PantallaCargando()
                is PersonajeUiState.Exito -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        BarraBusqueda(
                            query = state.searchQuery,
                            onQueryChange = { viewModel.onBuscarQueryChange(it) }
                        )

                        ListaPersonajes(
                            personajes = state.personajesFiltrados,
                            onPersonajeClick = { viewModel.seleccionarPersonaje(it) }
                        )

                        // Diálogo con la Ficha Técnica del Modelo Combinado si hay un personaje seleccionado
                        state.personajeSeleccionado?.let { seleccionado ->
                            DetallePersonajeDialog(
                                personaje = seleccionado,
                                onDismiss = { viewModel.seleccionarPersonaje(null) }
                            )
                        }
                    }
                }
                is PersonajeUiState.Error -> PantallaError(
                    mensaje = state.mensaje,
                    onReintentar = { viewModel.cargarPersonajes() }
                )
            }
        }
    }
}

@Composable
fun BarraBusqueda(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = { Text("🔍 Buscar por nombre, ocupación o perro...") },
        trailingIcon = {
            if (query.isNotEmpty()) {
                TextButton(onClick = { onQueryChange("") }) {
                    Text("✖")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun PantallaCargando() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Cargando datos de Simpsons y Perros...",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun PantallaError(mensaje: String, onReintentar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¡Upps! Ha ocurrido un error",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onReintentar) {
            Text(text = "Reintentar")
        }
    }
}

@Composable
fun ListaPersonajes(
    personajes: List<PersonajeConPerro>,
    onPersonajeClick: (PersonajeConPerro) -> Unit
) {
    if (personajes.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No se encontraron coincidencia para la búsqueda",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(personajes, key = { it.id }) { personaje ->
                ItemPersonajeCard(
                    personaje = personaje,
                    onClick = { onPersonajeClick(personaje) }
                )
            }
        }
    }
}

@Composable
fun ItemPersonajeCard(
    personaje: PersonajeConPerro,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Chip superior mostrando la unión del Dúo (Simpsons + Dog)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                    .padding(vertical = 6.dp, horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = personaje.tituloDuo,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Fila con Imágenes combinadas de las 2 APIs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Imagen API 1: Los Simpsons (Servida desde CDN)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = personaje.imagenSimpsonsUrl,
                        contentDescription = "Portrait CDN de ${personaje.nombre}",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Simpsons ",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Imagen API 2: Dog CEO
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = personaje.imagenPerroUrl,
                        contentDescription = "Perro ${personaje.razaPerro}",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = " (${personaje.razaPerro})",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre del personaje
            Text(
                text = personaje.nombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Ocupación y Edad
            Text(
                text = "Ocupación: ${personaje.ocupacion}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Edad: ${personaje.edad}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Estado y Raza Chip
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val estadoColor = if (personaje.estado.equals("Alive", ignoreCase = true)) Color(0xFF2E7D32) else Color(0xFFC62828)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(estadoColor.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Estado: ${personaje.estado}",
                        color = estadoColor,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "🐶 ${personaje.razaPerro}",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Frase Célebre seleccionada por el Mapeador
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(12.dp)
            ) {
                Text(
                    text = "\"${personaje.frasePrincipal}\"",
                    style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/**
 * Diálogo de Detalle que expone de forma clara la Ficha Técnica Unificada del Modelo
 */
@Composable
fun DetallePersonajeDialog(
    personaje: PersonajeConPerro,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        },
        title = {
            Text(
                text = "Ficha del Modelo Unificado",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    AsyncImage(
                        model = personaje.imagenSimpsonsUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    AsyncImage(
                        model = personaje.imagenPerroUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = personaje.tituloDuo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "👤 Nombre: ${personaje.nombre}")
                Text(text = "🎂 Edad: ${personaje.edad}")
                Text(text = "💼 Ocupación: ${personaje.ocupacion}")
                Text(text = "💚 Estado: ${personaje.estado}")
                Text(text = "🐶 Raza Perro : ${personaje.razaPerro}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "💬 Frase: \"${personaje.frasePrincipal}\"",
                    fontStyle = FontStyle.Italic
                )
            }
        }
    )
}
