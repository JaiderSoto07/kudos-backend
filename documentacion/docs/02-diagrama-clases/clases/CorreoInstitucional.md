# CorreoInstitucional

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Objetos de valor](../../01-modelo-dominio/README.md#objetos-de-valor)

| Campo | Valor |
|---|---|
| Clase | CorreoInstitucional |
| Estereotipo | value object (objeto de valor) |
| Descripción | Normaliza a minúsculas y sin espacios; valida el formato de correo y que termine en @ + dominio institucional. |
| Modificador acceso | public |
| Tipo clase | final (inmutable) |
| Usado en | Usuario.correoInstitucional |
| Políticas | P-USU-002 |

Un objeto de valor no tiene identidad: dos instancias con el mismo valor son iguales. Es **inmutable** y se **valida al construirse**, por eso nunca existe uno inválido en el sistema.

## Atributos

| Modificador de acceso | Nombre | Tipo de dato | Tipo modificación atributo | Descripción |
|---|---|---|---|---|
| private | valor | String | final | Valor ya validado. |

## Constructores

| Modificador de acceso | Nombre | Descripción |
|---|---|---|
| private | CorreoInstitucional | Constructor privado: solo lo usa el método de fábrica `crear`. |

## Métodos

| Modificador de acceso | Nombre | Tipo acceso método | Descripción | Retorno | Excepciones |
|---|---|---|---|---|---|
| public | crear(String valor, String dominio) | static | Método de fábrica: valida y crea el objeto de valor. | CorreoInstitucional | El valor no cumple la regla (P-USU-002). |
| public | obtenerValor() | no static | Devuelve el valor validado. | String | |
| public | equals(Object otro) / hashCode() | no static | Igualdad por valor, no por identidad. | boolean / int | |
