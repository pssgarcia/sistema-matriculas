package matriculas.repositorio;

import matriculas.modelo.Curriculo;
import matriculas.modelo.Curso;
import java.util.List;
import java.util.Optional;

public interface RepositorioCurriculo extends Repositorio<Curriculo, Long> {

    List<Curriculo> buscarPorSemestre(String semestre);

    Optional<Curriculo> buscarPorCursoESemestre(Curso curso, String semestre);
}
