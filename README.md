# Control+

Aplicativo Android focado em controle pessoal unificando **controle financeiro** e **controle de tempo** em um painel inteligente.

## Objetivo diário

> **"Estou no controle hoje?"**

## MVP entregue

- Dashboard principal com indicador circular de **Nível de Controle do Dia**.
- Card financeiro com limite diário calculado automaticamente.
- Registro manual de gastos (com confirmação simulada via ação de UI).
- Card de tempo com uso de apps vs limites e alertas de estouro.
- Card de metas com progresso.
- Calendário inteligente com ações rápidas (Concluir, Adiar, Reprogramar).
- Heatmap mensal de gastos.
- Estrutura de dados preparada para integrações futuras (incluindo eventos de banco e usage access).

## Arquitetura

- `data/Models.kt`: entidades de domínio requisitadas.
- `data/MvpRepository.kt`: cálculo de orçamento diário, nível de controle e dados do dashboard.
- `MainViewModel.kt`: estado de UI e interações do MVP.
- `ui/screen/DashboardScreen.kt`: tela principal em Jetpack Compose com estilo dark/neon.
- `ui/theme/Theme.kt`: paleta premium dark com neon.

## CI

Workflow GitHub Actions em `.github/workflows/android.yml` para compilar APK debug a cada push e pull request.
