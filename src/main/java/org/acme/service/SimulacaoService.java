package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.MemoriaCalculo;
import org.acme.domain.Simulacao;
import org.acme.dto.SimulacaoRequest;
import org.acme.repository.SimulacaoRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;

@ApplicationScoped
public class SimulacaoService {

    @Inject
    SimulacaoRepository repository;

    @Transactional
    public Simulacao gerarSimulacao(SimulacaoRequest request) {
        Simulacao simulacao = new Simulacao();
        simulacao.valorInicial = request.valorInicial();
        simulacao.taxaJurosMensal = request.taxaJurosMensal();
        simulacao.prazoMeses = request.prazoMeses();

        BigDecimal taxaDecimal = request.taxaJurosMensal()
                .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);

        BigDecimal saldoAtual = request.valorInicial();
        BigDecimal totalJuros = BigDecimal.ZERO;

        for (int mes = 1; mes <= request.prazoMeses(); mes++) {

            BigDecimal jurosMes = saldoAtual.multiply(taxaDecimal)
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal saldoFinalMes = saldoAtual.add(jurosMes);

            MemoriaCalculo memoria = new MemoriaCalculo();
            memoria.mes = mes;
            memoria.saldoInicial = saldoAtual;
            memoria.juro = jurosMes;
            memoria.saldoFinal = saldoFinalMes;
            memoria.simulacao = simulacao;

            simulacao.memoriaCalculo.add(memoria);

            totalJuros = totalJuros.add(jurosMes);
            saldoAtual = saldoFinalMes;
        }

        simulacao.valorTotalJuros = totalJuros;
        simulacao.valorTotalFinal = saldoAtual;

        repository.persist(simulacao);

        return simulacao;
    }

    public Simulacao obterSimulacao(Long id) {
        return repository.findById(id);
    }
}