package br.com.cairu.projeto.integrador.brecho.dtos;

public class CategoryRequestDTO {
    private String name;

    public CategoryRequestDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
