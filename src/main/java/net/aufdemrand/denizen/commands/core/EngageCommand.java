package net.aufdemrand.denizen.commands.core;

import net.aufdemrand.denizen.commands.Command;
import net.aufdemrand.denizen.scriptEngine.ScriptCommand;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

/**
 * Sets/unsets the Denizen's built in Engage List. 
 * When Engaged, a Denizen will not interact with a Player until DISENGAGED (or a timeout).
 * 
 * @author Jeremy Schroeder
 *
 */

public class EngageCommand extends Command {

	/* ENGAGE (# of Seconds) */

	/* Arguments: [] - Required, () - Optional 
	 * (# of Seconds) Will automatically DISENGAGE after specified amount of seconds.
	 *   If not set, the Denizen will remain ENGAGED until a DISENGAGE command is used.
	 *   
	 * Modifiers:
	 * (NPCID:#) Changes the Denizen to ENGAGE or DISENGAGE to the Citizens2 NPCID
	 */

	/* DISENGAGE */

	/* Modifiers:
	 * (NPCID:#) Changes the Denizen to ENGAGE or DISENGAGE to the Citizens2 NPCID
	 */
	
	@Override
	public boolean execute(ScriptCommand theCommand) {

		/* Initialize variables */ 

		Integer timedEngage = null;
		NPC theDenizen = theCommand.getDenizen();

		/* Get arguments */
		for (String thisArgument : theCommand.arguments()) {
			if (thisArgument.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+"))
				timedEngage = Integer.valueOf(thisArgument);

			if (thisArgument.toUpperCase().contains("NPCID:"))
				try {
					if (CitizensAPI.getNPCRegistry().getNPC(Integer.valueOf(thisArgument.split(":")[1])) != null)
						theDenizen = CitizensAPI.getNPCRegistry().getNPC(Integer.valueOf(thisArgument.split(":")[1]));	
				} catch (Throwable e) {
					theCommand.error("NPCID specified could not be matched to a Denizen.");
					return false;
				}
		}	

		/* If a DISENGAGE, take the Denizen out of the List. */
		if (theCommand.getCommand().equalsIgnoreCase("DISENGAGE")) {
			plugin.scriptEngine.setEngaged(theCommand.getDenizen(), false);
			return true;
		}

		/* ENGAGE the Denizen and set timer for DISENGAGE (if arguement is specified) */
		if (timedEngage != null) 
			plugin.scriptEngine.setEngaged(theDenizen, timedEngage);
		else 			
			plugin.scriptEngine.setEngaged(theCommand.getDenizen(), true);
		
		return true;
	}

}