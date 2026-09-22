package matriculas.servico;

import matriculas.modelo.Aluno;
import matriculas.modelo.Curriculo;
import matriculas.modelo.Curso;
import matriculas.modelo.Disciplina;
import matriculas.modelo.PeriodoMatricula;
import matriculas.modelo.Professor;
import matriculas.modelo.TipoDisciplina;
import matriculas.repositorio.RepositorioCurriculo;
import matriculas.repositorio.RepositorioCurso;
import matriculas.repositorio.RepositorioDisciplina;
import matriculas.repositorio.RepositorioPeriodoMatricula;
import matriculas.repositorio.RepositorioUsuario;
import java.time.LocalDate;
import java.util.List;

public class ServicoSecretaria {

    private final RepositorioCurso repositorioCurso;
    private final RepositorioDisciplina repositorioDisciplina;
    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioCurriculo repositorioCurriculo;
    private final RepositorioPeriodoMatricula repositorioPeriodoMatricula;

    public ServicoSecretaria(RepositorioCurso repositorioCurso,
                             RepositorioDisciplina repositorioDisciplina,
                             RepositorioUsuario repositorioUsuario,
                             RepositorioCurriculo repositorioCurriculo,
                             RepositorioPeriodoMatricula repositorioPeriodoMatricula) {
        this.repositorioCurso = repositorioCurso;
        this.repositorioDisciplina = repositorioDisciplina;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioCurriculo = repositorioCurriculo;
        this.repositorioPeriodoMatricula = repositorioPeriodoMatricula;
    }

    public Curso cadastrarCurso(String codigo, String nome, int creditos) {
        throw new UnsupportedOperationException("TODO: implementar cadastro de curso");
    }

    public Disciplina cadastrarDisciplina(String codigo, String nome, int creditos, TipoDisciplina tipo,
                                          Curso curso, Professor professor) {
        throw new UnsupportedOperationException("TODO: implementar cadastro de disciplina");
    }

    public Professor cadastrarProfessor(String nome, String login, String senha, String siape,
                                        String departamento) {
        throw new UnsupportedOperationException("TODO: implementar cadastro de professor");
    }

    public Aluno cadastrarAluno(String nome, String login, String senha, String matricula, Curso curso) {
        throw new UnsupportedOperationException("TODO: implementar cadastro de aluno");
    }

    public Curriculo gerarCurriculo(Curso curso, String semestre, List<Disciplina> disciplinas) {
        throw new UnsupportedOperationException("TODO: implementar geração do currículo do semestre");
    }

    public PeriodoMatricula abrirPeriodoMatriculas(String semestre, LocalDate inicio, LocalDate fim) {
        throw new UnsupportedOperationException("TODO: implementar abertura do período de matrículas");
    }

    public void encerrarPeriodoMatriculas(String semestre) {
        throw new UnsupportedOperationException("TODO: implementar encerramento do período de matrículas");
    }

    public List<Disciplina> verificarQuoruns(String semestre) {
        throw new UnsupportedOperationException("TODO: implementar verificação de quórum das disciplinas");
    }
}
