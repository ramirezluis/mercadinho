package com.soft.developer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarrinhoCompras {

    private final List<ItemCarrinho> itens = new ArrayList<>();

    /**
     * Adiciona um produto ao carrinho.
     */
    public void adicionarProduto(Produto produto, int quantidade) {

        if (produto == null) {
            throw new IllegalArgumentException("Produto inválido.");
        }

        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                "A quantidade deve ser maior que zero."
            );
        }

        // Calcula quanto desse produto já está no carrinho
        int quantidadeNoCarrinho = 0;

        for (ItemCarrinho item : itens) {
            if (item.getProduto().getId().equals(produto.getId())) {
                quantidadeNoCarrinho += item.getQuantidade();
            }
        }

        // Verifica estoque disponível
        if (quantidadeNoCarrinho + quantidade > produto.getEstoque()) {
            throw new IllegalArgumentException(
                "Estoque insuficiente para o produto: "
                + produto.getNome()
            );
        }

        itens.add(new ItemCarrinho(produto, quantidade));
    }

    /**
     * Retorna os itens existentes no carrinho.
     */
    public List<ItemCarrinho> getItens() {
        return Collections.unmodifiableList(itens);
    }

    /**
     * Calcula o valor total da compra.
     */
    public double calcularTotal() {

        double total = 0.0;

        for (ItemCarrinho item : itens) {
            total += item.getSubtotal();
        }

        return total;
    }

    /**
     * Remove todos os itens do carrinho.
     */
    public void limpar() {
        itens.clear();
    }

    /**
     * Finaliza a compra e atualiza o estoque.
     */
    public void finalizarCompra() {

        if (itens.isEmpty()) {
            throw new IllegalStateException(
                "O carrinho está vazio."
            );
        }

        // Verificação antes de alterar o estoque
        for (ItemCarrinho item : itens) {

            if (item.getQuantidade()
                    > item.getProduto().getEstoque()) {

                throw new IllegalStateException(
                    "Estoque insuficiente para o produto: "
                    + item.getProduto().getNome()
                );
            }
        }

        // Atualiza estoque
        for (ItemCarrinho item : itens) {

            item.getProduto().reduzirEstoque(
                item.getQuantidade()
            );
        }

        // Esvazia carrinho
        itens.clear();
    }
}