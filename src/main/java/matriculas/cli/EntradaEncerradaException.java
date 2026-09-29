package matriculas.cli;

public class EntradaEncerradaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntradaEncerradaException() {
        super("A entrada padrão foi encerrada.");
    }
}
