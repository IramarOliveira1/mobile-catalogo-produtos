package br.com.cairu.projeto.integrador.brecho.services;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import br.com.cairu.projeto.integrador.brecho.dtos.generic.MessageResponse;
import br.com.cairu.projeto.integrador.brecho.dtos.product.ProductAndCategory;
import br.com.cairu.projeto.integrador.brecho.dtos.product.ProductRequestDTO;
import br.com.cairu.projeto.integrador.brecho.dtos.product.ProductResponseDTO;
import br.com.cairu.projeto.integrador.brecho.dtos.user.UserResponseDTO;
import br.com.cairu.projeto.integrador.brecho.models.File;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductService {

    @GET("/product/all")
    Call<ProductAndCategory> all();

    @GET("/product/category/{id}")
    Call<List<ProductResponseDTO>> filterPerCategory(@Path("id") Long id);

    @DELETE("/product/{id}")
    Call<MessageResponse> delete(@Path("id") Long id);

    @Multipart
    @POST("/product/register")
    Call<MessageResponse> register(@Part("data") ProductRequestDTO data, @Part ArrayList<MultipartBody.Part> images);

    @Multipart
    @PUT("/product/{id}")
    Call<MessageResponse> update(@Path("id") Long id, @Part("data") ProductRequestDTO data, @Part ArrayList<MultipartBody.Part> images, @Part("urls") List<File> urls);

    @GET("/product/{id}")
    Call<ProductResponseDTO> index(@Path("id") Long id, @Query("detail") String detail);
}
