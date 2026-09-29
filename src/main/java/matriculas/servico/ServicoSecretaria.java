package matriculas.servico;

import matriculas.excecao.CadastroInvalidoException;
import matriculas.excecao.EntidadeNaoEncontradaException;
import matriculas.excecao.PeriodoFechadoException;
import matriculas.integracao.SistemaCobranca;
import matriculas.modelo.Aluno;
import matriculas.modelo.Curriculo;
import matriculas.modelo.Curso;
import matriculas.modelo.Disciplina;
import matriculas.modelo.Matricula;
import matriculas.modelo.PeriodoMatricula;
import matriculas.modelo.Professor;
import matriculas.modelo.StatusDisciplina;
import matriculas.modelo.TipoDisciplina;
import matriculas.repositorio.RepositorioCurriculo;
import matriculas.repositorio.RepositorioCurso;
import matriculas.repositorio.RepositorioDisciplina;
import matriculas.repositorio.RepositorioMatricula;
import matriculas.repositorio.RepositorioPeriodoMatricula;
import matriculas.repositorio.RepositorioUsuario;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServicoSecretaria {

    private final RepositorioCurso repositorioCurso;
    private final RepositorioDisciplina repositorioDisciplina;
    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioCurriculo repositorioCurriculo;
    private final RepositorioPeriodoMatricula repositorioPeriodoMatricula;
    private final RepositorioMatricula repositorioMatricula;
    private final SistemaCobranca sistemaCobranca;

    public ServicoSecretaria(RepositorioCurso repositorioCurso,
                             RepositorioDisciplina repositorioDisciplina,
                             RepositorioUsuario repositorioUsuario,
                             RepositorioCurriculo repositorioCurriculo,
                             RepositorioPeriodoMatricula repositorioPeriodoMatricula,
                             RepositorioMatricula repositorioMatricula,
                             SistemaCobranca sistemaCobranca) {
        this.repositorioCurso = repositorioCurso;
        this.repositorioDisciplina = repositorioDisciplina;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioCurriculo = repositorioCurriculo;
        this.repositorioPeriodoMatricula = repositorioPeriodoMatricula;
        this.repositorioMatricula = repositorioMatricula;
        this.sistemaCobranca = sistemaCobranca;
    }

    public Curso cadastrarCurso(String codigo, String nome, int creditos) {
        exigirTexto(codigo, "código");
        exigirTexto(nome, "nome");
        exigirPositivo(creditos, "créditos");
        if (repositorioCurso.buscarPorCodigo(codigo).isPresent()) {
            throw new CadastroInvalidoException("Já existe um curso com o código " + codigo + ".");
        }
        return repositorioCurso.salvar(new Curso(null, codigo, nome, creditos));
    }

    public Disciplina cadastrarDisciplina(String codigo, String nome, int creditos, TipoDisciplina tipo,
                                          Curso curso, Professor professor) {
        exigirTexto(codigo, "código");
        exigirTexto(nome, "nome");
        exigirPositivo(creditos, "créditos");
        if (tipo == null || curso == null || professor == null) {
            throw new CadastroInvalidoException("Tipo, curso e professor são obrigatórios.");
        }
        if (repositorioDisciplina.buscarPorCodigo(codigo).isPresent()) {
            throw new CadastroInvalidoException("Já existe uma disciplina com o código " + codigo + ".");
        }
        Disciplina disciplina = repositorioDisciplina.salvar(
                new Disciplina(null, codigo, nome, creditos, tipo, curso, professor));
        curso.adicionarDisciplina(disciplina);
        professor.adicionarDisciplina(disciplina);
        return disciplina;
    }

    public Professor cadastrarProfessor(String nome, String login, String senha, String siape,
                                        String departamento) {
        validarDadosDeAcesso(nome, login, senha);
        exigirTexto(siape, "SIAPE");
        return (Professor) repositorioUsuario.salvar(new Professor(null, nome, login, senha, siape, departamento));
    }

    public Aluno cadastrarAluno(String nome, String login, String senha, String matricula, Curso curso) {
        validarDadosDeAcesso(nome, login, senha);
        exigirTexto(matricula, "matrícula");
        if (curso == null) {
            throw new CadastroInvalidoException("O curso do aluno é obrigatório.");
        }
        boolean matriculaEmUso = repositorioUsuario.buscarPorPerfil(Aluno.class).stream()
                .anyMatch(aluno -> aluno.getMatricula().equalsIgnoreCase(matricula));
        if (matriculaEmUso) {
            throw new CadastroInvalidoException("Já existe um aluno com a matrícula " + matricula + ".");
        }
        return (Aluno) repositorioUsuario.salvar(new Aluno(null, nome, login, senha, matricula, curso));
    }

    public Curriculo gerarCurriculo(Curso curso, String semestre, List<Disciplina> disciplinas) {
        exigirTexto(semestre, "semestre");
        if (disciplinas == null || disciplinas.isEmpty()) {
            throw new CadastroInvalidoException("Informe ao menos uma disciplina para o currículo.");
        }
        for (Disciplina disciplina : disciplinas) {
            if (disciplina.getCurso() != curso) {
                throw new CadastroInvalidoException(String.format(
                        "A disciplina %s não pertence ao curso %s.", disciplina.getCodigo(), curso.getCodigo()));
            }
            if (disciplina.getStatus() != StatusDisciplina.PLANEJADA
                    && disciplina.getStatus() != StatusDisciplina.ABERTA) {
                throw new CadastroInvalidoException(String.format(
                        "A disciplina %s já foi %s e não pode ser ofertada.", disciplina.getCodigo(),
                        disciplina.getStatus().name().toLowerCase()));
            }
        }

        Curriculo curriculo = repositorioCurriculo.buscarPorCursoESemestre(curso, semestre)
                .orElseGet(() -> new Curriculo(null, semestre, curso));
        for (Disciplina disciplina : disciplinas) {
            curriculo.adicionarDisciplina(disciplina);
            disciplina.abrirParaMatricula();
            repositorioDisciplina.salvar(disciplina);
        }
        return repositorioCurriculo.salvar(curriculo);
    }

    public PeriodoMatricula abrirPeriodoMatriculas(String semestre, LocalDate inicio, LocalDate fim) {
        exigirTexto(semestre, "semestre");
        if (inicio == null || fim == null || fim.isBefore(inicio)) {
            throw new CadastroInvalidoException("A data de fim deve ser igual ou posterior à data de início.");
        }
        repositorioPeriodoMatricula.buscarPeriodoAberto().ifPresent(aberto -> {
            throw new CadastroInvalidoException(
                    "O período de matrículas de " + aberto.getSemestre() + " ainda está aberto.");
        });
        if (repositorioPeriodoMatricula.buscarPorSemestre(semestre).isPresent()) {
            throw new CadastroInvalidoException("O período de matrículas de " + semestre + " já foi realizado.");
        }
        PeriodoMatricula periodo = new PeriodoMatricula(null, semestre, inicio, fim);
        periodo.abrir();
        return repositorioPeriodoMatricula.salvar(periodo);
    }

    /**
     * Encerra o período e verifica o quórum de cada disciplina ofertada no semestre:
     * as que atingiram o mínimo são confirmadas e as demais são canceladas, junto com
     * suas matrículas (o sistema de cobrança é notificado de cada cancelamento).
     *
     * @return as disciplinas canceladas por falta de quórum
     */
    public List<Disciplina> encerrarPeriodoMatriculas(String semestre) {
        PeriodoMatricula periodo = repositorioPeriodoMatricula.buscarPorSemestre(semestre)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Não há período de matrículas para o semestre " + semestre + "."));
        if (!periodo.isAberto()) {
            throw new PeriodoFechadoException("O período de matrículas de " + semestre + " já está encerrado.");
        }
        periodo.encerrar();
        repositorioPeriodoMatricula.salvar(periodo);

        List<Disciplina> semQuorum = verificarQuoruns(semestre);
        for (Disciplina disciplina : disciplinasAbertasDoSemestre(semestre)) {
            if (semQuorum.contains(disciplina)) {
                List<Matricula> ativas = disciplina.getMatriculas().stream().filter(Matricula::estaAtiva).toList();
                disciplina.cancelarPorFaltaDeQuorum();
                for (Matricula matricula : ativas) {
                    repositorioMatricula.salvar(matricula);
                    sistemaCobranca.notificarCancelamento(matricula);
                }
            } else {
                disciplina.ativar();
            }
            repositorioDisciplina.salvar(disciplina);
        }
        return semQuorum;
    }

    public List<Disciplina> verificarQuoruns(String semestre) {
        return disciplinasAbertasDoSemestre(semestre).stream()
                .filter(disciplina -> !disciplina.atingiuQuorumMinimo())
                .toList();
    }

    private List<Disciplina> disciplinasAbertasDoSemestre(String semestre) {
        List<Disciplina> disciplinas = new ArrayList<>();
        for (Curriculo curriculo : repositorioCurriculo.buscarPorSemestre(semestre)) {
            for (Disciplina disciplina : curriculo.getDisciplinas()) {
                if (disciplina.getStatus() == StatusDisciplina.ABERTA && !disciplinas.contains(disciplina)) {
                    disciplinas.add(disciplina);
                }
            }
        }
        return disciplinas;
    }

    private void validarDadosDeAcesso(String nome, String login, String senha) {
        exigirTexto(nome, "nome");
        exigirTexto(login, "login");
        exigirTexto(senha, "senha");
        if (repositorioUsuario.buscarPorLogin(login).isPresent()) {
            throw new CadastroInvalidoException("O login " + login + " já está em uso.");
        }
    }

    private static void exigirTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new CadastroInvalidoException("O campo " + campo + " é obrigatório.");
        }
    }

    private static void exigirPositivo(int valor, String campo) {
        if (valor <= 0) {
            throw new CadastroInvalidoException("O campo " + campo + " deve ser maior que zero.");
        }
    }
}
