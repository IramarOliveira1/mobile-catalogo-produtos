package br.com.cairu.projeto.integrador.brecho.services;

import java.util.List;

import br.com.cairu.projeto.integrador.brecho.dtos.CategoryResponseDTO;
import br.com.cairu.projeto.integrador.brecho.dtos.HomeResponseDTO;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryService {
    @GET("/category/all")
    Call<List<CategoryResponseDTO>> categoryResponseDTO();
}

