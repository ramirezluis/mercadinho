package com.soft.developer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;

class CarrinhoProdutoIntegrationTest {
    @Test
    void deveIntegrarProdutoCarrinhoEItem() {
        Produto arroz = new Produto("1", "Arroz 5kg", 25.00, 10);
        CarrinhoCompras carrinho = new CarrinhoCompras();
        carrinho.adicionarProduto(arroz, 2);
        Assertions.assertEquals(1, carrinho.getItens().size());
        Assertions.assertEquals("Arroz 5kg", carrinho.getItens().get(0).getProduto().getNome());
        Assertions.assertEquals(50.00, carrinho.calcularTotal(), 0.001);
    }

    @Test
    void deveAtualizarEstoqueAoFinalizarCompra() {
        Produto cafe = new Produto("5", "Café", 15.00, 10);
        CarrinhoCompras carrinho = new CarrinhoCompras();
        carrinho.adicionarProduto(cafe, 3);
        carrinho.finalizarCompra();
        Assertions.assertEquals(7, cafe.getEstoque());
    }

    @Test
    void deveRealizarCompraCompleta() {

           Produto arroz = new Produto("1", "Arroz", 25.00, 10);
           Produto feijao = new Produto("2", "Feijão", 10.00, 20);

           CarrinhoCompras carrinho = new CarrinhoCompras();
           carrinho.adicionarProduto(arroz, 2);
           carrinho.adicionarProduto(feijao, 3);
           assertEquals(80.00, carrinho.calcularTotal(), 0.001);
           carrinho.finalizarCompra();

           assertEquals(8, arroz.getEstoque());
           assertEquals(17, feijao.getEstoque());
           assertTrue(carrinho.getItens().isEmpty());
    }

}

