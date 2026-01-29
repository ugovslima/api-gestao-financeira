package com.api_gestao_financeira.processor_worker.infra.persistence.repository;

import com.api_gestao_financeira.processor_worker.core.enums.StatusTransacao;
import com.api_gestao_financeira.processor_worker.infra.persistence.entity.TransacaoEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransacaoRepositoryJpa extends JpaRepository<TransacaoEntity, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("""
                update TransacaoEntity t
                set t.status = :status,
                    t.motivo = :motivo
                where t.id = :id
            """)
    int atualizarStatus(
            @Param("id") Long id,
            @Param("status") StatusTransacao status,
            @Param("motivo") String motivo
    );
}

