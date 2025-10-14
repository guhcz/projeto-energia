# Energia — API de Gestão de Consumo de Energia ⚡

Aplicação **Spring Boot** para cadastro e consulta de equipamentos e usuários. O projeto traz uma API REST para acompanhar consumo/instalações (equipamentos) e gerenciar usuários com perfis de acesso.

---

##  Como executar localmente com Docker

### Pré-requisitos
- Docker e Docker Compose instalados.  
- Portas **8080** (API) e **1521** (Oracle) livres.

### Passo a passo

### 1) Configurar variáveis de ambiente
Crie um arquivo `.env` na raiz do projeto (ou use o `.env.example`), por exemplo:
```env
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080

# JWT
APP_JWT_SECRET=troque-este-segredo
APP_JWT_ISSUER=Energia API

# Datasource (rede do compose → host do Oracle = "db")
SPRING_DATASOURCE_URL=jdbc:oracle:thin:@db:1521/XEPDB1
SPRING_DATASOURCE_USERNAME=energia
SPRING_DATASOURCE_PASSWORD=energiapwd

# Flyway
SPRING_FLYWAY_BASELINE_ON_MIGRATE=true

# Variáveis do container do Oracle (usadas no docker-compose)
ORACLE_PASSWORD=adminpwd
APP_USER=energia
APP_USER_PASSWORD=energiapwd
```

2) **Build da imagem da aplicação**
```bash
docker build -t energia:local .
```

3) **Suba os serviços**
```bash
docker compose up -d
```

4) **Acompanhe os logs no primeiro start**
```bash
docker compose logs -f db     # aguarde o Oracle inicializar
docker compose logs -f app    # verifique Flyway e o "Started EnergiaApplication..."
```

5) **Verifique a saúde da API**
```bash
curl http://localhost:8080/actuator/health
# ou abra no navegador: http://localhost:8080
```

6) **Fluxo rápido de autenticação**
```bash
# Registrar (se a rota estiver pública)
curl -X POST http://localhost:8080/auth/register   -H "Content-Type: application/json"   -d '{"nome":"Admin","email":"admin@exemplo.com","senha":"123456"}'

# Login (obter token JWT)
curl -X POST http://localhost:8080/auth/login   -H "Content-Type: application/json"   -d '{"email":"admin@exemplo.com","senha":"123456"}'
```

Use o token nas rotas protegidas:
```bash
curl -H "Authorization: Bearer SEU_TOKEN_AQUI" http://localhost:8080/api/equipamentos
```

7) **Parar e limpar**
```bash
docker compose down        # para e mantém dados
docker compose down -v     # para e apaga o volume do Oracle
```

> **Dicas**
> - Se a aplicação não conecta no Oracle, aguarde o **db** subir (veja logs) e confirme a URL `jdbc:oracle:thin:@db:1521/XEPDB1`.
> - Se mudar entidades, crie nova migração Flyway (V5+), não edite as antigas.

---

##  Pipeline CI/CD

> _Exemplo sugerido com **GitHub Actions** + **Docker Registry** (GHCR, Docker Hub, etc.). Ajuste para sua plataforma de CI/CD._

**Ferramentas**  
- **Maven**: build e testes (`mvn -B -DskipTests=false verify`)  
- **Docker Buildx**: build da imagem container multi-stage  
- **Registry**: push da imagem (`ghcr.io/SEU_USER/energia:TAG`)  
- **Ambientes**: _staging_ e _prod_ via `environments`/`secrets` do GitHub

**Etapas do pipeline**  
1. **Checkout & Setup JDK 21**  
2. **Cache Maven** e **build/testes**  
3. **Build de imagem Docker** com o Dockerfile do projeto  
4. **Push** da imagem para o registry (tag `sha` e `latest`/`main`)  
5. **Deploy**  
   - **Staging**: dispara deploy (por ex., `docker compose pull && up -d`) no servidor de staging via SSH ou Action própria  
   - **Produção**: gatilho manual (`workflow_dispatch`) com aprovação, repetindo o passo de deploy

