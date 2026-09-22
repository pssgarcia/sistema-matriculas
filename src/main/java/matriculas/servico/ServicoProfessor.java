package matriculas.servico;

import matriculas.modelo.Aluno;
import matriculas.modelo.Disciplina;
import matriculas.modelo.Professor;
import matriculas.repositorio.RepositorioDisciplina;
import matriculas.repositorio.RepositorioMatricula;
import java.util.List;

public class ServicoProfessor {

    private final RepositorioDisciplina repositorioDisciplina;
    private final RepositorioMatricula repositorioMatricula;

    public ServicoProfessor(RepositorioDisciplina repositorioDisciplina,
                            RepositorioMatricula repositorioMatricula) {
        this.repositorioDisciplina = repositorioDisciplina;
        this.repositorioMatricula = repositorioMatricula;
    }

    public List<Disciplina> consultarDisciplinas(Professor professor) {
        throw new UnsupportedOperationException("TODO: implementar consulta de disciplinas do professor");
    }

    public List<Aluno> consultarAlunosMatriculados(Professor professor, Disciplina disciplina) {
        throw new UnsupportedOperationException("TODO: implementar consulta de alunos matriculados");
    }
}
