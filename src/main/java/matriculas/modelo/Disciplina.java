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
        throw new UnsupportedOperationException("TODO: implementar verificação de vagas disponíveis");
    }

    public boolean atingiuQuorumMinimo() {
        throw new UnsupportedOperationException("TODO: implementar verificação de quórum mínimo");
    }

    public int getTotalMatriculados() {
        throw new UnsupportedOperationException("TODO: implementar contagem de matrículas ativas");
    }

    public int getVagasRestantes() {
        throw new UnsupportedOperationException("TODO: implementar cálculo de vagas restantes");
    }

    public void adicionarMatricula(Matricula matricula) {
        throw new UnsupportedOperationException("TODO: implementar inclusão de matrícula na disciplina");
    }

    public void removerMatricula(Matricula matricula) {
        throw new UnsupportedOperationException("TODO: implementar remoção de matrícula da disciplina");
    }

    public List<Aluno> listarAlunosMatriculados() {
        throw new UnsupportedOperationException("TODO: implementar listagem de alunos matriculados");
    }

    public void ativar() {
        throw new UnsupportedOperationException("TODO: implementar confirmação da disciplina");
    }

    public void cancelarPorFaltaDeQuorum() {
        throw new UnsupportedOperationException("TODO: implementar cancelamento por falta de quórum");
    }

    public boolean estaDisponivelParaMatricula() {
        throw new UnsupportedOperationException("TODO: implementar verificação de disponibilidade para matrícula");
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
