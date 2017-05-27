JsonImporter
===========

offre una serie di metodi statici per il caricamento di una lista di carte a
partire da un file JSON esterno.


# DEVELOPMENT CARD:
per quanto riguarda l'import delle DevelopmentCard JsonImporter offre un metodo
*importDevelopmentCard(FileReader filePath)* che restituisce una lista di carte sviluppo.

il metodo si presta molto bene ad essere utilizzato con la classe __`Deck<T>`__,
è possibile infatti creare un mazzo di carte direttamente da file esterno,
semplicemente invocando nel costruttore dell'oggetto Deck<T> il metodo
*importDevelopmentCard(FileReader filePath)*. in tal modo la seguente chiamata:

```
Deck<DevelopmentCard> developmentCardDeck = new Deck(JsonImporter.importDevelopmentCard(...));
```

crea un mazzo di carte su cui sono lecite tutte le operazioni definite dalla classe Deck.
ad esempio developmentCardDeck.drawElement() restituisce la carta in cima al mazzo.

# EXCOMMUNICATION CARD:
JsonImporter offre un metodo *importExcommunicationCard(FileReader filePath)* per l'import delle tessere scomunica:

```
Deck<ExcommunicationCard> excommunicationCardDeck = new Deck(JsonImporter.importExcommunicationCard(...));
```
