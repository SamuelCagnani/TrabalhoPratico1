# Resolva Já

Aplicativo Android desenvolvido como Trabalho Prático da disciplina de Engenharia de Computação (7º período), sob orientação do professor.

---

## Sobre o projeto

O **Resolva Já** é um sistema de gerenciamento de chamados técnicos voltado para equipes de suporte de infraestrutura e tecnologia. A ideia é permitir que o usuário registre uma demanda, acompanhe o andamento dela e consulte estatísticas rápidas sobre os chamados — tudo direto do celular, mesmo sem internet (os dados salvos localmente são sincronizados com a nuvem assim que houver conexão).

---

## Funcionalidades

- **Abertura de chamado** com título, descrição, local, categoria (Infra / Tecnologia) e status
- **Captura de foto** via câmera do dispositivo e anexo ao chamado
- **Listagem com filtros** por status (Aberto, Em andamento, Concluído), por data e busca textual
- **Fila de atendimento** exibindo apenas chamados abertos com contador
- **Tela de detalhes** do chamado com possibilidade de atualizar status e registrar solução técnica
- **Dashboard de estatísticas** com totalizadores por status
- **Sincronização com nuvem** via Back4App (Parse SDK) — salvamento local + remoto
- **Navegação por drawer lateral** com atalhos para as telas principais

---

## Tecnologias utilizadas

- **Linguagem:** Java
- **Interface:** Material Design 3 (com.google.android.material)
- **Banco local:** SQLite (SQLiteOpenHelper nativo, sem Room)
- **Backend cloud:** Parse Server (Back4App)
- **API de câmera:** ActivityResultLauncher + FileProvider
- **Build system:** Gradle (Kotlin DSL) + Version Catalog
- **Min SDK:** 24 (Android 7.0) | **Target SDK:** 36

---

## Estrutura do projeto

```
app/src/main/java/com/example/trabalhopratico1_samueldemellocagnani/
├── App.java                    // Inicialização do Parse
├── MainActivity.java           // Splash / tela de entrada
├── MainDrawerActivity.java     // Hub principal com drawer
├── DemandActivity.java         // Formulário de abertura de chamado
├── CallsActivity.java          // Lista de chamados com filtros
├── SpecificCallActivity.java   // Detalhes e edição do chamado
├── CustomerServiceActivity.java // Fila de atendimento
├── EstatisticasActivity.java   // Dashboard de estatísticas
├── SobreActivity.java          // Tela "Sobre"
├── Chamado.java                // Modelo de dados
├── ChamadoAdapter.java         // Adapter do RecyclerView
└── DatabaseHelper.java         // Gerenciador do SQLite
```

---

## Como compilar e rodar

1. Clone o repositório
2. Abra o projeto no Android Studio (Hedgehog ou superior recomendado)
3. Sincronize o Gradle (`File > Sync Project with Gradle Files`)
4. Execute em um dispositivo/emulador com Android 7.0+

> As credenciais do Back4App já estão configuradas na classe `App.java`. Não é necessário criar conta nem configurar chaves.

---

## Autor

**Samuel de Mello Cagnani**  
Engenharia de Computação — 7º período

---

## Versão

1.1 (versionCode 2)
