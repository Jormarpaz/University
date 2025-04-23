module Wordle where


{-
Correctly placed: la letra está en la palabra secreta y en la misma posición que en la
 palabra intento.
 ◦ Incorrectly placed: la letra está en la palabra secreta, pero no en la misma posición que
 en la palabra intento.
 ◦ Not in secret word: la letra no está en la palabra secreta.
 ◦ Unused: esta letra no ha sido utilizada en ningún intento (esta pista solo se utiliza en la
 lista de letras utilizadas)
 Este tipo se instancia en las clases Eq, Ord, Read y Show, lo que permite compararlo,
 establecer un orden, leerlo de un string y mostrarlo.
-}

data Clue = C | I | N | U
  deriving (Eq, Ord, Read, Show)

type Try = [(Char, Clue)]

letters :: [Char]
letters = "abcdefghijklmnopqrstuvwxyz"

-- Que recibirá una palabra intento y comprobará si contiene únicamente letras validas.

validLetters :: String -> Bool
validLetters str = [ c | c <- str, c `elem` letters] == str


{-
que recibirá una palabra intento y una palabra secreta y devolverá la palabra intento etiquetando cada una de sus letras con la siguiente información:
◦ C → esta letra está en la palabra secreta y en esta misma posición.
◦ I → esta letra está en la palabra secreta, pero no en esta posición.
◦ N → esta letra no está en la palabra secreta.
-}
-- hay que fijarse en las letras, en comodo y comino hay varias o, pero solo 2 son correctas y una incorrecta
-- provocando que esta no tenga que ser evaluada mas

newTry :: String -> String -> Try
newTry attempt secret = 
    let
        -- Primero marcar correctas (C) o (N)
        prelim = zipWith (\a s -> if a == s then (a, C) else (a, N)) attempt secret
        -- Letras secretas no marcadas como correctas
        remaining = [s | (a, s) <- zip attempt secret, a /= s]

        -- Función para actualizar y eliminar de 'remaining' cuando sea necesario
        update _ (a, C) remList = ((a, C), remList)  -- Mantener los correctos
        update _ (a, N) remList
            | a `elem` remList = ((a, I), removeFirst a remList)  -- Si está en la palabra, marcar I y eliminar una instancia
            | otherwise = ((a, N), remList)  -- Sino, sigue siendo incorrecta

        -- Procesamos la lista acumulando el estado de 'remaining'
        process [] remList = ([], remList)
        process (x:xs) remList = 
            let (newX, newRem) = update x x remList
                (rest, finalRem) = process xs newRem
            in (newX : rest, finalRem)

        (finalList, _) = process prelim remaining
    in
        finalList



-- Función para eliminar la primera aparición de un elemento en una lista

removeFirst :: Eq a => a -> [a] -> [a]
removeFirst _ [] = []  -- Si la lista está vacía, no hay nada que eliminar
removeFirst x (y:ys)
    | x == y    = ys  -- Si encontramos el elemento, lo eliminamos y devolvemos el resto
    | otherwise = y : removeFirst x ys  -- Si no es el elemento, seguimos buscando


{-
que devolverá la lista de letras admitidas etiquetando todas ellas como no
utilizadas. Para ello se utilizará el identificador U, de manera similar a los identificadores C, I y N utilizados por newTry.
-}

initialLS :: Try
initialLS = [(c, U) | c <- letters]


{-
que recibirá la lista de letras admitidas etiquetadas según estén o no presentes en
 la palabra secreta y una palabra intento etiquetada por newTry. Esta función devolverá la
 lista de letras admitidas etiquetadas, actualizando la información según la etiquetación de la
 palabra intento.
-}

updateLS :: Try -> Try -> Try -- >updateLS initialLS (newTry "comino" "camion") 8 lineas
updateLS letterStates attempt = 
    let
        bestClue char = case [cl | (c, cl) <- attempt, c == char] of
            [] -> Nothing
            clues
                | C `elem` clues -> Just C
                | I `elem` clues -> Just I
                | otherwise      -> Just N

        updateSingleLetter (char, oldClue) = case bestClue char of
            Nothing -> (char, oldClue)
            Just newClue -> case (oldClue, newClue) of
                (C, _) -> (char, C)
                (_, C) -> (char, C)
                (I, I) -> (char, I)
                (I, _) -> (char, I)
                (_, I) -> (char, I)
                _      -> (char, N)
    in
        map updateSingleLetter letterStates

-- para probar el juego hay que ejecutar el main