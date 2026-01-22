package com.api_gestao_financeira.transaction_api.infra.kafka.producer;

import com.api_gestao_financeira.transaction_api.application.gateway.PublicarTransacaoGateway;
import com.api_gestao_financeira.transaction_api.core.domain.Transacao;
import com.api_gestao_financeira.transaction_api.infra.dto.TransacaoEvento;
import com.api_gestao_financeira.transaction_api.infra.persistence.mapper.TransacaoMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransacaoKafkaProducer implements PublicarTransacaoGateway {

    private final KafkaTemplate<String, TransacaoEvento> kafkaTemplate;

    public TransacaoKafkaProducer(KafkaTemplate<String, TransacaoEvento> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publicarTransacao(Transacao transacao) {
        kafkaTemplate.send(
                "transacao",
                TransacaoMapper.toEvent(transacao)
        );
    }
}
