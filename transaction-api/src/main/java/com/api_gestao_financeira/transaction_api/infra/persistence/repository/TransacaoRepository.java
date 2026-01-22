package com.api_gestao_financeira.transaction_api.infra.persistence.repository;

import com.api_gestao_financeira.transaction_api.infra.persistence.entity.TransacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepository extends JpaRepository<TransacaoEntity, Long> {
}
