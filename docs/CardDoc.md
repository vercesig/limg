# DevelopmentCard - JSON format
questo documento presenta la versione definitiva relativa al formato JSON delle DevelopmentCard.


prendiamo, come primo esempio, la carta "Trading Town". Nella sua rappresentazione JSON essa sarà
cosi astratta:

```
{
        "minimumActionValue": 1,
        "name": "Commercial Hub",
        "instantEffect": null,
        "permanentPayload": {
            "COINS": 1
        },
        "cost": null,
        "exclusivePermanentEffect": "TRUE",
        "period": 1,
        "instantPayload": null,
        "permanentEffect": "ADD",
        "cardType": "TERRITORYCARD",
        "requirements": null
    },
```

la semantica dei vari attribuit è la seguente:
* **name**: il nome della carta
* **cost**: il costo in risorse della carta (qui null perchè le carte territorio non hanno costo)
* **requirments**: i requisiti necessari, in termini di risorse, che il giocatore deve possedere
per poter pagare il costo della carta (e quindi acquisirla). POTREBBE ESSERE SETTATO OPZIONALE CON VALORE DI DEFAULT NULL
* **instantEffect**: la tipologia di effetto istantaneo che caratterizza la carta
* **instantPayload**: ciò che caratterizza lo specifico effetto della carta in questione, gli
argomenti qui presenti saranno impiegati dall'**EffectBuilder** per generare l'effetto corretto. Il
parser si occuperà di associare l'effetto alla carta
* **permanentEffect**: la tipologia di effetto permanente che caratterizza la carta
* **permanentPayload**: ciò che caratterizza lo specifico effetto permanente della carta. gli
argomenti qui presenti saranno impiegati dall'**EffectBuilder** per generare l'effetto corretto. Il
parser si occuperà di associare l'effetto alla carta
* **minimumActionValue**: il valore minimo dell'azione che triggera l'attivazione degli effetti
permanenti della carta. CARTE PERSONAGGIO POTREBBERO AVERLO NULL IN QUANTO IL LORO EFFETTO PERMANENTE VIENE PRELEVATO E AGGIUNTO DIRETTAMENTE ALLA LISTA DI EFFETTI DEL PLAYER
* **period**: il periodo della carta.
* **cardType**: la tipologia di carta
* **exclusivePermanentEffect**: se true si puo' attivare solo un effetto permanente. POTREBBE ESSERE SETTATO OPZIONALE, PURCHE' VALORE DI DEFAULT TRUE E CHE VENGA SETTATO PER I CASI IN CUI E' FALSE.

#PAYLOAD EFFETTI

* ACTION: 
 	consente di eseguire un azione tipo TYPE con valore ACTIONVALUE applicando lo
"sconto" di BONUSRESOURCE risorse sull'acquisto di una carta. (il flag EXCLUSIVEBONUS è necessario per indicare se lo sconto, qualora fosse costituito da più di una risorsa, è applicabile solo per una delle risorse elencate). Un campo REGIONID è necessario per poter individuare una particolare regione all'interno della Board (ad esempio l'azione può essere eseguita solo su una torre GIALLA). Il campo REGIONID posto a null indica che non vi è alcuna limitazione sulla regione in cui l'azione può essere effettuata (ad esempio l'azione può essere effettuata su TUTTE le torri della board). 
	Formato del payload:

```
{ "TYPE" : HARVESTACTION ,
    "REGIONID" : 5,
    "BONUSACTIONVALUE" : 3,
    "BONUSRESOURCE" : {"WOOD" : -1,
                       "COINS" -1},
    "EXCLUSIVEBONUS" : true,
    "FLAGREGION": "ALL" } // serve per mappare alcuni effetti che toccano tutte le torri
```

* BONUS: 
	per ogni QUANTITY di CARTA o RISORSA posseduta consente di incrementare
INCREASE di INCREASINGQUANTITY unità. Un campo TYPE viene impiegato per informare l'effectBuilder se il conteggio su ciò che si possiede deve essere fatto su CARD o RESOURCE. 
	Formato del payload:
	
```
{ "TYPE" : "CARD",
    "FOREACH": "CHARACTERCARD",
    "QUANTITY" : 1,
    "INCREASE" : "MILITARY",
    "INCREASINGQUANTITY": 2}
```
ad esempio questo effetto consente di incrementare di 2 i punti militari del giocatore per ogni
carta personaggio posseduta.

* PERMANENT: 
	effetti permanenti delle carte Character. danno bonus ai tipi di azione. Le chiavi del Json sono ESATTAMENTE le stesse degli effetti ACTION.
	Formato del payload:


```
{ "TYPE" : HARVESTACTION ,
    "REGIONID" : 5,
    "BONUSACTIONVALUE" : 3,
    "BONUSRESOURCE" : [{"WOOD" : -1,
                       "COINS" -1},{"STONE" : -1}], // JSONARRAY
    "EXCLUSIVEBONUS" : true, // FLAG
	"FLAGREGION": "ALL" } // serve per mappare alcuni effetti che toccano tutte le torri

```

* CHANGE: 
	Effetto Change. Il piu' difficile del gioco. Apre il context Change.
	Formato del payload:
	
```
{	“RESOURCEOUT”: { “VICTORY_POINTS”: 6 }, 
	“RESOURCEIN” : {“SERVANTS”: 1, “STONE”:1, “WOOD”:1 }
}

```


* PRIVILEGE: Effetto privilege. Apre un contesto di tipo Privilege. Ha Questo payload
	
```	
{ "NUMBER": 1,
  "COST": { "COINS":1, "WOOD":3 }
}
 
``` 
 IL CAMPO COST E' STATO AGGIUNTO PER MASCHERARE ALCUNI EFFETTI CHANGE COME DEGLI EFFETTI PRIVILEGE. (VEDI CARTA 32)

si noti che la carta possiede un effetto permanente ma un valore minimo nullo per l'attivazione. Questo perchè gli effetti permanenti di queste carte sono attivate solo una volta alla fine della partita (basterà quindi chiamare l'effetto permanente della carta su tutte le VENTURECARD possedute senza alcun check sull'actionValue richiesto).
