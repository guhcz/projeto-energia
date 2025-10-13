package br.com.fiap.energia.service;

import br.com.fiap.energia.dto.EquipamentoCadastroDto;
import br.com.fiap.energia.dto.EquipamentoExibicaoDto;
import br.com.fiap.energia.exception.EquipamentoNaoEncontradoException;
import br.com.fiap.energia.model.EquipamentoModel;
import br.com.fiap.energia.repository.EquipamentoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EquipamentoService {

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    public EquipamentoExibicaoDto cadastrar(EquipamentoCadastroDto equipamentoCadastroDto) {
        EquipamentoModel equipamento = new EquipamentoModel();
        BeanUtils.copyProperties(equipamentoCadastroDto, equipamento);
        return new EquipamentoExibicaoDto(equipamentoRepository.save(equipamento));
    }

    public EquipamentoExibicaoDto buscarPeloId(Long id) {

        Optional<EquipamentoModel> equipamentoOptional = equipamentoRepository.findById(id);

        if (equipamentoOptional.isPresent()) {
            return new EquipamentoExibicaoDto(equipamentoOptional.get());
        } else {
            throw new EquipamentoNaoEncontradoException("Equipamento nao encontrado!");
        }
    }

    public Page<EquipamentoExibicaoDto> listarTodosOsEquipamento(Pageable paginacao) {
        return equipamentoRepository
                .findAll(paginacao)
                .map(EquipamentoExibicaoDto::new);
    }

    public void excluir(Long id) {
        Optional<EquipamentoModel> equipamentoOptional = equipamentoRepository.findById(id);

        if (equipamentoOptional.isPresent()) {
            equipamentoRepository.delete(equipamentoOptional.get());
        } else {
            throw new EquipamentoNaoEncontradoException("Equipamento nao encontrado!");
        }
    }

    public List<EquipamentoExibicaoDto> listarPelaDataConsumo(LocalDate dataInicial, LocalDate dataFinal) {
        return equipamentoRepository.findByDataConsumoBetween(dataInicial, dataFinal);
    }

    public EquipamentoExibicaoDto atualizar(EquipamentoCadastroDto equipamentoCadastroDto) {
        Optional<EquipamentoModel> equipamentoOptional = equipamentoRepository.findById(equipamentoCadastroDto.id());

        if (equipamentoOptional.isPresent()) {
            EquipamentoModel equipamentoModel = new EquipamentoModel();
            BeanUtils.copyProperties(equipamentoCadastroDto, equipamentoModel);
            return new EquipamentoExibicaoDto(equipamentoRepository.save(equipamentoModel));
        } else {
            throw new EquipamentoNaoEncontradoException("Equipamento nao encontrado!");
        }
    }

    public List<EquipamentoExibicaoDto> listarPeloTipoEquipamento(String tipoEquipamento) {
        return equipamentoRepository.findByTipoEquipamento(tipoEquipamento);
    }

    public EquipamentoExibicaoDto buscarPeloSensorId(String sensorId) {
        Optional<EquipamentoExibicaoDto> equipamentoOptional = equipamentoRepository.findBySensorId(sensorId);

        if (equipamentoOptional.isPresent()) {
            return equipamentoOptional.get();
        } else {
            throw new EquipamentoNaoEncontradoException("Id do sensor do equipamento nao encontrado!");
        }
    }

    public List<EquipamentoExibicaoDto> listarPelaLocalizacao(String localizacao) {
        return equipamentoRepository.findByLocalizacao(localizacao);
    }

}
