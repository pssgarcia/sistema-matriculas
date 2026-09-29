package matriculas.cli;

import matriculas.modelo.Aluno;
import matriculas.modelo.Curriculo;
import matriculas.modelo.Disciplina;
import matriculas.modelo.PeriodoMatricula;
import matriculas.modelo.Professor;
import java.util.List;
import java.util.stream.Collectors;

public class MenuConsultas extends Menu {

    public MenuConsultas(Contexto contexto) {
        super(contexto);
    }

    @Override
    protected String titulo() {
        return "Consultar cadastros";
    }

    @Override
    protected List<Opcao> opcoes() {
        return List.of(
                new Opcao("Cursos", this::listarCursos),
                new Opcao("Professores", this::listarProfessores),
                new Opcao("Alunos", this::listarAlunos),
                new Opcao("Disciplinas", this::listarDisciplinas),
                new Opcao("Currículos", this::listarCurriculos),
                new Opcao("Períodos de matrícula", this::listarPeriodos));
    }

    @Override
    protected String rotuloSaida() {
        return "Voltar";
    }

    private void listarCursos() {
        console.tabela(List.of("Código", "Curso", "Créditos", "Disciplinas"),
                contexto.repositorioCurso().buscarTodos().stream().map(curso -> List.of(
                        curso.getCodigo(), curso.getNome(), String.valueOf(curso.getCreditos()),
                        String.valueOf(curso.getDisciplinas().size()))).toList());
    }

    private void listarProfessores() {
        console.tabela(List.of("SIAPE", "Nome", "Departamento", "Login", "Disciplinas"),
                contexto.repositorioUsuario().buscarPorPerfil(Professor.class).stream().map(professor -> List.of(
                        professor.getSiape(), professor.getNome(), professor.getDepartamento(), professor.getLogin(),
                        professor.getDisciplinas().stream().map(Disciplina::getCodigo)
                                .collect(Collectors.joining(", ")))).toList());
    }

    private void listarAlunos() {
        console.tabela(List.of("Matrícula", "Nome", "Curso", "Login", "Matrículas ativas"),
                contexto.repositorioUsuario().buscarPorPerfil(Aluno.class).stream().map(aluno -> List.of(
                        aluno.getMatricula(), aluno.getNome(), aluno.getCurso().getCodigo(), aluno.getLogin(),
                        aluno.consultarDisciplinasMatriculadas().stream().map(Disciplina::getCodigo)
                                .collect(Collectors.joining(", ")))).toList());
    }

    private void listarDisciplinas() {
        console.tabela(List.of("Código", "Disciplina", "Tipo", "Créd.", "Curso", "Professor", "Situação", "Alunos"),
                contexto.repositorioDisciplina().buscarTodos().stream().map(disciplina -> List.of(
                        disciplina.getCodigo(), disciplina.getNome(), Formatos.tipo(disciplina.getTipo()),
                        String.valueOf(disciplina.getCreditos()), disciplina.getCurso().getCodigo(),
                        disciplina.getProfessor().getNome(), Formatos.status(disciplina.getStatus()),
                        disciplina.getTotalMatriculados() + "/" + Disciplina.MAXIMO_ALUNOS)).toList());
    }

    private void listarCurriculos() {
        List<Curriculo> curriculos = contexto.repositorioCurriculo().buscarTodos();
        console.tabela(List.of("Semestre", "Curso", "Disciplinas"),
                curriculos.stream().map(curriculo -> List.of(
                        curriculo.getSemestre(), curriculo.getCurso().getCodigo(),
                        curriculo.getDisciplinas().stream().map(Disciplina::getCodigo)
                                .collect(Collectors.joining(", ")))).toList());
    }

    private void listarPeriodos() {
        List<PeriodoMatricula> periodos = contexto.repositorioPeriodoMatricula().buscarTodos();
        console.tabela(List.of("Semestre", "Início", "Fim", "Situação"),
                periodos.stream().map(periodo -> List.of(
                        periodo.getSemestre(), periodo.getDataInicio().format(Console.FORMATO_DATA),
                        periodo.getDataFim().format(Console.FORMATO_DATA),
                        periodo.estaAberto() ? "Aberto" : periodo.isAberto() ? "Aberto (fora da vigência)"
                                : "Encerrado")).toList());
    }
}
