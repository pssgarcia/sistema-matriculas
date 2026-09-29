package matriculas.servico;

import matriculas.excecao.AutenticacaoException;
import matriculas.modelo.Usuario;
import matriculas.repositorio.RepositorioUsuario;
import java.util.Optional;

public class ServicoAutenticacao {

    private final RepositorioUsuario repositorioUsuario;
    private Usuario usuarioLogado;

    public ServicoAutenticacao(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    public Usuario autenticar(String login, String senha) {
        Usuario usuario = repositorioUsuario.buscarPorLogin(login)
                .filter(encontrado -> encontrado.autenticar(senha))
                .orElseThrow(() -> new AutenticacaoException("Login ou senha inválidos."));
        usuarioLogado = usuario;
        return usuario;
    }

    public void encerrarSessao(Usuario usuario) {
        if (usuarioLogado == null || !usuarioLogado.equals(usuario)) {
            throw new AutenticacaoException("Não há sessão ativa para o usuário informado.");
        }
        usuarioLogado = null;
    }

    public void alterarSenha(Usuario usuario, String senhaAtual, String novaSenha) {
        try {
            usuario.alterarSenha(senhaAtual, novaSenha);
        } catch (IllegalArgumentException e) {
            throw new AutenticacaoException(e.getMessage());
        }
        repositorioUsuario.salvar(usuario);
    }

    public Optional<Usuario> getUsuarioLogado() {
        return Optional.ofNullable(usuarioLogado);
    }
}
