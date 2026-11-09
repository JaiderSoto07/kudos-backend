# Estrellas

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Objetos de valor](../../01-modelo-dominio/README.md#objetos-de-valor)

| Campo | Valor |
|---|---|
| Clase | Estrellas |
| Estereotipo | value object (objeto de valor) |
| Descripción | Valida que sea un entero de 1 a 5. |
| Modificador acceso | public |
| Tipo clase | final (inmutable) |
| Usado en | Calificacion.estrellas |
| Políticas | P-CAL-005 |

Un objeto de valor no tiene identidad: dos instancias con el mismo valor son iguales. Es **inmutable** y se **valida al construirse**, por eso nunca existe uno inválido en el sistema.

## Atributos

| Modificador de acceso | Nombre | Tipo de dato | Tipo modificación atributo | Descripción |
|---|---|---|---|---|
| private | valor | int | final | Valor ya validado. |

## Constructores

| Modificador de acceso | Nombre | Descripción |
|---|---|---|
| private | Estrellas | Constructor privado: solo lo usa el método de fábrica `crear`. |

## Métodos

| Modificador de acceso | Nombre | Tipo acceso método | Descripción | Retorno | Excepciones |
|---|---|---|---|---|---|
| public | crear(int valor) | static | Método de fábrica: valida y crea el objeto de valor. | Estrellas | El valor no cumple la regla (P-CAL-005). |
| public | obtenerValor() | no static | Devuelve el valor validado. | int | |
| public | equals(Object otro) / hashCode() | no static | Igualdad por valor, no por identidad. | boolean / int | |
