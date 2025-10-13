package br.com.fiap.energia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EquipamentoCadastroDto(
        Long id,

        @NotBlank(message = "O tipo do equipamento é obrigatório!")
        String tipoEquipamento,

        @NotBlank(message = "O valor do consumo de energia é obrigatório!")
        String consumoEnergia,

        @NotBlank(message = "A localizacao é obrigatória!")
        String localizacao,

        @NotBlank(message = "O id do sensor é obrigatório!")
        @Size(min = 8, max = 8, message = "O id deve conter 8 caracteres!")
        String sensorId,

        @NotNull(message = "A data de cadastro é obrigatória!")
        LocalDate dataConsumo
) {
}
