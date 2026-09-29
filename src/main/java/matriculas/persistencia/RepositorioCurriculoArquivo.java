package matriculas.persistencia;

import matriculas.modelo.Curriculo;
import matriculas.modelo.Curso;
import matriculas.modelo.Disciplina;
import matriculas.repositorio.RepositorioCurriculo;
import matriculas.repositorio.RepositorioCurso;
import matriculas.repositorio.RepositorioDisciplina;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RepositorioCurriculoArquivo extends RepositorioArquivo<Curriculo> implements RepositorioCurriculo {

    private static final String SEPARADOR_IDS = ",";

    private final RepositorioCurso repositorioCurso;
    private final RepositorioDisciplina repositorioDisciplina;

    public RepositorioCurriculoArquivo(Path arquivo, RepositorioCurso repositorioCurso,
                                       RepositorioDisciplina repositorioDisciplina) {
        super(arquivo);
        this.repositorioCurso = repositorioCurso;
        this.repositorioDisciplina = repositorioDisciplina;
        carregar();
    }

    @Override
    public List<Curriculo> buscarPorSemestre(String semestre) {
        return todas().filter(curriculo -> curriculo.getSemestre().equalsIgnoreCase(semestre)).toList();
    }

    @Override
    public Optional<Curriculo> buscarPorCursoESemestre(Curso curso, String semestre) {
        return todas()
                .filter(curriculo -> curriculo.getCurso() == curso)
                .filter(curriculo -> curriculo.getSemestre().equalsIgnoreCase(semestre))
                .findFirst();
    }

    @Override
    protected Long idDe(Curriculo curriculo) {
        return curriculo.getId();
    }

    @Override
    protected void atribuirId(Curriculo curriculo, Long id) {
        curriculo.setId(id);
    }

    @Override
    protected String serializar(Curriculo curriculo) {
        String idsDisciplinas = curriculo.getDisciplinas().stream()
                .map(disciplina -> disciplina.getId().toString())
                .collect(Collectors.joining(SEPARADOR_IDS));
        return FormatoArquivo.juntar(curriculo.getId(), curriculo.getSemestre(), curriculo.getCurso().getId(),
                idsDisciplinas);
    }

    @Override
    protected Curriculo desserializar(String[] campos) {
        Long cursoId = Long.valueOf(campos[2]);
        Curso curso = repositorioCurso.buscarPorId(cursoId)
                .orElseThrow(() -> new IllegalStateException("Curso " + cursoId + " não encontrado"));
        Curriculo curriculo = new Curriculo(Long.valueOf(campos[0]), campos[1], curso);
        if (!campos[3].isEmpty()) {
            for (String id : campos[3].split(SEPARADOR_IDS)) {
                Disciplina disciplina = repositorioDisciplina.buscarPorId(Long.valueOf(id))
                        .orElseThrow(() -> new IllegalStateException("Disciplina " + id + " não encontrada"));
                curriculo.adicionarDisciplina(disciplina);
            }
        }
        return curriculo;
    }
}
