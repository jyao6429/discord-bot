package bot;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PollHandler
{
	public static final ArrayList<String> yes = new ArrayList<>(Arrays.asList("yes", "y", "ye", "yep", "si"));
	public static final ArrayList<String> no = new ArrayList<>(Arrays.asList("no", "nope", "n"));
	public static final HashMap<TextChannel, ArrayList<Member>> hasVoted = new HashMap<>();
	public static final HashMap<TextChannel, HashMap<String, Integer>> allPolls = new HashMap<>();
}
