# API de Gestão Financeira – Microsserviços com Spring Boot

Este projeto implementa uma **API de Gestão Financeira** baseada em **arquitetura de microsserviços**, utilizando **Java + Spring Boot**, **Apache Kafka** para mensageria, **JWT** para autenticação, simulação de consulta bancaria com MockAPI e integração com **BrasilAPI** para conversão de moeda.

O sistema simula o fluxo completo de:
- Autenticação de usuários via JWT
- Criação e registro de transações financeiras
- Análise automática de transações via Kafka
- Consulta de saldo e limite em bancos simulados
- Conversão automática de moedas estrangeiras
- Geração de relatórios financeiros

---

## Visão Geral da Arquitetura

A aplicação é composta por **3 microsserviços independentes**, cada um com responsabilidades bem definidas.

```
[ User Service ]
       ↓
      JWT
       ↓
[ Transaction API ] ---- Kafka ----> [ Processor Worker ]
```

### Comunicação
- **Entre microsserviços**: Apache Kafka
- **Cliente → API**: HTTP + REST
- **Autenticação**: JWT (Bearer Token)

---

## 1. User Service (Serviço de Usuários)

Responsável pelo **cadastro de usuários** e **autenticação**, emitindo um **token JWT** que é utilizado pelos demais serviços.

### Funcionalidades
- Registro de usuários individuais
- Registro de usuários em lote via CSV
- Autenticação e geração de JWT

### Endpoints

#### Autenticação

**POST** `/login`

Autentica o usuário e retorna um token JWT.

**Request**
```json
{
  "nome": "Ugo",
  "senha": "1234"
}
```

**Response – 200**
```json
{
  "tokenJwt": "eyJhbGciOiJIUzI1NiJ9..."
}
```

#### Registrar Usuário

**POST** `/registrar`

Cadastra um novo usuário.

**Response – 201**

#### Registrar Usuários em Lote

**POST** `/registrar/lote`

**Content-Type**: multipart/form-data

Arquivo CSV contendo usuários

**Response – 201**

---

## 2. Transaction API (API de Transações)

Responsável por receber, registrar, consultar e excluir transações financeiras, além de publicar transações pendentes no Kafka para processamento.

Todos os endpoints exigem JWT válido.

### Regras Gerais

- O usuário é sempre identificado pelo token JWT
- O campo `usuarioId` nunca é aceito diretamente no request
- Transações podem assumir diferentes estados
- Apenas transações `PENDENTE` são enviadas para processamento

### Status da Transação

| Status | Descrição |
|--------|-----------|
| PENDENTE | Criada e aguardando processamento |
| APROVADA | Processada com sucesso |
| REPROVADA | Processada e recusada |
| REGISTRADA | Apenas registrada, sem processamento |

### Criação e Registro de Transações

#### Criar Transação (Processável)

**POST** `/transacoes`

Cria uma transação que será enviada ao Processor Worker via Kafka para análise.

- Status inicial: `PENDENTE`
- Será analisada pelo Processor Worker
- O usuário deve informar o banco (ITAU ou SANTANDER)

**Response – 202 Accepted**

#### Registrar Transação Manual

**POST** `/transacoes/registrar`

Registra uma transação sem enviá-la para processamento.

- Status: `REGISTRADA`
- Ideal para lançamentos manuais ou históricos

**Regras**
- Se `parcelas` for null → 1
- Se `moeda` for null → BRL

**Response – 201 Created**

### Consulta de Transações

#### Buscar Transação por ID

**GET** `/transacoes/{transacaoId}`

Retorna apenas se a transação pertencer ao usuário autenticado

Caso contrário → 403

#### Buscar Todas as Transações do Usuário

**GET** `/transacoes`

Retorna todas as transações do usuário autenticado.

#### Excluir Transação

**DELETE** `/transacoes/{transacaoId}`

Apenas o dono da transação pode excluir

Retorna 204 em caso de sucesso

### Relatórios Financeiros

#### Resumo de Despesas

**GET** `/relatorios/resumo`

Gera um resumo financeiro baseado em:
- Data de referência
- Tipo de período (ex: MENSAL)
- Forma de pagamento (opcional)

#### Relatório em Excel

**GET** `/relatorios/excel`

Gera um arquivo `.xlsx` contendo as despesas do usuário para o mês informado.

- O arquivo é retornado como download
- Nome do arquivo baseado no mês/ano

