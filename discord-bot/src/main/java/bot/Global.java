package bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

public class Global
{
	public static ArrayList<String> mods = new ArrayList<String>(Arrays.asList("Mods","Testers","Chiefs"));
	public static HashMap<Guild, int[]> poll = new HashMap<Guild, int[]>();
	public static ArrayList<String> yes = new ArrayList<String>(Arrays.asList("yes","y","ye","yep","si"));
	public static ArrayList<String> no = new ArrayList<String>(Arrays.asList("no","nope","n"));
	public static HashMap<Guild, Boolean> isPolling = new HashMap<Guild, Boolean>();
	public static HashMap<Guild, ArrayList<Member>> hasVoted = new HashMap<Guild, ArrayList<Member>>();
	
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
