package matriculas.modelo;

import java.util.Objects;

public abstract class Usuario {

    private Long id;
    private String nome;
    private String login;
    private String senha;

    protected Usuario() {
    }

    protected Usuario(Long id, String nome, String login, String senha) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }

    public boolean autenticar(String senha) {
        throw new UnsupportedOperationException("TODO: implementar autenticação do usuário");
    }

    public void alterarSenha(String senhaAtual, String novaSenha) {
        throw new UnsupportedOperationException("TODO: implementar alteração de senha");
    }

    public abstract String getPerfil();

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    protected String getSenha() {
        return senha;
    }

    protected void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof Usuario)) {
            return false;
        }
        Usuario usuario = (Usuario) outro;
        return Objects.equals(login, usuario.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
