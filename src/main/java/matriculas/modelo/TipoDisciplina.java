package matriculas.modelo;

public enum TipoDisciplina {

    OBRIGATORIA(4),
    OPTATIVA(2);

    private final int limitePorAluno;

    TipoDisciplina(int limitePorAluno) {
        this.limitePorAluno = limitePorAluno;
    }

    public int getLimitePorAluno() {
        return limitePorAluno;
    }
}
