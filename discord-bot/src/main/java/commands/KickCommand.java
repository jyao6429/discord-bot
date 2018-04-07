package commands;

import java.util.List;
import bot.Command;
import bot.ModTools;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.*;
import net.dv8tion.jda.core.exceptions.PermissionException;

public class KickCommand implements Command
{
	
	private final String HELP = "USAGE: !kick";

	@Override
	public boolean called(String[] args, MessageReceivedEvent event)
	{
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event)
	{
		MessageChannel channel = event.getChannel();
		Message message = event.getMessage();
		Member sender = event.getMember();
		if (!ModTools.isMod(sender))
		{
			channel.sendMessage("Must be a mod to use this command!").queue();
			return;
		}
		if (message.isFromType(ChannelType.TEXT))
        {
            //If no users are provided, we can't kick anyone!
            if (message.getMentionedUsers().isEmpty())
            {
                channel.sendMessage("You must mention 1 or more Users to be kicked!").queue();
            }
            else
            {
                Guild guild = event.getGuild();
                Member selfMember = guild.getSelfMember();  //This is the currently logged in account's Member object.
                                                            // Very similar to JDA#getSelfUser()!

                //Now, we the the logged in account doesn't have permission to kick members.. well.. we can't kick!
                if (!selfMember.hasPermission(Permission.KICK_MEMBERS))
                {
                    channel.sendMessage("Sorry! I don't have permission to kick members in this Guild!").queue();
                    return; //We jump out of the method instead of using cascading if/else
                }

                //Loop over all mentioned users, kicking them one at a time. Mwauahahah!
                List<User> mentionedUsers = message.getMentionedUsers();
                for (User user : mentionedUsers)
                {
                    Member member = guild.getMember(user);  //We get the member object for each mentioned user to kick them!

                    //We need to make sure that we can interact with them. Interacting with a Member means you are higher
                    // in the Role hierarchy than they are. Remember, NO ONE is above the Guild's Owner. (Guild#getOwner())
                    if (!selfMember.canInteract(member))
                    {
                        channel.sendMessage("Cannot kick member: " + member.getEffectiveName() +", they are higher " +
                                "in the hierachy than I am!").queue();
                        continue;   //Continue to the next mentioned user to be kicked.
                    }

                    //Remember, due to the fact that we're using queue we will never have to deal with RateLimits.
                    // JDA will do it all for you so long as you are using queue!
                    guild.getController().kick(member).queue(
                        success -> channel.sendMessage("Kicked " + member.getEffectiveName() + "! Cya!").queue(),
                        error ->
                        {
                            //The failure consumer provides a throwable. In this case we want to check for a PermissionException.
                            if (error instanceof PermissionException)
                            {
                                //PermissionException pe = (PermissionException) error;
                                //Permission missingPermission = pe.getPermission();  //If you want to know exactly what permission is missing, this is how.
                                                                                    //Note: some PermissionExceptions have no permission provided, only an error message!

                                channel.sendMessage("PermissionError kicking [" + member.getEffectiveName()
                                        + "]: " + error.getMessage()).queue();
                            }
                            else
                            {
                                channel.sendMessage("Unknown error while kicking [" + member.getEffectiveName()
                                        + "]: " + "<" + error.getClass().getSimpleName() + ">: " + error.getMessage()).queue();
                            }
                        });
                }
            }
        }
        else
        {
            channel.sendMessage("This is a Guild-Only command!").queue();
        }
	}

	@Override
	public String help()
	{
		return HELP;
	}

	@Override
	public void executed(boolean success, MessageReceivedEvent event)
	{
		return;
	}

}
