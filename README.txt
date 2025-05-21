# 🚄 Simulador Ferroviário em Java  
*Um sistema completo de simulação de trens com controle de colisões, passageiros e geração de relatórios*  

---

## 🌟 Features  
✅ **Movimento realista** de trens (60km/h)  
✅ **Sistema de desvios** automático para evitar colisões  
✅ **Embarque/desembarque** com tempo proporcional ao número de passageiros  
✅ **Relatórios completos** em TXT (logs e estatísticas)  
✅ Interface no console com **mapa visual** das estações  

---

## 🛠️ Tecnologias 

📦 Java 17+
├── 📂 Model (Trilhos, Trens, Estações)
├── 📂 View (Console interativo)
└── 📂 Controller (Lógica principal)

🚀 Como Executar
Compilar:

```
javac src/main/java/Main.java
```

Executar:

```
java -cp src/main/java Main
```

Controles:

```
plaintext
[ENTER]  - Avança 1 minuto
Q + [ENTER] - Sair
```

##🏗️ Estrutura do Projeto
```
src/
├── main/
│   ├── java/
│   │   ├── controller/
│   │   ├── model/
│   │   ├── view/
│   │   ├── utils/
│   │   └── Main.java
```

📊 Mecânicas Principais
🚂 Comportamento dos Trens
Parâmetro	Valor
Velocidade	1 km/min (60 km/h)
Capacidade	10-50 passageiros
Frequência	Novo trem a cada 30 min


👥 Sistema de Passageiros
```
// Código de exemplo (Trem.java)
if (passageiros < 10) {
    int adicional = 10 - passageiros;
    ultimosQueEntraram += adicional;
}
```

📝 Saída do Sistema
Console:

```
plaintext
ID | Pos (km) | Estação | Status   | Passageiros
1  |   15     |    2    | VIA     |     23
```

Arquivos gerados:

log_simulacao.txt ➔ Registro minuto a minuto

relatorio_final.txt ➔ Estatísticas completas

📌 Observação
ℹ️ Diferença entre embarcados/desembarcados ocorre porque:

Passageiros iniciais só aparecem nos desembarques

Trens podem terminar com passageiros não contabilizados

🎉 Simule, analise e divirta-se com este sistema ferroviário completo!