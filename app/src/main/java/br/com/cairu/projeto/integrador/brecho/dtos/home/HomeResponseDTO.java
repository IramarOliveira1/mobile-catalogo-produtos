package br.com.cairu.projeto.integrador.brecho.dtos.home;

import br.com.cairu.projeto.integrador.brecho.models.File;

public class HomeResponseDTO {

    private String name;

    private int countClick;

    private File file;

    private Long totalProduct;

    private Long totalCategory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountClick() {
        return countClick;
    }

    public void setCountClick(int countClick) {
        this.countClick = countClick;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Long getTotalProduct() {
        return totalProduct;
    }

    public void setTotalProduct(Long totalProduct) {
        this.totalProduct = totalProduct;
    }

    public Long getTotalCategory() {
        return totalCategory;
    }

    public void setTotalCategory(Long totalCategory) {
        this.totalCategory = totalCategory;
    }
}
