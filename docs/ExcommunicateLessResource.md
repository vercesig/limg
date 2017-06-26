# Excommunicate Less Resource Effect

 Effect: the player loses one element of a resource specified on the Excommunication Card 
 every time he/she activates an Add effect or takes a bonus from an actionSpace of that resource.
 
 JSON of the effect: it uses the same keys of a DevelopmentCard
 
 	{
 		"instantEffect": "LESSRESOURCE",
 		"instantPayload": ["COINS", "SERVANTS", "STONE"] // in the game rules the max number of this
 															Array is 2.
 	}
 	