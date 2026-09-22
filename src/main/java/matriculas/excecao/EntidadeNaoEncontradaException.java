package matriculas.excecao;

public class EntidadeNaoEncontradaException extends MatriculaException {

    private static final long serialVersionUID = 1L;

    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
