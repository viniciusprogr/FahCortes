package com.barbearia.fahcortes.infra.controller.produto;

import com.barbearia.fahcortes.domain.entities.produto.Produto;
import com.barbearia.fahcortes.domain.usecases.produto.*;
import com.barbearia.fahcortes.infra.controller.produto.dtos.ProdutoRequestDto;
import com.barbearia.fahcortes.infra.controller.produto.dtos.ProdutoResponseDto;
import com.barbearia.fahcortes.infra.mapper.produto.ProdutoMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoMapper produtoMapper;
    private final CadastrarProdutoUseCase cadastrarProdutoUseCase;
    private final BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase;
    private final ListarTodosProdutosUseCase listarTodosProdutosUseCase;
    private final AtualizarProdutoPorIdUseCase atualizarProdutoPorIdUseCase;
    private final DeletarProdutoPorIdUseCase deletarProdutoPorIdUseCase;

    public ProdutoController(ProdutoMapper produtoMapper,
                             CadastrarProdutoUseCase cadastrarProdutoUseCase,
                             BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase,
                             ListarTodosProdutosUseCase listarTodosProdutosUseCase,
                             AtualizarProdutoPorIdUseCase atualizarProdutoPorIdUseCase,
                             DeletarProdutoPorIdUseCase deletarProdutoPorIdUseCase) {
        this.produtoMapper = produtoMapper;
        this.cadastrarProdutoUseCase = cadastrarProdutoUseCase;
        this.buscarProdutoPorIdUseCase = buscarProdutoPorIdUseCase;
        this.listarTodosProdutosUseCase = listarTodosProdutosUseCase;
        this.atualizarProdutoPorIdUseCase = atualizarProdutoPorIdUseCase;
        this.deletarProdutoPorIdUseCase = deletarProdutoPorIdUseCase;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDto>> listarTodos() {
        List<Produto> produtos = listarTodosProdutosUseCase.execute();
        return ResponseEntity.ok(produtoMapper.toResponseList(produtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> buscarPorId(@PathVariable Long id) {
        Produto produto = buscarProdutoPorIdUseCase.execute(id);
        return ResponseEntity.ok(produtoMapper.toResponse(produto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProdutoResponseDto> cadastrar(@RequestBody @Valid ProdutoRequestDto dto) {
        Produto produto = produtoMapper.toDomain(dto);
        Produto salvo = cadastrarProdutoUseCase.execute(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoMapper.toResponse(salvo));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> atualizar(@RequestBody @Valid ProdutoRequestDto dto,
                                                         @PathVariable Long id) {
        Produto produto = produtoMapper.toDomain(dto);
        Produto atualizado = atualizarProdutoPorIdUseCase.execute(produto, id);
        return ResponseEntity.ok(produtoMapper.toResponse(atualizado));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        deletarProdutoPorIdUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
