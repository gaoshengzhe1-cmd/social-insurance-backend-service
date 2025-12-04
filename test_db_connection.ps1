# 测试数据库连接
$dbUser = "db_user"
$dbPassword = "local"
$dbName = "social_insurance"

# 测试 JDBC 连接 (Flyway 使用)
$jdbcUrl = "jdbc:postgresql://localhost:5432/$dbName"
$jdbcDriver = "org.postgresql.Driver"

try {
    Add-Type -Path "C:\Program Files\PostgreSQL\18\jdbc\postgresql-42.6.0.jar"
    $conn = [System.Data.DriverManager]::GetConnection("$jdbcUrl?user=$dbUser&password=$dbPassword")
    Write-Host "✅ JDBC 连接成功!" -ForegroundColor Green
    $conn.Close()
} catch {
    Write-Host "❌ JDBC 连接失败: $_" -ForegroundColor Red
}

# 测试 R2DBC 连接 (应用程序使用)
$r2dbcUrl = "r2dbc:postgresql://localhost:5432/$dbName"
try {
    $r2dbcConn = [Npgsql.NpgsqlConnection]::new("Host=localhost;Port=5432;Database=$dbName;Username=$dbUser;Password=$dbPassword")
    $r2dbcConn.Open()
    Write-Host "✅ R2DBC 连接成功!" -ForegroundColor Green
    $r2dbcConn.Close()
} catch {
    Write-Host "❌ R2DBC 连接失败: $_" -ForegroundColor Red
}