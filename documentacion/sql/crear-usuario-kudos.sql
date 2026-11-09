-- Prepara SQL Server para que el back-end de UCO Kudos se pueda conectar.
-- Se ejecuta UNA sola vez en el SQL Server local (localhost), con un usuario administrador del servidor.
--
-- La clave del login debe coincidir con la que se configure en el archivo basedatos.properties del back-end
-- (ese archivo no se sube al repositorio). Cambie la clave de ejemplo antes de ejecutar el script.
--
-- Crea:
--   1. La base de datos Kudos (si no existe).
--   2. El login kudosUser, para entrar al servidor.
--   3. El usuario kudosUser dentro de la base Kudos, solo con permisos de leer y escribir datos
--      (select, insert, update, delete). No puede crear, modificar ni borrar tablas.

if db_id('Kudos') is null
    create database Kudos;
go

if not exists (select 1 from sys.server_principals where name = 'kudosUser')
    create login kudosUser with password = 'CambieEstaClave#2026', default_database = Kudos, check_policy = on;
go

use Kudos;
go

if not exists (select 1 from sys.database_principals where name = 'kudosUser')
    create user kudosUser for login kudosUser;
go

alter role db_datareader add member kudosUser;
go

alter role db_datawriter add member kudosUser;
go
