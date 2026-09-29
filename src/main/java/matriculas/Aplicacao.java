package matriculas;

import matriculas.cli.Console;
import matriculas.cli.Contexto;
import matriculas.cli.EntradaEncerradaException;
import matriculas.cli.TelaInicial;
import matriculas.integracao.SistemaCobranca;
import matriculas.integracao.SistemaCobrancaAdapter;
import matriculas.persistencia.PersistenciaException;
import matriculas.persistencia.RepositorioCurriculoArquivo;
import matriculas.persistencia.RepositorioCursoArquivo;
import matriculas.persistencia.RepositorioDisciplinaArquivo;
import matriculas.persistencia.RepositorioMatriculaArquivo;
import matriculas.persistencia.RepositorioPeriodoMatriculaArquivo;
import matriculas.persistencia.RepositorioUsuarioArquivo;
import matriculas.servico.ServicoAutenticacao;
import matriculas.servico.ServicoMatricula;
import matriculas.servico.ServicoProfessor;
import matriculas.servico.ServicoSecretaria;
import java.nio.file.Path;

public class Aplicacao {

    public static void main(String[] args) {
        Path pastaDados = Path.of(args.length > 0 ? args[0] : System.getProperty("matriculas.dados", "dados"));
        Console console = new Console();
        try {
            Contexto contexto = montar(pastaDados, console);
            new TelaInicial(contexto).executar();
        } catch (EntradaEncerradaException e) {
            console.mensagem("");
            console.mensagem("Entrada encerrada. Até logo!");
        } catch (PersistenciaException e) {
            console.erro(e.getMessage() + ": " + e.getCause().getMessage());
            System.exit(1);
        }
    }

    static Contexto montar(Path pastaDados, Console console) {
        RepositorioCursoArquivo repositorioCurso = new RepositorioCursoArquivo(pastaDados.resolve("cursos.txt"));
        RepositorioUsuarioArquivo repositorioUsuario =
                new RepositorioUsuarioArquivo(pastaDados.resolve("usuarios.txt"), repositorioCurso);
        RepositorioDisciplinaArquivo repositorioDisciplina = new RepositorioDisciplinaArquivo(
                pastaDados.resolve("disciplinas.txt"), repositorioCurso, repositorioUsuario);
        RepositorioCurriculoArquivo repositorioCurriculo = new RepositorioCurriculoArquivo(
                pastaDados.resolve("curriculos.txt"), repositorioCurso, repositorioDisciplina);
        RepositorioPeriodoMatriculaArquivo repositorioPeriodo =
                new RepositorioPeriodoMatriculaArquivo(pastaDados.resolve("periodos.txt"));
        RepositorioMatriculaArquivo repositorioMatricula = new RepositorioMatriculaArquivo(
                pastaDados.resolve("matriculas.txt"), repositorioUsuario, repositorioDisciplina);
        SistemaCobranca sistemaCobranca = new SistemaCobrancaAdapter(pastaDados.resolve("cobranca.txt"));

        ServicoAutenticacao servicoAutenticacao = new ServicoAutenticacao(repositorioUsuario);
        ServicoMatricula servicoMatricula = new ServicoMatricula(repositorioMatricula, repositorioCurriculo,
                repositorioPeriodo, sistemaCobranca);
        ServicoSecretaria servicoSecretaria = new ServicoSecretaria(repositorioCurso, repositorioDisciplina,
                repositorioUsuario, repositorioCurriculo, repositorioPeriodo, repositorioMatricula, sistemaCobranca);
        ServicoProfessor servicoProfessor = new ServicoProfessor(repositorioDisciplina, repositorioMatricula);

        if (repositorioUsuario.buscarTodos().isEmpty()) {
            DadosIniciais.carregar(repositorioUsuario, servicoSecretaria, servicoMatricula);
            console.mensagem("Primeira execução: dados de exemplo gravados em " + pastaDados.toAbsolutePath());
        }

        return new Contexto(console, servicoAutenticacao, servicoMatricula, servicoSecretaria, servicoProfessor,
                repositorioUsuario, repositorioCurso, repositorioDisciplina, repositorioCurriculo, repositorioPeriodo);
    }
}
