package com.example.apispractica.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.apispractica.data.PersonajeRepository
import com.example.apispractica.data.datasource.DogRemoteDataSourceImpl
import com.example.apispractica.data.datasource.SimpsonsRemoteDataSourceImpl
import com.example.apispractica.data.remote.RetrofitClient
import com.example.apispractica.model.PersonajeConPerro
import com.example.apispractica.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel que maneja el estado de la UI (MVVM) exponiendo un [StateFlow] de [PersonajeUiState],
 * permitiendo buscar y seleccionar elementos basados en las características combinadas del Modelo.
 */
class PersonajeViewModel(
    private val repository: PersonajeRepository = PersonajeRepository(
        simpsonsDataSource = SimpsonsRemoteDataSourceImpl(RetrofitClient.simpsonsApiService),
        dogDataSource = DogRemoteDataSourceImpl(RetrofitClient.dogApiService)
    )
) : ViewModel() {

    private val _uiState = MutableStateFlow<PersonajeUiState>(PersonajeUiState.Cargando)
    val uiState: StateFlow<PersonajeUiState> = _uiState.asStateFlow()

    private var todosLosPersonajes: List<PersonajeConPerro> = emptyList()

    init {
        cargarPersonajes()
    }

    fun cargarPersonajes() {
        viewModelScope.launch {
            _uiState.value = PersonajeUiState.Cargando
            when (val resultado = repository.getPersonajesConPerros()) {
                is Resultado.Exito -> {
                    todosLosPersonajes = resultado.data
                    _uiState.value = PersonajeUiState.Exito(
                        personajes = todosLosPersonajes,
                        personajesFiltrados = todosLosPersonajes
                    )
                }
                is Resultado.Error -> {
                    _uiState.value = PersonajeUiState.Error(resultado.mensaje)
                }
                is Resultado.Cargando -> {
                    _uiState.value = PersonajeUiState.Cargando
                }
            }
        }
    }

    fun onBuscarQueryChange(query: String) {
        val currentState = _uiState.value
        if (currentState is PersonajeUiState.Exito) {
            val filtrados = if (query.isBlank()) {
                todosLosPersonajes
            } else {
                todosLosPersonajes.filter { personaje ->
                    personaje.nombre.contains(query, ignoreCase = true) ||
                            personaje.ocupacion.contains(query, ignoreCase = true) ||
                            personaje.razaPerro.contains(query, ignoreCase = true) ||
                            personaje.frasePrincipal.contains(query, ignoreCase = true)
                }
            }
            _uiState.value = currentState.copy(
                searchQuery = query,
                personajesFiltrados = filtrados
            )
        }
    }

    fun seleccionarPersonaje(personaje: PersonajeConPerro?) {
        val currentState = _uiState.value
        if (currentState is PersonajeUiState.Exito) {
            _uiState.value = currentState.copy(personajeSeleccionado = personaje)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val repository: PersonajeRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PersonajeViewModel(repository) as T
        }
    }
}
