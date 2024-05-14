package br.com.cairu.projeto.integrador.brecho.models;

public class Category {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    private Long id;

    private String nome;
}
