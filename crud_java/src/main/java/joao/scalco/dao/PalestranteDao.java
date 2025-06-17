package joao.scalco.dao;

import joao.scalco.model.PalestranteModel;

import java.util.ArrayList;
import java.util.List;

public class PalestranteDao extends Dao implements DaoInterface {
    @Override
    public boolean save(Object entity) {
        try {
            var palestrante = (PalestranteModel) entity;

            String sqlInsert = "insert into palestrante(nome,foto_url,descricao,especilizacao,contato) values(?,?,?,?,?)";
            var ps = getConnection().prepareStatement(sqlInsert);
            ps.setString(1, palestrante.getNome());
            ps.setString(2, palestrante.getFoto());
            ps.setString(3, palestrante.getDescricao());
            ps.setString(4, palestrante.getEspecializacao());
            ps.setString(5, palestrante.getEmail());
            ps.execute();

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Object entity) {
        try {
            var palestrante = (PalestranteModel) entity;

            String sqlUpdate = "UPDATE palestrante SET nome=?, foto_url=?, descricao=?, especializacao=?, contato=? WHERE palestrante_id=?";

            var ps = getConnection().prepareStatement(sqlUpdate);
            ps.setString(1, palestrante.getNome());
            ps.setString(2, palestrante.getFoto());
            ps.setString(3, palestrante.getDescricao());
            ps.setString(4, palestrante.getEspecializacao());
            ps.setString(5, palestrante.getEmail());
            ps.setLong(6, palestrante.getId()); // Fixed: Added the ID parameter
            ps.execute();

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Object> select() {
        List<PalestranteModel> palestrantes = new ArrayList<>();

        try {
            var resultSet = getConnection()
                    .prepareStatement("select * from palestrante")
                    .executeQuery();

            while (resultSet.next()) {
                var palestrante = new PalestranteModel(
                        resultSet.getLong("palestrante_id"), // Fixed: Use correct column name
                        resultSet.getString("nome"),
                        resultSet.getString("foto_url"),
                        resultSet.getString("descricao"),
                        resultSet.getString("especilizacao"),
                        resultSet.getString("contato")
                );
                palestrantes.add(palestrante);
            }

            resultSet.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return new ArrayList<>(palestrantes);
    }

    @Override
    public Object selectId(Long id) {
        var palestrante = new PalestranteModel();
        try {
            var sqlRequest = "select * from palestrante where palestrante_id=?"; // Fixed: Added column name
            var ps = getConnection().prepareStatement(sqlRequest);
            ps.setLong(1, id);
            var rs = ps.executeQuery();

            while (rs.next()) {
                palestrante.setId(rs.getLong("palestrante_id")); // Fixed: Use correct column name
                palestrante.setNome(rs.getString("nome"));
                palestrante.setDescricao(rs.getString("descricao"));
                palestrante.setFoto(rs.getString("foto_url"));
                palestrante.setEspecializacao(rs.getString("especilizacao"));
                palestrante.setEmail(rs.getString("contato"));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return palestrante;
    }

    @Override
    public boolean delete(Long id) {
        try{
            String sqlDelete = "DELETE FROM palestrante WHERE palestrante_id=?";
            var ps = getConnection().prepareStatement(sqlDelete);
            ps.setLong(1, id);
            ps.execute();
            return true;
        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}
