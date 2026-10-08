package com.soft.developer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MercadinhoGUI extends JFrame {
    private List<Produto> catalogo = new ArrayList<>();
    private CarrinhoCompras carrinho = new CarrinhoCompras();
    
    private JTable tabelaProdutos;
    private JTable tabelaCarrinho;
    private DefaultTableModel modeloProdutos;
    private DefaultTableModel modeloCarrinho;
    private JLabel lblTotal;

    public MercadinhoGUI() {
        inicializarDados();
        configurarJanela();
        inicializarComponentes();
    }

    private void inicializarDados() {
        catalogo.add(new Produto("1", "Arroz 5kg", 24.90, 15));
        catalogo.add(new Produto("2", "Feijão Preto 1kg", 8.50, 20));
        catalogo.add(new Produto("3", "Óleo de Soja", 6.80, 30));
        catalogo.add(new Produto("4", "Macarrão Spagetti", 4.20, 25));
        catalogo.add(new Produto("5", "Café Torrado 500g", 14.90, 12));
    }

    private void configurarJanela() {
        setTitle("Supermercado Ideal - Painel de Vendas");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Estética moderna (Flat/Clean)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fallback silencioso
        }
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(15, 15));
        JPanel painelConteudo = new JPanel(new GridLayout(1, 2, 15, 15));
        painelConteudo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 1. PAINEL DE PRODUTOS (Esquerda)
        JPanel painelEsquerda = new JPanel(new BorderLayout(10, 10));
        painelEsquerda.setBorder(BorderFactory.createTitledBorder("Produtos Disponíveis"));
        
        String[] colunasProd = {"ID", "Nome", "Preço (R$)", "Estoque"};
        modeloProdutos = new DefaultTableModel(colunasProd, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaProdutos = new JTable(modeloProdutos);
        tabelaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        atualizarTabelaProdutos();
        
        painelEsquerda.add(new JScrollPane(tabelaProdutos), BorderLayout.CENTER);

        // Operações de compra abaixo da tabela de produtos
        JPanel painelAcoesProd = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        painelAcoesProd.add(new JLabel("Qtd:"));
        JSpinner spinnerQtd = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        painelAcoesProd.add(spinnerQtd);
        
        JButton btnAdicionar = new JButton("Adicionar ao Carrinho 🛒");
        btnAdicionar.setBackground(new Color(46, 204, 113));
        btnAdicionar.setForeground(Color.BLACK);
        painelAcoesProd.add(btnAdicionar);
        painelEsquerda.add(painelAcoesProd, BorderLayout.SOUTH);

        // 2. PAINEL DO CARRINHO (Direita)
        JPanel painelDireita = new JPanel(new BorderLayout(10, 10));
        painelDireita.setBorder(BorderFactory.createTitledBorder("Seu Carrinho de Compras"));

        String[] colunasCart = {"Item", "Preço Un.", "Qtd", "Subtotal"};
        modeloCarrinho = new DefaultTableModel(colunasCart, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaCarrinho = new JTable(modeloCarrinho);
        painelDireita.add(new JScrollPane(tabelaCarrinho), BorderLayout.CENTER);

        // Painel de Fechamento de Valor e Botões
        JPanel painelCheckout = new JPanel(new BorderLayout(10, 10));
        lblTotal = new JLabel("TOTAL: R$ 0,00", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        painelCheckout.add(lblTotal, BorderLayout.NORTH);

        JPanel btnCheckoutPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton btnLimpar = new JButton("Limpar Carrinho ❌");
        JButton btnFinalizar = new JButton("Finalizar Compra ✔️");
        
        btnFinalizar.setFont(new Font("Arial", Font.BOLD, 14));
        btnFinalizar.setBackground(new Color(52, 152, 219));
        btnFinalizar.setForeground(Color.BLACK);
        
        btnCheckoutPanel.add(btnLimpar);
        btnCheckoutPanel.add(btnFinalizar);
        painelCheckout.add(btnCheckoutPanel, BorderLayout.SOUTH);
        
        painelDireita.add(painelCheckout, BorderLayout.SOUTH);

        // Adicionar painéis principais ao container
        painelConteudo.add(painelEsquerda);
        painelConteudo.add(painelDireita);
        add(painelConteudo, BorderLayout.CENTER);

        // --- LÓGICA DE EVENTOS (LISTENERS) ---
        btnAdicionar.addActionListener(e -> {
            int linhaSelecionada = tabelaProdutos.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor, selecione um produto na tabela.");
                return;
            }
            
            Produto prodSelecionado = catalogo.get(linhaSelecionada);
            int qtd = (Integer) spinnerQtd.getValue();

            try {
                carrinho.adicionarProduto(prodSelecionado, qtd);
                atualizarInterfaceCarrinho();
                spinnerQtd.setValue(1);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLimpar.addActionListener(e -> {
            carrinho.limpar();
            atualizarInterfaceCarrinho();
        });

        btnFinalizar.addActionListener(e -> {
            try {
                carrinho.finalizarCompra();
                JOptionPane.showMessageDialog(this, "Compra finalizada com sucesso! Volte sempre.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarTabelaProdutos();
                atualizarInterfaceCarrinho();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void atualizarTabelaProdutos() {
        modeloProdutos.setRowCount(0);
        for (Produto p : catalogo) {
            modeloProdutos.addRow(new Object[]{p.getId(), p.getNome(), String.format("%.2f", p.getPreco()), p.getEstoque()});
        }
    }

    private void atualizarInterfaceCarrinho() {
        modeloCarrinho.setRowCount(0);
        for (ItemCarrinho item : carrinho.getItens()) {
            modeloCarrinho.addRow(new Object[]{
                item.getProduto().getNome(),
                String.format("%.2f", item.getProduto().getPreco()),
                item.getQuantidade(),
                String.format("%.2f", item.getSubtotal())
            });
        }
        lblTotal.setText(String.format("TOTAL: R$ %.2f", carrinho.calcularTotal()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MercadinhoGUI().setVisible(true);
        });
    }
}

