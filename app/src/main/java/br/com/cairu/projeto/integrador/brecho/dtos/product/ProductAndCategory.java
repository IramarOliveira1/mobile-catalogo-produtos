package br.com.cairu.projeto.integrador.brecho.dtos.product;

import java.util.Collection;
import java.util.List;

import br.com.cairu.projeto.integrador.brecho.dtos.category.CategoryResponseDTO;
import br.com.cairu.projeto.integrador.brecho.models.Category;
import br.com.cairu.projeto.integrador.brecho.models.File;
import br.com.cairu.projeto.integrador.brecho.models.Product;

public class ProductAndCategory {
    private List<ProductResponseDTO> products;
    private List<File> files;
    private List<CategoryResponseDTO> categories;

    public List<CategoryResponseDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryResponseDTO> categories) {
        this.categories = categories;
    }

    private Product product;

    public List<ProductResponseDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponseDTO> products) {
        this.products = products;
    }


    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
