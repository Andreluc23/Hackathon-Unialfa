package joao.scalco.dao;

import java.util.List;

public interface DaoInterface {

    boolean save(Object entity);

    boolean update(Object entity);

    List<Object> select();

    Object selectId(Long id);

    boolean delete(Long id);
}
