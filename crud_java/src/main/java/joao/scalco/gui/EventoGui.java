package joao.scalco.gui;

import joao.scalco.model.EventoModel;
import joao.scalco.model.PalestranteModel;
import joao.scalco.service.EventoService;
import joao.scalco.service.PalestranteService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EventoGui extends JFrame implements GuiUtil {

    private final EventoService eventoService;
    private final PalestranteService palestranteService;

    // Form fields
    private JTextField tfNome;
    private JTextArea taDescricao;
    private JTextField tfDataInicio;
    private JTextField tfDataFim;
    private JTextField tfLocal;
    private JComboBox<PalestranteModel> cbPalestrante;

    // Buttons
    private JButton btNovo;
    private JButton btSalvar;
    private JButton btExcluir;

    // Table
    private JTable tabela;

    // Current selected evento
    private EventoModel eventoAtual;

    // Date formatter
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public EventoGui(EventoService eventoService, PalestranteService palestranteService) {
        this.eventoService = eventoService;
        this.palestranteService = palestranteService;
        initializeComponents();
        setupLayout();
        carregarPalestrantes();
        carregarTabela();
    }

    private void initializeComponents() {
        setTitle("Gerenciamento de Eventos");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Initialize form fields
        tfNome = new JTextField(25);
        taDescricao = new JTextArea(4, 25);
        taDescricao.setLineWrap(true);
        taDescricao.setWrapStyleWord(true);
        tfDataInicio = new JTextField(25);
        tfDataFim = new JTextField(25);
        tfLocal = new JTextField(25);
        cbPalestrante = new JComboBox<>();

        // Initialize buttons
        btNovo = new JButton("Novo");
        btSalvar = new JButton("Salvar");
        btExcluir = new JButton("Excluir");

        // Add action listeners
        btNovo.addActionListener(this::novoRegistro);
        btSalvar.addActionListener(this::salvarEvento);
        btExcluir.addActionListener(this::excluirEvento);

        // Initialize table
        tabela = new JTable();
        tabela.setDefaultEditor(Object.class, null);
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.getSelectionModel().addListSelectionListener(this::selecionarEvento);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Left panel - Form
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Dados do Evento"));
        leftPanel.add(criarPainelFormulario(), BorderLayout.CENTER);
        leftPanel.add(criarPainelBotoes(), BorderLayout.SOUTH);

        // Right panel - Table
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Lista de Eventos"));
        rightPanel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Add panels to main frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    private JPanel criarPainelFormulario() {
        var panel = new JPanel(new GridBagLayout());

        // Nome
        panel.add(new JLabel("Nome do Evento:"), montarGrid(0, 0));
        panel.add(tfNome, montarGrid(1, 0));

        // Descrição
        panel.add(new JLabel("Descrição:"), montarGrid(0, 1));
        var scrollDesc = new JScrollPane(taDescricao);
        scrollDesc.setPreferredSize(new Dimension(250, 80));
        panel.add(scrollDesc, montarGrid(1, 1));

        // Data Início
        panel.add(new JLabel("Data/Hora Início:"), montarGrid(0, 2));
        panel.add(tfDataInicio, montarGrid(1, 2));

        // Hint for date format
        var hintInicio = new JLabel("(yyyy-MM-dd HH:mm)");
        hintInicio.setFont(new Font("Arial", Font.ITALIC, 10));
        hintInicio.setForeground(Color.GRAY);
        panel.add(hintInicio, montarGrid(1, 3));

        // Data Fim
        panel.add(new JLabel("Data/Hora Fim:"), montarGrid(0, 4));
        panel.add(tfDataFim, montarGrid(1, 4));

        // Hint for date format
        var hintFim = new JLabel("(yyyy-MM-dd HH:mm)");
        hintFim.setFont(new Font("Arial", Font.ITALIC, 10));
        hintFim.setForeground(Color.GRAY);
        panel.add(hintFim, montarGrid(1, 5));

        // Local
        panel.add(new JLabel("Local:"), montarGrid(0, 6));
        panel.add(tfLocal, montarGrid(1, 6));

        // Palestrante
        panel.add(new JLabel("Palestrante:"), montarGrid(0, 7));
        panel.add(cbPalestrante, montarGrid(1, 7));

        return panel;
    }

    private JPanel criarPainelBotoes() {
        var panel = new JPanel(new FlowLayout());
        panel.add(btNovo);
        panel.add(btSalvar);
        panel.add(btExcluir);
        return panel;
    }

    private void carregarPalestrantes() {
        cbPalestrante.removeAllItems();
        cbPalestrante.addItem(null); // Option for no palestrante selected

        palestranteService.listar().forEach(palestrante -> {
            cbPalestrante.addItem(palestrante);
        });
    }

    private void carregarTabela() {
        var model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nome");
        model.addColumn("Data Início");
        model.addColumn("Data Fim");
        model.addColumn("Local");
        model.addColumn("Palestrante");

        eventoService.listar().forEach(evento -> {
            // Find palestrante name
            String palestranteName = "Sem palestrante";
            if (evento.getPalestranteId() != null) {
                var palestrante = palestranteService.listar().stream()
                        .filter(p -> p.getId().equals(evento.getPalestranteId()))
                        .findFirst()
                        .orElse(null);
                if (palestrante != null) {
                    palestranteName = palestrante.getNome();
                }
            }

            model.addRow(new Object[]{
                    evento.getId(),
                    evento.getNome(),
                    evento.getDataInicio() != null ? evento.getDataInicio().toString() : "",
                    evento.getDataFim() != null ? evento.getDataFim().toString() : "",
                    evento.getLocal(),
                    palestranteName
            });
        });

        tabela.setModel(model);
    }

    private void selecionarEvento(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;

        int selectedRow = tabela.getSelectedRow();
        if (selectedRow != -1) {
            Long id = (Long) tabela.getValueAt(selectedRow, 0);

            // Find the complete evento object
            eventoAtual = eventoService.listar().stream()
                    .filter(evento -> evento.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (eventoAtual != null) {
                preencherFormulario(eventoAtual);
            }
        }
    }

    private void preencherFormulario(EventoModel evento) {
        tfNome.setText(evento.getNome());
        taDescricao.setText(evento.getDescricao());

        // Format dates for display
        if (evento.getDataInicio() != null) {
            tfDataInicio.setText(evento.getDataInicio().toLocalDateTime().format(dateFormatter));
        } else {
            tfDataInicio.setText("");
        }

        if (evento.getDataFim() != null) {
            tfDataFim.setText(evento.getDataFim().toLocalDateTime().format(dateFormatter));
        } else {
            tfDataFim.setText("");
        }

        tfLocal.setText(evento.getLocal());

        // Select palestrante in combobox
        if (evento.getPalestranteId() != null) {
            for (int i = 0; i < cbPalestrante.getItemCount(); i++) {
                PalestranteModel palestrante = cbPalestrante.getItemAt(i);
                if (palestrante != null && palestrante.getId().equals(evento.getPalestranteId())) {
                    cbPalestrante.setSelectedIndex(i);
                    break;
                }
            }
        } else {
            cbPalestrante.setSelectedIndex(0); // Select null option
        }
    }

    private void novoRegistro(ActionEvent e) {
        limparFormulario();
        eventoAtual = null;
        tfNome.requestFocus();
    }

    private void salvarEvento(ActionEvent e) {
        if (!validarFormulario()) return;

        var evento = new EventoModel();

        // If editing existing evento, keep the ID
        if (eventoAtual != null) {
            evento.setId(eventoAtual.getId());
        }

        evento.setNome(tfNome.getText().trim());
        evento.setDescricao(taDescricao.getText().trim());
        evento.setLocal(tfLocal.getText().trim());

        // Parse dates
        try {
            if (!tfDataInicio.getText().trim().isEmpty()) {
                LocalDateTime dataInicio = LocalDateTime.parse(tfDataInicio.getText().trim(), dateFormatter);
                evento.setDataInicio(Timestamp.valueOf(dataInicio));
            }

            if (!tfDataFim.getText().trim().isEmpty()) {
                LocalDateTime dataFim = LocalDateTime.parse(tfDataFim.getText().trim(), dateFormatter);
                evento.setDataFim(Timestamp.valueOf(dataFim));
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Formato de data inválido! Use o formato: yyyy-MM-dd HH:mm\nExemplo: 2024-12-25 14:30",
                    "Erro de Data",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate date logic
        if (evento.getDataInicio() != null && evento.getDataFim() != null) {
            if (evento.getDataInicio().after(evento.getDataFim())) {
                JOptionPane.showMessageDialog(this,
                        "A data de início não pode ser posterior à data de fim!",
                        "Erro de Data",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Set palestrante
        PalestranteModel palestranteSelecionado = (PalestranteModel) cbPalestrante.getSelectedItem();
        if (palestranteSelecionado != null) {
            evento.setPalestranteId(palestranteSelecionado.getId());
        }

        boolean sucesso = eventoService.salvar(evento);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Evento salvo com sucesso!");
            limparFormulario();
            carregarTabela();
            eventoAtual = null;
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao salvar evento!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirEvento(ActionEvent e) {
        if (eventoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um evento para excluir!");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir o evento: " + eventoAtual.getNome() + "?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            boolean sucesso = eventoService.delete(eventoAtual.getId());

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Evento excluído com sucesso!");
                limparFormulario();
                carregarTabela();
                eventoAtual = null;
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir evento!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validarFormulario() {
        if (tfNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome do evento é obrigatório!");
            tfNome.requestFocus();
            return false;
        }

        if (tfLocal.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Local do evento é obrigatório!");
            tfLocal.requestFocus();
            return false;
        }

        if (tfDataInicio.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data de início é obrigatória!");
            tfDataInicio.requestFocus();
            return false;
        }

        if (tfDataFim.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data de fim é obrigatória!");
            tfDataFim.requestFocus();
            return false;
        }

        // Validate date format without parsing yet
        try {
            LocalDateTime.parse(tfDataInicio.getText().trim(), dateFormatter);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Formato da data de início é inválido! Use: yyyy-MM-dd HH:mm",
                    "Erro de Formato",
                    JOptionPane.ERROR_MESSAGE);
            tfDataInicio.requestFocus();
            return false;
        }

        try {
            LocalDateTime.parse(tfDataFim.getText().trim(), dateFormatter);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Formato da data de fim é inválido! Use: yyyy-MM-dd HH:mm",
                    "Erro de Formato",
                    JOptionPane.ERROR_MESSAGE);
            tfDataFim.requestFocus();
            return false;
        }

        return true;
    }

    private void limparFormulario() {
        tfNome.setText("");
        taDescricao.setText("");
        tfDataInicio.setText("");
        tfDataFim.setText("");
        tfLocal.setText("");
        cbPalestrante.setSelectedIndex(0); // Select null option
        tabela.clearSelection();
    }
}