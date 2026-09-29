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
        if (dataInicio != null && dataFim != null && dataFim.isBefore(dataInicio)) {
            throw new IllegalArgumentException("A data de fim não pode ser anterior à data de início.");
        }
        this.id = id;
        this.semestre = semestre;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public void abrir() {
        this.aberto = true;
    }

    public void encerrar() {
        this.aberto = false;
    }

    public boolean estaAberto() {
        return aberto && estaVigente(LocalDate.now());
    }

    public boolean estaVigente(LocalDate data) {
        return !data.isBefore(dataInicio) && !data.isAfter(dataFim);
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
