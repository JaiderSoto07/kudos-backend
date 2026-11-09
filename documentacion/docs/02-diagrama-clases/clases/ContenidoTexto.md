# ContenidoTexto

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Objetos de valor](../../01-modelo-dominio/README.md#objetos-de-valor)

| Campo | Valor |
|---|---|
| Clase | ContenidoTexto |
| Estereotipo | value object (objeto de valor) |
| Descripción | Valida que no sea nulo ni esté vacío o solo con espacios. No limita la longitud ni trunca el texto. |
| Modificador acceso | public |
| Tipo clase | final (inmutable) |
| Usado en | Kudo.contenido, Replica.contenido |
| Políticas | P-KUD-004, P-RPL-003 |

Un objeto de valor no tiene identidad: dos instancias con el mismo valor son iguales. Es **inmutable** y se **valida al construirse**, por eso nunca existe uno inválido en el sistema.

## Atributos

| Modificador de acceso | Nombre | Tipo de dato | Tipo modificación atributo | Descripción |
|---|---|---|---|---|
| private | valor | String | final | Valor ya validado. |

## Constructores

| Modificador de acceso | Nombre | Descripción |
|---|---|---|
| private | ContenidoTexto | Constructor privado: solo lo usa el método de fábrica `crear`. |

## Métodos

| Modificador de acceso | Nombre | Tipo acceso método | Descripción | Retorno | Excepciones |
|---|---|---|---|---|---|
| public | crear(String valor) | static | Método de fábrica: valida y crea el objeto de valor. | ContenidoTexto | El valor no cumple la regla (P-KUD-004, P-RPL-003). |
| public | obtenerValor() | no static | Devuelve el valor validado. | String | |
| public | equals(Object otro) / hashCode() | no static | Igualdad por valor, no por identidad. | boolean / int | |
