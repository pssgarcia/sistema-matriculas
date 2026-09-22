package matriculas.servico;

import matriculas.modelo.Usuario;
import matriculas.repositorio.RepositorioUsuario;

public class ServicoAutenticacao {

    private final RepositorioUsuario repositorioUsuario;

    public ServicoAutenticacao(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    public Usuario autenticar(String login, String senha) {
        throw new UnsupportedOperationException("TODO: implementar autenticação de usuário");
    }

    public void encerrarSessao(Usuario usuario) {
        throw new UnsupportedOperationException("TODO: implementar encerramento de sessão");
    }
}
