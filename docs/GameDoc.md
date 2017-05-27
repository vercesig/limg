# Game
la classe Game contiene tutte le informazioni relative ad una partita:

```
private ArrayList<Player> playerList;
private Board board;

private HashMap<String, Deck<DevelopmentCard>> decks;
private ExcommunicationCard[] excommunicationCards;

private int blackDice;
private int whiteDice;
private int orangeDice;

private boolean flag2PlayersGame=true;
```

il costruttore di Game prevede il passaggio di una lista di Player, in modo che essa possa essere associata ad una partita. Game si occupa di generare una Board, inizializzare le strutture dati per la gestione dei mazzi e delle carte scomunica, e settare vari flag relativi alla particolare partita creata (in base al numero di giocatori presenti).
L'oggetto Game deve successivamente essere passato al costruttore di **Setup** per essere inizializzato. Un **TurnManager** si occuperà di gestire i turni in cui si svilupperà la partita.
