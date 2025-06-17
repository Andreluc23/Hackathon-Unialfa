package joao.scalco.gui;

import joao.scalco.service.EventoService;
import joao.scalco.service.PalestranteService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PrincipalGui extends JFrame implements GuiUtil {

    private JMenuBar menuBar;

    public PrincipalGui() {
        setTitle("Sistema de Gerenciamento de Eventos");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setJMenuBar(montarMenuBar());

        // Add welcome panel
        add(criarPainelBemVindo());
    }

    private JPanel criarPainelBemVindo() {
        var panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));

        var welcomeLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<h1>Sistema de Gerenciamento de Eventos</h1>" +
                "<p>Bem-vindo ao sistema de gestão de eventos e palestrantes</p>" +
                "<p>Use o menu acima para navegar pelas funcionalidades</p>" +
                "</div></html>", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        panel.add(welcomeLabel, BorderLayout.CENTER);
        return panel;
    }

    private JMenuBar montarMenuBar() {
        menuBar = new JMenuBar();
        menuBar.add(montarMenuCadastros());
        menuBar.add(montarMenuRelatorios());
        menuBar.add(montarMenuConfig());
        return menuBar;
    }

    private JMenu montarMenuCadastros() {
        var menuCad = new JMenu("Cadastros");
        var miPalestrante = new JMenuItem("Palestrantes");
        var miEvento = new JMenuItem("Eventos");

        menuCad.add(miPalestrante);
        menuCad.add(miEvento);

        menuCad.setFont(new Font("Arial", Font.PLAIN, 16));
        miPalestrante.setFont(new Font("Arial", Font.PLAIN, 14));
        miEvento.setFont(new Font("Arial", Font.PLAIN, 14));

        miPalestrante.addActionListener(this::abrirPalestranteGui);
        miEvento.addActionListener(this::abrirEventoGui);

        return menuCad;
    }

    private JMenu montarMenuRelatorios() {
        var menuRel = new JMenu("Relatórios");
        var miRelRelatorio = new JMenuItem("Relatório de Evento e Palestrante");

        menuRel.add(miRelRelatorio);

        menuRel.setFont(new Font("Arial", Font.PLAIN, 16));
        miRelRelatorio.setFont(new Font("Arial", Font.PLAIN, 14));

        // Aqui adiciona o evento
        miRelRelatorio.addActionListener(e -> new RelatorioGui().setVisible(true));

        return menuRel;
    }

    private JMenu montarMenuConfig() {
        var menuConfig = new JMenu("Configurações");
        var miSobre = new JMenuItem("Sobre");
        var miSair = new JMenuItem("Sair");

        menuConfig.add(miSobre);
        menuConfig.addSeparator();
        menuConfig.add(miSair);

        miSair.addActionListener(this::sair);
        miSobre.addActionListener(this::exibirSobre);

        menuConfig.setFont(new Font("Arial", Font.PLAIN, 16));
        miSobre.setFont(new Font("Arial", Font.PLAIN, 14));
        miSair.setFont(new Font("Arial", Font.PLAIN, 14));

        return menuConfig;
    }

    private void exibirSobre(ActionEvent actionEvent) {
        JOptionPane.showMessageDialog(this, """
                Sistema de Gerenciamento de Eventos
                
                Desenvolvido para gestão completa de eventos e palestrantes.
                
                Funcionalidades:
                • Cadastro de Palestrantes com foto
                • Cadastro de Eventos
                • Associação entre eventos e palestrantes
                • Interface intuitiva e moderna
                
                Versão 1.0
                """);
    }

    private void sair(ActionEvent actionEvent) {
        var result = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente sair do sistema?",
                "Confirmar Saída",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void abrirPalestranteGui(ActionEvent actionEvent) {
        var gui = new PalestranteGui(new PalestranteService());
        gui.setVisible(true);
    }

    private void abrirEventoGui(ActionEvent actionEvent) {
        var gui = new EventoGui(new EventoService(), new PalestranteService());
        gui.setVisible(true);
    }

    public static void launchGUI() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            new PrincipalGui().setVisible(true);
        });
    }
}