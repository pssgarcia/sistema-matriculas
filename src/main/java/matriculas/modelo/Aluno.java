package matriculas.modelo;

import java.util.ArrayList;
import java.util.List;

public class Aluno extends Usuario {

    private String matricula;
    private Curso curso;
    private final List<Matricula> matriculas = new ArrayList<>();

    public Aluno() {
    }

    public Aluno(Long id, String nome, String login, String senha, String matricula, Curso curso) {
        super(id, nome, login, senha);
        this.matricula = matricula;
        this.curso = curso;
    }

    public Matricula matricularEm(Disciplina disciplina) {
        throw new UnsupportedOperationException("TODO: implementar matrícula do aluno em disciplina");
    }

    public void cancelarMatricula(Disciplina disciplina) {
        throw new UnsupportedOperationException("TODO: implementar cancelamento de matrícula");
    }

    public List<Matricula> consultarMatriculasAtivas() {
        throw new UnsupportedOperationException("TODO: implementar consulta de matrículas ativas");
    }

    public List<Disciplina> consultarDisciplinasMatriculadas() {
        throw new UnsupportedOperationException("TODO: implementar consulta de disciplinas matriculadas");
    }

    public boolean podeSeMatricularEm(Disciplina disciplina) {
        throw new UnsupportedOperationException("TODO: implementar validação de elegibilidade para matrícula");
    }

    public int totalMatriculadasPorTipo(TipoDisciplina tipo) {
        throw new UnsupportedOperationException("TODO: implementar contagem de disciplinas por tipo");
    }

    public boolean estaMatriculadoEm(Disciplina disciplina) {
        throw new UnsupportedOperationException("TODO: implementar verificação de matrícula existente");
    }

    @Override
    public String getPerfil() {
        return "ALUNO";
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }
}
