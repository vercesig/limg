# Effect

si evidenziano 4 tipologie di effetti:

* ADD: tutto ciò che riguarda l'aumento di una risorsa (per risorsa si intende una qualunque tra  legno, pietra, servitori, monete, punti militari, punti vittoria, punti fede).
* BONUS: tutto ciò che riguarda l'incremento di risorse, però proporzionato al numero di carte o alla quantità di risorse possedute dal giocatore.
  - Ad esempio la carta Noble ha come effetto immediato quello di incrementare, per ogni carta territorio posseduta, di 2 unità i punti vittoria del giocatore.
  - La carta Paramour ha lo stesso effetto però applicato a carte personaggio.
  - La carta General è un esempio di carta che applica la stessa logica delle precedenti, ma considerando i punti militari (dunque una risorsa) posseduti dal giocatori (nello specifico da 1 punto vittoria per ogni 2 punti militari posseduti).
* CHANGE: tutto ciò che riguarda il cambiamento di una risorsa in un'altra (ad esempio molte buildingCard hanno come effetto quello di cambiare X risorse in Y risorse. Alcuni esempi:
  - StoneMeasons's shop permette di cambiare 1 pietra in 3 monete O 2 pietre in 5 monete (l'effetto necessita quindi anche di una interazione con il giocatore).
  - Church permette di cambiare 1 legno O 1 pietra per 2 punti fede, la scelta se trasformare pietra o legno deve essere esclusiva ed è delegata al giocatore
* ACTION: tutto ciò che non riguarda risorse bensì effetti che consentono di eseguire un ulteriore azione senza però piazzare alcun familiare. molte delle carte che possiedono questo effetto vincolano il piazzamento del familiare in una specifica regione. Ad esempio la carta architetto consente di eseguire un azione a valore 6 senza piazzare alcun familiare però solo nella torre relativa alle carte edificio. In più il costo della carta che viene presa è ridotto di 1 pietra e 1 legno.
altri effetti permanenti invece incrementano il valore di un certo tipo di azione ogni qual volta che tale azione viene eseguita. ad esempio la carta Peasant incrementa di 3 il valore delle azioni raccolto ogni qual volta il giocatore effettua un'azione raccolto.

sulla base di questa suddivisione:
* rientrano nella categoria ADD 54 carte (di cui 9 richiedono la gestione di almeno un privilegio del consiglio)
* rientano nella categoria CHANGE 15 carte
* rientano nella categoria ACTION 21 carte
* rientrano nella categoria BONUS 9 carte

molte di queste carte hanno più di un effetto di tipologia diversa (quasi tutte hanno almeno un effetto immediato di tipo ADD). per questo era stato pensato di individuare gli effetti tramite 4 OPCODE: ADD, CHANGE, ACTION, BONUS.

Proposta di strutturazione di un possibile file JSON:

```
"istantEffectList" :{
  "ADD" : {"WOOD" : 2, "STONE": 4, "MILITARY" : 2, ...},
  "CHANGE" : [{"RISORSAPOSSEDUTA" : 2, "RISORSAOTTENUTA" : 2},{"RISORSAPOSSEDUTA" : 2, "RISORSAOTTENUTA" : 2}],
  "ACTION" : {"TYPE" : tower/produzione/raccolto/... , "BONUSACTIONVALUE" : 3, "BONUSRESOURCE" : {"WOOD" : -1, "COINS" -1}, "EXCLUSIVEBONUS" : true/false},
  "BONUS" {"CARTA/RISORSAPOSSEDUTA": "CHARACTERCARD", "RISORSADAINCREMENTARE" : 1}
]
```

```
"ADD" : {"WOOD" : 2, "STONE": 4, "MILITARY" : 2, ...},
```
aggiunge le risorse elencate alle risorse del giocatore.

```
"CHANGE" : [{"RISORSAPOSSEDUTA" : 2, "RISORSAOTTENUTA" : 2},{"RISORSAPOSSEDUTA" : 2, "RISORSAOTTENUTA" : 2}],
```
consente di cambiare X unità' (qui 2) di RISORSAPOSSEDUTA in Y unità (qui 2) di RISORSAOTTENUTA. L'array è necessario perchè sono possibili più scelte.
```
"ACTION" : {"TYPE" : tower/produzione/raccolto/... , "BONUSACTIONVALUE" : 3, "BONUSRESOURCE" : {"WOOD" : -1, "COINS" -1}, "EXCLUSIVEBONUS" : true/false},
```
consente di eseguire un azione di tipo TYPE con valore BONUSACTIONVALUE applicando lo "sconto" di BONUSRESOURCE sull'acquisto di una carta. (il flag EXCLUSIVEBONUS è necessario per indicare se lo sconto, qualora fosse costituito da più di una risorsa, è applicabile solo per una delle risorse elencate)

```
"BONUS" {"CARTA/RISORSAPOSSEDUTA": "CHARACTERCARD", "QUANTITÀ1" : 1, "RISORSADAINCREMENTARE" : risorsa, "QUANTITÀ2" : 2}
```
per ogni QUANTITÀ1 di CARTA o RISORSA posseduta consente di incrementare RISORSADAINCREMENTARE di QUANTITÀ2 unità-

Del tutto simile per gli effetti permanenti. sarà compito dell'effectBuilder costruire l'effetto da associare alla carta in base al contenuto del JSON. ciò che viene passato alla classe effectBuilder sarà il JsonObject recuperato dall'istantEffectList.