**YAML de exemplo (resumo)**  
```yaml
name: ci-cd

on:
  push:
    branches: [ "main" ]
  workflow_dispatch:

jobs:
  build-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '21'
      - name: Build & Test
        run: mvn -B -DskipTests=false verify
      - name: Docker login
        run: echo "${{ secrets.REGISTRY_TOKEN }}" | docker login ghcr.io -u ${{ secrets.REGISTRY_USER }} --password-stdin
      - name: Build image
        run: docker build -t ghcr.io/${{ secrets.REGISTRY_USER }}/energia:${{ github.sha }} -t ghcr.io/${{ secrets.REGISTRY_USER }}/energia:latest .
      - name: Push image
        run: docker push ghcr.io/${{ secrets.REGISTRY_USER }}/energia --all-tags

  deploy-staging:
    needs: build-test
    runs-on: ubuntu-latest
    environment: staging
    steps:
      - name: Deploy via SSH
        uses: appleboy/ssh-action@v1.0.3
        with:
          host: ${{ secrets.STAGING_HOST }}
          username: ${{ secrets.STAGING_USER }}
          key: ${{ secrets.STAGING_SSH_KEY }}
          script: |
            docker login ghcr.io -u ${{ secrets.REGISTRY_USER }} -p ${{ secrets.REGISTRY_TOKEN }}
            docker pull ghcr.io/${{ secrets.REGISTRY_USER }}/energia:latest
            cd /opt/apps/energia && docker compose up -d

  deploy-prod:
    if: github.ref == 'refs/heads/main'
    needs: [build-test, deploy-staging]
    runs-on: ubuntu-latest
    environment: production
    steps:
      # mesmo passo de deploy alterando host/segredos de produção
      - name: Deploy prod via SSH
        uses: appleboy/ssh-action@v1.0.3
        with: { ... }
```

---

##  Containerização

**Dockerfile (do projeto)**  
```dockerfile
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -B -DskipTests clean package

FROM eclipse-temurin:24-jre
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

**Estratégias adotadas**
- **Multi-stage build**: reduz tamanho da imagem final e isola o ambiente de build do runtime
- **Cache de dependências** (via `go-offline`) para builds mais rápidos
- **Imagem JRE enxuta** (Temurin) na etapa final
- **Config por env**: `SPRING_PROFILES_ACTIVE=prod` e variáveis lidas no `docker-compose.yml`

**docker-compose.yml (trecho relevante)**  
```yaml
version: "3.9"

networks:
  energia-net:

volumes:
  oracle-data:

services:
  db:
    image: gvenzl/oracle-xe:21-slim
    container_name: oracle-xe
    environment:
      ORACLE_PASSWORD: adminpwd
      APP_USER: energia
      APP_USER_PASSWORD: energiapwd
    networks: [energia-net]
    volumes:
      - oracle-data:/opt/oracle/oradata
    ports:
      - "1521:1521"

  app:
    image: energia:local
    container_name: energia-app
    depends_on: [db]
    environment:
      SPRING_PROFILES_ACTIVE: prod
      DB_URL: jdbc:oracle:thin:@db:1521/XEPDB1
      DB_USER: energia
      DB_PASS: energiapwd
    ports:
      - "8080:8080"
    networks: [energia-net]
```

---

##  Prints do funcionamento

> Inclua aqui evidências do ciclo completo. Sugestões de checklist:

- **Pipeline CI/CD**
> <img width="1523" height="235" alt="image" src="https://github.com/user-attachments/assets/9cf8e637-86c4-47df-ba9b-1337317e8a8c" />
 

---

##  Tecnologias utilizadas

- **Linguagem/Runtime**: Java 21 (Temurin)  
- **Framework**: Spring Boot 3.4.x (Web, Validation)  
- **Segurança**: Spring Security + JWT (filtro `OncePerRequestFilter`, `TokenService`)  
- **Persistência**: Spring Data JPA (Oracle)  
- **Migrações**: Flyway (`src/main/resources/db.migrations`)  
- **Build**: Maven (multi-stage no Dockerfile)  
- **Banco**: Oracle XE 21 (imagem `gvenzl/oracle-xe`)  
- **Container**: Docker, Docker Compose  
- **Observabilidade**: Spring Actuator  
- **CI/CD (sugerido)**: GitHub Actions + Docker Registry (GHCR/Docker Hub)
