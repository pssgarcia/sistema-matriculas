package matriculas.integracao;

import matriculas.modelo.Matricula;

public class SistemaCobrancaAdapter implements SistemaCobranca {

    private final String urlServico;

    public SistemaCobrancaAdapter(String urlServico) {
        this.urlServico = urlServico;
    }

    @Override
    public void notificarMatricula(Matricula matricula) {
        throw new UnsupportedOperationException("TODO: implementar envio de notificação de matrícula");
    }

    @Override
    public void notificarCancelamento(Matricula matricula) {
        throw new UnsupportedOperationException("TODO: implementar envio de notificação de cancelamento");
    }

    public String getUrlServico() {
        return urlServico;
    }
}
