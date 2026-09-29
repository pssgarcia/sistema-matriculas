package matriculas.excecao;

public class CadastroInvalidoException extends MatriculaException {

    private static final long serialVersionUID = 1L;

    public CadastroInvalidoException(String mensagem) {
        super(mensagem);
    }
}
