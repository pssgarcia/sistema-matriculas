package matriculas;

import matriculas.modelo.Aluno;
import matriculas.modelo.Curso;
import matriculas.modelo.Disciplina;
import matriculas.modelo.Professor;
import matriculas.modelo.Secretaria;
import matriculas.modelo.TipoDisciplina;
import matriculas.repositorio.RepositorioUsuario;
import matriculas.servico.ServicoMatricula;
import matriculas.servico.ServicoSecretaria;
import java.time.LocalDate;
import java.util.List;

/**
 * Popula os arquivos na primeira execução para que o protótipo possa ser usado de imediato.
 * Todas as senhas de exemplo são "123", exceto a da secretaria ("admin").
 */
public final class DadosIniciais {

    public static final String SEMESTRE = "2026/2";

    private DadosIniciais() {
    }

    public static void carregar(RepositorioUsuario repositorioUsuario, ServicoSecretaria secretaria,
                                ServicoMatricula servicoMatricula) {
        repositorioUsuario.salvar(new Secretaria(null, "Secretaria Acadêmica", "admin", "admin", "Registro Acadêmico"));

        Curso engenharia = secretaria.cadastrarCurso("ES", "Engenharia de Software", 240);
        Curso computacao = secretaria.cadastrarCurso("CC", "Ciência da Computação", 220);

        Professor joao = secretaria.cadastrarProfessor("João Pereira", "joao", "123", "1001", "Computação");
        Professor maria = secretaria.cadastrarProfessor("Maria Souza", "maria", "123", "1002", "Computação");
        Professor paulo = secretaria.cadastrarProfessor("Paulo Lima", "paulo", "123", "1003", "Matemática");

        List<Disciplina> ofertaEs = List.of(
                secretaria.cadastrarDisciplina("ES101", "Algoritmos e Estruturas de Dados", 4,
                        TipoDisciplina.OBRIGATORIA, engenharia, joao),
                secretaria.cadastrarDisciplina("ES102", "Cálculo I", 4, TipoDisciplina.OBRIGATORIA, engenharia, paulo),
                secretaria.cadastrarDisciplina("ES103", "Laboratório de Desenvolvimento de Software", 4,
                        TipoDisciplina.OBRIGATORIA, engenharia, maria),
                secretaria.cadastrarDisciplina("ES104", "Engenharia de Requisitos", 2,
                        TipoDisciplina.OBRIGATORIA, engenharia, maria),
                secretaria.cadastrarDisciplina("ES105", "Banco de Dados", 4, TipoDisciplina.OBRIGATORIA,
                        engenharia, joao),
                secretaria.cadastrarDisciplina("ES201", "Computação Gráfica", 2, TipoDisciplina.OPTATIVA,
                        engenharia, joao),
                secretaria.cadastrarDisciplina("ES202", "Desenvolvimento de Jogos", 2, TipoDisciplina.OPTATIVA,
                        engenharia, maria),
                secretaria.cadastrarDisciplina("ES203", "Inteligência Artificial", 2, TipoDisciplina.OPTATIVA,
                        engenharia, paulo));
        List<Disciplina> ofertaCc = List.of(
                secretaria.cadastrarDisciplina("CC101", "Fundamentos de Programação", 4,
                        TipoDisciplina.OBRIGATORIA, computacao, joao),
                secretaria.cadastrarDisciplina("CC201", "Teoria dos Grafos", 2, TipoDisciplina.OPTATIVA,
                        computacao, paulo));

        Aluno ana = secretaria.cadastrarAluno("Ana Costa", "ana", "123", "2026001", engenharia);
        Aluno bruno = secretaria.cadastrarAluno("Bruno Alves", "bruno", "123", "2026002", engenharia);
        Aluno carla = secretaria.cadastrarAluno("Carla Mendes", "carla", "123", "2026003", engenharia);
        secretaria.cadastrarAluno("Diego Rocha", "diego", "123", "2026004", engenharia);
        secretaria.cadastrarAluno("Elisa Martins", "elisa", "123", "2026005", computacao);

        secretaria.gerarCurriculo(engenharia, SEMESTRE, ofertaEs);
        secretaria.gerarCurriculo(computacao, SEMESTRE, ofertaCc);
        LocalDate hoje = LocalDate.now();
        secretaria.abrirPeriodoMatriculas(SEMESTRE, hoje.minusDays(7), hoje.plusDays(30));

        Disciplina algoritmos = ofertaEs.get(0);
        Disciplina requisitos = ofertaEs.get(3);
        Disciplina computacaoGrafica = ofertaEs.get(5);
        for (Aluno aluno : List.of(ana, bruno, carla)) {
            servicoMatricula.matricular(aluno, algoritmos);
        }
        servicoMatricula.matricular(ana, requisitos);
        servicoMatricula.matricular(ana, computacaoGrafica);
    }
}
