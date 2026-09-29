package matriculas.persistencia;

import matriculas.repositorio.Repositorio;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Repositório mantido em memória e espelhado em um arquivo texto, uma entidade por linha.
 * O arquivo é regravado a cada alteração; as referências a outras entidades são gravadas
 * pelo id e resolvidas na carga pelos repositórios dos quais este depende.
 */
public abstract class RepositorioArquivo<T> implements Repositorio<T, Long> {

    private final Path arquivo;
    private final Map<Long, T> entidades = new LinkedHashMap<>();
    private long proximoId = 1;

    protected RepositorioArquivo(Path arquivo) {
        this.arquivo = arquivo;
    }

    protected abstract Long idDe(T entidade);

    protected abstract void atribuirId(T entidade, Long id);

    protected abstract String serializar(T entidade);

    protected abstract T desserializar(String[] campos);

    /** Deve ser chamado ao final do construtor das subclasses, após as dependências estarem atribuídas. */
    protected final void carregar() {
        if (!Files.exists(arquivo)) {
            return;
        }
        try {
            for (String linha : Files.readAllLines(arquivo, StandardCharsets.UTF_8)) {
                if (!linha.isBlank()) {
                    T entidade = desserializar(FormatoArquivo.separar(linha));
                    registrar(entidade);
                }
            }
        } catch (IOException | RuntimeException e) {
            throw new PersistenciaException("Falha ao carregar o arquivo " + arquivo, e);
        }
    }

    @Override
    public T salvar(T entidade) {
        if (idDe(entidade) == null) {
            atribuirId(entidade, proximoId);
        }
        registrar(entidade);
        persistir();
        return entidade;
    }

    @Override
    public Optional<T> buscarPorId(Long id) {
        return Optional.ofNullable(entidades.get(id));
    }

    @Override
    public List<T> buscarTodos() {
        return List.copyOf(entidades.values());
    }

    @Override
    public void remover(Long id) {
        if (entidades.remove(id) != null) {
            persistir();
        }
    }

    protected Stream<T> todas() {
        return entidades.values().stream();
    }

    private void registrar(T entidade) {
        Long id = idDe(entidade);
        entidades.put(id, entidade);
        proximoId = Math.max(proximoId, id + 1);
    }

    private void persistir() {
        List<String> linhas = new ArrayList<>();
        entidades.values().forEach(entidade -> linhas.add(serializar(entidade)));
        try {
            if (arquivo.getParent() != null) {
                Files.createDirectories(arquivo.getParent());
            }
            Path temporario = arquivo.resolveSibling(arquivo.getFileName() + ".tmp");
            Files.write(temporario, linhas, StandardCharsets.UTF_8);
            Files.move(temporario, arquivo, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new PersistenciaException("Falha ao gravar o arquivo " + arquivo, e);
        }
    }
}
