# Protocol Specification

All messages exchanged from and to the server will be in
a json dictionary. The dictionary is composed of 2 elements: code and payload.

```
{
	code: "NTURN",
	payload:{
		...additional payload data
	}
}
```

## Game Packet Flow
When the client connects to the server the first message it receives is
a `CONNEST` message, that establishes that the server has successfully registered the
player to the game.
Afterwards there is a `TURNBGN` packet that dictates which playerid is entitled to make
moves. Clients should refrain from sending game-related packets when they didn't receive a 
packet with their id. Each time the packet is sent in broadcast to all clients and it is
assumed that the previous turn has ended.

Following there is a rough draft concerning the codes and the inherent payload

## Server-sent

#### CONNEST
* playerid: the player id
* masterid: the game master player id

#### GMRULES
* timeout: turn timeout

#### TURNBGN
* playerid: player id that will play

#### ACTCHK
* result: true or false, if true the server already applied the action
server inoltra al client che checkMove e' fallito.


#### BRDSTS
payload is a json-diff of the player object

#### MSG
* playerid: the id of the player that sent the message
* text: the text of the message

#### NAMECHG
* playerid: the id of the player that changed name
* name: new name to assign to the player

#### DICEROLL
* orange: roll of the orange dice
* white: roll of the white dice
* black: roll of the black dice

#### POPEREQ
* request the client if he/she want's to pay or be excommunicate

## Client-sent

#### SMSG
* text: the text to display in the chatbox

#### CHGNAME
* name: the name that will be displayed

#### ASKACT
* regionId: id of the region to move the pawn to
* spaceId: id of the ActionSpace to move to
* pawnId: numeric Id of the pawn

#### ASKLDRACT
* leaderId: id of the leader card

#### POPEANSW
* the answer of the client to the pope