package matriculas.modelo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Objects;

public abstract class Usuario {

    private Long id;
    private String nome;
    private String login;
    private String hashSenha;

    protected Usuario() {
    }

    protected Usuario(Long id, String nome, String login, String senha) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.hashSenha = gerarHash(senha);
    }

    public boolean autenticar(String senha) {
        return senha != null && hashSenha != null && hashSenha.equals(gerarHash(senha));
    }

    public void alterarSenha(String senhaAtual, String novaSenha) {
        if (!autenticar(senhaAtual)) {
            throw new IllegalArgumentException("Senha atual incorreta.");
        }
        if (novaSenha == null || novaSenha.isBlank()) {
            throw new IllegalArgumentException("A nova senha não pode ser vazia.");
        }
        this.hashSenha = gerarHash(novaSenha);
    }

    public abstract String getPerfil();

    private static String gerarHash(String senha) {
        if (senha == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(senha.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algoritmo SHA-256 indisponível", e);
        }
    }

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

    public String getHashSenha() {
        return hashSenha;
    }

    public void setHashSenha(String hashSenha) {
        this.hashSenha = hashSenha;
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
