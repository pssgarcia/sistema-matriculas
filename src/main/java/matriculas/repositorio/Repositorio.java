package matriculas.repositorio;

import java.util.List;
import java.util.Optional;

public interface Repositorio<T, ID> {

    T salvar(T entidade);

    Optional<T> buscarPorId(ID id);

    List<T> buscarTodos();

    void remover(ID id);
}
