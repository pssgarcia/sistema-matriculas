package matriculas.cli;

import matriculas.excecao.EntidadeNaoEncontradaException;
import matriculas.modelo.Aluno;
import matriculas.modelo.Curriculo;
import matriculas.modelo.Curso;
import matriculas.modelo.Disciplina;
import matriculas.modelo.PeriodoMatricula;
import matriculas.modelo.Professor;
import matriculas.modelo.Secretaria;
import matriculas.modelo.TipoDisciplina;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MenuSecretaria extends Menu {

    private final Secretaria secretaria;

    public MenuSecretaria(Contexto contexto, Secretaria secretaria) {
        super(contexto);
        this.secretaria = secretaria;
    }

    @Override
    protected String titulo() {
        return "Secretaria: " + secretaria.getNome();
    }

    @Override
    protected List<Opcao> opcoes() {
        return List.of(
                new Opcao("Cadastrar curso", this::cadastrarCurso),
                new Opcao("Cadastrar professor", this::cadastrarProfessor),
                new Opcao("Cadastrar aluno", this::cadastrarAluno),
                new Opcao("Cadastrar disciplina", this::cadastrarDisciplina),
                new Opcao("Gerar currículo do semestre", this::gerarCurriculo),
                new Opcao("Abrir período de matrículas", this::abrirPeriodo),
                new Opcao("Encerrar período de matrículas", this::encerrarPeriodo),
                new Opcao("Verificar quórum das disciplinas", this::verificarQuoruns),
                new Opcao("Consultar cadastros", () -> new MenuConsultas(contexto).executar()),
                new Opcao("Alterar senha", () -> alterarSenha(secretaria)));
    }

    @Override
    protected String rotuloSaida() {
        return "Sair (logout)";
    }

    private void cadastrarCurso() {
        String codigo = console.lerTexto("Código");
        String nome = console.lerTexto("Nome");
        int creditos = console.lerInteiro("Número de créditos");
        Curso curso = contexto.servicoSecretaria().cadastrarCurso(codigo, nome, creditos);
        console.sucesso("Curso " + curso.getCodigo() + " cadastrado.");
    }

    private void cadastrarProfessor() {
        String nome = console.lerTexto("Nome");
        String siape = console.lerTexto("SIAPE");
        String departamento = console.lerTexto("Departamento");
        String login = console.lerTexto("Login");
        String senha = console.lerTexto("Senha inicial");
        Professor professor = contexto.servicoSecretaria().cadastrarProfessor(nome, login, senha, siape, departamento);
        console.sucesso("Professor " + professor.getNome() + " cadastrado (login: " + professor.getLogin() + ").");
    }

    private void cadastrarAluno() {
        Curso curso = escolherCurso();
        String nome = console.lerTexto("Nome");
        String matricula = console.lerTexto("Matrícula");
        String login = console.lerTexto("Login");
        String senha = console.lerTexto("Senha inicial");
        Aluno aluno = contexto.servicoSecretaria().cadastrarAluno(nome, login, senha, matricula, curso);
        console.sucesso("Aluno " + aluno.getNome() + " cadastrado em " + curso.getNome()
                + " (login: " + aluno.getLogin() + ").");
    }

    private void cadastrarDisciplina() {
        Curso curso = escolherCurso();
        Professor professor = escolherProfessor();
        String codigo = console.lerTexto("Código");
        String nome = console.lerTexto("Nome");
        int creditos = console.lerInteiro("Créditos");
        console.mensagem("Tipo: 1 - Obrigatória | 2 - Optativa");
        TipoDisciplina tipo = console.lerInteiro("Tipo") == 2 ? TipoDisciplina.OPTATIVA : TipoDisciplina.OBRIGATORIA;
        Disciplina disciplina = contexto.servicoSecretaria()
                .cadastrarDisciplina(codigo, nome, creditos, tipo, curso, professor);
        console.sucesso("Disciplina " + disciplina.getCodigo() + " (" + Formatos.tipo(tipo).toLowerCase()
                + ") cadastrada no curso " + curso.getCodigo() + ".");
    }

    private void gerarCurriculo() {
        Curso curso = escolherCurso();
        String semestre = console.lerTexto("Semestre (ex.: 2026/2)");
        console.tabela(List.of("Código", "Disciplina", "Tipo", "Situação"),
                curso.getDisciplinas().stream().map(disciplina -> List.of(
                        disciplina.getCodigo(), disciplina.getNome(), Formatos.tipo(disciplina.getTipo()),
                        Formatos.status(disciplina.getStatus()))).toList());
        String codigos = console.lerTexto("Códigos das disciplinas ofertadas (separados por vírgula)");
        List<Disciplina> disciplinas = new ArrayList<>();
        for (String codigo : codigos.split(",")) {
            if (!codigo.isBlank()) {
                disciplinas.add(contexto.disciplinaPorCodigo(codigo.trim()));
            }
        }
        Curriculo curriculo = contexto.servicoSecretaria().gerarCurriculo(curso, semestre, disciplinas);
        console.sucesso("Currículo " + curriculo.getSemestre() + " de " + curso.getNome() + " com "
                + curriculo.getDisciplinas().size() + " disciplina(s). As disciplinas estão abertas para matrícula.");
    }

    private void abrirPeriodo() {
        String semestre = console.lerTexto("Semestre (ex.: 2026/2)");
        LocalDate inicio = console.lerData("Data de início", LocalDate.now());
        LocalDate fim = console.lerData("Data de fim", null);
        PeriodoMatricula periodo = contexto.servicoSecretaria().abrirPeriodoMatriculas(semestre, inicio, fim);
        console.sucesso("Período de matrículas de " + periodo.getSemestre() + " aberto de "
                + periodo.getDataInicio().format(Console.FORMATO_DATA) + " a "
                + periodo.getDataFim().format(Console.FORMATO_DATA) + ".");
    }

    private void encerrarPeriodo() {
        PeriodoMatricula periodo = contexto.repositorioPeriodoMatricula().buscarPeriodoAberto()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Não há período de matrículas aberto."));
        console.mensagem("Período aberto: " + periodo.getSemestre());
        List<Disciplina> semQuorum = contexto.servicoSecretaria().verificarQuoruns(periodo.getSemestre());
        console.mensagem(semQuorum.size() + " disciplina(s) sem o quórum mínimo de " + Disciplina.MINIMO_ALUNOS
                + " alunos serão canceladas.");
        if (!console.confirmar("Confirma o encerramento do período de " + periodo.getSemestre() + "?")) {
            return;
        }
        List<Disciplina> canceladas = contexto.servicoSecretaria().encerrarPeriodoMatriculas(periodo.getSemestre());
        console.sucesso("Período de " + periodo.getSemestre() + " encerrado.");
        exibirSituacaoDoSemestre(periodo.getSemestre());
        if (!canceladas.isEmpty()) {
            console.mensagem("Matrículas das disciplinas canceladas foram canceladas e a cobrança foi notificada.");
        }
    }

    private void verificarQuoruns() {
        PeriodoMatricula periodo = contexto.repositorioPeriodoMatricula().buscarPeriodoAberto()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Não há período de matrículas aberto."));
        console.mensagem("Semestre " + periodo.getSemestre() + " - quórum mínimo: " + Disciplina.MINIMO_ALUNOS
                + " alunos");
        exibirSituacaoDoSemestre(periodo.getSemestre());
    }

    private void exibirSituacaoDoSemestre(String semestre) {
        List<List<String>> linhas = new ArrayList<>();
        for (Curriculo curriculo : contexto.repositorioCurriculo().buscarPorSemestre(semestre)) {
            for (Disciplina disciplina : curriculo.getDisciplinas()) {
                linhas.add(List.of(curriculo.getCurso().getCodigo(), disciplina.getCodigo(), disciplina.getNome(),
                        String.valueOf(disciplina.getTotalMatriculados()),
                        disciplina.atingiuQuorumMinimo() ? "sim" : "não",
                        Formatos.status(disciplina.getStatus())));
            }
        }
        console.tabela(List.of("Curso", "Código", "Disciplina", "Matriculados", "Quórum", "Situação"), linhas);
    }

    private Curso escolherCurso() {
        List<Curso> cursos = contexto.repositorioCurso().buscarTodos();
        if (cursos.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Cadastre um curso antes.");
        }
        cursos.forEach(curso -> console.mensagem("  " + curso.getCodigo() + " - " + curso.getNome()));
        return contexto.cursoPorCodigo(console.lerTexto("Código do curso"));
    }

    private Professor escolherProfessor() {
        List<Professor> professores = contexto.repositorioUsuario().buscarPorPerfil(Professor.class);
        if (professores.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Cadastre um professor antes.");
        }
        professores.forEach(p -> console.mensagem("  " + p.getSiape() + " - " + p.getNome()));
        String siape = console.lerTexto("SIAPE do professor");
        return professores.stream()
                .filter(professor -> professor.getSiape().equalsIgnoreCase(siape))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Professor com SIAPE " + siape
                        + " não encontrado."));
    }
}
