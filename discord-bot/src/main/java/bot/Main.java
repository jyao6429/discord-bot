package bot;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

import commands.BlockCommand;
import commands.EndPollCommand;
import commands.HelpCommand;
import commands.KickCommand;
import commands.PingCommand;
import commands.PlayCommand;
import commands.RollCommand;
import commands.SkipCommand;
import commands.StartPollCommand;
import commands.StopCommand;
import commands.VoteCommand;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import youtube.Search;

public class Main
{
	private JFrame frame;		//GUI instance variables
	private JPanel mainPanel;
	private JPanel sidePanel;
	private static JTextArea text;
	private static JDA jda;
	public static final CommandParser parser = new CommandParser();		//Command stuff
	public static HashMap<String, Command> commands = new HashMap<String, Command>();
	private DefaultCaret caret;		//More GUI stuff
	private JScrollPane scroller;

	public static void main(String[] args)
	{
		try
		{
			BufferedReader r = new BufferedReader(new FileReader("bot.key"));		//Import the bot token from file on computer named "bot.key"
			String botToken = r.readLine();						//Set the string botToken as the token in the file
			jda = new JDABuilder(AccountType.BOT)				//Initialize the bot
					.addEventListener(new BotListener())		//Adds the bot listener so it can respond when messages are sent
					.setGame(Game.of("Pwning Cheaters"))		//Set the "game" that the bot will display as what it is "playing"
					.setToken(botToken)							//Set the bot token
					.buildBlocking();
			jda.setAutoReconnect(true);		//Set it to reconnect if disconnected from Internet
			r.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();	//Catch exceptions
		}
		commands.put("ping", new PingCommand());		//Put all the commands into the HashMap,
		commands.put("help", new HelpCommand());		//the key is the command that people call in the text channel,
		commands.put("roll", new RollCommand());		//the value is the command class that actually does the work
		commands.put("kick", new KickCommand());		
		commands.put("block", new BlockCommand());
		commands.put("play", new PlayCommand());
		commands.put("skip", new SkipCommand());
		commands.put("stop", new StopCommand());
		commands.put("startpoll", new StartPollCommand());
		commands.put("vote", new VoteCommand());
		commands.put("endpoll", new EndPollCommand());
		commands.put("stoppoll", new EndPollCommand());
		
		Main bot = new Main();		//Create a new Main object and call the go() method
		bot.go();
		MusicController.go();		//Call the static go() method for both classes
		Search.go();
	}
	public static void logMessage(String temp)		//Method to add text to the GUI text box
	{
		text.append(temp + "\n");
	}
	public void go()		//Builds the GUI
	{
		frame = new JFrame("Discord Bot Server");		//Sets the name
		
		text = new JTextArea(17,50);		//Sets the size of the text box
		text.setLineWrap(true);				//Sets line wrap
		text.setWrapStyleWord(true);		//Sets line wrap by word instead of by letter
		text.setEditable(false);			//Sets so that you can't edit the text in it
		
		scroller = new JScrollPane(text);	//New scroll pane, so that the text box can scroll
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);		//Always have a vertical scrollbar
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);	//Never have a horizonal scrollbar
		caret = (DefaultCaret) text.getCaret();		//Initialize the DefaultCaret and set it so that it auto-scrolls down
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		mainPanel = new JPanel();		//New JPanel, add the scrolling text box to it
		mainPanel.add(scroller);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		
		JButton saveButton = new JButton("Save");		//Create buttons
		JButton clearButton = new JButton("Clear");
		JButton versionButton = new JButton("Version");
		JButton scrollDownButton = new JButton("Scroll Down");
		
		sidePanel = new JPanel();			
		sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
		
		saveButton.addActionListener(new SaveButtonListener());
		clearButton.addActionListener(new ClearButtonListener());
		versionButton.addActionListener(new VersionButtonListener());
		scrollDownButton.addActionListener(new ScrollButtonListener());
		
		sidePanel.add(saveButton);
		sidePanel.add(clearButton);
		sidePanel.add(versionButton);
		
		frame.getContentPane().add(BorderLayout.SOUTH, scrollDownButton);
		frame.getContentPane().add(BorderLayout.EAST, sidePanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(660, 340);
		frame.setVisible(true);
		
	}	
	public class SaveButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent ev)
		{
			File file;
			JFileChooser fileSave = new JFileChooser();
			fileSave.showSaveDialog(frame);
			file = fileSave.getSelectedFile();
			
			try
			{
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				PrintWriter pWriter = new PrintWriter(writer);
				
				String log = text.getText();
				
				String[] parts = log.split("\n");
				
				for(String temp : parts)
				{
					pWriter.println(temp);
				}
				pWriter.close();
			}
			catch(IOException ex)
			{
				System.out.println("Could not save");
				ex.printStackTrace();
			}
		}
	}
	public class ClearButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent ev)
		{
			text.setText("");
		}
	}
	public class VersionButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent ev)
		{
			logMessage("Bot Version: 1.1.4");
		}
	}
	public class ScrollButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent ev)
		{
			JScrollBar vertical = scroller.getVerticalScrollBar();
			vertical.setValue(vertical.getMaximum());
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		}
	}
	public static void handleCommand(CommandParser.CommandContainer cmd)
	{
		if(commands.containsKey(cmd.invoke))
		{
			boolean safe = commands.get(cmd.invoke).called(cmd.args, cmd.event);
			
			if (safe)
			{
				commands.get(cmd.invoke).action(cmd.args, cmd.event);
				commands.get(cmd.invoke).executed(safe, cmd.event);
			}
			else
			{
				commands.get(cmd.invoke).executed(safe, cmd.event);
			}
			
		}
		
	}
	
}
