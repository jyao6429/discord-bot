package bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

public class PollHandler
{
	public static HashMap<Guild, int[]> poll = new HashMap<Guild, int[]>();
	public static ArrayList<String> yes = new ArrayList<String>(Arrays.asList("yes", "y", "ye", "yep", "si"));
	public static ArrayList<String> no = new ArrayList<String>(Arrays.asList("no", "nope", "n"));
	public static HashMap<Guild, Boolean> isPolling = new HashMap<Guild, Boolean>();
	public static HashMap<Guild, ArrayList<Member>> hasVoted = new HashMap<Guild, ArrayList<Member>>();
}
