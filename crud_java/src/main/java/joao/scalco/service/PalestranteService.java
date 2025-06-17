package joao.scalco.service;

import joao.scalco.dao.PalestranteDao;
import joao.scalco.model.PalestranteModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PalestranteService {

    public Boolean salvar(PalestranteModel palestrante){
        var dao = new PalestranteDao();
        return palestrante.getId() == null
                ? dao.save(palestrante)
                : dao.update(palestrante);
    }

    public List<PalestranteModel> listar(){
        List<PalestranteModel> palestrantes = new ArrayList<>();
        var dao = new PalestranteDao();


        dao.select().forEach(object -> palestrantes.add((PalestranteModel) object));
        return palestrantes;
    }

    public Boolean delete(Long id){
        if (id == null) return false;
        var dao = new PalestranteDao();
        return dao.delete(id);
    }

    public void saveArchive(PalestranteModel palestrante) {
        var arquivo = new File(System.getProperty("user.dir"), "\\palestrante.txt");
        writerFile(palestrante.toString(), arquivo.toString());
    }

    public String listarArquivos() {
        var dao = new PalestranteDao();

        String result = "";
        for (Object palestrante : dao.select()) {
            result = result + "\n" + palestrante;
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

    public PalestranteModel buscarPorId(Long id) {
        var dao = new PalestranteDao();
        return (PalestranteModel) dao.selectId(id);
    }
}
