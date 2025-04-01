{-# OPTIONS_GHC -Wno-unrecognised-pragmas #-}
{-# HLINT ignore "Use infix" #-}
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
        -- Primero marcamos las letras correctas (C)
        marked = zipWith markCorrect attempt secret
        -- Contamos las letras restantes en la palabra secreta (excluyendo las correctas)
        remaining = countRemaining marked secret
    in
        -- Luego marcamos las letras incorrectamente colocadas (I) o no presentes (N)
        markIncorrect marked remaining

-- Marca las letras correctas (misma posición)
markCorrect :: Char -> Char -> (Char, Clue)
markCorrect a s = if a == s then (a, C) else (a, N)

-- Cuenta las letras restantes en la palabra secreta (excluyendo las correctas)
countRemaining :: Try -> String -> [(Char, Int)]
countRemaining marked secret =
    let
        correctChars = [ c | (c, C) <- marked ]
        secret' = filter (`notElem` correctChars) secret
    in
        [ (c, length (filter (==c) secret')) | c <- letters ]

-- Marca las letras como incorrectamente colocadas (I) o no presentes (N)
markIncorrect :: Try -> [(Char, Int)] -> Try
markIncorrect marked remaining =
    [ if clue == C then pair
      else case lookup char remaining of
             Just n | n > 0 -> (char, I)
             _ -> (char, N)
    | pair@(char, clue) <- marked ]

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
        updateSingleLetter (char, oldClue) = 
            case lookup char attempt of
                Nothing -> (char, oldClue)
                Just newClue ->
                    case (oldClue, newClue) of
                        (C, _) -> (char, C)
                        (_, C) -> (char, C)
                        (I, _) -> (char, I)
                        (_, I) -> (char, I)
                        _      -> (char, N)
    in
        map updateSingleLetter letterStates

-- para probar el juego hay que ejecutar el main