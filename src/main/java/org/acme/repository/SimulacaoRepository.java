package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.Simulacao;

@ApplicationScoped
public class SimulacaoRepository implements PanacheRepository<Simulacao> {
    // Aqui podemos adicionar métodos de consulta personalizados no futuro, se necessário.
    // Por padrão, esta classe já herda métodos como persist(), findById(), etc.
}