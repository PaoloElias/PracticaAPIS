package com.example.apispractica.data

import com.example.apispractica.data.datasource.DogRemoteDataSource
import com.example.apispractica.data.datasource.SimpsonsRemoteDataSource
import com.example.apispractica.model.PersonajeConPerro
import com.example.apispractica.util.Resultado
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * Repositorio que orquesta el llamado a ambos DataSources (Simpsons y Dog API),
 * combinando las respuestas a través del Mapeador en una lista del modelo único de dominio [PersonajeConPerro].
 */
class PersonajeRepository(
    private val simpsonsDataSource: SimpsonsRemoteDataSource,
    private val dogDataSource: DogRemoteDataSource
) {
    suspend fun getPersonajesConPerros(page: Int = 1): Resultado<List<PersonajeConPerro>> {
        return try {
            coroutineScope {
                // 1. Obtener lista de DTOs de los Simpsons desde el DataSource correspondiente
                val simpsonsDtos = simpsonsDataSource.getCharacters(page)

                // 2. Obtener imágenes aleatorias de perros desde el DataSource de perros en paralelo
                val dogTasks = simpsonsDtos.map {
                    async {
                        try {
                            dogDataSource.getRandomDogImage()
                        } catch (_: Exception) {
                            null
                        }
                    }
                }
                val dogDtos = dogTasks.awaitAll().filterNotNull()

                // 3. El Mapeador elige las características de ambos DTOs y construye el Modelo único de dominio
                val modelosCombinados = simpsonsDtos.toDomainList(dogDtos)

                Resultado.Exito(modelosCombinados)
            }
        } catch (e: Exception) {
            Resultado.Error("Error al obtener los datos de las APIs: ${e.localizedMessage}", e)
        }
    }
}
