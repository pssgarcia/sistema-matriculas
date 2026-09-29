package matriculas.servico;

import matriculas.excecao.DisciplinaIndisponivelException;
import matriculas.excecao.EntidadeNaoEncontradaException;
import matriculas.excecao.LimiteDisciplinasExcedidoException;
import matriculas.excecao.MatriculaDuplicadaException;
import matriculas.excecao.PeriodoFechadoException;
import matriculas.excecao.SemVagaDisponivelException;
import matriculas.integracao.SistemaCobranca;
import matriculas.modelo.Aluno;
import matriculas.modelo.Curriculo;
import matriculas.modelo.Disciplina;
import matriculas.modelo.Matricula;
import matriculas.modelo.PeriodoMatricula;
import matriculas.modelo.StatusDisciplina;
import matriculas.modelo.TipoDisciplina;
import matriculas.repositorio.RepositorioCurriculo;
import matriculas.repositorio.RepositorioMatricula;
import matriculas.repositorio.RepositorioPeriodoMatricula;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ServicoMatricula {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
        PeriodoMatricula periodo = validarPeriodoAberto();
        return repositorioCurriculo.buscarPorCursoESemestre(aluno.getCurso(), periodo.getSemestre())
                .map(Curriculo::listarDisciplinasComVaga)
                .orElse(List.of());
    }

    public Matricula matricular(Aluno aluno, Disciplina disciplina) {
        PeriodoMatricula periodo = validarPeriodoAberto();
        validarOferta(aluno, disciplina, periodo);
        if (aluno.estaMatriculadoEm(disciplina)) {
            throw new MatriculaDuplicadaException("Você já está matriculado em " + disciplina.getCodigo() + ".");
        }
        validarDisponibilidadeDeVagas(disciplina);
        validarLimiteDeDisciplinas(aluno, disciplina);

        Matricula matricula = aluno.matricularEm(disciplina);
        repositorioMatricula.salvar(matricula);
        sistemaCobranca.notificarMatricula(matricula);
        return matricula;
    }

    public void cancelarMatricula(Aluno aluno, Disciplina disciplina) {
        validarPeriodoAberto();
        Matricula matricula = aluno.buscarMatriculaAtiva(disciplina)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Você não possui matrícula ativa em " + disciplina.getCodigo() + "."));
        matricula.cancelar();
        repositorioMatricula.salvar(matricula);
        sistemaCobranca.notificarCancelamento(matricula);
    }

    public List<Matricula> consultarMatriculas(Aluno aluno) {
        return repositorioMatricula.buscarPorAluno(aluno);
    }

    private PeriodoMatricula validarPeriodoAberto() {
        PeriodoMatricula periodo = repositorioPeriodoMatricula.buscarPeriodoAberto()
                .orElseThrow(() -> new PeriodoFechadoException("Não há período de matrículas aberto."));
        if (!periodo.estaAberto()) {
            throw new PeriodoFechadoException(String.format(
                    "O período de matrículas de %s vai de %s a %s.", periodo.getSemestre(),
                    periodo.getDataInicio().format(FORMATO_DATA), periodo.getDataFim().format(FORMATO_DATA)));
        }
        return periodo;
    }

    private void validarOferta(Aluno aluno, Disciplina disciplina, PeriodoMatricula periodo) {
        boolean ofertada = repositorioCurriculo.buscarPorCursoESemestre(aluno.getCurso(), periodo.getSemestre())
                .map(curriculo -> curriculo.contemDisciplina(disciplina))
                .orElse(false);
        if (!ofertada) {
            throw new DisciplinaIndisponivelException(String.format(
                    "A disciplina %s não é ofertada para o seu curso em %s.", disciplina.getCodigo(),
                    periodo.getSemestre()));
        }
        if (disciplina.getStatus() != StatusDisciplina.ABERTA) {
            throw new DisciplinaIndisponivelException(String.format(
                    "A disciplina %s não está aberta para matrícula (situação: %s).", disciplina.getCodigo(),
                    disciplina.getStatus()));
        }
    }

    private void validarDisponibilidadeDeVagas(Disciplina disciplina) {
        if (!disciplina.temVagaDisponivel()) {
            throw new SemVagaDisponivelException(String.format(
                    "A disciplina %s já atingiu o limite de %d alunos.", disciplina.getCodigo(),
                    Disciplina.MAXIMO_ALUNOS));
        }
    }

    private void validarLimiteDeDisciplinas(Aluno aluno, Disciplina disciplina) {
        TipoDisciplina tipo = disciplina.getTipo();
        if (aluno.totalMatriculadasPorTipo(tipo) >= tipo.getLimitePorAluno()) {
            throw new LimiteDisciplinasExcedidoException(String.format(
                    "Você já atingiu o limite de %d disciplinas do tipo %s.", tipo.getLimitePorAluno(), tipo));
        }
    }
}
