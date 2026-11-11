# Manual de puesta a punto — UCO Kudos

[Inicio](README.md)

Guía paso a paso para instalar y ejecutar UCO Kudos **desde cero en un equipo con Windows** (MAN-001 a MAN-007, RNF-030). Al terminar tendrá:

- La base de datos `Kudos` en SQL Server, con el primer Administrador y, si lo desea, usuarios de demostración.
- El back-end respondiendo en `http://localhost:8080`.
- El front-end abierto en `http://localhost:5173`, donde podrá ingresar **con su cuenta de Google**.

> **Estado de verificación** (10 de noviembre de 2026, Windows 11, SQL Server 2025, JDK 26.0.2, Node.js 24)
>
> | Parte | Estado |
> |---|---|
> | Requisitos previos y base de datos (§1, §2.2, §3.1) | ✅ Probado |
> | Compilación, pruebas y ejecución del back-end (§2.3, §5, §6) | ✅ Probado: `BUILD SUCCESS`, 240 pruebas, arranque en el puerto 8080 |
> | Instalación, pruebas, análisis estático y compilación del front-end (§2.4, §5, §6) | ✅ Probado: 112 pruebas, `npm run lint` sin errores, `npm run build` correcto |
> | Ingreso con Google (§3.2) | ✅ Probado en el navegador con el cliente OAuth del proyecto. En otro equipo se necesita un cliente propio (§3.2) |

---

## Antes de empezar

**Convenciones**

