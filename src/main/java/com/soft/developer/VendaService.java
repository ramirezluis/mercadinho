package com.soft.developer;

public class VendaService {

    private final ProdutoRepository repository;

    public VendaService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public CarrinhoCompras criarCarrinho() {
        return new CarrinhoCompras();
    }

    public void adicionarProduto(
            CarrinhoCompras carrinho,
            String idProduto,
            int quantidade) {

        Produto produto =
            repository.buscarPorId(idProduto);

        if (produto == null) {
            throw new IllegalArgumentException(
                "Produto não encontrado."
            );
        }

        carrinho.adicionarProduto(
            produto,
            quantidade
        );
    }

    public void finalizarVenda(
            CarrinhoCompras carrinho) {

        carrinho.finalizarCompra();
    }
}