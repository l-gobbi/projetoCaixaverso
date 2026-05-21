package org.acme.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;

@Entity
public class MemoriaCalculo extends PanacheEntity {

    public Integer mes;
    public BigDecimal saldoInicial;
    public BigDecimal juro;
    public BigDecimal saldoFinal;

    @ManyToOne
    @JoinColumn(name = "simulacao_id")
    @JsonIgnore // Evita loop infinito na hora de serializar o JSON
    public Simulacao simulacao;
}