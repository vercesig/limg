gestione degli effetti:

un interfaccia funzionale Effect viene implementata da ogni funzione che realizza un effetto del gioco. 
tali funzioni vengono poi caricate all'interno di un EffectRegistry, una Singleton nella quale ogni effetto, una volta che vi è stato caricato, viene associato ad un riferimento di tipo String.

in tal modo attraverso il metodo getEffect(String OPCODE) chiamato sull'istanza di EffectRegistry è possibile invocare l'effetto desiderato conoscendone il solo riferimento simbolico.

in ottica di parsing, ogni carta possiederà due array di OPCODE, uno relativi agli effetti immediati e uno relativo ad effetti permanenti. 
il parser nel momento in cui preleva gli OPCODE, chiama sull'EffectRegistry il metodo getEffect() e associa alla nuova istanza di Card tutti gli effetti ad essa associata secondo quanto descritto nel file esterno.

un esempio di formattazione del file JSON per l'import delle carte potrebbe essere il seguente:

	[{
		"name" : "Cavaliere",
		"istantEffectList" : ["OPCODE1", "OPCODE2"],
		"permanentEffectList" : ["OPCODE1", "OPCODE2"],
		"resourceSet" : {
			"WOOD":10,
			"STONE":5,
			"SERVANT":0,
			"COIN":0
		}
		"period" : 1,
		"cardType" : "CHARACTERCARD"
	},{...},
	...
	]


istantEffectList e permanentEffectList sono due array di stringhe secondo la sintassi del JSON. nel momento in cui il parser cicla sugli elementi dell'array effettua una chiamata di questo tipo:

istantEffectList.forEach(effectOPCODE -> card.addIstantEffect(EffectRegistry.getEffect(effectOPCODE)));

dove card rappresenta l'oggetto Card in fase di creazione. in questo modo ciascuna carta avrà un riferimento a tutti gli effetti ad essa pertinenti e tali effetti potranno essere richiamati nel momento opportuno, attravero i metodi activateInstantEffect() e activatePermanentEffect(), esposti da ogni istanza di Card. 
