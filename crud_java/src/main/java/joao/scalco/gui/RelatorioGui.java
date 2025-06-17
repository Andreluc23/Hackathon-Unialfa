package joao.scalco.gui;

import joao.scalco.model.EventoModel;
import joao.scalco.model.PalestranteModel;
import joao.scalco.service.EventoService;
import joao.scalco.service.PalestranteService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RelatorioGui extends JFrame {

    private JTable tabelaEventos;
    private JTable tabelaPalestrantes;

    public RelatorioGui() {
        setTitle("Relatórios de Eventos e Palestrantes");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(2, 1));

        // Tabela de Eventos
        tabelaEventos = new JTable();
        JScrollPane scrollEventos = new JScrollPane(tabelaEventos);
        add(scrollEventos);

        // Tabela de Palestrantes
        tabelaPalestrantes = new JTable();
        JScrollPane scrollPalestrantes = new JScrollPane(tabelaPalestrantes);
        add(scrollPalestrantes);

        carregarDados();
        setVisible(true);
    }

    private void carregarDados() {
        // Eventos
        EventoService eventoService = new EventoService();
        PalestranteService palestranteService = new PalestranteService();

        List<EventoModel> eventos = eventoService.listar();
        DefaultTableModel modeloEventos = new DefaultTableModel(new Object[]{"ID", "Nome", "Data", "Local", "Palestrante"}, 0);

        for (EventoModel e : eventos) {
            String nomePalestrante = "";
            Long palestranteId = e.getPalestranteId();

            if (palestranteId != null) {
                PalestranteModel palestrante = palestranteService.buscarPorId(palestranteId);
                if (palestrante != null) {
                    nomePalestrante = palestrante.getNome();
                }
            }

            modeloEventos.addRow(new Object[]{
                    e.getId(), e.getNome(), e.getDataInicio(), e.getLocal(), nomePalestrante
            });
        }

        tabelaEventos.setModel(modeloEventos);

        // Palestrantes
        List<PalestranteModel> palestrantes = palestranteService.listar();
        DefaultTableModel modeloPalestrantes = new DefaultTableModel(new Object[]{"ID", "Nome", "Email", "Especialidade"}, 0);
        for (PalestranteModel p : palestrantes) {
            modeloPalestrantes.addRow(new Object[]{p.getId(), p.getNome(), p.getEmail(), p.getEspecializacao()});
        }
        tabelaPalestrantes.setModel(modeloPalestrantes);
    }
}
