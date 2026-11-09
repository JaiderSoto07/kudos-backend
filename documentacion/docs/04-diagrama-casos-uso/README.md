# Diagrama de casos de uso

[Inicio](../../README.md) · [Especificación de casos de uso](../00-analisis/casos-de-uso.md) · [Modelo de dominio](../01-modelo-dominio/README.md)

Diagrama UML de casos de uso de UCO Kudos (UML-001). Para que sea legible se divide en una página de actores y una página por módulo. La especificación de cada caso (precondiciones, flujos, excepciones y eventos) está en [casos-de-uso.md](../00-analisis/casos-de-uso.md).

> Fuente editable en draw.io: [`fuentes/casos-uso.drawio`](../../fuentes/casos-uso.drawio), con una página por cada sección de este documento.

## 1. Actores

| Actor | Tipo | Descripción |
|---|---|---|
| Persona no registrada | Concreto | Integrante de la comunidad que aún no tiene cuenta |
| Usuario registrado | Abstracto | Tiene cuenta, aunque todavía no esté activa (verifica datos, inicia sesión) |
| Usuario activo | Abstracto | Cuenta aprobada y verificada; lo especializan los 7 roles |
| Administrador, Rector, Coordinador Académico, Coordinador de Facultad, Coordinador de Programa, Docente, Estudiante | Concretos | Roles institucionales (doc. 01 §7) |
| Moderador | Abstracto | Rector o Coordinador Académico (D-02) |
| Gestor de categorías | Abstracto | Administrador, Rector o Coordinador Académico (D-01) |
| Autoridad | Abstracto | Quien aprueba, rechaza y gestiona cuentas dentro de su ámbito según la Matriz de aprobaciones y de gestión (doc. 10 §5-6) |

La flecha de generalización indica que el actor especializado hereda todos los casos de uso del actor general. Por ejemplo, el Rector puede hacer todo lo que hace un Usuario activo, un Moderador, un Gestor de categorías y una Autoridad.

[![Actores](../../assets/diagramas/casos-uso-1.svg)](../../assets/diagramas/casos-uso-1.svg)

> Haz clic en la imagen para verla en tamaño completo. También disponible en [PNG](../../assets/diagramas/casos-uso-1.png). Fuente editable en draw.io: [`fuentes/casos-uso.drawio`](../../fuentes/casos-uso.drawio).

<details>
<summary>Versión en texto del diagrama (Mermaid)</summary>

```mermaid
flowchart BT
  N0(("Usuario activo"))
  N1(("Usuario registrado"))
  N2(("Administrador"))
  N3(("Rector"))
  N4(("Coordinador Académico"))
  N5(("Coordinador de Facultad"))
  N6(("Coordinador de Programa"))
  N7(("Docente"))
  N8(("Estudiante"))
  N9(("Moderador"))
  N10(("Gestor de categorías"))
  N11(("Autoridad"))
  N12(("Persona no registrada"))
  N0 -.->|hereda de| N1
  N2 -.->|hereda de| N0
  N3 -.->|hereda de| N0
  N4 -.->|hereda de| N0
  N5 -.->|hereda de| N0
  N6 -.->|hereda de| N0
  N7 -.->|hereda de| N0
  N8 -.->|hereda de| N0
  N3 -.->|hereda de| N9
  N4 -.->|hereda de| N9
  N2 -.->|hereda de| N10
  N3 -.->|hereda de| N10
  N4 -.->|hereda de| N10
  N2 -.->|hereda de| N11
  N3 -.->|hereda de| N11
  N4 -.->|hereda de| N11
  N5 -.->|hereda de| N11
  N6 -.->|hereda de| N11
```

</details>


## 2. Identidad y acceso

[![Identidad y acceso](../../assets/diagramas/casos-uso-2.svg)](../../assets/diagramas/casos-uso-2.svg)

> Haz clic en la imagen para verla en tamaño completo. También disponible en [PNG](../../assets/diagramas/casos-uso-2.png). Fuente editable en draw.io: [`fuentes/casos-uso.drawio`](../../fuentes/casos-uso.drawio).

<details>
<summary>Versión en texto del diagrama (Mermaid)</summary>

```mermaid
flowchart LR
  A0(("Persona no registrada"))
  A1(("Usuario registrado"))
  A2(("Usuario activo"))
  subgraph S["UCO Kudos — Identidad y acceso"]
    CU016(["CU-016 Registrarse (solicitar cuenta)"])
    CU017(["CU-017 Verificar correo o celular"])
    CU018(["CU-018 Iniciar sesión"])
    CU019(["CU-019 Cerrar sesión"])
    CU020(["CU-020 Consultar y actualizar información personal"])
    CU021(["CU-021 Consultar historial de actividad propia"])
    CU039(["CU-039 Consultar notificaciones"])
  end
  A0 --- CU016
  A1 --- CU017
  A1 --- CU018
  A2 --- CU019
  A2 --- CU020
  A2 --- CU021
  A2 --- CU039
```

</details>


