package br.com.fiap.energia.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "tbl_equipamentos")
public class EquipamentoModel {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SEQ_EQUIPAMENTOS"
    )
    @SequenceGenerator(
            name = "SEQ_EQUIPAMENTOS",
            sequenceName = "SEQ_EQUIPAMENTOS",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "tipo_equipamento")
    private String tipoEquipamento;

    @Column(name = "consumo_energia")
    private String consumoEnergia;

    private String localizacao;

    @Column(name = "sensor_id")
    private String sensorId;

    @Column(name = "data_consumo")
    private LocalDate dataConsumo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoEquipamento() {
        return tipoEquipamento;
    }

    public void setTipoEquipamento(String tipoEquipamento) {
        this.tipoEquipamento = tipoEquipamento;
    }

    public String getConsumoEnergia() {
        return consumoEnergia;
    }

    public void setConsumoEnergia(String consumoEnergia) {
        this.consumoEnergia = consumoEnergia;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public LocalDate getDataConsumo() {
        return dataConsumo;
    }

    public void setDataConsumo(LocalDate dataConsumo) {
        this.dataConsumo = dataConsumo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquipamentoModel that = (EquipamentoModel) o;
        return Objects.equals(id, that.id) && Objects.equals(tipoEquipamento, that.tipoEquipamento) && Objects.equals(consumoEnergia, that.consumoEnergia) && Objects.equals(localizacao, that.localizacao) && Objects.equals(sensorId, that.sensorId) && Objects.equals(dataConsumo, that.dataConsumo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipoEquipamento, consumoEnergia, localizacao, sensorId, dataConsumo);
    }
}
