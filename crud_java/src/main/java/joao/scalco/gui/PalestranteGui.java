package joao.scalco.gui;

import joao.scalco.model.PalestranteModel;
import joao.scalco.service.PalestranteService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class PalestranteGui extends JFrame implements GuiUtil {

    private final PalestranteService palestranteService;

    // Form fields
    private JTextField tfNome;
    private JTextField tfFotoUrl;
    private JTextArea taDescricao;
    private JTextField tfEspecializacao;
    private JTextField tfEmail;

    // Buttons
    private JButton btNovo;
    private JButton btSalvar;
    private JButton btExcluir;

    // Table and image
    private JTable tabela;
    private JLabel lblImagem;

    // Current selected palestrante
    private PalestranteModel palestranteAtual;

    public PalestranteGui(PalestranteService palestranteService) {
        this.palestranteService = palestranteService;
        initializeComponents();
        setupLayout();
        carregarTabela();
    }

    private void initializeComponents() {
        setTitle("Gerenciamento de Palestrantes");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Initialize form fields
        tfNome = new JTextField(20);
        tfFotoUrl = new JTextField(20);
        taDescricao = new JTextArea(4, 20);
        taDescricao.setLineWrap(true);
        taDescricao.setWrapStyleWord(true);
        tfEspecializacao = new JTextField(20);
        tfEmail = new JTextField(20);

        // Initialize buttons
        btNovo = new JButton("Novo");
        btSalvar = new JButton("Salvar");
        btExcluir = new JButton("Excluir");

        // Add action listeners
        btNovo.addActionListener(this::novoRegistro);
        btSalvar.addActionListener(this::salvarPalestrante);
        btExcluir.addActionListener(this::excluirPalestrante);
        tfFotoUrl.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { carregarImagem(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { carregarImagem(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { carregarImagem(); }
        });

        // Initialize table
        tabela = new JTable();
        tabela.setDefaultEditor(Object.class, null);
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.getSelectionModel().addListSelectionListener(this::selecionarPalestrante);

        // Initialize image label
        lblImagem = new JLabel("Imagem do Palestrante");
        lblImagem.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagem.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblImagem.setPreferredSize(new Dimension(200, 200));
        lblImagem.setBackground(Color.WHITE);
        lblImagem.setOpaque(true);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Left panel - Form
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Dados do Palestrante"));
        leftPanel.add(criarPainelFormulario(), BorderLayout.CENTER);
        leftPanel.add(criarPainelBotoes(), BorderLayout.SOUTH);

        // Center panel - Image
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Foto"));
        centerPanel.add(lblImagem, BorderLayout.CENTER);

        // Right panel - Table
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Lista de Palestrantes"));
        rightPanel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Add panels to main frame
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    private JPanel criarPainelFormulario() {
        var panel = new JPanel(new GridBagLayout());

        panel.add(new JLabel("Nome:"), montarGrid(0, 0));
        panel.add(tfNome, montarGrid(1, 0));

        panel.add(new JLabel("URL da Foto:"), montarGrid(0, 1));
        panel.add(tfFotoUrl, montarGrid(1, 1));

        panel.add(new JLabel("Descrição:"), montarGrid(0, 2));
        var scrollDesc = new JScrollPane(taDescricao);
        scrollDesc.setPreferredSize(new Dimension(200, 80));
        panel.add(scrollDesc, montarGrid(1, 2));

        panel.add(new JLabel("Especialização:"), montarGrid(0, 3));
        panel.add(tfEspecializacao, montarGrid(1, 3));

        panel.add(new JLabel("Email:"), montarGrid(0, 4));
        panel.add(tfEmail, montarGrid(1, 4));

        return panel;
    }

    private JPanel criarPainelBotoes() {
        var panel = new JPanel(new FlowLayout());
        panel.add(btNovo);
        panel.add(btSalvar);
        panel.add(btExcluir);
        return panel;
    }

    private void carregarTabela() {
        var model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nome");
        model.addColumn("Especialização");
        model.addColumn("Email");

        palestranteService.listar().forEach(palestrante -> {
            model.addRow(new Object[]{
                    palestrante.getId(),
                    palestrante.getNome(),
                    palestrante.getEspecializacao(),
                    palestrante.getEmail()
            });
        });

        tabela.setModel(model);
    }

    private void selecionarPalestrante(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;

        int selectedRow = tabela.getSelectedRow();
        if (selectedRow != -1) {
            Long id = (Long) tabela.getValueAt(selectedRow, 0);

            // Find the complete palestrante object
            palestranteAtual = palestranteService.listar().stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (palestranteAtual != null) {
                preencherFormulario(palestranteAtual);
                carregarImagem();
            }
        }
    }

    private void preencherFormulario(PalestranteModel palestrante) {
        tfNome.setText(palestrante.getNome());
        tfFotoUrl.setText(palestrante.getFoto());
        taDescricao.setText(palestrante.getDescricao());
        tfEspecializacao.setText(palestrante.getEspecializacao());
        tfEmail.setText(palestrante.getEmail());
    }

    private void carregarImagem() {
        String urlText = tfFotoUrl.getText().trim();

        if (urlText.isEmpty()) {
            lblImagem.setIcon(null);
            lblImagem.setText("Imagem do Palestrante");
            return;
        }

        SwingWorker<ImageIcon, Void> worker = new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                try {
                    URL url = new URL(urlText);
                    BufferedImage originalImage = ImageIO.read(url);

                    if (originalImage != null) {
                        // Resize image to fit the label
                        Image scaledImage = originalImage.getScaledInstance(190, 190, Image.SCALE_SMOOTH);
                        return new ImageIcon(scaledImage);
                    }
                } catch (IOException ex) {
                    // URL invalid or image not found
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    if (icon != null) {
                        lblImagem.setIcon(icon);
                        lblImagem.setText("");
                    } else {
                        lblImagem.setIcon(null);
                        lblImagem.setText("Imagem não encontrada");
                    }
                } catch (Exception ex) {
                    lblImagem.setIcon(null);
                    lblImagem.setText("Erro ao carregar imagem");
                }
            }
        };

        worker.execute();
    }

    private void novoRegistro(ActionEvent e) {
        limparFormulario();
        palestranteAtual = null;
        tfNome.requestFocus();
    }

    private void salvarPalestrante(ActionEvent e) {
        if (!validarFormulario()) return;

        var palestrante = new PalestranteModel();

        // If editing existing palestrante, keep the ID
        if (palestranteAtual != null) {
            palestrante.setId(palestranteAtual.getId());
        }

        palestrante.setNome(tfNome.getText().trim());
        palestrante.setFoto(tfFotoUrl.getText().trim());
        palestrante.setDescricao(taDescricao.getText().trim());
        palestrante.setEspecializacao(tfEspecializacao.getText().trim());
        palestrante.setEmail(tfEmail.getText().trim());

        boolean sucesso = palestranteService.salvar(palestrante);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Palestrante salvo com sucesso!");
            limparFormulario();
            carregarTabela();
            palestranteAtual = null;
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao salvar palestrante!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirPalestrante(ActionEvent e) {
        if (palestranteAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um palestrante para excluir!");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir o palestrante: " + palestranteAtual.getNome() + "?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            boolean sucesso = palestranteService.delete(palestranteAtual.getId());

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Palestrante excluído com sucesso!");
                limparFormulario();
                carregarTabela();
                palestranteAtual = null;
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir palestrante!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validarFormulario() {
        if (tfNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome é obrigatório!");
            tfNome.requestFocus();
            return false;
        }

        if (tfEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email é obrigatório!");
            tfEmail.requestFocus();
            return false;
        }

        if (tfEspecializacao.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Especialização é obrigatória!");
            tfEspecializacao.requestFocus();
            return false;
        }

        return true;
    }

    private void limparFormulario() {
        tfNome.setText("");
        tfFotoUrl.setText("");
        taDescricao.setText("");
        tfEspecializacao.setText("");
        tfEmail.setText("");
        lblImagem.setIcon(null);
        lblImagem.setText("Imagem do Palestrante");
        tabela.clearSelection();
    }
}