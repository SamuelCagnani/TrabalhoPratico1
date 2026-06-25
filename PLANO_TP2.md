# Plano de Desenvolvimento — Trabalho Prático 02

**App:** Resolva Já — Sistema de gerenciamento de chamados técnicos para suporte de infraestrutura e tecnologia.
**Autor:** Samuel de Mello Cagnani | Engenharia de Computação — 7º Período

---

## Credenciais Back4App
| Campo | Valor |
|-------|-------|
| App ID | `TpeRAGDu8RvvV4eravrUyelLzTlnUzvfzseBpmBk` |
| Client Key | `DXj3YPNozME9k14GyQR7890NsYgli2hsZnNemNDh` |
| API URL | `https://parseapi.back4app.com` |

---

## Etapa 1: Drawer Navigation ✅
- [x] 1.1 `activity_drawer_main.xml` — DrawerLayout + NavigationView + Toolbar + FrameLayout
- [x] 1.2 `menu/drawer_menu.xml` — Novo Chamado, Listagem, Atendimentos, Estatísticas, Sobre
- [x] 1.3 `layout/drawer_header.xml` — header com nome do app
- [x] 1.4 `MainDrawerActivity.java` — navegação entre Activities
- [x] 1.5 Ajustar `MainActivity.java` → abrir `MainDrawerActivity`
- [x] 1.6 Atualizar `AndroidManifest.xml`
- [x] 1.7 Remover `HomeActivity`

## Etapa 2: Tela Sobre ✅
- [x] 2.1 `activity_sobre.xml`
- [x] 2.2 `SobreActivity.java`
- [x] 2.3 Conectar ao drawer + manifest

## Etapa 3: Tela Estatísticas ✅
- [x] 3.1 Queries de contagem no `DatabaseHelper`
- [x] 3.2 `activity_estatisticas.xml`
- [x] 3.3 `EstatisticasActivity.java`
- [x] 3.4 Conectar ao drawer + manifest

## Etapa 4: Status no Cadastro ✅
- [x] 4.1 RadioGroup de Status em `activity_demand.xml`
- [x] 4.2 `DemandActivity.java` — ler e salvar status
- [x] 4.3 Padronizar status (Aberto / Em andamento / Concluído)

## Etapa 5: Câmera + Imagem ✅
- [x] 5.1 DB migration v1→v2: coluna `imagem_path`
- [x] 5.2 Campo `imagemPath` no modelo `Chamado`
- [x] 5.3 FileProvider + `file_paths.xml`
- [x] 5.4 Permissão `CAMERA` + runtime request
- [x] 5.5 Botão foto + ImageView preview no layout
- [x] 5.6 Lógica de captura no `DemandActivity`
- [x] 5.7 Miniatura no `item_chamado.xml`
- [x] 5.8 `ChamadoAdapter` — exibir miniatura
- [x] 5.9 `SpecificCallActivity` — imagem no detalhe

## Etapa 6: Back4App (Nuvem) ✅
- [x] 6.1 Parse SDK nas dependências
- [x] 6.2 Permissão `INTERNET`
- [x] 6.3 `App.java` — inicializar Parse
- [x] 6.4 Registrar no manifest
- [x] 6.5 Sincronizar `Chamado` com Parse
- [x] 6.6 Salvar na nuvem pós-cadastro local
- [x] 6.7 Tratamento de erro de rede
- [x] 6.8 Sincronizar atualizações de status

## Etapa 7: Finalização
- [ ] 7.1 Testar navegação
- [ ] 7.2 Testar cadastro + foto + cloud
- [ ] 7.3 Testar listagem com thumbnail
- [ ] 7.4 Testar estatísticas
- [ ] 7.5 Testar persistência cloud