- Todos los comandos son para **PowerShell**: menú Inicio → escriba `PowerShell` → *Windows PowerShell*. En Linux o macOS, los equivalentes están en la §10.
- Cada paso termina con una **verificación**: un comando y el resultado que debe ver. Si el resultado no coincide, vaya a [Solución de problemas](#9-solución-de-problemas) antes de seguir.
- Donde vea `<algo>`, reemplácelo por su valor sin los signos `<` `>`.

**Resumen del proceso** (unos 30 minutos la primera vez, sin contar las descargas)

| Paso | Qué hace | Sección |
|:---:|---|---|
| 1 | Verificar o instalar las herramientas | [§1](#1-requisitos-previos-man-001) |
| 2 | Crear la base de datos | [§2.2](#22-base-de-datos) |
| 3 | Instalar las dependencias del back-end y del front-end | [§2.3](#23-back-end) y [§2.4](#24-front-end) |
| 4 | Configurar la conexión del back-end a la base | [§3](#3-configuración-man-003) |
| 5 | Arrancar los dos componentes e ingresar | [§5](#5-ejecución-man-005) |
| 6 | Ejecutar las pruebas | [§6](#6-pruebas-man-006) |

---

## 1. Requisitos previos (MAN-001)

### 1.1 Herramientas

| Herramienta | Versión | Para qué | Cómo verificar | Debe ver algo como |
|---|---|---|---|---|
| Git | 2.40 o superior | Descargar los repositorios | `git --version` | `git version 2.53.0.windows.3` |
| JDK (Java) | **26** | Compilar y ejecutar el back-end | `java -version` | `openjdk version "26.0.2"` |
| Maven | No se instala | El back-end trae su propio Maven (`mvnw.cmd`) | — | — |
| SQL Server | 2022 o 2025 (Developer o Express) | Base de datos | `Get-Service MSSQLSERVER` | `Running` |
| sqlcmd | Cualquiera reciente | Ejecutar los scripts SQL | `sqlcmd -?` | `Microsoft (R) SQL Server Command Line Tool` |
| Node.js | 22.18 o superior, o 24.12 o superior | Ejecutar el front-end | `node -v` | `v24.21.0` |
| npm | El que trae Node.js | Dependencias del front-end | `npm -v` | `11.19.0` |
| Bruno (opcional) | Reciente | Probar la API con la colección incluida | Abrir la aplicación | — |
| SQL Server Management Studio (opcional) | Reciente | Ver la base con interfaz gráfica | — | — |

Si falta alguna, estas son las descargas oficiales:

| Herramienta | Dónde |
|---|---|
| Git | <https://git-scm.com/download/win> |
| JDK 26 | <https://jdk.java.net/26/> o <https://adoptium.net/> |
| SQL Server Developer | <https://www.microsoft.com/es-es/sql-server/sql-server-downloads> (en el instalador elija *Básico*) |
| sqlcmd | Viene con SQL Server. Si no aparece: <https://learn.microsoft.com/sql/tools/sqlcmd/sqlcmd-utility> |
| Node.js | <https://nodejs.org/> (versión LTS) |
| Bruno | <https://www.usebruno.com/downloads> |

> **Java y `JAVA_HOME`.** Si `java -version` muestra otra versión o no se reconoce, configure la variable `JAVA_HOME` con la carpeta del JDK 26 y agregue `%JAVA_HOME%\bin` al `Path`: *Inicio → "Editar las variables de entorno del sistema" → Variables de entorno*. Después cierre y vuelva a abrir PowerShell.

### 1.2 Configuración de SQL Server

El back-end entra a la base con un **usuario y contraseña de SQL Server** (`kudosUser`) por **TCP en el puerto 1433**. Por eso SQL Server necesita dos cosas:

**a) Autenticación en modo mixto** (Windows + SQL Server)

Verificación:

```powershell
sqlcmd -S localhost -E -Q "select case serverproperty('IsIntegratedSecurityOnly') when 0 then 'MODO MIXTO: OK' else 'SOLO WINDOWS: hay que cambiarlo' end"
```

Si dice `SOLO WINDOWS`:

1. Abra SQL Server Management Studio y conéctese con *Autenticación de Windows*.
2. Clic derecho sobre el servidor → *Propiedades* → *Seguridad*.
3. Elija *Modo de autenticación de SQL Server y de Windows* y acepte.
4. Reinicie el servicio desde un PowerShell **como administrador**: `Restart-Service MSSQLSERVER`.

**b) Protocolo TCP/IP activo en el puerto 1433**

Verificación:

```powershell
sqlcmd -S "tcp:localhost,1433" -E -Q "select 'TCP 1433: OK'"
```

Si falla con un error de conexión:

1. Abra *SQL Server Configuration Manager*: busque `SQLServerManager17.msc` (2025) o `SQLServerManager16.msc` (2022) en el menú Inicio.
2. *Configuración de red de SQL Server* → *Protocolos de MSSQLSERVER* → clic derecho en **TCP/IP** → *Habilitar*.
3. Doble clic en **TCP/IP** → pestaña *Direcciones IP* → al final, en *IPAll*, ponga `1433` en *Puerto TCP* y deje vacío *Puertos TCP dinámicos*.
4. Reinicie el servicio: `Restart-Service MSSQLSERVER` (como administrador).

---

## 2. Instalación (MAN-002)

### 2.1 Obtener el código

Elija una carpeta de trabajo, por ejemplo `C:\UCOKudos`, y descargue los dos repositorios:

```powershell
New-Item -ItemType Directory -Force C:\UCOKudos
Set-Location C:\UCOKudos
git clone <url-repositorio-back-end> kudos-backend
git clone <url-repositorio-front-end> kudos-frontend
```

Si recibió el proyecto en carpetas o en un `.zip`, cópielas en `C:\UCOKudos` con esos mismos nombres.

**Verificación:** `Get-ChildItem C:\UCOKudos` muestra `kudos-backend` y `kudos-frontend`.

### 2.2 Base de datos

Los scripts están en la carpeta [`sql/`](sql) de la documentación y también en el repositorio del back-end. Se ejecutan **una sola vez**, **en este orden**.

**Paso 0. Ponga su cuenta de Google como primer Administrador.** Abra `sql\datos-iniciales-kudos.sql` con el Bloc de notas, busque `admin.kudos@uco.edu.co` (aparece en el comentario y en el `insert`) y cámbielo por **su correo de Google**. Ese será el único usuario que puede ingresar al principio (D-18). El dominio de su correo debe estar en el parámetro `DOMINIO_CORREO` del mismo archivo (por defecto `gmail.com,uco.net.co`).

**Paso 1. Elija la clave del usuario de la base de datos.** Abra `sql\crear-usuario-kudos.sql` con el Bloc de notas, busque `CambieEstaClave#2026` y cámbiela por su clave. Debe tener al menos 8 caracteres, con mayúsculas, minúsculas, números y símbolos. Guarde el archivo y **anote la clave**: la usará en la §3.

**Paso 2. Ejecute los scripts.** Abra PowerShell en la carpeta `documentacion` (o en la raíz del repositorio del back-end, que también tiene la carpeta `sql`). Ejecute:

```powershell
# 1. Crea la base Kudos y el usuario kudosUser (solo lectura y escritura de datos)
sqlcmd -S localhost -E -I -f 65001 -i .\sql\crear-usuario-kudos.sql
# 2. Crea las 16 tablas, sus restricciones y sus índices
sqlcmd -S localhost -E -I -f 65001 -i .\sql\crear-tablas-kudos.sql
# 3. Carga los parámetros globales, el primer Administrador y las categorías
sqlcmd -S localhost -E -I -f 65001 -i .\sql\datos-iniciales-kudos.sql
# 4. (Opcional) Carga los usuarios de demostración, uno por rol, con Kudos y réplicas
sqlcmd -S localhost -E -I -f 65001 -i .\sql\datos-demo-kudos.sql
```

Qué significa cada opción:

| Opción | Significado |
|---|---|
| `-S localhost` | Servidor SQL Server de este equipo |
| `-E` | Entrar con su usuario de Windows, que es administrador del servidor |
| `-I` | Activa `QUOTED_IDENTIFIER`, obligatorio para los índices filtrados. **No la omita** |
| `-f 65001` | Lee los archivos en UTF-8, para que las tildes queden bien |
| `-i` | Archivo a ejecutar |

Si un comando no muestra nada, terminó bien. Si muestra `Msg ...`, es un error: vea la [§9](#9-solución-de-problemas).

> Con SQL Server Management Studio se obtiene lo mismo: abra cada archivo en el mismo orden y ejecútelo con **F5**.

**Paso 3. Verifique.**

```powershell
# Deben existir 16 tablas
sqlcmd -S localhost -E -d Kudos -Q "select count(*) as tablas from sys.tables"
# El usuario de la aplicación debe poder entrar con su clave
sqlcmd -S localhost -U kudosUser -P "<su clave>" -d Kudos -Q "select count(*) as usuarios from usuario"
```

Resultado esperado:

| Consulta | Resultado |
|---|---|
| Tablas | `16` |
| Usuarios | `1` solo con los datos iniciales, o `10` con los datos de demostración |

> Para volver a empezar desde cero, borre la base y el usuario, y repita el paso 2:
> `sqlcmd -S localhost -E -Q "drop database if exists Kudos; drop login kudosUser"`

### 2.3 Back-end

```powershell
Set-Location C:\UCOKudos\kudos-backend
.\mvnw.cmd clean install -DskipTests
```

La primera vez descarga Maven y las dependencias, así que puede tardar varios minutos.

**Verificación:** al final aparece `BUILD SUCCESS` y existe el archivo `target\kudos-<versión>.jar`.

### 2.4 Front-end

```powershell
Set-Location C:\UCOKudos\kudos-frontend
npm install
```

**Verificación:** termina sin líneas `npm ERR!` y se crea la carpeta `node_modules`. Los avisos `npm warn` no impiden continuar.

---

## 3. Configuración (MAN-003)

### 3.1 Conexión del back-end a la base de datos

Cree el archivo `C:\UCOKudos\kudos-backend\src\main\resources\basedatos.properties` con este contenido, poniendo la clave que eligió en la §2.2:

```properties
basedatos.url=jdbc:sqlserver://localhost:1433;databaseName=Kudos;encrypt=true;trustServerCertificate=true
basedatos.usuario=kudosUser
basedatos.clave=<su clave>
```

Este archivo **no se sube al repositorio**, igual que en Sigma, porque contiene la clave. Para crearlo con el Bloc de notas:

```powershell
Set-Location C:\UCOKudos\kudos-backend\src\main\resources
notepad basedatos.properties
```

El Bloc de notas pregunta si desea crear el archivo: responda **Sí**, pegue las tres líneas de arriba con su clave y guarde.

| Propiedad | Qué es |
|---|---|
| `basedatos.url` | Dirección de SQL Server. `trustServerCertificate=true` acepta el certificado local de desarrollo |
| `basedatos.usuario` | Usuario creado por `crear-usuario-kudos.sql` |
| `basedatos.clave` | La clave del paso 1 de la §2.2 |

### 3.2 Inicio de sesión con Google

La plataforma usa Google para saber quién es la persona, igual que Sigma. La cuenta, su aprobación y su rol los decide la base de datos de UCO Kudos.

1. En [Google Cloud Console](https://console.cloud.google.com/) cree un proyecto (o use uno existente).
2. En **APIs y servicios → Pantalla de consentimiento de OAuth**, configure la aplicación como **Externa**. Mientras esté en modo **Prueba**, agregue como **usuarios de prueba** las cuentas de Google con las que se va a ingresar.
3. En **APIs y servicios → Credenciales → Crear credenciales → ID de cliente de OAuth**, elija **Aplicación web** y en **URI de redireccionamiento autorizados** agregue exactamente:

   ```
   http://localhost:8080/login/oauth2/code/google
   ```

4. Copie el **ID de cliente** y el **Secreto del cliente** y cree el archivo `C:\UCOKudos\kudos-backend\src\main\resources\oauth.properties`:

   ```powershell
   Set-Location C:\UCOKudos\kudos-backend\src\main\resources
   notepad oauth.properties
   ```

   ```properties
   spring.security.oauth2.client.registration.google.client-id=<su id de cliente>
   spring.security.oauth2.client.registration.google.client-secret=<su secreto>
   spring.security.oauth2.client.registration.google.scope=openid,profile,email
   ```

Este archivo tampoco se sube al repositorio. Si el back-end corre en otro puerto (§4), la URI de redireccionamiento del paso 3 debe usar ese mismo puerto.

**Verificación:** con el back-end en ejecución (§5), abra `http://localhost:8080/oauth2/authorization/google` en el navegador: debe aparecer la pantalla de Google para elegir la cuenta.

### 3.3 Front-end

No requiere configuración. En desarrollo, Vite redirige todas las peticiones `/api` hacia el back-end (*proxy*), así que no hace falta configurar CORS. Si el back-end corre en otra dirección, use las variables `BACKEND_URL` y `VITE_URL_INICIO_SESION_GOOGLE` (§4).

---

## 4. Variables de entorno (MAN-004)

Son **opcionales**. Si existen, tienen prioridad sobre `basedatos.properties` y los valores por defecto. Sirven sobre todo en un servidor (§7), donde no conviene guardar la clave en un archivo.

| Variable | Componente | Valor por defecto | Descripción |
|---|---|---|---|
| `KUDOS_BD_URL` | Back-end | El de `basedatos.properties` | Cadena JDBC de SQL Server |
| `KUDOS_BD_USUARIO` | Back-end | El de `basedatos.properties` | Usuario de la base de datos |
| `KUDOS_BD_CLAVE` | Back-end | El de `basedatos.properties` | Clave de la base de datos |
| `SERVER_PORT` | Back-end | `8080` | Puerto del back-end |
| `BACKEND_URL` | Front-end | `http://localhost:8080` | Dirección del back-end para el *proxy* de desarrollo de Vite |
| `VITE_URL_INICIO_SESION_GOOGLE` | Front-end | `http://localhost:8080/oauth2/authorization/google` | Dirección a la que lleva el botón "Ingresar con Google" |
| `VITE_API_BASE_URL` | Front-end | `/api` | Prefijo de las llamadas a la API (cámbielo solo si publica el back-end en otra ruta) |

**Solo para la ventana actual de PowerShell:**

```powershell
$env:KUDOS_BD_CLAVE = "<su clave>"
$env:SERVER_PORT = "8081"
```

**Permanente para su usuario de Windows** (se aplica en las ventanas que abra **después**):

```powershell
setx KUDOS_BD_CLAVE "<su clave>"
```

**Verificación:** `echo $env:KUDOS_BD_CLAVE` muestra el valor. Para quitar una variable permanente: `[Environment]::SetEnvironmentVariable("KUDOS_BD_CLAVE", $null, "User")`.

---

## 5. Ejecución (MAN-005)

Necesita **dos ventanas de PowerShell**, una para cada componente. Arranque primero el back-end.

**Ventana 1 — back-end**

```powershell
Set-Location C:\UCOKudos\kudos-backend
.\mvnw.cmd spring-boot:run
```

Espere la línea `Started KudosApplication in ... seconds`. El back-end queda en `http://localhost:8080`. Si en su lugar aparece un error de conexión a la base, revise la §3.1 y la §9.

**Ventana 2 — front-end**

```powershell
Set-Location C:\UCOKudos\kudos-frontend
npm run dev
```

Espere la línea `Local: http://localhost:5173/`.

**Verificación:**

1. Abra `http://localhost:5173` en Chrome, Edge o Firefox. Debe ver la pantalla de ingreso con el botón **Ingresar con Google**.
2. Ingrese con la cuenta de Google que puso como Administrador en la §2.2. Debe ver el muro de Kudos y, a la izquierda, el menú del Administrador.
3. Para comprobar que el back-end responde por sí solo, abra `http://localhost:8080/api/v1/kudos` en el navegador **sin haber ingresado** (o en una ventana privada). Debe responder **401**: eso confirma que está activo y protegido.

**Cuentas para ingresar**

No hay contraseñas: se ingresa con Google. Las personas de `datos-demo-kudos.sql` (una por rol, con correos `@uco.edu.co`) sirven para ver datos, pero para **entrar** con uno de esos roles hay que asignarle una cuenta de Google real. Use cuentas de prueba (por ejemplo Gmail) y agréguelas como **usuarios de prueba** en la pantalla de consentimiento de Google (§3.2). Luego, por cada rol que quiera probar:

```powershell
sqlcmd -S localhost -E -d Kudos -Q "update usuario set correo_institucional = '<cuenta de google en minúsculas>' where correo_institucional = 'mgomez@uco.edu.co'"
```

| Rol | Correo en los datos de demostración |
|---|---|
| Rector | `mgomez@uco.edu.co` |
| Coordinador Académico | `crestrepo@uco.edu.co` |
| Coordinadora de Facultad (Ingeniería) | `lospina@uco.edu.co` |
| Coordinador de Programa (Ingeniería de Sistemas) | `jzuluaga@uco.edu.co` |
| Docente | `agiraldo@uco.edu.co` |
| Estudiantes | `shenao@uco.edu.co` · `vcardona@uco.edu.co` |
| Cuenta pendiente de aprobación | `marango@uco.edu.co` |

También puede probar el **registro** con una cuenta de Google que no esté en la base: al ingresar, la aplicación muestra el formulario de registro.

**Recorrido rápido para comprobar que todo funciona**

1. Ingrese como **Estudiante** y dé un Kudo al Docente. Responda un Kudo del muro y califíquelo.
2. Ingrese como **Docente**: la campana muestra el aviso del Kudo recibido. Reporte un contenido que no sea suyo.
3. Ingrese como **Rector**: en *Moderación* verá el reporte. Inicie la revisión y vete o libere el contenido. En *Auditoría* verá cada acción.
4. Ingrese como **Coordinador de Programa**: en *Solicitudes* apruebe la cuenta pendiente.

**Códigos de verificación.** El envío de SMS está **simulado** (D-10). Al registrarse o al cambiar el celular, el código de 6 dígitos aparece en la pantalla de verificación, en un aviso de **modo demostración**, y en la **ventana 1** (log del back-end). El correo no necesita código: lo verifica Google.

**Para detener** cada componente, pulse `Ctrl + C` en su ventana.

---

## 6. Pruebas (MAN-006)

| Qué se prueba | Comando | Resultado esperado |
|---|---|---|
| Back-end: pruebas unitarias del dominio y del negocio, con cobertura | `Set-Location C:\UCOKudos\kudos-backend` y luego `.\mvnw.cmd test` | `Tests run: 240, Failures: 0, Errors: 0` y `BUILD SUCCESS` |
| Front-end: pruebas con Vitest | `Set-Location C:\UCOKudos\kudos-frontend` y luego `npm run test:unit -- --run` | `Tests  112 passed (112)` |
| Front-end: análisis estático | `npm run lint` | Sin errores ni advertencias |
| Base de datos: restricciones de integridad | Desde la carpeta `documentacion`: `python .\sql\validar_sql.py` | `Pruebas que no se rechazaron: 0` y `Base temporal eliminada` |

- **Reporte de cobertura del back-end** (JaCoCo): después de `.\mvnw.cmd test`, abra `C:\UCOKudos\kudos-backend\target\site\jacoco\index.html` en el navegador.
- **Pruebas de la API con Bruno:**
  1. Arranque el back-end (§5) e ingrese con Google en el navegador.
  2. Copie la cookie de sesión: con la página del back-end abierta, `F12` → *Application* → *Cookies* → `http://localhost:8080` → valor de `JSESSIONID`.
  3. En Bruno, elija *Open Collection* y seleccione la carpeta `bruno\Kudos` del repositorio del back-end.
  4. Arriba a la derecha, elija el entorno **Local** y pegue la cookie en la variable secreta `sesion`. Las variables `idKudo`, `idReplica` y demás traen ids de la base de demostración; si su base es otra, cópielos de las respuestas de consulta.
  5. Ejecute las peticiones de cada carpeta (16 carpetas, 84 peticiones). Sin la cookie, todas responden 401.
- **Validación de la base de datos:** necesita Python 3 y SQL Server local con autenticación de Windows. Crea una base temporal (`KudosValidacion`), intenta 16 operaciones que violan reglas de negocio, comprueba que todas se rechacen y borra la base al terminar. No toca la base `Kudos`.

---

## 7. Despliegue (MAN-007)

Para instalar la solución en un servidor de la Universidad:

1. **Base de datos.** En el SQL Server de destino, ejecute los scripts 1 a 3 de la §2.2, con una clave fuerte en `crear-usuario-kudos.sql`. El script 4 es solo para demostraciones.
2. **Google.** Cree el cliente OAuth con la URI de redireccionamiento del servidor (`https://<servidor>/login/oauth2/code/google`) y publique la aplicación en la pantalla de consentimiento.
3. **Back-end.**
   - Genere el ejecutable en el equipo de desarrollo: `.\mvnw.cmd clean package`.
   - Copie `target\kudos-<versión>.jar` al servidor, que necesita el JDK 26.
   - Configure en el servidor las variables `KUDOS_BD_URL`, `KUDOS_BD_USUARIO` y `KUDOS_BD_CLAVE` (§4), en lugar de usar `basedatos.properties`, y el archivo `oauth.properties` con el cliente de Google del servidor.
   - En `SeguridadConfiguracion`, la dirección del front-end (`URL_FRONTEND`) debe ser la del servidor.
   - Ejecute `java -jar kudos-<versión>.jar`. Para que arranque con el equipo, regístrelo como servicio de Windows, por ejemplo con WinSW o NSSM.
4. **Front-end.**
   - Genere los archivos estáticos: `npm run build`. Se crea la carpeta `dist\`.
   - Publique `dist\` en un servidor web, como IIS o Nginx.
   - Configure que las rutas `/api` se redirijan al back-end, igual que hace el *proxy* en desarrollo. En IIS se usa *URL Rewrite* con *Application Request Routing*.
   - Configure que las demás rutas devuelvan `index.html`, porque la aplicación usa rutas del lado del cliente.
5. **Verificación.**
   - Ingrese con la cuenta de Google del Administrador inicial (§2.2, paso 0).
   - Registre al Rector. Desde ahí se arma la estructura: el Rector aprueba al Coordinador Académico y este crea Facultades y Programas (D-18).

---

## 8. Detener, reiniciar y actualizar

| Quiero… | Cómo |
|---|---|
| Detener la aplicación | `Ctrl + C` en cada ventana de PowerShell |
| Volver a arrancarla | Repita la §5. No hace falta repetir la instalación |
| Traer cambios nuevos del código | `git pull` en cada repositorio. Después: `.\mvnw.cmd clean install -DskipTests` en el back-end y `npm install` en el front-end |
| Reiniciar los datos | Borre la base (§2.2, nota final) y vuelva a ejecutar los scripts |

---

## 9. Solución de problemas

| Síntoma | Causa probable | Solución |
|---|---|---|
| `CREATE INDEX failed because ... QUOTED_IDENTIFIER` | Se ejecutó `sqlcmd` sin la opción `-I` | Borre la base (§2.2) y repita los scripts con `-I`, o ejecútelos desde SSMS |
| Las tildes se ven como `Ã©` | Se ejecutó `sqlcmd` sin `-f 65001` | Repita los scripts con `-f 65001` |
| `Login failed for user 'kudosUser'` | Clave distinta a la del script, o SQL Server en modo *solo Windows* | Revise la clave en `basedatos.properties` y verifique el modo mixto (§1.2 a) |
| `The TCP/IP connection to the host localhost, port 1433 has failed` | TCP/IP desactivado, otro puerto o servicio detenido | Active TCP/IP en el puerto 1433 (§1.2 b) y verifique `Get-Service MSSQLSERVER` |
| `Cannot open database "Kudos"` | No se ejecutaron los scripts | Ejecute la §2.2 |
| `There is already an object named ...` | Los scripts se ejecutaron dos veces | Borre la base y repita la §2.2 desde el principio |
| `.\mvnw.cmd : El término no se reconoce` | PowerShell no está en la carpeta del back-end | `Set-Location C:\UCOKudos\kudos-backend` |
| `JAVA_HOME is not defined correctly` o versión de Java equivocada | `JAVA_HOME` sin configurar o apuntando a otro JDK | Configure `JAVA_HOME` con el JDK 26 (§1.1) y abra una ventana nueva |
| `No se puede cargar el archivo ...\npm.ps1 porque la ejecución de scripts está deshabilitada` | Política de ejecución de PowerShell | Ejecute una vez `Set-ExecutionPolicy -Scope CurrentUser RemoteSigned`, o use `npm.cmd` en lugar de `npm` |
| `Port 8080 was already in use` | Otro programa usa el puerto | Vea cuál es con `netstat -ano \| findstr :8080`, ciérrelo, o arranque con `$env:SERVER_PORT="8081"` y `$env:BACKEND_URL="http://localhost:8081"` (y la URI de Google con ese puerto) |
| `Port 5173 is in use` | Otra instancia de Vite | Vite toma el siguiente puerto libre: use la dirección que muestra en pantalla |
| El navegador muestra errores de red al ingresar | El back-end no está corriendo | Revise la ventana 1 (§5) |
| `401` en todas las peticiones | No hay sesión iniciada o expiró (30 minutos sin actividad) | Ingrese de nuevo |
| Al ingresar aparece el registro en lugar del muro | Su correo de Google no está en la base | Regístrese, o asigne su correo a un usuario de demostración (§5) |
| La pantalla muestra el estado de la cuenta en lugar del muro | Falta la aprobación o la verificación del celular | Revise lo que indica la pantalla. El código del celular aparece en el aviso de demostración y en el log del back-end |
| Google muestra `Error 403: access_denied` | La cuenta no está como usuario de prueba del cliente OAuth | Agréguela en la pantalla de consentimiento (§3.2) |
| Google muestra `redirect_uri_mismatch` | La URI de redireccionamiento no coincide con el puerto del back-end | Use exactamente `http://localhost:8080/login/oauth2/code/google` (§3.2) |

---

## 10. Linux y macOS

Los pasos son los mismos. Solo cambian estos comandos:

| Windows (PowerShell) | Linux / macOS |
|---|---|
| `.\mvnw.cmd ...` | `./mvnw ...` |
| `Set-Location <carpeta>` | `cd <carpeta>` |
| `-i .\sql\crear-tablas-kudos.sql` | `-i ./sql/crear-tablas-kudos.sql` |
| `-E` (usuario de Windows) | `-U sa -P '<clave de sa>'` |
| `$env:VARIABLE = "valor"` | `export VARIABLE=valor` |
| `Get-Service MSSQLSERVER` | `systemctl status mssql-server` |
