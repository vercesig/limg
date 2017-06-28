# Excommunicate Effect

 Effect: the player loses one element of a resource specified on the Excommunication Card 
 every time he/she activates an Add effect or takes a bonus from an actionSpace of that resource.
 
 JSON of the effect: it uses the same keys of a DevelopmentCard
 
 	{
 		"instantEffect": "LESSRESOURCE",
 		"instantPayload": ["COINS", "SERVANTS", "STONE"] // in the game rules the max number of this
 															Array is 2.
 	}
 	
 Flag List:
 
 *1-1: "MILITARY_POINTS"
 *1-2: "COINS"
 *1-3: "SERVANTS"
 *1-4: "WOOD", "STONE"
 *1-5: none (PERMANENT EFFECT)
 *1-6: none (PERMANENT EFFECT)
 *1-7: none (as a PERMANENT EFFECT)
 
 *2-1: none (PERMANENT EFFECT)
 *2-2: none (PERMANENT EFFECT)
 *2-3: none (PERMANENT EFFECT)
 *2-4: none (PERMANENT EFFECT)
 *2-5: none (as a PERMANENT EFFECT)
 *2-6: "DOUBLESERVANTS"
 *2-7: "SKIPTURN"
 
 *3-1: "NOENDBLUE"
 *3-2: "NOENDPURPLE"
 *3-3: "NOENDGREEN"
 *3-4: "LESSFORVICTORY"
 *3-5: "LESSFORMILITARY"
 *3-6: "LESSFORBUILDING"
 *3-7: "LESSFORRESOURCE"