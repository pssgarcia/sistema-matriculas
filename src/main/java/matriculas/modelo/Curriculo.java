package matriculas.modelo;

import java.util.ArrayList;
import java.util.List;

public class Curriculo {

    private Long id;
    private String semestre;
    private Curso curso;
    private final List<Disciplina> disciplinas = new ArrayList<>();

    public Curriculo() {
    }

    public Curriculo(Long id, String semestre, Curso curso) {
        this.id = id;
        this.semestre = semestre;
        this.curso = curso;
    }

    public void adicionarDisciplina(Disciplina disciplina) {
        throw new UnsupportedOperationException("TODO: implementar inclusão de disciplina no currículo");
    }

    public void removerDisciplina(Disciplina disciplina) {
        throw new UnsupportedOperationException("TODO: implementar remoção de disciplina do currículo");
    }

    public List<Disciplina> listarDisciplinasComVaga() {
        throw new UnsupportedOperationException("TODO: implementar listagem de disciplinas com vaga");
    }

    public boolean contemDisciplina(Disciplina disciplina) {
        throw new UnsupportedOperationException("TODO: implementar verificação de disciplina no currículo");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }
}
