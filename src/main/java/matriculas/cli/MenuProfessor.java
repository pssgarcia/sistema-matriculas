package matriculas.cli;

import matriculas.modelo.Aluno;
import matriculas.modelo.Disciplina;
import matriculas.modelo.Professor;
import java.util.List;

public class MenuProfessor extends Menu {

    private final Professor professor;

    public MenuProfessor(Contexto contexto, Professor professor) {
        super(contexto);
        this.professor = professor;
    }

    @Override
    protected String titulo() {
        return "Professor: " + professor.getNome() + " (SIAPE " + professor.getSiape() + ")";
    }

    @Override
    protected List<Opcao> opcoes() {
        return List.of(
                new Opcao("Consultar minhas disciplinas", this::consultarDisciplinas),
                new Opcao("Consultar alunos matriculados", this::consultarAlunosMatriculados),
                new Opcao("Alterar senha", () -> alterarSenha(professor)));
    }

    @Override
    protected String rotuloSaida() {
        return "Sair (logout)";
    }

    private void consultarDisciplinas() {
        List<Disciplina> disciplinas = contexto.servicoProfessor().consultarDisciplinas(professor);
        console.tabela(List.of("Código", "Disciplina", "Tipo", "Curso", "Situação", "Matriculados"),
                disciplinas.stream().map(disciplina -> List.of(
                        disciplina.getCodigo(),
                        disciplina.getNome(),
                        Formatos.tipo(disciplina.getTipo()),
                        disciplina.getCurso().getCodigo(),
                        Formatos.status(disciplina.getStatus()),
                        disciplina.getTotalMatriculados() + "/" + Disciplina.MAXIMO_ALUNOS)).toList());
    }

    private void consultarAlunosMatriculados() {
        consultarDisciplinas();
        Disciplina disciplina = contexto.disciplinaPorCodigo(console.lerTexto("Código da disciplina"));
        List<Aluno> alunos = contexto.servicoProfessor().consultarAlunosMatriculados(professor, disciplina);
        console.mensagem(disciplina.getCodigo() + " - " + disciplina.getNome() + ": " + alunos.size()
                + " aluno(s) matriculado(s)");
        console.tabela(List.of("Matrícula", "Nome", "Curso"),
                alunos.stream().map(aluno -> List.of(
                        aluno.getMatricula(), aluno.getNome(), aluno.getCurso().getCodigo())).toList());
    }
}
