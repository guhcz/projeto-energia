package br.com.fiap.energia.controller;

import br.com.fiap.energia.dto.EquipamentoCadastroDto;
import br.com.fiap.energia.dto.EquipamentoExibicaoDto;
import br.com.fiap.energia.model.EquipamentoModel;
import br.com.fiap.energia.service.EquipamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EquipamentoController {

    @Autowired
    private EquipamentoService service;

    @PostMapping("/equipamentos")
    @ResponseStatus(HttpStatus.CREATED)
    public EquipamentoExibicaoDto cadastrar(@RequestBody @Valid EquipamentoCadastroDto equipamentoCadastroDto) {
        return service.cadastrar(equipamentoCadastroDto);
    }

    @PutMapping("/equipamentos")
    @ResponseStatus(HttpStatus.OK)
    public EquipamentoExibicaoDto atualizar(@RequestBody EquipamentoCadastroDto equipamentoCadastroDto) {
        return service.atualizar(equipamentoCadastroDto);
    }

    @DeleteMapping("/equipamentos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }

    @GetMapping("/equipamentos")
    @ResponseStatus(HttpStatus.OK)
    public Page<EquipamentoExibicaoDto> buscarTodosOsEquipamento(Pageable paginacao) {
        return service.listarTodosOsEquipamento(paginacao);
    }

    @GetMapping("/equipamentos/{tipoEquipamento}")
    @ResponseStatus(HttpStatus.OK)
    public List<EquipamentoExibicaoDto> buscarPeloTipoEquipamento(@PathVariable String tipoEquipamento) {
        return service.listarPeloTipoEquipamento(tipoEquipamento);
    }

    @GetMapping("/equipamentos/{dataInicial}/{dataFinal}")
    @ResponseStatus(HttpStatus.OK)
    public List<EquipamentoExibicaoDto> buscarPelaDataConsumo(@PathVariable LocalDate dataInicial, @PathVariable LocalDate dataFinal) {
        return service.listarPelaDataConsumo(dataInicial, dataFinal);
    }

    @GetMapping("/equipamentos/buscarPeloId/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EquipamentoExibicaoDto buscarPeloId(@PathVariable Long id) {
        return service.buscarPeloId(id);
    }

    @GetMapping("/equipamentos/buscarPeloSensorId/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EquipamentoExibicaoDto buscarPeloSensorId(@PathVariable String sensorId) {
        return service.buscarPeloSensorId(sensorId);
    }

    @GetMapping("/equipamentos/buscarPelaLocalizacao/{localizacao}")
    @ResponseStatus(HttpStatus.OK)
    public List<EquipamentoExibicaoDto> buscarPelaLocalizacao(@PathVariable String localizacao) {
        return service.listarPelaLocalizacao(localizacao);
    }

}
