package com.soft.developer;

import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {

    private final List<Produto> produtos =
        new ArrayList<>();

    public void salvar(Produto produto) {
        produtos.add(produto);
    }

    public Produto buscarPorId(String id) {

        return produtos.stream()
            .filter(p -> p.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    public List<Produto> buscarTodos() {
        return new ArrayList<>(produtos);
    }
}
