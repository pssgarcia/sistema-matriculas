package matriculas.servico;

import matriculas.excecao.EntidadeNaoEncontradaException;
import matriculas.modelo.Aluno;
import matriculas.modelo.Disciplina;
import matriculas.modelo.Matricula;
import matriculas.modelo.Professor;
import matriculas.repositorio.RepositorioDisciplina;
import matriculas.repositorio.RepositorioMatricula;
import java.util.Comparator;
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
        return repositorioDisciplina.buscarPorProfessor(professor);
    }

    public List<Aluno> consultarAlunosMatriculados(Professor professor, Disciplina disciplina) {
        if (!professor.lecionaDisciplina(disciplina)) {
            throw new EntidadeNaoEncontradaException(
                    "A disciplina " + disciplina.getCodigo() + " não está entre as suas disciplinas.");
        }
        return repositorioMatricula.buscarPorDisciplina(disciplina).stream()
                .filter(Matricula::estaAtiva)
                .map(Matricula::getAluno)
                .sorted(Comparator.comparing(Aluno::getNome, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }
}
