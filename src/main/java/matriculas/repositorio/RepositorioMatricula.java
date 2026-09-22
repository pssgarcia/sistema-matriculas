package matriculas.repositorio;

import matriculas.modelo.Aluno;
import matriculas.modelo.Disciplina;
import matriculas.modelo.Matricula;
import java.util.List;
import java.util.Optional;

public interface RepositorioMatricula extends Repositorio<Matricula, Long> {

    List<Matricula> buscarPorAluno(Aluno aluno);

    List<Matricula> buscarPorDisciplina(Disciplina disciplina);

    Optional<Matricula> buscarPorAlunoEDisciplina(Aluno aluno, Disciplina disciplina);
}
