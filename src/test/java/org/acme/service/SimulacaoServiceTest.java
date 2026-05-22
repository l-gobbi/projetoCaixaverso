package org.acme.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.domain.Simulacao;
import org.acme.dto.SimulacaoRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class SimulacaoServiceTest {

    @Inject
    SimulacaoService service;

    @Test
    public void testCalculoJurosCompostos() {
        SimulacaoRequest request = new SimulacaoRequest(
                new BigDecimal("1000.00"),
                new BigDecimal("1.0"), // 1% de juros
                2 // 2 meses
        );

        Simulacao resultado = service.gerarSimulacao(request);

        assertNotNull(resultado.id);
        assertEquals(2, resultado.memoriaCalculo.size());

        // Mês 1: 1000 * 1% = 10. Saldo Final = 1010.00
        assertEquals(new BigDecimal("10.00"), resultado.memoriaCalculo.get(0).juro);
        assertEquals(new BigDecimal("1010.00"), resultado.memoriaCalculo.get(0).saldoFinal);

        // Mês 2: 1010 * 1% = 10.10. Saldo Final = 1020.10
        assertEquals(new BigDecimal("10.10"), resultado.memoriaCalculo.get(1).juro);
        assertEquals(new BigDecimal("1020.10"), resultado.memoriaCalculo.get(1).saldoFinal);

        // Totais
        assertEquals(new BigDecimal("20.10"), resultado.valorTotalJuros);
        assertEquals(new BigDecimal("1020.10"), resultado.valorTotalFinal);
    }
}