package br.com.cairu.projeto.integrador.brecho.dtos.home;

public class TotalCountsDTO {

    private long totalProducts;

    private long totalCategories;

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public long getTotalCategories() {
        return totalCategories;
    }

    public void setTotalCategories(long totalCategories) {
        this.totalCategories = totalCategories;
    }
}
