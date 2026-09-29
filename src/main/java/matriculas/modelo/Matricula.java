package matriculas.modelo;

import java.time.LocalDateTime;

public class Matricula {

    private Long id;
    private Aluno aluno;
    private Disciplina disciplina;
    private LocalDateTime dataMatricula;
    private LocalDateTime dataCancelamento;
    private StatusMatricula status = StatusMatricula.ATIVA;

    public Matricula() {
    }

    public Matricula(Long id, Aluno aluno, Disciplina disciplina, LocalDateTime dataMatricula) {
        this.id = id;
        this.aluno = aluno;
        this.disciplina = disciplina;
        this.dataMatricula = dataMatricula;
    }

    public void cancelar() {
        if (!estaAtiva()) {
            throw new IllegalStateException("A matrícula já está cancelada.");
        }
        this.status = StatusMatricula.CANCELADA;
        this.dataCancelamento = LocalDateTime.now();
    }

    public boolean estaAtiva() {
        return status == StatusMatricula.ATIVA;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public LocalDateTime getDataMatricula() {
        return dataMatricula;
    }

    public void setDataMatricula(LocalDateTime dataMatricula) {
        this.dataMatricula = dataMatricula;
    }

    public LocalDateTime getDataCancelamento() {
        return dataCancelamento;
    }

    public void setDataCancelamento(LocalDateTime dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }

    public StatusMatricula getStatus() {
        return status;
    }

    public void setStatus(StatusMatricula status) {
        this.status = status;
    }
}
