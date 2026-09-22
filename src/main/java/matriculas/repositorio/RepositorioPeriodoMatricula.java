package matriculas.repositorio;

import matriculas.modelo.PeriodoMatricula;
import java.util.Optional;

public interface RepositorioPeriodoMatricula extends Repositorio<PeriodoMatricula, Long> {

    Optional<PeriodoMatricula> buscarPorSemestre(String semestre);

    Optional<PeriodoMatricula> buscarPeriodoAberto();
}
