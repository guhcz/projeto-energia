package br.com.fiap.energia.repository;

import br.com.fiap.energia.dto.EquipamentoExibicaoDto;
import br.com.fiap.energia.model.EquipamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EquipamentoRepository extends JpaRepository<EquipamentoModel, Long> {

    List<EquipamentoExibicaoDto> findByTipoEquipamento(String tipoEquipamento);

    Optional<EquipamentoExibicaoDto> findBySensorId(String sensorId);

    List<EquipamentoExibicaoDto> findByLocalizacao(String localizacao);

    List<EquipamentoExibicaoDto> findByDataConsumoBetween(LocalDate dataInicial, LocalDate dataFinal);

}
