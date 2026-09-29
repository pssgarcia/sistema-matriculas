package matriculas.persistencia;

import matriculas.modelo.Aluno;
import matriculas.modelo.Disciplina;
import matriculas.modelo.Matricula;
import matriculas.modelo.StatusMatricula;
import matriculas.repositorio.RepositorioDisciplina;
import matriculas.repositorio.RepositorioMatricula;
import matriculas.repositorio.RepositorioUsuario;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RepositorioMatriculaArquivo extends RepositorioArquivo<Matricula> implements RepositorioMatricula {

    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioDisciplina repositorioDisciplina;

    public RepositorioMatriculaArquivo(Path arquivo, RepositorioUsuario repositorioUsuario,
                                       RepositorioDisciplina repositorioDisciplina) {
        super(arquivo);
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioDisciplina = repositorioDisciplina;
        carregar();
    }

    @Override
    public List<Matricula> buscarPorAluno(Aluno aluno) {
        return todas().filter(matricula -> aluno.equals(matricula.getAluno())).toList();
    }

    @Override
    public List<Matricula> buscarPorDisciplina(Disciplina disciplina) {
        return todas().filter(matricula -> matricula.getDisciplina() == disciplina).toList();
    }

    @Override
    public Optional<Matricula> buscarPorAlunoEDisciplina(Aluno aluno, Disciplina disciplina) {
        List<Matricula> encontradas = todas()
                .filter(matricula -> aluno.equals(matricula.getAluno()))
                .filter(matricula -> matricula.getDisciplina() == disciplina)
                .toList();
        return encontradas.stream()
                .filter(Matricula::estaAtiva)
                .findFirst()
                .or(() -> encontradas.stream().reduce((primeira, ultima) -> ultima));
    }

    @Override
    protected Long idDe(Matricula matricula) {
        return matricula.getId();
    }

    @Override
    protected void atribuirId(Matricula matricula, Long id) {
        matricula.setId(id);
    }

    @Override
    protected String serializar(Matricula matricula) {
        return FormatoArquivo.juntar(matricula.getId(), matricula.getAluno().getId(),
                matricula.getDisciplina().getId(), matricula.getDataMatricula(), matricula.getDataCancelamento(),
                matricula.getStatus());
    }

    @Override
    protected Matricula desserializar(String[] campos) {
        Long alunoId = Long.valueOf(campos[1]);
        Long disciplinaId = Long.valueOf(campos[2]);
        Aluno aluno = repositorioUsuario.buscarPorId(alunoId)
                .filter(Aluno.class::isInstance)
                .map(Aluno.class::cast)
                .orElseThrow(() -> new IllegalStateException("Aluno " + alunoId + " não encontrado"));
        Disciplina disciplina = repositorioDisciplina.buscarPorId(disciplinaId)
                .orElseThrow(() -> new IllegalStateException("Disciplina " + disciplinaId + " não encontrada"));

        Matricula matricula = new Matricula(Long.valueOf(campos[0]), aluno, disciplina,
                LocalDateTime.parse(campos[3]));
        String dataCancelamento = FormatoArquivo.paraTexto(campos[4]);
        if (dataCancelamento != null) {
            matricula.setDataCancelamento(LocalDateTime.parse(dataCancelamento));
        }
        matricula.setStatus(StatusMatricula.valueOf(campos[5]));
        aluno.getMatriculas().add(matricula);
        disciplina.adicionarMatricula(matricula);
        return matricula;
    }
}
