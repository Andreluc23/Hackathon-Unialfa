package joao.scalco.service;

import joao.scalco.dao.EventoDao;
import joao.scalco.model.EventoModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EventoService {

    public Boolean salvar(EventoModel evento) {
        var dao = new EventoDao();
        return evento.getId() == null
                ? dao.save(evento)
                : dao.update(evento);
    }

    public List<EventoModel> listar() {
        List<EventoModel> eventos = new ArrayList<>();
        var dao = new EventoDao();

        dao.select().forEach(object -> eventos.add((EventoModel) object));
        return eventos;
    }

    public Boolean delete(Long id) {
        if (id == null) return false;
        var dao = new EventoDao();
        return dao.delete(id);
    }

    public void saveArchive(EventoModel evento) {
        var arquivo = new File(System.getProperty("user.dir"), "\\evento.txt");
        writerFile(evento.toString(), arquivo.toString());
    }

    public String listarArquivos() {
        var dao = new EventoDao();

        String result = "";
        for (Object evento : dao.select()) {
            result = result + "\n" + evento;
        }

        return result;
    }

    private List<String> readerFile(String nomeArquivo) {
        List<String> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(nomeArquivo))) {
            reader.lines().forEach(result::add);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    private void writerFile(String conteudo, String nomeArquivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo, true))) {
            writer.newLine();
            writer.write(conteudo);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
