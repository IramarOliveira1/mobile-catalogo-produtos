package br.com.cairu.projeto.integrador.brecho.dtos.product;

import java.util.ArrayList;

import br.com.cairu.projeto.integrador.brecho.dtos.category.CategoryResponseDTO;
import br.com.cairu.projeto.integrador.brecho.models.File;

public class ProductResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String price;
    private boolean isActive;
    private CategoryResponseDTO categoryResponseDTO;
    private ArrayList<File> files;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public CategoryResponseDTO getCategoryResponseDTO() {
        return categoryResponseDTO;
    }

    public void setCategoryResponseDTO(CategoryResponseDTO categoryResponseDTO) {
        this.categoryResponseDTO = categoryResponseDTO;
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }
}
