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
        if (!contemDisciplina(disciplina)) {
            disciplinas.add(disciplina);
        }
    }

    public void removerDisciplina(Disciplina disciplina) {
        disciplinas.remove(disciplina);
    }

    public List<Disciplina> listarDisciplinasComVaga() {
        return disciplinas.stream()
                .filter(Disciplina::estaDisponivelParaMatricula)
                .toList();
    }

    public boolean contemDisciplina(Disciplina disciplina) {
        return disciplinas.contains(disciplina);
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
