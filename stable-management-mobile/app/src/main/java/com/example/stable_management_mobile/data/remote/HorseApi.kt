package com.example.stable_management_mobile.data.remote

import com.example.stable_management_mobile.data.remote.dto.HorseRequestDTO
import com.example.stable_management_mobile.data.remote.dto.HorseResponseDTO
import com.example.stable_management_mobile.data.remote.dto.RatingRequestDTO
import com.example.stable_management_mobile.data.remote.dto.RatingResponseDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface HorseApi {

    @GET("api/horses/{horseId}")
    suspend fun getHorseById(@Path("horseId") horseId: Int): HorseResponseDTO

    @GET("api/horses/{horseId}/ratings")
    suspend fun getHorseRatings(@Path("horseId") horseId: Int): List<RatingResponseDTO>

    @POST("api/horses")
    suspend fun addHorse(@Body horse: HorseRequestDTO): HorseResponseDTO

    @POST("api/horses/{horseId}/ratings")
    suspend fun addRating(@Body rating: RatingRequestDTO,
                          @Path("horseId") horseId: Int): RatingResponseDTO

    @PUT("api/horses/{horseId}")
    suspend fun updateHorse(@Path("horseId") horseId: Int,
        @Body horseRequestDTO: HorseRequestDTO,): HorseResponseDTO

    @PUT("api/horses/{horseId}/ratings/{ratingId}")
    suspend fun updateHorseRating(@Path("horseId") horseId: Int,
                                  @Path("ratingId") ratingId: Int,
                                  @Body ratingRequestDTO: RatingRequestDTO): RatingResponseDTO

    @DELETE("api/horses/{horseId}")
    suspend fun deleteHorse(@Path("horseId") horseId: Int): Unit

    @DELETE("api/horses/{horseId}/ratings/{ratingId}")
    suspend fun deleteRating(@Path("horseId") horseId: Int,
                             @Path("ratingId") ratingId: Int): Unit
}