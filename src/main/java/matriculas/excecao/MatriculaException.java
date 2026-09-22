package matriculas.excecao;

public class MatriculaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MatriculaException(String mensagem) {
        super(mensagem);
    }

    public MatriculaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
