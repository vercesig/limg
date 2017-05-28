# Card - JSON format
prendiamo, come primo esempio, la carta "Trading Town". Nella sua rappresentazione JSON essa sarà
cosi astratta:

```
{
"name" : "Trading Town",
"cost" : null,
"requirements":null
"instantEffect": "ADD",
"instantPayload": {
    "SERVANTS": 1,
    "COINS":1
},
"customInstantEffect" : null,
"permanentEffect" : "ADD",
"permanentPayload" : {
    "COINS":3
},
"minimumActionValue" : 1,
"period" : 3,
"cardType" : "TERRITORYCARD"
}
```

la semantica dei vari attribuit è la seguente:
* **name**: il nome della carta
* **cost**: il costo in risorse della carta (qui null perchè le carte territorio non hanno costo)
* **requirement**: il numero di risorse necessarie per poter giocare o acquistare la carta.
* **instantEffect**: la tipologia di effetto istantaneo che caratterizza la carta
* **instantPayload**: ciò che caratterizza lo specifico effetto della carta in questione, gli
argomenti qui presenti saranno impiegati dall'**EffectBuilder** per generare l'effetto corretto. Il
parser si occuperà di associare l'effetto alla carta
* **customIstantEffect**: nel caso in cui la carta abbia un ulteriore effetto istantaneo, questo
campo contiene l'OPCODE dell'effetto custom presente nel **EffectRegistry**, sarà compito del parser
associare l'effetto alla carta
* **permanentEffect**: la tipologia di effetto permanente che caratterizza la carta
* **permanentPayload**: ciò che caratterizza lo specifico effetto permanente della carta. gli
argomenti qui presenti saranno impiegati dall'**EffectBuilder** per generare l'effetto corretto. Il
parser si occuperà di associare l'effetto alla carta
* **minimumActionValue**: il valore minimo dell'azione che triggera l'attivazione degli effetti
permanenti della carta.
* **period**: il periodo della carta.
* **cardType**: la tipologia di carta

esistono 4 tipologie fondamentali di effetti che posso essere generati attraverso un payload:
* ADD : aggiunge le risorse elencate alle risorse del giocatore. Formato del payload:
```
{ "WOOD" : 2,
    "STONE": 4,
    "MILITARY" : 2,
  ...
}
```
* CHANGE: consente di cambiare RESOURCEOUT IN RESOURCEIN. Formato del payload:
```
{[
    {"RESOURCEOUT": {"COINS" : 3},
    "RESOURCEIN":{"WOOD" : 2,
                  "STONE" : 2}},
    {"RESOURCEOUT": {"COINS" : 5},
    "RESOURCEIN":{"SERVANTS" : 1,
                  "MILITARY" : 1}}
]}
```
ad esempio questo effetto consente di scambiare 3 monete per 2 legni e 2 pietre o 5 moente
per 1 servitore e 1 punto militare.
* ACTION: consente di eseguire un azione tipo TYPE con valore ACTIONVALUE applicando lo
"sconto" di BONUSRESOURCE risorse sull'acquisto di una carta. (il flag EXCLUSIVEBONUS è necessario per
indicare se lo sconto, qualora fosse costituito da più di una risorsa, è applicabile solo per
una delle risorse elencate). Formato del payload:
```
{ "TYPE" : HARVESTACTION ,
    "BONUSACTIONVALUE" : 3,
    "BONUSRESOURCE" : {"WOOD" : -1,
                       "COINS" -1},
    "EXCLUSIVEBONUS" : true}},
```

* BONUS: per ogni QUANTITY di CARTA o RISORSA posseduta consente di incrementare
INCREASE di INCREASINGQUANTITY unità. Formato del payload:
```
{ "FOREACH": "CHARACTERCARD",
    "QUANTITY" : 1,
    "INCREASE" : "MILITARY",
    "INCREASINGQUANTITY": 2}
```
ad esempio questo effetto consente di incrementare di 2 i punti militari del giocatore per ogni
carta personaggio posseduta.

Secondo questo formato carte più complesse sono cosi rappresentate:

```
{
"name" : "Marketplace",
"cost" : {
    "WOOD" : 2,
    "STONE" : 1
},
"requirements": null,
"instantEffect": "ADD",
"instantPayload": {
    "VP":3
},
"customInstantEffect" : null,
"permanentEffect" : "CHANGE",
"permanentPayload" : {
    [{"RESOURCEOUT": {"COINS" : 3},
      "RESOURCEIN":{"WOOD" : 2,
                    "STONE" : 2},
    }]
},
"minimumActionValue" : 3,
"period" : 2,
"cardType" : "BUILDINGCARD"
}
```

```
{
"name" : "Scholar",
"cost" : {
    "COINS" : 4,
},
"requirements": null,
"instantEffect": null,
"instantPayload": null,
"customInstantEffect" : null,
"permanentEffect" : "ACTION",
"permanentPayload" : {
    "TYPE" : "PRODUCTIONACTION",
    "BONUSACTIONVALUE" : 3,
    "BONUSRESOURCE" : null,
    "EXCLUSIVEBONUS" : false
},
"minimumActionValue" : null,
"period" : 2,
"cardType" : "CHARACTERCARD"
}
```

```
{
"name" : "Cardinal",
"cost" : {
    "COINS" : 4,
},
"instantEffect": ADD,
"instantPayload": {
  "FAITH" : 2
},
"customInstantEffect" : 4ActionValueProductionAction,
"permanentEffect" : null,
"permanentPayload" : null,
"minimumActionValue" : null,
"period" : 3,
"cardType" : "CHARACTERCARD"
}
```
