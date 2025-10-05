# compsecurity (Spring Boot 3 + Custom JWT + JPA + SAST/SCA)

## Запуск
```bash
export APP_JWT_SECRET="$(python3 - <<'PY'
import secrets, base64; print(base64.urlsafe_b64encode(secrets.token_bytes(32)).decode().rstrip('='))
PY)"

docker run --name compsecurity-pg -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=compsecurity -p 5432:5432 -d postgres:16

mvn spring-boot:run
```

Swagger UI: http://localhost:8080/swagger-ui/index.html

## Как это все работает (примеры)
```bash
# Регистрация
curl -s -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"StrongPass1!"}'

# Логин
TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"StrongPass1!"}' | jq -r .token)

# GET
curl -s http://localhost:8080/api/data -H "Authorization: Bearer $TOKEN"

# POST
curl -s -X POST http://localhost:8080/api/data \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"title":"<b>Hello</b>","content":"Hi <script>alert(1)</script> all!"}'
```
