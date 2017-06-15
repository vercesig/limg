# Context Packet

pacchetto cambio di contesto usato ovunque 
	esempio: azioni production
ha un payload in json che dipende dal  tipo di contesto
	esempio: contesto production

	SERTVER  CHGCONTEXT
	CLIENT   CONTEXTREPLY
	ACK: 	  CONTEXTRESULT
	
json payload : 
{	
	[
		[ {int index} {object(resourceSet)} {object(ResourceSet)} ]	
	]	
}
	La roba si applica una sola volta, 

The ContextSwitch message is created taking a JsonObject with some informations. The payload needs for the Server to move the check of the decision in the client.
 The JsonObject has this structure based on the type of decision that the Server is asking to the client to make.
 
  { "OPCODE" : "STRING", "PAYLOAD": {...} }
  
#List of ContextSwitch:
 <li>
<h2>
 Opcode: "SERVANTS".
</h2>
 Used when the Server is asking if the Client wants to use some Servants.
<h1> 
 Payload: "NUMBER_SERVANTS": "INTEGER"
</h1>  	 

<li>
<h2>
 Opcode: "PRIVILEGE". </h2>
 Used when the Server is asking if to convert a number of privilege in Resources.
<h1> 
 Payload: {"NUMBER" : "INTEGER"} 
</h1>  	 
 INTEGER is the number int of change that the player can obtain with the privilege.

<li>
<h2>
 Opcode: "ACTION". </h2>
 Used when the Server is asking if the Client to perform a new Action.
<h1> 
 Payload: "REGION ID": "INTEGER", "NUMBER_OF_ID_VALID" : "MAX_ACTION_ID_INTEGER"
</h1>  	 

<li>
<h2>
 Opcode: "EXCOMMUNICATE".
</h2>
 Used when the Server is asking if the Client wants to pay Faith_points or be Excommunicated.
<h1> 
 Payload: "FAITH_POINTS_PLAYER": "INTEGER", "FAITH_POINTS_NEEDED": "INTEGER"
</h1>  	 