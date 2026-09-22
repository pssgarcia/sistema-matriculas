package matriculas.excecao;

public class MatriculaDuplicadaException extends MatriculaException {

    private static final long serialVersionUID = 1L;

    public MatriculaDuplicadaException(String mensagem) {
        super(mensagem);
    }
}
