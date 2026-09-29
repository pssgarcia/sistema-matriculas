package matriculas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import matriculas.excecao.AutenticacaoException;
import matriculas.excecao.CadastroInvalidoException;
import matriculas.excecao.DisciplinaIndisponivelException;
import matriculas.excecao.EntidadeNaoEncontradaException;
import matriculas.excecao.LimiteDisciplinasExcedidoException;
import matriculas.excecao.MatriculaDuplicadaException;
import matriculas.excecao.PeriodoFechadoException;
import matriculas.excecao.SemVagaDisponivelException;
import matriculas.modelo.Aluno;
import matriculas.modelo.Curso;
import matriculas.modelo.Disciplina;
import matriculas.modelo.Professor;
import matriculas.modelo.StatusDisciplina;
import matriculas.modelo.TipoDisciplina;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SistemaMatriculasTest {

    private static final String SEMESTRE = "2026/2";

    private Path pasta;
    private Ambiente ambiente;
    private Curso curso;
    private Professor professor;
    private final List<Disciplina> obrigatorias = new ArrayList<>();
    private final List<Disciplina> optativas = new ArrayList<>();

    @BeforeEach
    void preparar() throws IOException {
        Path raiz = Files.createDirectories(Path.of("target", "dados-teste"));
        pasta = Files.createTempDirectory(raiz, "teste-");
        ambiente = new Ambiente(pasta);
        curso = ambiente.secretaria.cadastrarCurso("ES", "Engenharia de Software", 240);
        professor = ambiente.secretaria.cadastrarProfessor("João", "joao", "123", "1001", "Computação");
        for (int i = 1; i <= 5; i++) {
            obrigatorias.add(ambiente.secretaria.cadastrarDisciplina("OB" + i, "Obrigatória " + i, 4,
                    TipoDisciplina.OBRIGATORIA, curso, professor));
        }
        for (int i = 1; i <= 3; i++) {
            optativas.add(ambiente.secretaria.cadastrarDisciplina("OP" + i, "Optativa " + i, 2,
                    TipoDisciplina.OPTATIVA, curso, professor));
        }
        List<Disciplina> oferta = new ArrayList<>(obrigatorias);
        oferta.addAll(optativas);
        ambiente.secretaria.gerarCurriculo(curso, SEMESTRE, oferta);
        ambiente.secretaria.abrirPeriodoMatriculas(SEMESTRE, LocalDate.now(), LocalDate.now().plusDays(10));
    }

    /** Remoção sem garantia: no Windows o antivírus pode manter algum arquivo aberto por instantes. */
    @AfterEach
    void limpar() throws IOException {
        try (Stream<Path> caminhos = Files.walk(pasta)) {
            caminhos.sorted(Comparator.reverseOrder()).forEach(caminho -> caminho.toFile().delete());
        }
    }

    private Aluno novoAluno(int numero) {
        return ambiente.secretaria.cadastrarAluno("Aluno " + numero, "aluno" + numero, "123", "M" + numero, curso);
    }

    @Test
    void autenticaApenasComSenhaCorreta() {
        assertSame(professor, ambiente.autenticacao.autenticar("joao", "123"));
        assertThrows(AutenticacaoException.class, () -> ambiente.autenticacao.autenticar("joao", "errada"));
        assertThrows(AutenticacaoException.class, () -> ambiente.autenticacao.autenticar("ninguem", "123"));
    }

    @Test
    void matriculaNotificaCobrancaEOcupaVaga() {
        Aluno aluno = novoAluno(1);
        ambiente.matriculas.matricular(aluno, obrigatorias.get(0));

        assertTrue(aluno.estaMatriculadoEm(obrigatorias.get(0)));
        assertEquals(Disciplina.MAXIMO_ALUNOS - 1, obrigatorias.get(0).getVagasRestantes());
        assertEquals(List.of("MATRICULA OB1"), ambiente.notificacoes);
    }

    @Test
    void limitaQuatroObrigatoriasEDuasOptativas() {
        Aluno aluno = novoAluno(1);
        for (int i = 0; i < 4; i++) {
            ambiente.matriculas.matricular(aluno, obrigatorias.get(i));
        }
        ambiente.matriculas.matricular(aluno, optativas.get(0));
        ambiente.matriculas.matricular(aluno, optativas.get(1));

        assertThrows(LimiteDisciplinasExcedidoException.class,
                () -> ambiente.matriculas.matricular(aluno, obrigatorias.get(4)));
        assertThrows(LimiteDisciplinasExcedidoException.class,
                () -> ambiente.matriculas.matricular(aluno, optativas.get(2)));
    }

    @Test
    void impedeMatriculaDuplicada() {
        Aluno aluno = novoAluno(1);
        ambiente.matriculas.matricular(aluno, obrigatorias.get(0));
        assertThrows(MatriculaDuplicadaException.class,
                () -> ambiente.matriculas.matricular(aluno, obrigatorias.get(0)));
    }

    @Test
    void impedeMatriculaEmDisciplinaLotada() {
        Disciplina disciplina = obrigatorias.get(0);
        for (int i = 1; i <= Disciplina.MAXIMO_ALUNOS; i++) {
            ambiente.matriculas.matricular(novoAluno(i), disciplina);
        }
        assertFalse(disciplina.temVagaDisponivel());
        assertThrows(SemVagaDisponivelException.class,
                () -> ambiente.matriculas.matricular(novoAluno(99), disciplina));
    }

    @Test
    void impedeMatriculaEmDisciplinaForaDoCurriculoDoCurso() {
        Curso outroCurso = ambiente.secretaria.cadastrarCurso("CC", "Ciência da Computação", 200);
        Aluno aluno = ambiente.secretaria.cadastrarAluno("Outro", "outro", "123", "X1", outroCurso);
        assertThrows(DisciplinaIndisponivelException.class,
                () -> ambiente.matriculas.matricular(aluno, obrigatorias.get(0)));
    }

    @Test
    void cancelamentoLiberaLimiteENotificaCobranca() {
        Aluno aluno = novoAluno(1);
        ambiente.matriculas.matricular(aluno, optativas.get(0));
        ambiente.matriculas.matricular(aluno, optativas.get(1));
        ambiente.matriculas.cancelarMatricula(aluno, optativas.get(0));

        assertFalse(aluno.estaMatriculadoEm(optativas.get(0)));
        assertEquals("CANCELAMENTO OP1", ambiente.notificacoes.get(2));
        ambiente.matriculas.matricular(aluno, optativas.get(2));
        assertThrows(EntidadeNaoEncontradaException.class,
                () -> ambiente.matriculas.cancelarMatricula(aluno, obrigatorias.get(0)));
    }

    @Test
    void encerramentoConfirmaDisciplinasComQuorumECancelaAsDemais() {
        Disciplina comQuorum = obrigatorias.get(0);
        Disciplina semQuorum = obrigatorias.get(1);
        for (int i = 1; i <= Disciplina.MINIMO_ALUNOS; i++) {
            ambiente.matriculas.matricular(novoAluno(i), comQuorum);
        }
        Aluno solitario = novoAluno(10);
        ambiente.matriculas.matricular(solitario, semQuorum);
        ambiente.notificacoes.clear();

        List<Disciplina> canceladas = ambiente.secretaria.encerrarPeriodoMatriculas(SEMESTRE);

        assertTrue(canceladas.contains(semQuorum));
        assertFalse(canceladas.contains(comQuorum));
        assertEquals(StatusDisciplina.CONFIRMADA, comQuorum.getStatus());
        assertEquals(StatusDisciplina.CANCELADA, semQuorum.getStatus());
        assertFalse(solitario.estaMatriculadoEm(semQuorum));
        assertEquals(List.of("CANCELAMENTO OB2"), ambiente.notificacoes);
        assertThrows(PeriodoFechadoException.class,
                () -> ambiente.matriculas.matricular(novoAluno(20), comQuorum));
    }

    @Test
    void naoPermiteDoisPeriodosAbertosNemLoginRepetido() {
        assertThrows(CadastroInvalidoException.class, () -> ambiente.secretaria
                .abrirPeriodoMatriculas("2027/1", LocalDate.now(), LocalDate.now().plusDays(5)));
        assertThrows(CadastroInvalidoException.class,
                () -> ambiente.secretaria.cadastrarProfessor("Outro", "joao", "1", "2", "X"));
    }

    @Test
    void professorConsultaApenasAlunosDasSuasDisciplinas() {
        Professor outro = ambiente.secretaria.cadastrarProfessor("Maria", "maria", "123", "1002", "Computação");
        Aluno aluno = novoAluno(1);
        ambiente.matriculas.matricular(aluno, obrigatorias.get(0));

        assertEquals(List.of(aluno), ambiente.professores.consultarAlunosMatriculados(professor, obrigatorias.get(0)));
        assertThrows(EntidadeNaoEncontradaException.class,
                () -> ambiente.professores.consultarAlunosMatriculados(outro, obrigatorias.get(0)));
    }

    @Test
    void dadosSobrevivemAoRecarregarOsArquivos() {
        Aluno aluno = ambiente.secretaria.cadastrarAluno("Ana; da Silva", "ana", "segredo", "M1", curso);
        ambiente.matriculas.matricular(aluno, obrigatorias.get(0));
        ambiente.matriculas.matricular(aluno, optativas.get(0));
        ambiente.matriculas.cancelarMatricula(aluno, optativas.get(0));

        Ambiente recarregado = new Ambiente(pasta);
        Aluno lido = (Aluno) recarregado.autenticacao.autenticar("ana", "segredo");
        Disciplina ob1 = recarregado.repositorioDisciplina.buscarPorCodigo("OB1").orElseThrow();

        assertEquals("Ana; da Silva", lido.getNome());
        assertEquals("ES", lido.getCurso().getCodigo());
        assertEquals(List.of(ob1), lido.consultarDisciplinasMatriculadas());
        assertEquals(2, recarregado.matriculas.consultarMatriculas(lido).size());
        assertEquals(StatusDisciplina.ABERTA, ob1.getStatus());
        assertTrue(recarregado.repositorioPeriodo.buscarPeriodoAberto().isPresent());
        assertEquals(8, recarregado.repositorioCurriculo.buscarPorSemestre(SEMESTRE).get(0).getDisciplinas().size());
        assertTrue(recarregado.professores.consultarDisciplinas(
                (Professor) recarregado.autenticacao.autenticar("joao", "123")).contains(ob1));
    }
}
