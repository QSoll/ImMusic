markdown
# 🎶 I'MUSIC Platform

Plataforma colaborativa para cadastro e pesquisa de artistas musicais, utilizando Java com Spring Boot e integração com Google Sheets como base de dados.

---

## 📦 Estrutura do Projeto


---

## ✅ Funcionalidades Implementadas

- [x] Cadastro de artistas via API REST (`/api/artistas`)
- [x] Pesquisa de artistas por nome e estilo (`/api/artistas/buscar`)
- [x] Listagem completa de artistas (`/api/artistas`)
- [x] Consulta por ID (`/api/artistas/{id}`)
- [x] Estatísticas da plataforma (`/api/stats`)
- [x] Health check da aplicação (`/api/health`)
- [x] Página de cadastro (`cadastro.html`)
- [x] Página de pesquisa (`pesquisa.html`)
- [x] Página de administração (`admin.html`)
- [x] Integração com Google Sheets via `GoogleSheetsService`

---

## 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot 3.1.2
- Maven
- Google Sheets API
- HTML, CSS, JavaScript (interface)
- SLF4J (para logs)

---

## 📋 Como rodar o projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/immusic.git
   cd immusic
Configure as credenciais da Google Sheets API:

Crie um projeto no Google Cloud Console

Ative a Google Sheets API

Baixe o credentials.json e coloque em src/main/resources

Compile e rode:

bash
mvn spring-boot:run
Acesse:

http://localhost:8080 → Página inicial

http://localhost:8080/cadastro → Cadastro de artista

http://localhost:8080/pesquisa → Pesquisa de artistas

http://localhost:8080/admin → Administração

📌 Próximos passos
[ ] Criar GoogleSheetsService.java com autenticação e métodos de leitura/escrita

[ ] Criar index.html com resumo da plataforma

[ ] Criar admin.html com painel de estatísticas

[ ] Criar CSS base para unificar o visual

[ ] Adicionar autenticação (opcional)

[ ] Criar exportação para CSV ou PDF (opcional)

👩‍💻 Desenvolvedora
Solange — Espírito Santo, Brasil Projeto iniciado em agosto de 2025 com apoio do Copilot da Microsoft 🤝

Cadastro de Artistas com Google Sheets + Apps Script
Este projeto permite cadastrar artistas diretamente em uma planilha do Google Sheets, usando um formulário personalizado em HTML e JavaScript. Os dados são enviados via requisição POST para um script do Google Apps Script que insere os registros na planilha.

📌 Objetivo
Criar um sistema simples e funcional para:

Cadastrar artistas com nome artístico, estilo musical, especialidade e Instagram.

Armazenar os dados em uma planilha do Google Sheets.

Utilizar Google Apps Script como backend gratuito e confiável.

🛠️ Tecnologias Utilizadas
HTML + CSS (interface do formulário)

JavaScript (envio dos dados via fetch)

Google Sheets (armazenamento dos dados)

Google Apps Script (API personalizada para receber os dados)

📋 Campos do Formulário
Nome Artístico

Estilo Musical (dropdown)

Especialidade (dropdown)

Link do Instagram

🔄 Fluxo de Funcionamento
Usuário preenche o formulário na página web.

Ao clicar em “Cadastrar”, os dados são enviados via fetch() para o endpoint do Apps Script.

O Apps Script recebe os dados e insere uma nova linha na planilha.

O usuário recebe uma mensagem de sucesso ou erro.

🚀 Etapas para Implementação
Criar a planilha no Google Sheets

Nome da aba: Artistas

Cabeçalhos: Nome Artístico | Estilo Musical | Especialidade | Instagram

Criar o script no Google Apps Script

Código para receber os dados e inserir na planilha

Publicar como Web App (com permissão “Qualquer um, mesmo anônimo”)

Desenvolver o formulário HTML

Interface amigável com campos e botões

JavaScript para capturar os dados e enviar via fetch

Testar o sistema

Verificar se os dados estão sendo inseridos corretamente

Validar mensagens de sucesso/erro

⚠️ Limites e Cuidados
Google Sheets aceita até 10 milhões de células por planilha.

O Apps Script tem limite de 60 requisições por minuto por usuário.

Evite spam ou envios em massa para não ultrapassar os limites.