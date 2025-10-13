package br.com.fiap.energia.dto;

import br.com.fiap.energia.model.EquipamentoModel;

import java.time.LocalDate;
import java.util.List;

public record EquipamentoExibicaoDto(
        Long id,
        String tipoEquipamento,
        String consumoEnergia,
        String localizacao,
        String sensorId,
        LocalDate dataConsumo
) {

    public EquipamentoExibicaoDto(EquipamentoModel equipamento) {
        this(
                equipamento.getId(),
                equipamento.getTipoEquipamento(),
                equipamento.getConsumoEnergia(),
                equipamento.getLocalizacao(),
                equipamento.getSensorId(),
                equipamento.getDataConsumo());
    }

}
