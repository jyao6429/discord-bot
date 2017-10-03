package bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

public class ModTools
{
	public static ArrayList<String> mods = new ArrayList<String>(Arrays.asList("Mods","Testers","Chiefs"));
	
	public static boolean isMod(Member member)
	{
		List<Role> roles = member.getRoles();
		boolean isMod = false;
		
		for(Role temp : roles)
		{
			String roleName = temp.getName();
			if(mods.contains(roleName))
			{
				isMod = true;
				break;
			}
		}
		return isMod;
	}
}
