package br.com.cairu.projeto.integrador.brecho.services;

import java.util.List;

import br.com.cairu.projeto.integrador.brecho.dtos.category.CategoryRequestDTO;
import br.com.cairu.projeto.integrador.brecho.dtos.category.CategoryResponseDTO;
import br.com.cairu.projeto.integrador.brecho.dtos.generic.MessageResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CategoryService {
    @GET("/category/all")
    Call<List<CategoryResponseDTO>> all();

    @POST("/category/register")
    Call<MessageResponse> register(@Body CategoryRequestDTO name);

    @DELETE("/category/{id}")
    Call<MessageResponse> delete(@Path("id") Long id);

    @GET("/category/{id}")
    Call<CategoryResponseDTO> index(@Path("id") Long id);

    @PUT("/category/{id}")
    Call<MessageResponse> update(@Path("id") Long id, @Body CategoryRequestDTO categoryRequestDTO);
}

