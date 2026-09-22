package matriculas.servico;

import matriculas.integracao.SistemaCobranca;
import matriculas.modelo.Aluno;
import matriculas.modelo.Disciplina;
import matriculas.modelo.Matricula;
import matriculas.repositorio.RepositorioCurriculo;
import matriculas.repositorio.RepositorioMatricula;
import matriculas.repositorio.RepositorioPeriodoMatricula;
import java.util.List;

public class ServicoMatricula {

    private final RepositorioMatricula repositorioMatricula;
    private final RepositorioCurriculo repositorioCurriculo;
    private final RepositorioPeriodoMatricula repositorioPeriodoMatricula;
    private final SistemaCobranca sistemaCobranca;

    public ServicoMatricula(RepositorioMatricula repositorioMatricula,
                            RepositorioCurriculo repositorioCurriculo,
                            RepositorioPeriodoMatricula repositorioPeriodoMatricula,
                            SistemaCobranca sistemaCobranca) {
        this.repositorioMatricula = repositorioMatricula;
        this.repositorioCurriculo = repositorioCurriculo;
        this.repositorioPeriodoMatricula = repositorioPeriodoMatricula;
        this.sistemaCobranca = sistemaCobranca;
    }

    public List<Disciplina> consultarDisciplinasOfertadas(Aluno aluno) {
        throw new UnsupportedOperationException("TODO: implementar consulta de disciplinas ofertadas");
    }

    public Matricula matricular(Aluno aluno, Disciplina disciplina) {
        throw new UnsupportedOperationException("TODO: implementar matrícula do aluno em disciplina");
    }

    public void cancelarMatricula(Aluno aluno, Disciplina disciplina) {
        throw new UnsupportedOperationException("TODO: implementar cancelamento de matrícula");
    }

    public List<Matricula> consultarMatriculas(Aluno aluno) {
        throw new UnsupportedOperationException("TODO: implementar consulta de matrículas do aluno");
    }

    private void validarPeriodoAberto() {
        throw new UnsupportedOperationException("TODO: implementar validação de período aberto");
    }

    private void validarDisponibilidadeDeVagas(Disciplina disciplina) {
        throw new UnsupportedOperationException("TODO: implementar validação de vagas disponíveis");
    }

    private void validarLimiteDeDisciplinas(Aluno aluno, Disciplina disciplina) {
        throw new UnsupportedOperationException("TODO: implementar validação de limite de disciplinas");
    }
}
