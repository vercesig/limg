# SetupGame

la classe **Setup** inizializza la classe **Game** in modo tale che questa assuma lo stato
iniziale della partita, in particolare Setup possiede al suo interno i seguenti metodi privati:

   * *setUpCard:* tale metodo carica da file esterno (vedi **JsonImporter**) le informazioni
   relative ai mazzi di gioco (sia DevelopmentCard che ExcommunicationCard). Per le DevelopmentCard
   esse vengono suddivise per tipologia e successivamente per periodo, i vari sotto-mazzi vengono
   poi mischiati singolarmente e riassemblati in ordine crescente secondo il periodo. I mazzi
   risultanti sono poi caricati nella classe Game.
   Per quanto concerne le ExcommunicationCard, setUpCard esegue sostanzialmente le stesse operazioni effettuate per la gestione delle DevelopmentCard ed estrae casualmente da ciascun sotto-mazzo (relativo ad uno specifico periodo) una tessera scomunica che viene quindi caricata in Game.
   Si noti come non si sia fatto accenno nè al numero di periodi nè al numero di tipologie di carte sviluppo, questo perchè il funzionamento del metodo setUpCard prescinde da queste informazioni. Il metodo è in grado di dedurre queste informazioni dai file che contengono le informazioni per la generazione delle carte, rendendo la procedura totalmente scalabile. Inoltre per adattare la classe **Board** alla particolare configurazione assunta dal gioco, setUpCard configura anche le torri della Board in modo che esse mappino esattamente gli specifici tipi inferiti dal file di caricamento delle carte.
   * *setUpTurnOrder:* tale metodo si occupa di assegnare l'ordine di turno iniziale in maniera casuale. L'ordine di turno è codificato mediante l'ordine in cui i player si presentano all'interno dell'attributo *__`ArrayList<Player> playerList`__* della classe Game.
   * *setUpPlayer:* tale metodo presuppone che la classe Game abbia già una lista di player inizializzata. setUpPlayer predispone ciascun player registrato allo stato iniziale: poichè gran parte della configurazione viene preparata nel momento della creazione del Player, setUpPlayer si limita a settare le risorse iniziali e ad assegnare le monete in base all'ordine di turno. infine viene assegnata una personalBonusTile ad ogni giocatore.

L'utilizzatore della classe Setup ha accesso al suo solo costruttore, che si occuperà di prendere un istanza di Game e inizializzarla. Basterà quindi eseguire una chiamata di questo tipo:

```
Game game = new Game(playerList);
SetUp setupGame = new SetUp(game);
```

per avere una classe Game completamente inizializzata.
