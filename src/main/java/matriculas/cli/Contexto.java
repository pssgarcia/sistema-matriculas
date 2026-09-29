package matriculas.cli;

import matriculas.excecao.EntidadeNaoEncontradaException;
import matriculas.modelo.Curso;
import matriculas.modelo.Disciplina;
import matriculas.repositorio.RepositorioCurriculo;
import matriculas.repositorio.RepositorioCurso;
import matriculas.repositorio.RepositorioDisciplina;
import matriculas.repositorio.RepositorioPeriodoMatricula;
import matriculas.repositorio.RepositorioUsuario;
import matriculas.servico.ServicoAutenticacao;
import matriculas.servico.ServicoMatricula;
import matriculas.servico.ServicoProfessor;
import matriculas.servico.ServicoSecretaria;

/**
 * Agrupa os serviços usados pelas telas e os repositórios usados apenas para consultas
 * (localizar entidades pelo código digitado e montar listagens).
 */
public record Contexto(Console console,
                       ServicoAutenticacao servicoAutenticacao,
                       ServicoMatricula servicoMatricula,
                       ServicoSecretaria servicoSecretaria,
                       ServicoProfessor servicoProfessor,
                       RepositorioUsuario repositorioUsuario,
                       RepositorioCurso repositorioCurso,
                       RepositorioDisciplina repositorioDisciplina,
                       RepositorioCurriculo repositorioCurriculo,
                       RepositorioPeriodoMatricula repositorioPeriodoMatricula) {

    public Disciplina disciplinaPorCodigo(String codigo) {
        return repositorioDisciplina.buscarPorCodigo(codigo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Disciplina " + codigo + " não encontrada."));
    }

    public Curso cursoPorCodigo(String codigo) {
        return repositorioCurso.buscarPorCodigo(codigo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Curso " + codigo + " não encontrado."));
    }
}
