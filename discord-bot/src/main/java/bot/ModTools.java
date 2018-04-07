package bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

public class ModTools
{
	public static ArrayList<String> mods = new ArrayList<String>(Arrays.asList("mods", "testers", "chiefs"));	// List of valid mod role names

	public static boolean isMod(Member member)
	{
		List<Role> roles = member.getRoles();	// Get all the roles of the memver
		boolean isMod = false;

		for (Role temp : roles)		// Check every single one until one is a mod role
		{
			String roleName = temp.getName().toLowerCase();
			if (mods.contains(roleName))
			{
				isMod = true;
				break;
			}
		}
		return isMod;
	}
}
