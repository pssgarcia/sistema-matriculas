package matriculas.repositorio;

import matriculas.modelo.Usuario;
import java.util.Optional;

public interface RepositorioUsuario extends Repositorio<Usuario, Long> {

    Optional<Usuario> buscarPorLogin(String login);
}
