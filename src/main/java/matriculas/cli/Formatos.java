package matriculas.cli;

import matriculas.modelo.Matricula;
import matriculas.modelo.StatusDisciplina;
import matriculas.modelo.TipoDisciplina;

final class Formatos {

    private Formatos() {
    }

    static String tipo(TipoDisciplina tipo) {
        return tipo == TipoDisciplina.OBRIGATORIA ? "Obrigatória" : "Optativa";
    }

    static String status(StatusDisciplina status) {
        return switch (status) {
            case PLANEJADA -> "Planejada";
            case ABERTA -> "Aberta";
            case CONFIRMADA -> "Confirmada";
            case CANCELADA -> "Cancelada";
        };
    }

    static String situacao(Matricula matricula) {
        if (matricula.estaAtiva()) {
            return "Ativa";
        }
        return "Cancelada em " + matricula.getDataCancelamento().format(Console.FORMATO_DATA_HORA);
    }
}
