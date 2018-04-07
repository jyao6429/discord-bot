// Used example from JDA

package bot;

import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BotListener extends ListenerAdapter
{
	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{

		// Event specific information
		User author = event.getAuthor(); // The user that sent the message
		Message message = event.getMessage(); // The message that was received.

		String msg = message.getContentDisplay(); // This returns a human readable version of the Message. Similar to
		// what you would see in the client.

		boolean bot = author.isBot(); // This boolean is useful to determine if the User that
										// sent the Message is a BOT or not!
		if (msg.startsWith("!") && bot != true)		// Tell Main to handle the command
		{
			Main.handleCommand(Main.parser.parse(event.getMessage().getContentDisplay().toLowerCase(), event));
		}
		
		// Log all the messages in the Guilds in the GUI text box
		if (event.isFromType(ChannelType.TEXT)) // If this message was sent to a Guild TextChannel
		{
			// Because we now know that this message was sent in a Guild, we can do guild
			// specific things
			// Note, if you don't check the ChannelType before using these methods, they
			// might return null due
			// the message possibly not being from a Guild!

			Guild guild = event.getGuild(); // The Guild that this message was sent in. (note, in the API, Guilds are
											// Servers)
			TextChannel textChannel = event.getTextChannel(); // The TextChannel that this message was sent to.
			Member member = event.getMember(); // This Member that sent the message. Contains Guild specific information
												// about the User!
			try
			{
				String name;
				if (message.isWebhookMessage())
				{
					name = author.getName(); // If this is a Webhook message, then there is no Member associated
				} // with the User, thus we default to the author for name.
				else
				{
					name = member.getEffectiveName(); // This will either use the Member's nickname if they have one,
				} // otherwise it will default to their username. (User#getName())

				String log = String.format("(%s)[%s]<%s>: %s\n", guild.getName(), textChannel.getName(), name, msg);
				Main.logMessage(log);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if (event.isFromType(ChannelType.PRIVATE)) // If this message was sent to a PrivateChannel
		{
			// The message was sent in a PrivateChannel.
			// In this example we don't directly use the privateChannel, however, be sure,
			// there are uses for it!

			String log = String.format("[PRIV]<%s>: %s\n", author.getName(), msg);
			Main.logMessage(log);
		}
		else if (event.isFromType(ChannelType.GROUP)) // If this message was sent to a Group. This is CLIENT only!
		{
			// The message was sent in a Group. It should be noted that Groups are CLIENT
			// only.
			Group group = event.getGroup();
			String groupName = group.getName() != null ? group.getName() : ""; // A group name can be null due to it
																				// being unnamed.

			String log = String.format("[GRP: %s]<%s>: %s\n", groupName, author.getName(), msg);
			Main.logMessage(log);
		}
	}
}
