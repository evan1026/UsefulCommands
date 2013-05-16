package org.noip.evan1026;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class UsefulCommands extends JavaPlugin {

	private HashMap<String, Command> _aliases = new HashMap<String, Command>();

	public void onEnable(){
		loadFiles();
	}
	public void onDisable(){
		saveFiles();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

		if (command.getName().equalsIgnoreCase("alias")){

			if (args.length < 2) return false;

			String commandName = "";

			for (int i = 1; i < args.length; i++){
				commandName += args[i] + " ";
			}

			commandName = commandName.substring(0, commandName.length() - 1);

			final String commandNameFinal = commandName;

			Command newCommand = new Command(args[0]){
				@Override
				public boolean execute(CommandSender sender, String commandLabel, String[] args){

					getServer().dispatchCommand(sender, commandNameFinal);
					return true;
				}
			};
			getCommandMap().register("", newCommand);
			_aliases.put(args[0], newCommand);

			sender.sendMessage(ChatColor.BLUE + "Successfully aliased \"" + args[0] + "\" to \"" + commandNameFinal + "\"");
			return true;
		}

		else if (command.getName().equalsIgnoreCase("removeAlias")){
			if (!_aliases.containsKey(args[0])){
				return false;
			}

			unRegisterBukkitCommand(_aliases.get(args[0]));

			sender.sendMessage(ChatColor.BLUE + "Successfully removed alias \"" + args[0] + "\"");

			return true;
		}

		else if (command.getName().equalsIgnoreCase("aliasList")){
			String message = "";

			for (String name : _aliases.keySet()){
				message += name + ", ";
			}

			sender.sendMessage(ChatColor.BLUE + message.substring(0, message.length() - 2));

			return true;
		}

		return false;
	}

	//Taken from Icyene in his ReflectCommand library
	public CommandMap getCommandMap() {
		Field map;
		try {
			map = SimplePluginManager.class.getDeclaredField("commandMap");
			map.setAccessible(true);
			return (CommandMap) map.get(getServer().getPluginManager());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//Taken from zeeveener
	private Object getPrivateField(Object object, String field) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

		Class<?> clazz = object.getClass();

		Field objectField = clazz.getDeclaredField(field);
		objectField.setAccessible(true);

		Object result = objectField.get(object);

		objectField.setAccessible(false);

		return result;
	}


	//Taken from zeeveener
	public void unRegisterBukkitCommand(Command cmd) {

		try {

			Object result = getPrivateField(this.getServer().getPluginManager(), "commandMap");
			SimpleCommandMap commandMap = (SimpleCommandMap) result;
			Object map = getPrivateField(commandMap, "knownCommands");

			@SuppressWarnings("unchecked")
			HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;

			knownCommands.remove(cmd.getName());

			for (String alias : cmd.getAliases()){

				if(knownCommands.containsKey(alias) && knownCommands.get(alias).toString().contains(this.getName())){
					knownCommands.remove(alias);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void loadFiles(){
		
		List<String> fileContents;

		try {
			
			fileContents = Files.readAllLines(Paths.get("plugins/UsefulCommands/config"), Charset.defaultCharset());
			
		} catch (IOException e) {

			getLogger().info("Save file not found. It will be created next time this plugin is disabled.");
			return;

		}
		
		for (String line : fileContents){
			String aliasName = line.substring(0, line.indexOf(":"));
			String commandName = line.substring(line.indexOf(":") + 1);
			
			
		}
	}
	
	private void saveFiles(){
		List<String> file = new ArrayList<String>();
		File path = new File("plugins/UsefulCommands");
		
		path.mkdirs();
		path = new File("plugins/UsefulCommands/config");
		
		try {
			
			path.createNewFile();
			
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		
		for(String alias : _aliases.keySet()){
			String line = alias + ":" + _aliases.get(alias).getName();
			
			file.add(line);
		}
		
		try {
			
			Files.write(Paths.get("plugins/UsefulCommands/config"), file, Charset.defaultCharset());
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
