package matriculas.persistencia;

import matriculas.modelo.Curso;
import matriculas.repositorio.RepositorioCurso;
import java.nio.file.Path;
import java.util.Optional;

public class RepositorioCursoArquivo extends RepositorioArquivo<Curso> implements RepositorioCurso {

    public RepositorioCursoArquivo(Path arquivo) {
        super(arquivo);
        carregar();
    }

    @Override
    public Optional<Curso> buscarPorCodigo(String codigo) {
        return todas().filter(curso -> curso.getCodigo().equalsIgnoreCase(codigo)).findFirst();
    }

    @Override
    protected Long idDe(Curso curso) {
        return curso.getId();
    }

    @Override
    protected void atribuirId(Curso curso, Long id) {
        curso.setId(id);
    }

    @Override
    protected String serializar(Curso curso) {
        return FormatoArquivo.juntar(curso.getId(), curso.getCodigo(), curso.getNome(), curso.getCreditos());
    }

    @Override
    protected Curso desserializar(String[] campos) {
        return new Curso(Long.valueOf(campos[0]), campos[1], campos[2], Integer.parseInt(campos[3]));
    }
}
