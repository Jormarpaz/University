%Ready Player One%
%Distopia Tecnologica%

%Constantes

distopia.
concurso.
control_corporativo.

parzival.
ache.
artemisa.
daito.
shoto.
nolan_sorrento.
baile.
contrato.
carrera.
invasion.
enfrentamiento_ioi.
discurso.

%Hechos

gunter(parzival).
gunter(ache).
gunter(artemisa).
gunter(daito).
gunter(shoto).

ioi(nolan_sorrento).

tiene(parzival,5).
tiene(artemisa,3).
tiene(ache,2).
tiene(daito,2).
tiene(shoto,1).

desafios(baile).
desafios(contrato).
desafios(carrera).
desafios(invasion).
desafios(enfrentamiento_ioi).
desafio_final(discurso).

vida_boss(desafio,100).
no_cae_en_trampa(parzival).
escucha_discurso(gunter).

%Predicados

es_gunter(X):- gunter(X).

tiene_todas_las_llaves(X):- tiene(X,llaves), llaves =:= 5.
tiene_muchas_llaves(X):- tiene(X,llaves), llaves > 3,llaves < 5.
tiene_pocas_llaves(X):- tiene(X,llaves), llaves < 3 .

interaccion(X,desafio):- gunter(X),random(0,2,opcion), reducir_vida(desafio,opcion).
reducir_vida(desafio,1):-vida_boss(desafio,vida),vida is max(vida-50,0),retractall(vida_boss(desafio,_)),asserta(vida_boss(desafio,vida)).
reducir_vida(desafio,0).
vencio_al_boss(X,desafio):-gunter(X),vida_boss(desafio,0).

se_enfrenta_a_desafio(X):-gunter(X), desafios(desafio), not(resuelto(X,desafio)).
resuelto(X,desafio):-gunter(X),desafios(desafio),vencio_al_boss(X,desafio).

puede_acceder_a_zona_final(X):-gunter(X),tiene_todas_las_llaves(X).
vencer_desafio_final(X):-puede_acceder_a_zona_final(X), no_cae_en_trampa(X),escucha_discurso(X), tiene_todas_las_llaves(X).
domina_oasis(X):-gunter(X),vencer_desafio_final(X), not(ioi(nolan_sorrent)).

agregar_nuevo_desafio:-retractall(desafios(desafio_final)),asserta(desafios(desafio_final_nuevo)).
