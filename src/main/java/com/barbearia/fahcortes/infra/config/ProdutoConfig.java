package com.barbearia.fahcortes.infra.config;

import com.barbearia.fahcortes.domain.gateways.produto.ProdutoGateway;
import com.barbearia.fahcortes.domain.usecases.produto.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProdutoConfig {

    @Bean
    public CadastrarProdutoUseCase cadastrarProdutoUseCase(ProdutoGateway produtoGateway) {
        return new CadastrarProdutoUseCase(produtoGateway);
    }

    @Bean
    public BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase(ProdutoGateway produtoGateway) {
        return new BuscarProdutoPorIdUseCase(produtoGateway);
    }

    @Bean
    public ListarTodosProdutosUseCase listarTodosProdutosUseCase(ProdutoGateway produtoGateway) {
        return new ListarTodosProdutosUseCase(produtoGateway);
    }

    @Bean
    public AtualizarProdutoPorIdUseCase atualizarProdutoPorIdUseCase(ProdutoGateway produtoGateway) {
        return new AtualizarProdutoPorIdUseCase(produtoGateway);
    }

    @Bean
    public DeletarProdutoPorIdUseCase deletarProdutoPorIdUseCase(ProdutoGateway produtoGateway) {
        return new DeletarProdutoPorIdUseCase(produtoGateway);
    }
}
