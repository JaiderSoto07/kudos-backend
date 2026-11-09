# Valida los scripts SQL de UCO Kudos (ejecutar: python validar_sql.py, requiere SQL Server local con autenticación de Windows y sqlcmd)
# en una base temporal y prueba que las restricciones rechacen violaciones.
import subprocess, os, tempfile

SQL = os.path.dirname(os.path.abspath(__file__))
BD = "KudosValidacion"
TMP = tempfile.mkdtemp()


def sqlcmd(texto, base="master"):
    ruta = os.path.join(TMP, "q.sql")
    with open(ruta, "w", encoding="utf-8") as f:
        f.write(texto)
    r = subprocess.run(["sqlcmd", "-S", "localhost", "-E", "-b", "-I", "-f", "65001", "-d", base, "-i", ruta],
                       capture_output=True, text=True, encoding="utf-8", errors="replace")
    return r.returncode, (r.stdout + r.stderr).strip()


def script(nombre):
    t = open(os.path.join(SQL, nombre), encoding="utf-8").read()
    return t.replace("use Kudos;", f"use {BD};")


print(sqlcmd(f"if db_id('{BD}') is not null begin alter database {BD} set single_user with rollback immediate; drop database {BD}; end; create database {BD};"))
for s in ("crear-tablas-kudos.sql", "datos-iniciales-kudos.sql", "datos-demo-kudos.sql"):
    code, out = sqlcmd(script(s), BD)
    print(f"{s}: {'OK' if code == 0 else 'ERROR'}", "" if code == 0 else out[:1500])

code, out = sqlcmd("""set nocount on;
select 'tablas', count(*) from sys.tables;
select 'restricciones', count(*) from sys.objects where type in ('C','F','UQ','PK');
select 'indices_filtrados', count(*) from sys.indexes where has_filter = 1;
select 'usuarios', count(*) from usuario; select 'kudos', count(*) from kudo; select 'replicas', count(*) from replica;""", BD)
print(out)

# Pruebas negativas: cada una debe FALLAR
PRUEBAS = {
 "Segundo Rector vigente (INV-001)": "insert into usuario (nombres, apellidos, correo_institucional, numero_celular) values ('A','B','otro@uco.edu.co','3009998888'); insert into asignacion_rol (id_usuario, rol) select id_usuario, 'RECTOR' from usuario where correo_institucional='otro@uco.edu.co';",
 "Usuario con dos roles vigentes (RN-001)": "insert into asignacion_rol (id_usuario, rol) select id_usuario, 'ESTUDIANTE' from usuario where correo_institucional='shenao@uco.edu.co';",
 "Segundo coordinador vigente de una Facultad (INV-003)": "insert into usuario (nombres, apellidos, correo_institucional, numero_celular) values ('C','D','otro2@uco.edu.co','3009997777'); insert into asignacion_rol (id_usuario, rol, id_facultad) select (select id_usuario from usuario where correo_institucional='otro2@uco.edu.co'), 'COORDINADOR_FACULTAD', id_facultad from facultad where nombre=N'Ingeniería';",
 "Coordinador de Facultad sin Facultad (INV-010)": "insert into usuario (nombres, apellidos, correo_institucional, numero_celular) values ('E','F','otro3@uco.edu.co','3009996666'); insert into asignacion_rol (id_usuario, rol) select id_usuario, 'COORDINADOR_FACULTAD' from usuario where correo_institucional='otro3@uco.edu.co';",
 "Cuenta ACTIVA sin verificar (RN-088)": "update usuario set estado_cuenta='ACTIVA' where correo_institucional='marango@uco.edu.co';",
 "Correo duplicado (RN-073)": "insert into usuario (nombres, apellidos, correo_institucional, numero_celular) values ('G','H','shenao@uco.edu.co','3009995555');",
 "Celular con formato inválido (D-12)": "insert into usuario (nombres, apellidos, correo_institucional, numero_celular) values ('G','H','nuevo@uco.edu.co','2001234567');",
 "Kudo a sí mismo (D-19)": "insert into kudo (id_autor, id_destinatario, id_categoria, contenido) select id_usuario, id_usuario, (select top 1 id_categoria from categoria), N'Hola' from usuario where correo_institucional='shenao@uco.edu.co';",
 "Kudo con contenido vacío (INV-024)": "insert into kudo (id_autor, id_destinatario, id_categoria, contenido) select (select id_usuario from usuario where correo_institucional='shenao@uco.edu.co'), (select id_usuario from usuario where correo_institucional='agiraldo@uco.edu.co'), (select top 1 id_categoria from categoria), N'   ';",
 "Segunda calificación activa del mismo usuario (RN-180)": "insert into calificacion (id_autor, id_kudo, estrellas) select (select id_usuario from usuario where correo_institucional='lospina@uco.edu.co'), id_kudo, 3 from kudo where contenido like N'Santiago%';",
 "Calificación de 6 estrellas (RN-177)": "insert into calificacion (id_autor, id_kudo, estrellas) select (select id_usuario from usuario where correo_institucional='vcardona@uco.edu.co'), id_kudo, 6 from kudo where contenido like N'Santiago%';",
 "Calificación sin contenido (RN-184)": "insert into calificacion (id_autor, estrellas) select id_usuario, 4 from usuario where correo_institucional='vcardona@uco.edu.co';",
 "Reporte OTRO sin descripción (RN-190)": "insert into reporte (id_reportante, id_kudo, motivo) select (select id_usuario from usuario where correo_institucional='vcardona@uco.edu.co'), id_kudo, 'OTRO' from kudo where contenido like N'Santiago%';",
 "Moderación cerrada sin motivo (RN-250)": "insert into moderacion (id_kudo, id_moderador, estado, fecha_cierre, decision) select id_kudo, (select id_usuario from usuario where correo_institucional='mgomez@uco.edu.co'), 'CERRADA', sysdatetime(), 'VETAR' from kudo where contenido like N'Santiago%';",
 "Autoaprobación de solicitud (RN-108)": "update solicitud_registro set estado='APROBADA', id_decidida_por=id_solicitante, fecha_decision=sysdatetime();",
 "Segunda solicitud pendiente (UQ-SOL-1)": "insert into solicitud_registro (id_solicitante, rol_solicitado, id_programa) select id_solicitante, 'ESTUDIANTE', id_programa from solicitud_registro;",
}
fallos = 0
for nombre, q in PRUEBAS.items():
    code, out = sqlcmd("set nocount on; begin tran; " + q + " rollback;", BD)
    rechazada = code != 0
    fallos += 0 if rechazada else 1
    detalle = ""
    if rechazada:
        import re
        m = re.search(r'(constraint|index|índice|restricción)[^"]*"([^"]+)"', out, re.I) or re.search(r"'([a-z]{2}_[a-z_]+)'", out)
        detalle = m.group(m.lastindex) if m else out.splitlines()[-1][:120]
    print(f"{'RECHAZADA' if rechazada else 'ACEPTADA (ERROR)'} - {nombre} - {detalle}")
print("Pruebas que no se rechazaron:", fallos)
print(sqlcmd(f"alter database {BD} set single_user with rollback immediate; drop database {BD};")[0] == 0 and "Base temporal eliminada")
