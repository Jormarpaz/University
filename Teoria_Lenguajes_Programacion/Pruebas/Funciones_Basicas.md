# Funciones Básicas de Haskell

## Listas

```
- `head`: toma una lista y devuelve su cabeza. La cabeza de una lista es básicamente el primer elemento.
```

```
- `tail`: toma una lista y devuelve su cola. En otros palabras, corta la cabeza de la lista.
```

```
- `last`: toma una lista y devuelve su último elemento.
```

```
- `init`: toma una lista y devuelve toda la lista excepto su último elemento.
```

```
- `length`: toma una lista y obviamente devuelve su tamaño.
```

```
- `null`: comprueba si una lista está vacía. Si lo está, devuelve True, en caso contrario devuelve False. Usa esta función en lugar de xs == [] (si tienes una lista que se llame xs).
```

```
- `reverse`: pone del revés una lista.
```

```
- `take`: toma un número y una lista y extrae dicho número de elementos de una lista. Observa.
```

```
- `drop`: funciona de forma similar, solo que quita un número de elementos del comienzo de la lista.
```

```
- `maximum`: toma una lista de cosas que se pueden poner en algún tipo de orden y devuelve el elemento más grande.
```

```
- `minimum`: devuelve el más pequeño.
```

```
- `sum`: toma una lista de números y devuelve su suma.
```

```
- `product`: toma una lista de números y devuelve su producto.
```

```
- `elem`: toma una cosa y una lista de cosas y nos dice si dicha cosa es un elemento de la lista. Normalmente, esta función es llamada de forma infija porque resulta más fácil de leer.
```

``` bash
ghci> [1..20] -> [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20]
```

``` bash
ghci> [2,4..20] -> [2,4,6,8,10,12,14,16,18,20]
```

``` bash
ghci> [0.1, 0.3 .. 1] -> [0.1,0.3,0.5,0.7,0.8999999999999999,1.0999999999999999]
```

## Rangos

```
- `cycle`: toma una lista y crea un ciclo de listas iguales infinito. Si intentáramos mostrar el resultado nunca terminaría así que hay que cortarlo en alguna parte.
```

```
- `repeat`: toma un elemento y produce una lista infinita que contiene ese único elemento repetido. Es como hacer un ciclo de una lista con un solo elemento.
```
## Lista Intensional

``` bash
ghci> [x*2 | x <- [1..10]] -> [2,4,6,8,10,12,14,16,18,20]
```

``` bash
ghci> [x*2 | x <- [1..10], x*2 >= 12] -> [12,14,16,18,20]
```

``` bash
ghci> [ x | x <- [50..100], x `mod` 7 == 3] -> [52,59,66,73,80,87,94]
```

Al hecho de eliminar elementos de la lista utilizando predicados también se conoce como filtrado

``` bash
boomBangs xs = [ if x < 10 then "BOOM!" else "BANG!" | x <- xs, odd x]
```

``` bash
ghci> let noums = ["rana","zebra","cabra"]
ghci> let adjetives = ["perezosa","enfadada","intrigante"]
ghci> [noum ++ " " ++ adjetive | noum <- noums, adjetive <- adjetives]
["rana perezosa","rana enfadada","rana intrigante","zebra perezosa",
"zebra enfadada","zebra intrigante","cabra perezosa","cabra enfadada",
"cabra intrigante"]
```

``` bash
length' xs = sum [1 | _ <- xs]
```

``` bash
removeNonUppercase st = [ c | c <- st, c `elem` ['A'..'Z']]
```

``` bash
ghci> let xxs = [[1,3,5,2,3,1,2,4,5],[1,2,3,4,5,6,7,8,9],[1,2,4,2,1,6,3,1,3,2,3,6]]
ghci> [ [ x | x <- xs, even x ] | xs <- xxs]
[[2,2,4],[2,4,6,8],[2,4,2,6,2,6]]
```

## Tuplas

Las tuplas se definen con corchetes, (), dentro de una lista, es decir; [(),(),()], por ejemplo. Y, como las listas son homogéneas y las tuplas están contenidas en listas, estas deben tener el mismo formato, una dupla (una tupla de 2 elementos) no tiene el mismo formato que una tripla ( una tupla de 3 elementos), de modo que algo como: [(1, 2), (8, 11, 5), (4, 5)] no estaría permitido.

``` bash
-'fst': toma una dupla y devuelve su primer componente.

ghci> fst (8,11)
8
ghci> fst ("Wow", False)
"Wow"
```

``` bash
-'snd': toma una dupla y devuelve su segundo componente. ¡Sorpresa!

ghci> snd (8,11)
11
ghci> snd ("Wow", False)
False
```

Estas funciones solo operan sobre duplas. No funcionaran sobre triplas, cuádruplas, quíntuplas, etc.

```
-'zip': esta función toma dos listas y las une en un lista uniendo sus elementos en una dupla.
```
