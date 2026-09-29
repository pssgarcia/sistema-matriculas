package matriculas.modelo;

import java.util.ArrayList;
import java.util.List;

public class Professor extends Usuario {

    private String siape;
    private String departamento;
    private final List<Disciplina> disciplinas = new ArrayList<>();

    public Professor() {
    }

    public Professor(Long id, String nome, String login, String senha, String siape, String departamento) {
        super(id, nome, login, senha);
        this.siape = siape;
        this.departamento = departamento;
    }

    public List<Aluno> consultarAlunosMatriculados(Disciplina disciplina) {
        if (!lecionaDisciplina(disciplina)) {
            throw new IllegalArgumentException("O professor não leciona a disciplina " + disciplina.getCodigo());
        }
        return disciplina.listarAlunosMatriculados();
    }

    public List<Disciplina> consultarDisciplinas() {
        return List.copyOf(disciplinas);
    }

    public boolean lecionaDisciplina(Disciplina disciplina) {
        return disciplinas.contains(disciplina);
    }

    public void adicionarDisciplina(Disciplina disciplina) {
        if (!disciplinas.contains(disciplina)) {
            disciplinas.add(disciplina);
        }
    }

    @Override
    public String getPerfil() {
        return "PROFESSOR";
    }

    public String getSiape() {
        return siape;
    }

    public void setSiape(String siape) {
        this.siape = siape;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }
}
