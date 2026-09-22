package matriculas.modelo;

import java.time.LocalDate;

public class PeriodoMatricula {

    private Long id;
    private String semestre;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private boolean aberto;

    public PeriodoMatricula() {
    }

    public PeriodoMatricula(Long id, String semestre, LocalDate dataInicio, LocalDate dataFim) {
        this.id = id;
        this.semestre = semestre;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public void abrir() {
        throw new UnsupportedOperationException("TODO: implementar abertura do período de matrículas");
    }

    public void encerrar() {
        throw new UnsupportedOperationException("TODO: implementar encerramento do período de matrículas");
    }

    public boolean estaAberto() {
        throw new UnsupportedOperationException("TODO: implementar verificação de período aberto");
    }

    public boolean estaVigente(LocalDate data) {
        throw new UnsupportedOperationException("TODO: implementar verificação de vigência do período");
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

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public boolean isAberto() {
        return aberto;
    }

    public void setAberto(boolean aberto) {
        this.aberto = aberto;
    }
}
