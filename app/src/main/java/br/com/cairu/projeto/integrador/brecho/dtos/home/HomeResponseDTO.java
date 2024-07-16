package br.com.cairu.projeto.integrador.brecho.dtos.home;

import java.util.List;

import br.com.cairu.projeto.integrador.brecho.models.Product;

public class HomeResponseDTO {

    private List<Product> products;

    private TotalCountsDTO counts;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public TotalCountsDTO getCounts() {
        return counts;
    }

    public void setCounts(TotalCountsDTO counts) {
        this.counts = counts;
    }
}
