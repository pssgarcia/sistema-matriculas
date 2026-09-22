package matriculas.excecao;

public class AutenticacaoException extends MatriculaException {

    private static final long serialVersionUID = 1L;

    public AutenticacaoException(String mensagem) {
        super(mensagem);
    }
}
