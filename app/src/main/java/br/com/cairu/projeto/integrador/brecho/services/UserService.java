package br.com.cairu.projeto.integrador.brecho.services;

import java.util.List;

import br.com.cairu.projeto.integrador.brecho.dtos.generic.MessageResponse;
import br.com.cairu.projeto.integrador.brecho.dtos.user.UserRequestDTO;
import br.com.cairu.projeto.integrador.brecho.dtos.user.UserResponseDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    @GET("/user/all")
    Call<List<UserResponseDTO>> all();

    @POST("/user/register")
    Call<MessageResponse> register(@Body UserRequestDTO data);

    @DELETE("/user/{id}")
    Call<MessageResponse> delete(@Path("id") Long id);

    @GET("/user/{id}")
    Call<UserResponseDTO> index(@Path("id") Long id);

    @PUT("/user/{id}")
    Call<MessageResponse> update(@Path("id") Long id, @Body UserRequestDTO data);
}

