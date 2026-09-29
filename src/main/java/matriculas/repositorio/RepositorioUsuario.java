package matriculas.repositorio;

import matriculas.modelo.Usuario;
import java.util.List;
import java.util.Optional;

public interface RepositorioUsuario extends Repositorio<Usuario, Long> {

    Optional<Usuario> buscarPorLogin(String login);

    <U extends Usuario> List<U> buscarPorPerfil(Class<U> perfil);
}
