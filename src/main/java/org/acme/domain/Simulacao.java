package org.acme.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Simulacao extends PanacheEntity {

    public BigDecimal valorInicial;
    public BigDecimal taxaJurosMensal;
    public Integer prazoMeses;

    public BigDecimal valorTotalFinal;
    public BigDecimal valorTotalJuros;

    @OneToMany(mappedBy = "simulacao", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<MemoriaCalculo> memoriaCalculo = new ArrayList<>();
}