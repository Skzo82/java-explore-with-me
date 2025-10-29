Write-Host "`n=== [1] POST /hit ===" -ForegroundColor Green

$body = @{
  app = 'ewm-main-service'
  uri = '/events/1'
  ip  = '192.168.0.1'
  timestamp = (Get-Date).ToString('yyyy-MM-dd HH:mm:ss')
} | ConvertTo-Json

Write-Host "Invio hit..."
$response = Invoke-RestMethod -Uri "http://localhost:9090/hit" -Method Post -ContentType "application/json" -Body $body -ErrorAction SilentlyContinue
Write-Host "Risposta POST /hit:" $response
Start-Sleep -Seconds 1

Write-Host "`n=== [2] GET /stats (tutti) ===" -ForegroundColor Cyan

$start = (Get-Date).AddDays(-1).ToString('yyyy-MM-dd HH:mm:ss')
$end   = (Get-Date).AddDays(1).ToString('yyyy-MM-dd HH:mm:ss')
$startQ = [uri]::EscapeDataString($start)
$endQ   = [uri]::EscapeDataString($end)

$url = "http://localhost:9090/stats?start=$startQ`&end=$endQ`&unique=false"
Write-Host "Chiamo $url..."
$response = Invoke-RestMethod -Uri $url -Method Get -ErrorAction SilentlyContinue
$response | ConvertTo-Json -Depth 4

Write-Host "`n=== [3] GET /stats filtrato per URI ===" -ForegroundColor Yellow

$url = "http://localhost:9090/stats?start=$startQ`&end=$endQ`&uris=/events/1`&unique=true"
Write-Host "Chiamo $url..."
$response = Invoke-RestMethod -Uri $url -Method Get -ErrorAction SilentlyContinue
$response | ConvertTo-Json -Depth 4

Write-Host "`nTest completato!" -ForegroundColor Green