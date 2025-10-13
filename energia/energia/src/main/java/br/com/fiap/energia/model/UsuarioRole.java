package br.com.fiap.energia.model;

public enum UsuarioRole {

    ADMIN("admin"),
    MANAGER("manager"),
    USER("user");

    private String role;

    UsuarioRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }

}
