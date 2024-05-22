package br.com.cairu.projeto.integrador.brecho.dtos.user;

public class UserRequestDTO {

    private String name;
    private String email;
    private String cpf;
    private String password;
    private boolean isAdmin;
    private String phone;

    public UserRequestDTO(String name, String email, String cpf, String password, boolean isAdmin, String phone) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.password = password;
        this.isAdmin = isAdmin;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
