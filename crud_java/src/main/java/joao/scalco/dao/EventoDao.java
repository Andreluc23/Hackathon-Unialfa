package joao.scalco.dao;

import joao.scalco.model.EventoModel;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class EventoDao extends Dao implements DaoInterface {

    @Override
    public boolean save(Object entity) {
        try {
            var evento = (EventoModel) entity;

            String sqlInsert = "INSERT INTO evento(nome, descricao, data_ini, data_fim, local, palestrante_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = getConnection().prepareStatement(sqlInsert);
            ps.setString(1, evento.getNome());
            ps.setString(2, evento.getDescricao());
            ps.setTimestamp(3, evento.getDataInicio());
            ps.setTimestamp(4, evento.getDataFim());
            ps.setString(5, evento.getLocal());
            ps.setLong(6, evento.getPalestranteId());
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
            var evento = (EventoModel) entity;

            String sqlUpdate = "UPDATE evento SET nome=?, descricao=?, data_ini=?, data_fim=?, local=?, palestrante_id=? WHERE eve_id=?";
            PreparedStatement ps = getConnection().prepareStatement(sqlUpdate);
            ps.setString(1, evento.getNome());
            ps.setString(2, evento.getDescricao());
            ps.setTimestamp(3, evento.getDataInicio());
            ps.setTimestamp(4, evento.getDataFim());
            ps.setString(5, evento.getLocal());
            ps.setLong(6, evento.getPalestranteId());
            ps.setLong(7, evento.getId());
            ps.execute();

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Object> select() {
        List<EventoModel> eventos = new ArrayList<>();

        try {
            var resultSet = getConnection()
                    .prepareStatement("SELECT * FROM evento")
                    .executeQuery();

            while (resultSet.next()) {
                var evento = new EventoModel(
                        resultSet.getLong("eve_id"),
                        resultSet.getString("nome"),
                        resultSet.getString("descricao"),
                        resultSet.getTimestamp("data_ini"),
                        resultSet.getTimestamp("data_fim"),
                        resultSet.getString("local"),
                        resultSet.getLong("palestrante_id")
                );
                eventos.add(evento);
            }

            resultSet.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return new ArrayList<>(eventos);
    }

    @Override
    public Object selectId(Long id) {
        EventoModel evento = new EventoModel();
        try {
            String sqlRequest = "SELECT * FROM evento WHERE eve_id=?";
            PreparedStatement ps = getConnection().prepareStatement(sqlRequest);
            ps.setLong(1, id);
            var rs = ps.executeQuery();

            while (rs.next()) {
                evento.setId(rs.getLong("eve_id"));
                evento.setNome(rs.getString("nome"));
                evento.setDescricao(rs.getString("descricao"));
                evento.setDataInicio(rs.getTimestamp("data_ini"));
                evento.setDataFim(rs.getTimestamp("data_fim"));
                evento.setLocal(rs.getString("local"));
                evento.setPalestranteId(rs.getLong("palestrante_id"));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return evento;
    }

    @Override
    public boolean delete(Long id) {
        try {
            String sqlDelete = "DELETE FROM evento WHERE eve_id=?";
            PreparedStatement ps = getConnection().prepareStatement(sqlDelete);
            ps.setLong(1, id);
            ps.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
