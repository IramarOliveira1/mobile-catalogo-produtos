package br.com.cairu.projeto.integrador.brecho.services;

import java.util.List;

import br.com.cairu.projeto.integrador.brecho.dtos.CategoryRequestDTO;
import br.com.cairu.projeto.integrador.brecho.dtos.CategoryResponseDTO;
import br.com.cairu.projeto.integrador.brecho.dtos.MessageResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CategoryService {
    @GET("/category/all")
    Call<List<CategoryResponseDTO>> categoryResponseDTO();

    @POST("/category/register")
    Call<MessageResponse> register(@Body CategoryRequestDTO name);
}

