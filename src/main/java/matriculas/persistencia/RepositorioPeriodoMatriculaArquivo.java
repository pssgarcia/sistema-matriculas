package matriculas.persistencia;

import matriculas.modelo.PeriodoMatricula;
import matriculas.repositorio.RepositorioPeriodoMatricula;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;

public class RepositorioPeriodoMatriculaArquivo extends RepositorioArquivo<PeriodoMatricula>
        implements RepositorioPeriodoMatricula {

    public RepositorioPeriodoMatriculaArquivo(Path arquivo) {
        super(arquivo);
        carregar();
    }

    @Override
    public Optional<PeriodoMatricula> buscarPorSemestre(String semestre) {
        return todas().filter(periodo -> periodo.getSemestre().equalsIgnoreCase(semestre)).findFirst();
    }

    @Override
    public Optional<PeriodoMatricula> buscarPeriodoAberto() {
        return todas().filter(PeriodoMatricula::isAberto).findFirst();
    }

    @Override
    protected Long idDe(PeriodoMatricula periodo) {
        return periodo.getId();
    }

    @Override
    protected void atribuirId(PeriodoMatricula periodo, Long id) {
        periodo.setId(id);
    }

    @Override
    protected String serializar(PeriodoMatricula periodo) {
        return FormatoArquivo.juntar(periodo.getId(), periodo.getSemestre(), periodo.getDataInicio(),
                periodo.getDataFim(), periodo.isAberto());
    }

    @Override
    protected PeriodoMatricula desserializar(String[] campos) {
        PeriodoMatricula periodo = new PeriodoMatricula(Long.valueOf(campos[0]), campos[1],
                LocalDate.parse(campos[2]), LocalDate.parse(campos[3]));
        periodo.setAberto(Boolean.parseBoolean(campos[4]));
        return periodo;
    }
}