| Caso de uso | Actor |
|---|---|
| [CU-016 Registrarse (solicitar cuenta)](../00-analisis/casos-de-uso.md) | Persona no registrada |
| [CU-017 Verificar correo o celular](../00-analisis/casos-de-uso.md) | Usuario registrado |
| [CU-018 Iniciar sesión](../00-analisis/casos-de-uso.md) | Usuario registrado |
| [CU-019 Cerrar sesión](../00-analisis/casos-de-uso.md) | Usuario activo |
| [CU-020 Consultar y actualizar información personal](../00-analisis/casos-de-uso.md) | Usuario activo |
| [CU-021 Consultar historial de actividad propia](../00-analisis/casos-de-uso.md) | Usuario activo |
| [CU-039 Consultar notificaciones](../00-analisis/casos-de-uso.md) | Usuario activo |

## 3. Organización y aprobaciones

[![Organización y aprobaciones](../../assets/diagramas/casos-uso-3.svg)](../../assets/diagramas/casos-uso-3.svg)

> Haz clic en la imagen para verla en tamaño completo. También disponible en [PNG](../../assets/diagramas/casos-uso-3.png). Fuente editable en draw.io: [`fuentes/casos-uso.drawio`](../../fuentes/casos-uso.drawio).

<details>
<summary>Versión en texto del diagrama (Mermaid)</summary>

```mermaid
flowchart LR
  A0(("Autoridad"))
  A1(("Administrador"))
  A2(("Coordinador Académico"))
  A3(("Coordinador de Programa"))
  A4(("Usuario activo"))
  subgraph S["UCO Kudos — Organización y aprobaciones"]
    CU022(["CU-022 Aprobar solicitud de registro"])
    CU023(["CU-023 Rechazar solicitud de registro"])
    CU026(["CU-026 Suspender o reactivar cuenta"])
    CU027(["CU-027 Desactivar cuenta"])
    CU024(["CU-024 Registrar Rector"])
    CU025(["CU-025 Registrar Administrador"])
    CU028(["CU-028 Crear Facultad"])
    CU029(["CU-029 Modificar o inactivar Facultad"])
    CU030(["CU-030 Crear Programa Académico"])
    CU031(["CU-031 Modificar o inactivar Programa"])
    CU032(["CU-032 Vincular docente o estudiante a Programa"])
    CU033(["CU-033 Desvincular docente o estudiante de Programa"])
    CU034(["CU-034 Consultar estructura organizacional"])
  end
  A0 --- CU022
  A0 --- CU023
  A0 --- CU026
  A0 --- CU027
  A1 --- CU024
  A1 --- CU025
  A2 --- CU028
  A2 --- CU029
  A2 --- CU030
  A2 --- CU031
  A3 --- CU032
  A3 --- CU033
  A4 --- CU034
```

</details>


| Caso de uso | Actor |
|---|---|
| [CU-022 Aprobar solicitud de registro](../00-analisis/casos-de-uso.md) | Autoridad |
| [CU-023 Rechazar solicitud de registro](../00-analisis/casos-de-uso.md) | Autoridad |
| [CU-026 Suspender o reactivar cuenta](../00-analisis/casos-de-uso.md) | Autoridad |
| [CU-027 Desactivar cuenta](../00-analisis/casos-de-uso.md) | Autoridad |
| [CU-024 Registrar Rector](../00-analisis/casos-de-uso.md) | Administrador |
| [CU-025 Registrar Administrador](../00-analisis/casos-de-uso.md) | Administrador |
| [CU-028 Crear Facultad](../00-analisis/casos-de-uso.md) | Coordinador Académico |
| [CU-029 Modificar o inactivar Facultad](../00-analisis/casos-de-uso.md) | Coordinador Académico |
| [CU-030 Crear Programa Académico](../00-analisis/casos-de-uso.md) | Coordinador Académico |
| [CU-031 Modificar o inactivar Programa](../00-analisis/casos-de-uso.md) | Coordinador Académico |
| [CU-032 Vincular docente o estudiante a Programa](../00-analisis/casos-de-uso.md) | Coordinador de Programa |
| [CU-033 Desvincular docente o estudiante de Programa](../00-analisis/casos-de-uso.md) | Coordinador de Programa |
| [CU-034 Consultar estructura organizacional](../00-analisis/casos-de-uso.md) | Usuario activo |

## 4. Kudos, conversación y calificaciones

[![Kudos, conversación y calificaciones](../../assets/diagramas/casos-uso-4.svg)](../../assets/diagramas/casos-uso-4.svg)

> Haz clic en la imagen para verla en tamaño completo. También disponible en [PNG](../../assets/diagramas/casos-uso-4.png). Fuente editable en draw.io: [`fuentes/casos-uso.drawio`](../../fuentes/casos-uso.drawio).

<details>
<summary>Versión en texto del diagrama (Mermaid)</summary>

