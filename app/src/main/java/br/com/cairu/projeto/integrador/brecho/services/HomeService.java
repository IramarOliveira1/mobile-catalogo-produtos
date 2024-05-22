package br.com.cairu.projeto.integrador.brecho.services;

import java.util.List;

import br.com.cairu.projeto.integrador.brecho.dtos.home.HomeResponseDTO;
import retrofit2.Call;
import retrofit2.http.GET;

public interface HomeService {

    @GET("/product/home/all")
    Call<List<HomeResponseDTO>> homeResponseDTO();
}
