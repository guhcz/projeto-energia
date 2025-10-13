package br.com.fiap.energia.controller;

import br.com.fiap.energia.dto.UsuarioCadastroDto;
import br.com.fiap.energia.dto.UsuarioExibicaoDto;
import br.com.fiap.energia.model.UsuarioModel;
import br.com.fiap.energia.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping("/usuarios")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioExibicaoDto cadastrarUsuario(@RequestBody @Valid UsuarioCadastroDto usuarioCadastroDto) {
        return service.cadastrar(usuarioCadastroDto);
    }

    @PutMapping("/usuarios")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioExibicaoDto atualizarUsuario(@RequestBody UsuarioCadastroDto usuarioCadastroDto) {
        return service.atualizar(usuarioCadastroDto);
    }

    @DeleteMapping("/usuarios/{id}")
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }

    @GetMapping("/usuarios/buscarPeloId/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioExibicaoDto buscarPeloId(@PathVariable Long id) {
        return service.buscarPeloId(id);
    }

    @GetMapping("/usuarios")
    @ResponseStatus(HttpStatus.OK)
    public List<UsuarioExibicaoDto> buscarTodosOsUsuarios() {
        return service.listarTodosOsUsuarios();
    }

    @GetMapping("/usuarios/buscarPeloNome/{nome}")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioExibicaoDto buscarPeloNome(@PathVariable String nome) {
        return service.buscarPeloNome(nome);
    }

//    @GetMapping("/usuarios/buscarPeloNome/{email}")
//    @ResponseStatus(HttpStatus.OK)
//    public UsuarioExibicaoDto buscarPeloEmail(@PathVariable String email) {
//        return service.buscarPeloEmail(email);
//    }

    @GetMapping("/usuarios/listarPelaDataCadastro/{dataInicial}/{dataFinal}")
    public List<UsuarioExibicaoDto> listarPelaDataCadastro(@PathVariable LocalDate dataInicial, @PathVariable LocalDate dataFinal) {
        return service.listarPelaDataCadastro(dataInicial, dataFinal);
    }
}
