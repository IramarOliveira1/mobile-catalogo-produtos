package br.com.cairu.projeto.integrador.brecho.dtos.category;

import androidx.annotation.NonNull;

public class CategoryResponseDTO {
    private Long id;

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    private String name;

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
}
