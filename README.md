# Boilerplate de Autenticação JWT em Java com Spring Boot

## Introdução
Este documento fornece um boilerplate para configurar a autenticação JWT (JSON Web Token) em uma aplicação Spring Boot. JWT é um meio compacto e seguro de representar reivindicações a serem transferidas entre duas partes. Este exemplo guiará você na configuração de um sistema de autenticação básico usando JWT.

## Dependências
Aqui estão as dependências utilizadas no projeto, especificadas no arquivo `pom.xml`:

- `org.springframework.boot:spring-boot-starter-data-jpa`
- `org.springframework.boot:spring-boot-starter-security`
- `org.springframework.boot:spring-boot-starter-web`
- `io.jsonwebtoken:jjwt-api:0.12.5`
- `io.jsonwebtoken:jjwt-impl:0.12.5`
- `io.jsonwebtoken:jjwt-jackson:0.12.5`
- `org.postgresql:postgresql`
- `org.projectlombok:lombok`
- `org.springframework.boot:spring-boot-starter-test`
- `org.springframework.security:spring-security-test`


## Arquivo `SecurityConfig.java`

Este arquivo representa a configuração de segurança da aplicação, onde são definidas as políticas de autenticação e autorização.

- `@Configuration`: Indica que esta classe é uma configuração Spring.
- `@EnableWebSecurity`: Habilita a segurança web para a aplicação.
- `@RequiredArgsConstructor`: É uma anotação do Lombok que gera um construtor com todos os campos da classe que são final.

### Métodos:

- `securityFilterChain`: Este método configura as regras de segurança HTTP para a aplicação. Define que qualquer requisição para `/auth/**` e `/error` é permitida a todos (`permitAll()`), enquanto todas as outras requisições devem ser autenticadas (`authenticated()`). Além disso, configura a política de gerenciamento de sessão como STATELESS, ou seja, sem estado.
- `passwordEncoder`: Define um bean para o codificador de senhas. Neste caso, é utilizado o BCryptPasswordEncoder.
- `authenticationManager`: Define um bean para o gerenciador de autenticação, necessário para o processo de autenticação.

## Arquivo `JwtService.java`

Este arquivo contém a lógica para gerar, validar e extrair informações de tokens JWT (JSON Web Token).

- `@Service`: Indica que esta classe é um componente de serviço Spring.
- `SECRET_KEY`: Chave secreta utilizada para assinar e verificar a autenticidade dos tokens JWT.

### Métodos:

- `generateToken(User user)`: Gera um token JWT com base nas informações do usuário fornecido. O token é assinado com a chave secreta e inclui o nome de usuário como sujeito, a data de emissão e a data de expiração (24 horas após a emissão).
- `extractClaim(String token, Function<Claims, T> resolver)`: Extrai uma reivindicação específica do token JWT, utilizando uma função de resolução.
- `isValid(String token, UserDetails user)`: Verifica se o token JWT é válido para o usuário fornecido. Ele extrai o nome de usuário do token e verifica se coincide com o nome de usuário do usuário fornecido e se o token não expirou.
- `isTokenExpired(String token)`: Verifica se o token JWT expirou.
- `extractUsername(String token)`: Extrai o nome de usuário do token JWT.
- `ExtractExpirationTime(String token)`: Extrai a data de expiração do token JWT.
- `getSingInKey()`: Obtém a chave de assinatura a partir da chave secreta.
- `extractAllClaims(String token)`: Extrai todas as reivindicações do token JWT.

Essas funções são essenciais para lidar com a geração, validação e extração de informações de tokens JWT em um sistema de autenticação JWT com Spring Boot.


## Arquivo `JwtAuthenticationFilter.java`

Este arquivo contém a lógica para filtrar requisições HTTP e verificar se elas contêm um token JWT válido.

- `@Component`: Indica que esta classe é um componente Spring gerenciado pelo Spring Framework.
- `@RequiredArgsConstructor`: É uma anotação do Lombok que gera um construtor com todos os campos da classe que são final.

### Classe `JwtAuthenticationFilter`

A classe `JwtAuthenticationFilter` estende `OncePerRequestFilter`, garantindo que o filtro seja executado uma vez por requisição.

#### Campos:

- `jwtService`: Serviço responsável por lidar com tokens JWT.
- `userDetailsServiceImp`: Serviço para carregar detalhes do usuário com base no nome de usuário.

### Métodos:

- `doFilterInternal`: Este método é chamado para cada requisição HTTP. Ele contém a lógica para extrair e validar o token JWT da requisição.

  - `String authHeader = request.getHeader("Authorization")`: Extrai o cabeçalho "Authorization" da requisição.
  - `if (authHeader == null || !authHeader.startsWith("Bearer "))`: Verifica se o cabeçalho está presente e se começa com "Bearer ". Se não, continua com o próximo filtro no chain.
  - `String token = authHeader.substring(7)`: Extrai o token JWT removendo o prefixo "Bearer ".
  - `String username = jwtService.extractUsername(token)`: Extrai o nome de usuário do token.
  - `if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)`: Verifica se o nome de usuário foi extraído e se não há autenticação presente no contexto de segurança.
  - `UserDetails userDetails = userDetailsServiceImp.loadUserByUsername(username)`: Carrega os detalhes do usuário com base no nome de usuário.
  - `if (jwtService.isValid(token, userDetails))`: Verifica se o token é válido.
    - `UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())`: Cria um token de autenticação.
    - `authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request))`: Define os detalhes da autenticação.
    - `SecurityContextHolder.getContext().setAuthentication(authenticationToken)`: Define a autenticação no contexto de segurança.

  - `filterChain.doFilter(request, response)`: Passa a requisição e resposta para o próximo filtro na cadeia.

Este filtro assegura que cada requisição HTTP é verificada para um token JWT válido, e se válido, configura a autenticação no contexto de segurança do Spring.