```mermaid
flowchart LR
  A0(("Usuario activo"))
  subgraph S["UCO Kudos — Kudos, conversación y calificaciones"]
    CU042(["CU-042 Consultar muro de Kudos"])
    CU001(["CU-001 Crear Kudo"])
    CU002(["CU-002 Consultar Kudo"])
    CU003(["CU-003 Consultar historial de Kudos"])
    CU004(["CU-004 Crear réplica sobre Kudo"])
    CU005(["CU-005 Responder réplica"])
    CU006(["CU-006 Consultar conversación"])
    CU007(["CU-007 Calificar Kudo"])
    CU008(["CU-008 Calificar réplica"])
    CU009(["CU-009 Consultar valoraciones"])
    CU010(["CU-010 Reportar Kudo"])
    CU011(["CU-011 Reportar réplica"])
    CU040(["CU-040 Consultar indicadores"])
  end
  A0 --- CU042
  A0 --- CU001
  A0 --- CU002
  A0 --- CU003
  A0 --- CU004
  A0 --- CU005
  A0 --- CU006
  A0 --- CU007
  A0 --- CU008
  A0 --- CU009
  A0 --- CU010
  A0 --- CU011
  A0 --- CU040
```

</details>


| Caso de uso | Actor |
|---|---|
| [CU-042 Consultar muro de Kudos](../00-analisis/casos-de-uso.md) | Usuario activo |
| [CU-001 Crear Kudo](../00-analisis/casos-de-uso.md) | Usuario activo |
| [CU-002 Consultar Kudo](../00-analisis/casos-de-uso.md) | Usuario activo |
| [CU-003 Consultar historial de Kudos](../00-analisis/casos-de-uso.md) | Usuario activo |
| [CU-004 Crear réplica sobre Kudo](../00-analisis/casos-de-uso.md) | Usuario activo |
| [CU-005 Responder réplica](../00-analisis/casos-de-uso.md) | Usuario activo |
| [CU-006 Consultar conversación](../00-analisis/casos-de-uso.md) | Usuario activo |
| [CU-007 Calificar Kudo](../00-analisis/casos-de-uso.md) | Usuario activo |
| [CU-008 Calificar réplica](../00-analisis/casos-de-uso.md) | Usuario activo |
| [CU-009 Consultar valoraciones](../00-analisis/casos-de-uso.md) | Usuario activo |
| [CU-010 Reportar Kudo](../00-analisis/casos-de-uso.md) | Usuario activo |
| [CU-011 Reportar réplica](../00-analisis/casos-de-uso.md) | Usuario activo |
| [CU-040 Consultar indicadores](../00-analisis/casos-de-uso.md) | Usuario activo |

## 5. Moderación y administración

[![Moderación y administración](../../assets/diagramas/casos-uso-5.svg)](../../assets/diagramas/casos-uso-5.svg)

> Haz clic en la imagen para verla en tamaño completo. También disponible en [PNG](../../assets/diagramas/casos-uso-5.png). Fuente editable en draw.io: [`fuentes/casos-uso.drawio`](../../fuentes/casos-uso.drawio).

<details>
<summary>Versión en texto del diagrama (Mermaid)</summary>

```mermaid
flowchart LR
  A0(("Moderador"))
  A1(("Gestor de categorías"))
  A2(("Autoridad"))
  A3(("Administrador"))
  subgraph S["UCO Kudos — Moderación y administración"]
    CU012(["CU-012 Consultar reportes"])
    CU036(["CU-036 Iniciar revisión de contenido"])
    CU013(["CU-013 Vetar Kudo"])
    CU014(["CU-014 Vetar réplica"])
    CU037(["CU-037 Liberar contenido revisado"])
    CU015(["CU-015 Consultar histórico de vetos"])
    CU035(["CU-035 Gestionar categorías"])
    CU038(["CU-038 Consultar auditoría"])
    CU041(["CU-041 Gestionar parámetros globales"])
  end
  A0 --- CU012
  A0 --- CU036
  A0 --- CU013
  A0 --- CU014
  A0 --- CU037
  A0 --- CU015
  A1 --- CU035
  A2 --- CU038
  A3 --- CU041
```

</details>


| Caso de uso | Actor |
|---|---|
| [CU-012 Consultar reportes](../00-analisis/casos-de-uso.md) | Moderador |
| [CU-036 Iniciar revisión de contenido](../00-analisis/casos-de-uso.md) | Moderador |
| [CU-013 Vetar Kudo](../00-analisis/casos-de-uso.md) | Moderador |
| [CU-014 Vetar réplica](../00-analisis/casos-de-uso.md) | Moderador |
| [CU-037 Liberar contenido revisado](../00-analisis/casos-de-uso.md) | Moderador |
| [CU-015 Consultar histórico de vetos](../00-analisis/casos-de-uso.md) | Moderador |
| [CU-035 Gestionar categorías](../00-analisis/casos-de-uso.md) | Gestor de categorías |
| [CU-038 Consultar auditoría](../00-analisis/casos-de-uso.md) | Autoridad |
| [CU-041 Gestionar parámetros globales](../00-analisis/casos-de-uso.md) | Administrador |
