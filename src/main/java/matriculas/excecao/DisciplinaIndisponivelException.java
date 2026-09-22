package matriculas.excecao;

public class DisciplinaIndisponivelException extends MatriculaException {

    private static final long serialVersionUID = 1L;

    public DisciplinaIndisponivelException(String mensagem) {
        super(mensagem);
    }
}
