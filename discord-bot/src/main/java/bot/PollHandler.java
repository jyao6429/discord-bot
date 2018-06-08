package bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

public class PollHandler
{
	public static ArrayList<String> yes = new ArrayList<String>(Arrays.asList("yes", "y", "ye", "yep", "si"));
	public static ArrayList<String> no = new ArrayList<String>(Arrays.asList("no", "nope", "n"));	
	public static HashMap<TextChannel, ArrayList<Member>> hasVoted = new HashMap<>();
	public static HashMap<TextChannel, HashMap<String, Integer>> allPolls = new HashMap<>();
}
