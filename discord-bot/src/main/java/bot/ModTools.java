package bot;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModTools
{
	private static final ArrayList<String> mods = new ArrayList<>(Arrays.asList("mods", "testers", "chiefs"));    // List of valid mod role names

	public static boolean isMod(Member member)
	{
		List<Role> roles = member.getRoles();    // Get all the roles of the memver
		boolean isMod = false;

		for (Role temp : roles)        // Check every single one until one is a mod role
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
