package matriculas.cli;

import matriculas.modelo.Aluno;
import matriculas.modelo.Disciplina;
import matriculas.modelo.Matricula;
import matriculas.modelo.TipoDisciplina;
import java.util.List;

public class MenuAluno extends Menu {

    private final Aluno aluno;

    public MenuAluno(Contexto contexto, Aluno aluno) {
        super(contexto);
        this.aluno = aluno;
    }

    @Override
    protected String titulo() {
        return "Aluno: " + aluno.getNome() + " (" + aluno.getMatricula() + " - " + aluno.getCurso().getNome() + ")";
    }

    @Override
    protected List<Opcao> opcoes() {
        return List.of(
                new Opcao("Consultar disciplinas ofertadas", this::consultarDisciplinasOfertadas),
                new Opcao("Matricular-se em disciplina", this::matricular),
                new Opcao("Cancelar matrícula", this::cancelarMatricula),
                new Opcao("Consultar minhas matrículas", this::consultarMatriculas),
                new Opcao("Alterar senha", () -> alterarSenha(aluno)));
    }

    @Override
    protected String rotuloSaida() {
        return "Sair (logout)";
    }

    private void consultarDisciplinasOfertadas() {
        List<Disciplina> ofertadas = contexto.servicoMatricula().consultarDisciplinasOfertadas(aluno);
        console.tabela(List.of("Código", "Disciplina", "Tipo", "Créditos", "Professor", "Vagas", "Situação"),
                ofertadas.stream().map(disciplina -> List.of(
                        disciplina.getCodigo(),
                        disciplina.getNome(),
                        Formatos.tipo(disciplina.getTipo()),
                        String.valueOf(disciplina.getCreditos()),
                        disciplina.getProfessor().getNome(),
                        String.valueOf(disciplina.getVagasRestantes()),
                        aluno.estaMatriculadoEm(disciplina) ? "matriculado" : "")).toList());
        exibirLimites();
    }

    private void matricular() {
        Disciplina disciplina = contexto.disciplinaPorCodigo(console.lerTexto("Código da disciplina"));
        contexto.servicoMatricula().matricular(aluno, disciplina);
        console.sucesso("Matrícula em " + disciplina.getCodigo() + " - " + disciplina.getNome()
                + " realizada. O sistema de cobrança foi notificado.");
        exibirLimites();
    }

    private void cancelarMatricula() {
        List<Disciplina> matriculadas = aluno.consultarDisciplinasMatriculadas();
        if (matriculadas.isEmpty()) {
            console.mensagem("Você não possui matrículas ativas.");
            return;
        }
        matriculadas.forEach(d -> console.mensagem("  " + d.getCodigo() + " - " + d.getNome()));
        Disciplina disciplina = contexto.disciplinaPorCodigo(console.lerTexto("Código da disciplina"));
        if (console.confirmar("Confirma o cancelamento da matrícula em " + disciplina.getCodigo() + "?")) {
            contexto.servicoMatricula().cancelarMatricula(aluno, disciplina);
            console.sucesso("Matrícula cancelada. O sistema de cobrança foi notificado.");
        }
    }

    private void consultarMatriculas() {
        List<Matricula> matriculas = contexto.servicoMatricula().consultarMatriculas(aluno);
        console.tabela(List.of("Código", "Disciplina", "Tipo", "Créditos", "Matriculado em", "Situação"),
                matriculas.stream().map(matricula -> List.of(
                        matricula.getDisciplina().getCodigo(),
                        matricula.getDisciplina().getNome(),
                        Formatos.tipo(matricula.getDisciplina().getTipo()),
                        String.valueOf(matricula.getDisciplina().getCreditos()),
                        matricula.getDataMatricula().format(Console.FORMATO_DATA_HORA),
                        Formatos.situacao(matricula))).toList());
        exibirLimites();
    }

    private void exibirLimites() {
        console.mensagem(String.format("%nObrigatórias: %d de %d | Optativas: %d de %d",
                aluno.totalMatriculadasPorTipo(TipoDisciplina.OBRIGATORIA),
                TipoDisciplina.OBRIGATORIA.getLimitePorAluno(),
                aluno.totalMatriculadasPorTipo(TipoDisciplina.OPTATIVA),
                TipoDisciplina.OPTATIVA.getLimitePorAluno()));
    }
}
