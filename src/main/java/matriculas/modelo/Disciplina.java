package matriculas.modelo;

import java.util.ArrayList;
import java.util.List;

public class Disciplina {

    public static final int MAXIMO_ALUNOS = 60;
    public static final int MINIMO_ALUNOS = 3;

    private Long id;
    private String codigo;
    private String nome;
    private int creditos;
    private TipoDisciplina tipo;
    private StatusDisciplina status = StatusDisciplina.PLANEJADA;
    private Curso curso;
    private Professor professor;
    private final List<Matricula> matriculas = new ArrayList<>();

    public Disciplina() {
    }

    public Disciplina(Long id, String codigo, String nome, int creditos, TipoDisciplina tipo,
                      Curso curso, Professor professor) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.creditos = creditos;
        this.tipo = tipo;
        this.curso = curso;
        this.professor = professor;
    }

    public boolean temVagaDisponivel() {
        return getTotalMatriculados() < MAXIMO_ALUNOS;
    }

    public boolean atingiuQuorumMinimo() {
        return getTotalMatriculados() >= MINIMO_ALUNOS;
    }

    public int getTotalMatriculados() {
        return (int) matriculas.stream().filter(Matricula::estaAtiva).count();
    }

    public int getVagasRestantes() {
        return Math.max(0, MAXIMO_ALUNOS - getTotalMatriculados());
    }

    public void adicionarMatricula(Matricula matricula) {
        if (!matriculas.contains(matricula)) {
            matriculas.add(matricula);
        }
    }

    public void removerMatricula(Matricula matricula) {
        matriculas.remove(matricula);
    }

    public List<Aluno> listarAlunosMatriculados() {
        return matriculas.stream()
                .filter(Matricula::estaAtiva)
                .map(Matricula::getAluno)
                .toList();
    }

    public void abrirParaMatricula() {
        if (status == StatusDisciplina.PLANEJADA) {
            status = StatusDisciplina.ABERTA;
        }
    }

    public void ativar() {
        if (status != StatusDisciplina.ABERTA) {
            throw new IllegalStateException("Só é possível confirmar uma disciplina aberta para matrícula.");
        }
        status = StatusDisciplina.CONFIRMADA;
    }

    public void cancelarPorFaltaDeQuorum() {
        if (atingiuQuorumMinimo()) {
            throw new IllegalStateException("A disciplina atingiu o quórum mínimo e não pode ser cancelada.");
        }
        matriculas.stream().filter(Matricula::estaAtiva).forEach(Matricula::cancelar);
        status = StatusDisciplina.CANCELADA;
    }

    public boolean estaDisponivelParaMatricula() {
        return status == StatusDisciplina.ABERTA && temVagaDisponivel();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public TipoDisciplina getTipo() {
        return tipo;
    }

    public void setTipo(TipoDisciplina tipo) {
        this.tipo = tipo;
    }

    public StatusDisciplina getStatus() {
        return status;
    }

    public void setStatus(StatusDisciplina status) {
        this.status = status;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }
}
