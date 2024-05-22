package br.com.cairu.projeto.integrador.brecho.services;


import br.com.cairu.projeto.integrador.brecho.dtos.login.LoginRequestDTO;
import br.com.cairu.projeto.integrador.brecho.dtos.login.LoginResponseDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {

    @POST("/user/login")
    Call<LoginResponseDTO> login(@Body LoginRequestDTO loginRequestDTO);
}
