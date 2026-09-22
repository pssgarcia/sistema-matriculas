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
        throw new UnsupportedOperationException("TODO: implementar consulta de alunos matriculados");
    }

    public List<Disciplina> consultarDisciplinas() {
        throw new UnsupportedOperationException("TODO: implementar consulta de disciplinas do professor");
    }

    public boolean lecionaDisciplina(Disciplina disciplina) {
        throw new UnsupportedOperationException("TODO: implementar verificação de vínculo com a disciplina");
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
