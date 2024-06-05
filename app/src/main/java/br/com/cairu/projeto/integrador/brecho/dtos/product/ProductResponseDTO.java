package br.com.cairu.projeto.integrador.brecho.dtos.product;

import java.util.ArrayList;

import br.com.cairu.projeto.integrador.brecho.dtos.category.CategoryResponseDTO;
import br.com.cairu.projeto.integrador.brecho.models.Category;
import br.com.cairu.projeto.integrador.brecho.models.File;

public class ProductResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String price;
    private boolean isActive;
    private Category category;
    private ArrayList<File> files;
    private int countClick;

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

    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getCountClick() {
        return countClick;
    }

    public void setCountClick(int countClick) {
        this.countClick = countClick;
    }
}
