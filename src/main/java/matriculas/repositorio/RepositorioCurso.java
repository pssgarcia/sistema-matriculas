package matriculas.repositorio;

import matriculas.modelo.Curso;
import java.util.Optional;

public interface RepositorioCurso extends Repositorio<Curso, Long> {

    Optional<Curso> buscarPorCodigo(String codigo);
}
