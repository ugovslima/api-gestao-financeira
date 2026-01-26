package com.api_gestao_financeira.processor_worker.infra.kafka.consumer;

import com.api_gestao_financeira.processor_worker.application.usecase.ProcessarTransacaoUseCase;
import com.api_gestao_financeira.processor_worker.core.domain.Transacao;
import com.api_gestao_financeira.processor_worker.infra.dto.TransacaoEvento;
import com.api_gestao_financeira.processor_worker.infra.mapper.TransacaoMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransacaoKafkaConsumer {

    private final ProcessarTransacaoUseCase useCase;

    public TransacaoKafkaConsumer(ProcessarTransacaoUseCase useCase) {
        this.useCase = useCase;
    }

    @KafkaListener(
            topics = "transacao",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumir(TransacaoEvento evento) {

        Transacao transacao =
                TransacaoMapper.toDomain(evento);

        useCase.executar(transacao);
    }
}
