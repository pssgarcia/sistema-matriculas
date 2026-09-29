package matriculas;

import matriculas.integracao.SistemaCobranca;
import matriculas.modelo.Matricula;
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
import java.util.ArrayList;
import java.util.List;

/** Monta repositórios em arquivo e serviços sobre uma pasta temporária, registrando as notificações de cobrança. */
class Ambiente {

    final RepositorioCursoArquivo repositorioCurso;
    final RepositorioUsuarioArquivo repositorioUsuario;
    final RepositorioDisciplinaArquivo repositorioDisciplina;
    final RepositorioCurriculoArquivo repositorioCurriculo;
    final RepositorioPeriodoMatriculaArquivo repositorioPeriodo;
    final RepositorioMatriculaArquivo repositorioMatricula;
    final List<String> notificacoes = new ArrayList<>();
    final ServicoAutenticacao autenticacao;
    final ServicoMatricula matriculas;
    final ServicoSecretaria secretaria;
    final ServicoProfessor professores;

    Ambiente(Path pasta) {
        repositorioCurso = new RepositorioCursoArquivo(pasta.resolve("cursos.txt"));
        repositorioUsuario = new RepositorioUsuarioArquivo(pasta.resolve("usuarios.txt"), repositorioCurso);
        repositorioDisciplina = new RepositorioDisciplinaArquivo(pasta.resolve("disciplinas.txt"),
                repositorioCurso, repositorioUsuario);
        repositorioCurriculo = new RepositorioCurriculoArquivo(pasta.resolve("curriculos.txt"),
                repositorioCurso, repositorioDisciplina);
        repositorioPeriodo = new RepositorioPeriodoMatriculaArquivo(pasta.resolve("periodos.txt"));
        repositorioMatricula = new RepositorioMatriculaArquivo(pasta.resolve("matriculas.txt"),
                repositorioUsuario, repositorioDisciplina);
        SistemaCobranca cobranca = new SistemaCobranca() {
            @Override
            public void notificarMatricula(Matricula matricula) {
                notificacoes.add("MATRICULA " + matricula.getDisciplina().getCodigo());
            }

            @Override
            public void notificarCancelamento(Matricula matricula) {
                notificacoes.add("CANCELAMENTO " + matricula.getDisciplina().getCodigo());
            }
        };
        autenticacao = new ServicoAutenticacao(repositorioUsuario);
        matriculas = new ServicoMatricula(repositorioMatricula, repositorioCurriculo, repositorioPeriodo, cobranca);
        secretaria = new ServicoSecretaria(repositorioCurso, repositorioDisciplina, repositorioUsuario,
                repositorioCurriculo, repositorioPeriodo, repositorioMatricula, cobranca);
        professores = new ServicoProfessor(repositorioDisciplina, repositorioMatricula);
    }
}
