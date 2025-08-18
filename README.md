<img src="img/immusic.png">

Plataforma colaborativa para cadastro e pesquisa de artistas musicais, desenvolvida com Java e Spring Boot. Interface web intuitiva e backend robusto com persistência em banco de dados relacional.

---

## Estrutura do Projeto

- `src/main/java`: Código-fonte da aplicação
- `src/main/resources`: Templates HTML e arquivos de configuração
- `public/`: Arquivos estáticos (CSS, JS, imagens)
- `ArtistaController.java`: Endpoints REST e páginas web
- `ArtistaService.java`: Lógica de negócio
- `ArtistaRepository.java`: Interface com o banco de dados
- `templates/`: Páginas HTML (cadastro, pesquisa, admin)
- `static/`: Estilos e scripts da interface

---

## Funcionalidades Implementadas

- [x] Cadastro de artistas via API REST (`POST /api/artistas`)
- [x] Pesquisa por nome, estilo ou especialidade (`GET /api/artistas/buscar`)
- [x] Listagem completa de artistas (`GET /api/artistas`)
- [x] Consulta por ID (`GET /api/artistas/{id}`)
- [x] Estatísticas da plataforma (`GET /api/stats`)
- [x] Health check da aplicação (`GET /api/health`)
- [x] Página de cadastro com formulário validado (`/cadastro`)
- [x] Página de pesquisa com filtros dinâmicos (`/pesquisa`)
- [x] Página de administração com painel de estatísticas (`/admin`)
- [x] Persistência em banco de dados relacional (ex: PostgreSQL ou H2)
- [x] Logs com SLF4J
- [x] Deploy na nuvem via Railway

---

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.1.2
- Maven
- HTML, CSS, JavaScript (interface)
- Thymeleaf (templates dinâmicos)
- PostgreSQL ou H2 (persistência)
- SLF4J (logging)
- Railway (deploy)

---

## Como rodar o projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/QSoll/ImMusic
   cd immusic


<img src="img/logo_SM.png" widht="100px" height="100px">