package bot;

import commands.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import youtube.Search;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;

public class Main
{
	private JFrame frame; // GUI instance variables
	private JPanel mainPanel;
	private JPanel sidePanel;
	private static JTextArea text;
	public static JDA jda;
	private DefaultCaret caret;
	private JScrollPane scroller;

	public static final CommandParser parser = new CommandParser(); // Command stuff
	private static HashMap<String, Command> commands = new HashMap<>();

	public static void main(String[] args)
	{
		try
		{
			BufferedReader r = new BufferedReader(new FileReader("bot.key")); // Import the bot token from file on computer named "bot.key"
			String botToken = r.readLine(); // Set the string botToken as the token in the file
			jda = new JDABuilder(AccountType.BOT) // Initialize the bot
					.addEventListener(new BotListener()) // Adds the bot listener so it can respond when messages are sent
					.setGame(Game.playing("!help")) // Set the "game" that the bot will display as what it is "playing"
					.setToken(botToken) // Set the bot token
					.buildBlocking();
			jda.setAutoReconnect(true); // Set it to reconnect if disconnected from Internet
			r.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		commands.put("ping", new PingCommand()); // Put all the commands into the HashMap,
		commands.put("help", new HelpCommand()); // the key is the command that people call in the text channel,
		commands.put("roll", new RollCommand()); // the value is the command class that actually does the work
		commands.put("kick", new KickCommand());
		commands.put("block", new BlockCommand());
		commands.put("play", new PlayCommand());
		commands.put("skip", new SkipCommand());
		commands.put("stop", new StopCommand());
		commands.put("startpoll", new StartPollCommand());
		commands.put("vote", new VoteCommand());
		commands.put("endpoll", new EndPollCommand());
		commands.put("stoppoll", new EndPollCommand());
		commands.put("lmgtfy", new LMGTFYCommand());
		commands.put("resume", new ResumeCommand());
		commands.put("pause", new PauseCommand());

		Main bot = new Main(); // Create a new Main object and call the go() method
		bot.go();
		MusicController.go(); // Call the static go() method for both classes
		Search.go();
	}
	public static void logMessage(String temp) // Method to add text to the GUI text box
	{
		text.append(temp + "\n");
	}
	private void go() // Builds the GUI
	{
		frame = new JFrame("Discord Bot Server"); // Sets the name

		text = new JTextArea(17, 50); // Sets the size of the text box
		text.setLineWrap(true); // Sets line wrap
		text.setWrapStyleWord(true); // Sets line wrap by word instead of by letter
		text.setEditable(false); // Sets so that you can't edit the text in it

		scroller = new JScrollPane(text); // New scroll pane, so that the text box can scroll
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // Always have a vertical scrollbar
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Never have a horizontal scrollbar
		caret = (DefaultCaret) text.getCaret(); // Initialize the DefaultCaret and set it so that it auto-scrolls down
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		mainPanel = new JPanel(); // New JPanel, add the scrolling text box to it
		mainPanel.add(scroller);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);

		JButton saveButton = new JButton("Save"); // Create buttons
		JButton clearButton = new JButton("Clear");
		JButton versionButton = new JButton("Version");
		JButton scrollDownButton = new JButton("Scroll Down");

		sidePanel = new JPanel(); // New panel for the buttons
		sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS)); // Set box layout so buttons will be vertical

		saveButton.addActionListener(new SaveButtonListener()); // Add all Listeners to the buttons
		clearButton.addActionListener(new ClearButtonListener());
		versionButton.addActionListener(new VersionButtonListener());
		scrollDownButton.addActionListener(new ScrollButtonListener());

		sidePanel.add(saveButton); // Add the three buttons to the sidePanel
		sidePanel.add(clearButton);
		sidePanel.add(versionButton);

		frame.getContentPane().add(BorderLayout.SOUTH, scrollDownButton); // Add the scrollDownButton to the bottom
		frame.getContentPane().add(BorderLayout.EAST, sidePanel); // Add the sidePanel to the right
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Exits Java program if you close the window
		frame.setSize(660, 340); // Set frame dimensions
		frame.setVisible(true); // Make it visible
	}
	class SaveButtonListener implements ActionListener // When Save button is clicked
	{
		public void actionPerformed(ActionEvent ev)
		{
			try
			{
				// Create new file using GUI dialogue
				File file;
				JFileChooser fileSave = new JFileChooser();
				fileSave.showSaveDialog(frame);
				file = fileSave.getSelectedFile();

				// Writers for the file
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				PrintWriter pWriter = new PrintWriter(writer);

				String log = text.getText();

				String[] parts = log.split("\n");

				// Write everything to the file
				for (String temp : parts)
				{
					pWriter.println(temp);
				}
				pWriter.close();
			}
			catch (IOException ex) // Catch the exceptions
			{
				System.out.println("Could not save");
				ex.printStackTrace();
			}
			catch (Exception ex)
			{
				System.out.println("Other error");
				ex.printStackTrace();
			}
		}
	}
	class ClearButtonListener implements ActionListener // Clears the text box when clicked
	{
		public void actionPerformed(ActionEvent ev)
		{
			text.setText("");
		}
	}
	class VersionButtonListener implements ActionListener // Tells the version of the bot when clicked
	{
		public void actionPerformed(ActionEvent ev)
		{
			logMessage("Bot Version: 1.2.0");
		}
	}
	class ScrollButtonListener implements ActionListener // Send the scrollbar to the bottom when clicked
	{
		public void actionPerformed(ActionEvent ev)
		{
			JScrollBar vertical = scroller.getVerticalScrollBar();
			vertical.setValue(vertical.getMaximum());
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		}
	}
	public static void handleCommand(CommandParser.CommandContainer cmd)    // Handle the command when BotListener receives a message starting with an "!"
	{
		if (commands.containsKey(cmd.invoke))    // Checks if it is a valid command
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
