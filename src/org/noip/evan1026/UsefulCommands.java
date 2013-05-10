package org.noip.evan1026;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class UsefulCommands extends JavaPlugin {
    
    HashMap<String, Command> commands = new HashMap<String, Command>();
    
    public void onEnable(){
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
            commands.put(args[0], newCommand);
            
            sender.sendMessage(ChatColor.BLUE + "Successfully aliased \"" + args[0] + "\" to \"" + commandNameFinal + "\"");
            return true;
        }
        
        else if (command.getName().equalsIgnoreCase("removeAlias")){
            if (!commands.containsKey(args[0])){
                return false;
            }
            
            unRegisterBukkitCommand(commands.get(args[0]));
            
            sender.sendMessage(ChatColor.BLUE + "Successfully removed alias \"" + args[0] + "\"");
            
            return true;
        }
        
        else if (command.getName().equalsIgnoreCase("aliasList")){
            String message = "";
            
            for (String name : commands.keySet()){
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
}
