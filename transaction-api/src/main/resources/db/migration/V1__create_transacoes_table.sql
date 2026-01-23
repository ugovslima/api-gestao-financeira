CREATE TABLE transacoes (
    id BIGSERIAL PRIMARY KEY,
    banco VARCHAR(50) NOT NULL,
    data TIMESTAMP NOT NULL,
    descricao VARCHAR(255),
    forma_pagamento VARCHAR(30) NOT NULL,
    parcelas INTEGER,
    status VARCHAR(20) NOT NULL,
    motivo VARCHAR(255),
    usuario_id BIGINT NOT NULL,
    valor NUMERIC(15, 2) NOT NULL
);

CREATE INDEX idx_transacoes_status ON transacoes(status);
CREATE INDEX idx_transacoes_usuario_id ON transacoes(usuario_id);