### Conversão de Moeda (Câmbio)

A Transaction API realiza conversão automática de moeda **no momento da criação da transação**.

#### Regras de Câmbio

**Moedas aceitas:**
- **USD** (Dólar Americano)
- **EUR** (Euro)
- **Outras moedas** → consideradas BRL (Real Brasileiro)

**Fluxo de conversão para USD ou EUR:**

1. A API consulta a **BrasilAPI**
2. Obtém a **taxa de fechamento do dia útil anterior**
3. Converte o valor para **BRL**
4. Salva no banco de dados:
    - Valor original (USD ou EUR)
    - **Valor convertido em BRL**
    - **Taxa de câmbio utilizada**

**Exemplo:**
- Transação de **$100 USD**
- Taxa do dia anterior: **R$ 5,30**
- Valor salvo: **R$ 530,00**
- Taxa salva: **5.30**

#### Evento de Transação (Kafka)

As transações pendentes são publicadas no Kafka no formato:

```json
{
  "id": 1,
  "usuarioId": 1,
  "formaPagamento": "CREDITO",
  "valor": 199.90,
  "status": "PENDENTE",
  "banco": "ITAU"
}
```

---

## 3. Processor Worker (Serviço de Processamento)

O Processor Worker **não possui endpoints HTTP**.

Ele é responsável por:
- Consumir transações do Kafka
- Consultar saldo e limite do usuário
- Aprovar ou reprovar transações
- Atualizar o status no banco de dados

### Fluxo de Análise de Transação

1. **Consome** a transação do Kafka
2. **Verifica** se o status é `PENDENTE`
3. **Consulta** saldo e limite via **MockAPI** (JSON)
    - O banco é informado pelo usuário no request (ITAU ou SANTANDER)
4. **Aplica regras de negócio** (ver abaixo)
5. **Atualiza** a transação no banco:
    - Status: `APROVADA` ou `REPROVADA`
    - Motivo da decisão

### Mock de Saldo e Limite (MockAPI)

O Processor Worker consulta um **JSON hospedado na MockAPI** que simula dados bancários.

**Estrutura do JSON por banco:**

```json
{
  "saldo": 500.00,
  "limite": 300.00
}
```

**Bancos suportados:**
- ITAU
- SANTANDER

O usuário informa qual banco deseja usar ao criar a transação.

### Regras de Decisão

O Processor Worker aplica as seguintes regras para aprovar ou reprovar transações:

#### Forma de Pagamento: CRÉDITO
- **Consulta**: Limite disponível
- **Regra**: Se `limite ≥ valor da transação` → `APROVADA`
- **Caso contrário**: `REPROVADA`

**Exemplo:**
- Limite: R$ 500,00
- Transação: R$ 450,00
- **Resultado**: `APROVADA`
- **Motivo**: "Limite disponível após compra: 50.00"

#### Forma de Pagamento: DÉBITO ou PIX
- **Consulta**: Saldo disponível
- **Regra**: Se `saldo ≥ valor da transação` → `APROVADA`
- **Caso contrário**: `REPROVADA`

**Exemplo:**
- Saldo: R$ 120,00
- Transação: R$ 200,00
- **Resultado**: `REPROVADA`
- **Motivo**: "Saldo insuficiente. Saldo atual: 120.00"

---

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot** (Framework principal)
- **Spring Security** (Autenticação e autorização)
- **JWT** (JSON Web Token para autenticação stateless)
- **Apache Kafka** (Mensageria entre microsserviços)
- **OpenAPI / Swagger** (Documentação da API)
- **BrasilAPI** (Conversão de moedas USD/EUR para BRL)
- **MockAPI** (Simulação de dados bancários - saldo e limite)
- **Banco de Dados Relacional** (Postgres)

---

## Considerações Arquiteturais

- **Separação de responsabilidades**: Cada microsserviço tem uma função específica e bem definida
- **Desacoplamento via Kafka**: Transaction API e Processor Worker se comunicam via mensageria
- **Autenticação stateless**: Utilização de JWT para autenticação sem sessão
- **Integração externa**: Consumo de APIs externas (BrasilAPI) para dados reais de câmbio
- **Simulação realista**: MockAPI simula comportamento de bancos reais com saldo e limite
- **Clean Architecture**: Serviços Transaction-api e Processor Worker organizadas em clean architecture

---

