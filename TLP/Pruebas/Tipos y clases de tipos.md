# Tipos y clases de tipos

## Tipos

```
- `Int`: representa enteros. Se utiliza para representar número enteros, por lo que 7 puede ser un Int pero 7.2 no puede. Int está acotado, lo que significa que tiene un valor máximo y un valor mínimo. Normalmente en máquinas de 32bits el valor máximo de Int es 2147483647 y el mínimo -2147483648.
```

```
- `Integer`: representa... esto... enteros también. La diferencia es que no están acotados así que pueden representar números muy grandes. Sin embargo, Int es más eficiente.
```

```
- `Float`: es un número real en coma flotante de simple precisión.
```

```
- `Bool`: es el tipo booleano. Solo puede tener dos valores: True o False.
```

```
- `Char`: representa un carácter. Se define rodeado por comillas simples. Una lista de caracteres es una cadena.
```

## Variables de tipo

``` bash
ghci> :t head
head :: [a] -> a
```

```
-'Eq': es utilizada por los tipos que soportan comparaciones por igualdad. Los miembros de esta clase implementan las funciones == o /= en algún lugar de su definición. Todos los tipos que mencionamos anteriormente forman parte de la clase Eq exceptuando las funciones, así que podemos realizar comparaciones de igualdad sobre ellos.
```

```
-'Ord': es para tipos que poseen algún orden.
```

``` bash
ghci> :t (>)
(>) :: (Ord a) => a -> a -> Bool
```

```
-'Show': Toma un valor de un tipo que pertenezca a la clase Show y lo representa como una cadena de texto.
```

```
-'Read': La función read toma una cadena y devuelve un valor del tipo que es miembro de Read.
```

``` bash
-'Num': Sus miembros tienen la propiedad de poder comportarse como números. Vamos a examinar el tipo de un número.

ghci> :t 20
20 :: (Num t) => t
```