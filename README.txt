🚆 Simulador de Sistema Ferroviário em Java
📌 Visão Geral
Este projeto simula o funcionamento de trens em uma linha ferroviária de via única com desvios, incluindo:

Movimentação de trens em sentidos opostos

Operações em estações

Prevenção de colisões

Geração de relatórios

🚀 Como Executar
Pré-requisitos:

Java JDK 17+ instalado

Terminal/Command Prompt

Execução:

bash
javac src/main/java/Main.java
java -cp src/main/java Main
🕹️ Controles da Simulação
Enter: Avança 1 minuto na simulação

Q + Enter: Encerra a simulação

⚙️ Mecânicas da Simulação
🚂 Sistema de Trens
Característica	Detalhe
Sentidos	Horário (A→B) e Anti-horário (B→A)
Velocidade	60 km/h (1 km/min)
Capacidade	Máximo de 50 passageiros
Frequência	Novo trem a cada 30 minutos (8h-17h30)
🏗️ Infraestrutura
Elemento	Descrição	Distância
Estações	Pontos de parada para operações	A cada 20 km
Desvios	Áreas para evitar colisões	1 km antes/depois estações
👥 Sistema de Passageiros
Embarque/Desembarque:

Até 10 passageiros por operação

30 segundos por passageiro

Total sempre par (ajuste automático)

Tempo de Parada:

Mínimo: 1 minuto (se nenhum passageiro)

Máximo: 10 minutos (20 passageiros)

📊 Saída do Sistema
Console:

plaintext
ID | Pos (km) | Estação | Status   | Passageiros | Entraram | Sairam | Tempo/Acao
--------------------------------------------------------------------------------
 1 |      15  |    1    | VIA     |         23  |     -    |   -    | -
Arquivos:

log_simulacao.txt: Registro detalhado por minuto

relatorio_final.txt: Estatísticas consolidados

📈 Estatísticas Geradas
Totais por estação:

Passageiros embarcados

Passageiros desembarcados

Métricas gerais:

Colisões evitadas

Tempo total em desvios

Passageiros transportados

🛠️ Estrutura do Código
bash
src/
├── main/
│   ├── java/
│   │   ├── controller/  # Lógica principal
│   │   ├── model/       # Entidades do sistema
│   │   ├── view/        # Exibição no console
│   │   ├── utils/       # Ferramentas auxiliares
│   │   └── Main.java    # Ponto de entrada
⏱️ Ciclo de Simulação
Cria trens conforme horário

Atualiza posição dos trens

Verifica colisões

Processa estações

Gera saída visual

Repete até término (17h30 + todos trens chegarem)

Este simulador oferece uma representação realista de sistemas ferroviários com vias únicas, demonstrando desafios operacionais e soluções de controle.