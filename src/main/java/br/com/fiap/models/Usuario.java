package br.com.fiap.models;

/**
 * Classe que representa um usuário.
 */
public class Usuario {
    private String nomeCompleto;
    private String email;
    private String telefone;
    private int tamanhoEmpresa;
    private String pais;
    private String idioma;
    private String senha;

    // Construtor padrão
    public Usuario() {
    }

    // Construtor parametrizado
    public Usuario(String idioma, String pais, int tamanhoEmpresa, String telefone, String senha, String email, String nomeCompleto) {
        this.idioma = idioma;
        this.pais = pais;
        this.tamanhoEmpresa = tamanhoEmpresa;
        this.telefone = telefone;
        this.senha = senha;
        this.email = email;
        this.nomeCompleto = nomeCompleto;
    }

    // Representação em string do objeto Usuario
    @Override
    public String toString() {
        return "Usuario{" +
                "nomeCompleto='" + nomeCompleto + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", telefone='" + telefone + '\'' +
                ", tamanhoEmpresa=" + tamanhoEmpresa +
                ", pais='" + pais + '\'' +
                ", idioma='" + idioma + '\'' +
                '}';
    }

    // Getters e Setters
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getTamanhoEmpresa() {
        return tamanhoEmpresa;
    }

    public void setTamanhoEmpresa(int tamanhoEmpresa) {
        this.tamanhoEmpresa = tamanhoEmpresa;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }
}
