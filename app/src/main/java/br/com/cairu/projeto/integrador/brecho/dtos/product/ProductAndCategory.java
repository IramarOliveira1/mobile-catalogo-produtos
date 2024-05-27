package br.com.cairu.projeto.integrador.brecho.dtos.product;

import java.util.Collection;
import java.util.List;

import br.com.cairu.projeto.integrador.brecho.models.Category;
import br.com.cairu.projeto.integrador.brecho.models.Product;

public class ProductAndCategory {
    private List<ProductResponseDTO> products;
    private List<Category> categories;

    public List<ProductResponseDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponseDTO> products) {
        this.products = products;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}