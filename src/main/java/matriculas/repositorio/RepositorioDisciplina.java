package matriculas.repositorio;

import matriculas.modelo.Curso;
import matriculas.modelo.Disciplina;
import matriculas.modelo.Professor;
import java.util.List;
import java.util.Optional;

public interface RepositorioDisciplina extends Repositorio<Disciplina, Long> {

    Optional<Disciplina> buscarPorCodigo(String codigo);

    List<Disciplina> buscarPorProfessor(Professor professor);

    List<Disciplina> buscarPorCurso(Curso curso);
}
