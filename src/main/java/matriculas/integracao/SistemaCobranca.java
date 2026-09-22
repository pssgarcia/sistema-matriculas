package matriculas.integracao;

import matriculas.modelo.Matricula;

public interface SistemaCobranca {

    void notificarMatricula(Matricula matricula);

    void notificarCancelamento(Matricula matricula);
}
