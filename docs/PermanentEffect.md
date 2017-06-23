# Permanent Effect info

 It is used for the most Characters Permanent Effect. Effect which add extra actionValue to a specific action or extra resource to buy a card.
The Effect has this structure in Json:

 * PERMANENT: effetti permanenti delle carte Character. danno bonus ai tipi di azione:

{ "TYPE" : HARVEST ,
    "REGIONID" : 5,
    "BONUSACTIONVALUE" : 3,
    "BONUSRESOURCE" : [{"WOOD" : -1,
                       "COINS" -1},{"STONE" : -1}], // JSONARRAY
    "EXCLUSIVEBONUS" : true, // FLAG
    "FLAGREGION": "ALL" } // serve per mappare alcuni effetti che toccano tutte le torri
    
  The values permitted are the follow:
 * type: [Permanent]. It is the Opcode used to register this effectBuilder in the EffectRegistry
 <h1>
* actionType : [TowerGreen], [TowerYellow],[TowerBlue],[TowerPurple],[Production], [Harvest], [Council], [Market]. the last two are not used in the standard card of the game.
* actionValueBonus: integer. This Is the extra actionValue to add to the action.
* cost: [{Resource set}, {ResoureSet}, ...]. An Array of resource Set used for card in which the player needs to decide the specific discount.
* flagCost: [exclusive], [null]. if it is null then the cost is always a single value of ResourceSet. Otherwise the player has to decide which discount to apply.