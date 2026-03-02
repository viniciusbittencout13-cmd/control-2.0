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

Workflow GitHub Actions em `.github/workflows/android.yml` para compilar APK debug a cada push/pull request e permitir execução manual (`workflow_dispatch`).

## Como baixar e executar no celular

### Opção 1 (mais simples): baixar APK pelo GitHub Actions

1. Faça push deste repositório para o GitHub.
2. Abra a aba **Actions** e execute o workflow **Android CI** (botão **Run workflow**) ou aguarde rodar no push.
3. Entre em uma execução com status verde.
4. Na seção **Artifacts**, baixe `controlplus-debug-apk`.
5. Extraia o `.zip` e pegue o arquivo `app-debug.apk`.
6. Envie o APK para seu celular e instale.

> No Android, ative temporariamente **Instalar apps desconhecidos** para o app usado para abrir o APK (Arquivos, Drive, etc.).

### Opção 2: instalar via cabo USB (ADB)

Com o celular em modo desenvolvedor e depuração USB ativa:

```bash
adb install -r app-debug.apk
```

### Observações importantes

- Esta versão é **debug** (MVP), ideal para testes.
- Alguns recursos de uso de apps e bloqueio dependem de permissões extras no aparelho.
- Se quiser distribuição pública (Play Store), o próximo passo é gerar build **release assinado**.
