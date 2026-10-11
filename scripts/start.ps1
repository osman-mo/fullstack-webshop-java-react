$ErrorActionPreference = "Stop"

if (-not (Test-Path ".env")) {
    Copy-Item ".env.example" ".env"
    Write-Host "Created .env from .env.example. You can change the local-only passwords later."
}

docker compose up --detach --build --wait --wait-timeout 240
if ($LASTEXITCODE -ne 0) {
    docker compose ps
    docker compose logs --tail 100
    throw "The webshop stack did not become healthy. Logs are shown above."
}

Write-Host "Webshop is ready at http://localhost:5173" -ForegroundColor Green
Write-Host "Backend health: http://localhost:8080/api/actuator/health"
