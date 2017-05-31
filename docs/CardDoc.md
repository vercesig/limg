# DevelopmentCard - JSON format
questo documento presenta la versione definitiva relativa al formato JSON delle DevelopmentCard.


prendiamo, come primo esempio, la carta "Trading Town". Nella sua rappresentazione JSON essa sarà
cosi astratta:

```
{
"name" : "Trading Town",
"cost" : null,
"requirments" : null,
"instantEffect": "ADD",
"instantPayload": {
    "SERVANTS": 1,
    "COINS":1
},
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
* **requirments**: i requisiti necessari, in termini di risorse, che il giocatore deve possedere
per poter pagare il costo della carta (e quindi acquisirla).
* **instantEffect**: la tipologia di effetto istantaneo che caratterizza la carta
* **instantPayload**: ciò che caratterizza lo specifico effetto della carta in questione, gli
argomenti qui presenti saranno impiegati dall'**EffectBuilder** per generare l'effetto corretto. Il
parser si occuperà di associare l'effetto alla carta
* **permanentEffect**: la tipologia di effetto permanente che caratterizza la carta
* **permanentPayload**: ciò che caratterizza lo specifico effetto permanente della carta. gli
argomenti qui presenti saranno impiegati dall'**EffectBuilder** per generare l'effetto corretto. Il
parser si occuperà di associare l'effetto alla carta
* **minimumActionValue**: il valore minimo dell'azione che triggera l'attivazione degli effetti
permanenti della carta.
* **period**: il periodo della carta.
* **cardType**: la tipologia di carta

tale formato può essere esteso al caso di carte con più effetti istantanei, ecco un esempio:

```
{
"name" : "Cardinal",
"cost" : {
    "COINS" : 4,
},
"instantEffect": ["ADD", "ACTION"],
"instantPayload": [
  {
    "FAITH" : 2
  },
  {
    "TYPE" : "HARVESTACTION" ,
    "BONUSACTIONVALUE" : 4,
    "BONUSRESOURCE" : null,
    "EXCLUSIVEBONUS" : false
  }]
"permanentEffect" : null,
"permanentPayload" : null,
"minimumActionValue" : null,
"period" : 3,
"cardType" : "CHARACTERCARD"
}
```
la carta Cardinal possiede due effetti immediati, uno di tipo ADD e l'altro di tipo ACTION.
I payload sono riportati sotto la voce "instantPayload" come un array di oggetti.

**ATTENZIONE:
l'ordine con cui devono essere riportati i payload deve essere uguale all'ordine con cui si
presentano i rispettivi riferimenti relativi alla tipologia di effetto nel campo "istantEffect".
Nell'esempio in questione non è ammesso che il payload ACTION si trovi come primo elemento dell'array poichè in tal caso sarebbe erroneamente riferito ad un effetto tipo ADD**

esistono 5 tipologie fondamentali di effetti che posso essere generati attraverso un payload:
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
[
    {"RESOURCEOUT": {"COINS" : 3},
     "RESOURCEIN":  {"WOOD" : 2,
                    "STONE" : 2}},

    {"RESOURCEOUT": {"COINS" : 5},
     "RESOURCEIN":  {"SERVANTS" : 1,
                    "MILITARY" : 1}}
]
```
ad esempio questo effetto consente di scambiare 3 monete per 2 legni e 2 pietre o 5 moente
per 1 servitore e 1 punto militare. Se lo scambio non offre possibilità di scelta l'array può essere omesso (sarà il codice a gestire la faccenda).
* ACTION: consente di eseguire un azione tipo TYPE con valore ACTIONVALUE applicando lo
"sconto" di BONUSRESOURCE risorse sull'acquisto di una carta. (il flag EXCLUSIVEBONUS è necessario per indicare se lo sconto, qualora fosse costituito da più di una risorsa, è applicabile solo per una delle risorse elencate). Un campo REGIONID è necessario per poter individuare una particolare regione all'interno della Board (ad esempio l'azione può essere eseguita solo su una torre GIALLA). Il campo REGIONID posto a null indica che non vi è alcuna limitazione sulla regione in cui l'azione può essere effettuata (ad esempio l'azione può essere effettuata su TUTTE le torri della board). Formato del payload:
```
{ "TYPE" : HARVESTACTION ,
    "REGIONID" : 5,
    "BONUSACTIONVALUE" : 3,
    "BONUSRESOURCE" : {"WOOD" : -1,
                       "COINS" -1},
    "EXCLUSIVEBONUS" : true}},
```

* BONUS: per ogni QUANTITY di CARTA o RISORSA posseduta consente di incrementare
INCREASE di INCREASINGQUANTITY unità. Un campo TYPE viene impiegato per informare l'effectBuilder se il conteggio su ciò che si possiede deve essere fatto su CARD o RESOURCE. Formato del payload:
```
{ "TYPE" : "CARD",
    "FOREACH": "CHARACTERCARD",
    "QUANTITY" : 1,
    "INCREASE" : "MILITARY",
    "INCREASINGQUANTITY": 2}
```
ad esempio questo effetto consente di incrementare di 2 i punti militari del giocatore per ogni
carta personaggio posseduta.

* PRIVILEGE: consente di generare un privilegio del consiglio. Un campo NUMBER individua il numero di privilegi che possono essere spesi. Formato del payload:
```
{ "NUMBER" : 1}
```

Secondo questo formato carte più complesse sono cosi rappresentate:

```
{
"name" : "Marketplace",
"cost" : {
    "WOOD" : 2,
    "STONE" : 1
},
"requirments" : null,
"instantEffect": "ADD",
"instantPayload": {
    "VP":3
},
"permanentEffect" : "CHANGE",
"permanentPayload" :
    {
      "RESOURCEOUT": {"COINS" : 3},
      "RESOURCEIN":  {"WOOD" : 2,
                      "STONE" : 2},
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
"requirments" : null,
"instantEffect": null,
"instantPayload": null,
"permanentEffect" : "ACTION",
"permanentPayload" : {
    "TYPE" : "PRODUCTIONACTION",
    "REGIONID" : null,
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
"name" : "Ambassador",
"cost" : {
    "COINS" : 6,
},
"requirments" : null,
"instantEffect": ["ACTION", "PRIVILEGE"],
"instantPayload": [
  {
    "TYPE" : "TOWER",
    "REGIONID" : null,
    "BONUSACTIONVALUE" : 6,
    "BONUSRESOURCE" : null,
    "EXCLUSIVEBONUS" : false
  },
  {
    "NUMBER" : 1
  }
],
"permanentEffect" : null,
"permanentPayload" : null,
"minimumActionValue" : null,
"period" : 3,
"cardType" : "CHARACTERCARD"
}
```

```
{
"name" : "Support to the Bishop",
"cost" : [
  {
    "MILITARY" : 2
    },
  {
    "WOOD" : 1,
    "STONE" : 1,
    "COINS" : 2
    }],
"requirments" : {"MILITARY" : 4},
"instantEffect": "ADD",
"instantPayload": {
    "FAITH" : 3
  },
"permanentEffect" : "ADD",
"permanentPayload" : {"VICTORY" : 1},
"minimumActionValue" : null,
"period" : 2,
"cardType" : "VENTURECARD"
}
```
si noti che la carta possiede un effetto permanente ma un valore minimo nullo per l'attivazione. Questo perchè gli effetti permanenti di queste carte sono attivate solo una volta alla fine della partita (basterà quindi chiamare l'effetto permanente della carta su tutte le VENTURECARD possedute senza alcun check sull'actionValue richiesto).
