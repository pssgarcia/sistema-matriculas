package matriculas.excecao;

public class SemVagaDisponivelException extends MatriculaException {

    private static final long serialVersionUID = 1L;

    public SemVagaDisponivelException(String mensagem) {
        super(mensagem);
    }
}
