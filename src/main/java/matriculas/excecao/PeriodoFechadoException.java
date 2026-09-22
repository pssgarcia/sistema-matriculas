package matriculas.excecao;

public class PeriodoFechadoException extends MatriculaException {

    private static final long serialVersionUID = 1L;

    public PeriodoFechadoException(String mensagem) {
        super(mensagem);
    }
}
