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

        // Converte a taxa de juros de percentagem para decimal (ex: 1.5% -> 0.015)
        BigDecimal taxaDecimal = request.taxaJurosMensal()
                .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);

        BigDecimal saldoAtual = request.valorInicial();
        BigDecimal totalJuros = BigDecimal.ZERO;

        // Loop para calcular a evolução mês a mês
        for (int mes = 1; mes <= request.prazoMeses(); mes++) {
            // Calcula o juro do mês: Saldo Atual * Taxa
            BigDecimal jurosMes = saldoAtual.multiply(taxaDecimal)
                    .setScale(2, RoundingMode.HALF_UP); // Arredonda a 2 casas decimais

            BigDecimal saldoFinalMes = saldoAtual.add(jurosMes);

            // Regista na Memória de Cálculo
            MemoriaCalculo memoria = new MemoriaCalculo();
            memoria.mes = mes;
            memoria.saldoInicial = saldoAtual;
            memoria.juro = jurosMes;
            memoria.saldoFinal = saldoFinalMes;
            memoria.simulacao = simulacao; // Associa à simulação principal

            simulacao.memoriaCalculo.add(memoria);

            // Atualiza os acumuladores para o próximo ciclo
            totalJuros = totalJuros.add(jurosMes);
            saldoAtual = saldoFinalMes;
        }

        // Regista os totais finais na simulação
        simulacao.valorTotalJuros = totalJuros;
        simulacao.valorTotalFinal = saldoAtual;

        // Persiste a simulação e, por cascata, toda a memória de cálculo
        repository.persist(simulacao);

        return simulacao;
    }

    public Simulacao obterSimulacao(Long id) {
        return repository.findById(id);
    }
}