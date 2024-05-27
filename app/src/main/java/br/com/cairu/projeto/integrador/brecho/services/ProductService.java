package br.com.cairu.projeto.integrador.brecho.services;

import java.util.List;

import br.com.cairu.projeto.integrador.brecho.dtos.generic.MessageResponse;
import br.com.cairu.projeto.integrador.brecho.dtos.product.ProductAndCategory;
import br.com.cairu.projeto.integrador.brecho.dtos.product.ProductResponseDTO;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductService {

    @GET("/product/all")
    Call<ProductAndCategory> all();

    @GET("/product/category/{id}")
    Call<List<ProductResponseDTO>> filterPerCategory(@Path("id") Long id);

    @DELETE("/product/{id}")
    Call<MessageResponse> delete(@Path("id") Long id);
}
