package matriculas.excecao;

public class LimiteDisciplinasExcedidoException extends MatriculaException {

    private static final long serialVersionUID = 1L;

    public LimiteDisciplinasExcedidoException(String mensagem) {
        super(mensagem);
    }
}
