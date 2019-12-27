package bot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Collections;

public class CommandParser
{
	public CommandContainer parse(String rw, MessageReceivedEvent e)
	{
		// Remove the "!" from the beginning of the command and split it into the arguments
		ArrayList<String> split = new ArrayList<>();
		String beheaded = rw.replaceFirst("!", "");
		String[] splitBeheaded = beheaded.split(" ");
		Collections.addAll(split, splitBeheaded);
		String invoke = split.get(0);
		String[] args = new String[split.size() - 1];
		split.subList(1, split.size()).toArray(args);

		return new CommandContainer(rw, beheaded, splitBeheaded, invoke, args, e);
	}
	public class CommandContainer    // Class for holding the entirety of the command with arguments
	{
		public final String invoke;
		public final String[] args;
		public final MessageReceivedEvent event;
		final String raw;
		final String beheaded;
		final String[] splitBeheaded;

		CommandContainer(String rw, String beheaded, String[] splitBeheaded, String invoke, String[] args, MessageReceivedEvent e)
		{
			this.raw = rw;
			this.beheaded = beheaded;
			this.splitBeheaded = splitBeheaded;
			this.invoke = invoke;
			this.args = args;
			this.event = e;
		}
	}
}
