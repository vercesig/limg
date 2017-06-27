# How to Import from external configuration file the Action Space bonus

  Just modify bonus_space.json. It has this structure:
  
 [
  {
  	"RegionID" : 1,
    "SpaceID" :  3,
    "Bonus" : {"COINS":5, "FAITH_POINTS": 3, "WOOD": 2} 
  },
  ....
 ]  
 
* RegionID: the unique id of the region
* SpaceID: the unique id of the action space in that region
* Bonus: a dictionary of the resource set of the bonus.