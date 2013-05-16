package org.noip.evan1026.alias;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.noip.evan1026.UsefulCommands;
import org.noip.evan1026.Utils;

public class AliasHandler {

    private UsefulCommands                  _plugin;
    private HashMap<String, AliasedCommand> _aliases = new HashMap<String, AliasedCommand>();

    public AliasHandler(UsefulCommands plugin){
        _plugin = plugin;
    }

    public boolean createAlias(CommandSender sender, String[] args){
        if (args.length < 2) return false;

        String commandName = "";

        for (int i = 1; i < args.length; i++){
            commandName += args[i] + " ";
        }

        commandName = commandName.substring(0, commandName.length() - 1);

        AliasedCommand newCommand = new AliasedCommand(args[0], _plugin, commandName);

        _plugin.getCommandMap().register("", newCommand);
        _aliases.put(args[0], newCommand);

        sender.sendMessage(ChatColor.BLUE + "Successfully aliased \"" + args[0] + "\" to \"" + commandName + "\"");
        return true;
    }

    public boolean removeAlias(CommandSender sender, String[] args){
        if (!_aliases.containsKey(args[0])){
            
            sender.sendMessage(ChatColor.RED + "That alias does not exist.");
            
            return true;
        }

        unRegisterBukkitCommand(_aliases.get(args[0]));

        _aliases.remove(args[0]);

        sender.sendMessage(ChatColor.BLUE + "Successfully removed alias \"" + args[0] + "\"");

        return true;
    }

    public boolean aliasList(CommandSender sender){
        String message = "";

        for (String name : _aliases.keySet()){
            message += name + ", ";
        }

        if (!message.equals("")) sender.sendMessage(ChatColor.BLUE + message.substring(0, message.length() - 2));
        else                     sender.sendMessage(ChatColor.BLUE + "No aliases found.");

        return true;
    }

    //Taken from zeeveener
    private void unRegisterBukkitCommand(Command cmd) {

        try {

            Object result = getPrivateField(_plugin.getServer().getPluginManager(), "commandMap");
            SimpleCommandMap commandMap = (SimpleCommandMap) result;
            Object map = getPrivateField(commandMap, "knownCommands");

            @SuppressWarnings("unchecked")
            HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;

            knownCommands.remove(cmd.getName());

            for (String alias : cmd.getAliases()){

                if(knownCommands.containsKey(alias) && knownCommands.get(alias).toString().contains(_plugin.getName())){
                    knownCommands.remove(alias);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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
    
    public void loadAliases(){
        File         file         = new File("plugins/UsefulCommands/aliases");
        List<String> fileContents = Utils.readFile(file.getAbsolutePath());
        
        for (String line : fileContents){
            String aliasName = line.substring(0, line.indexOf(":"));

            String commandName = line.substring(line.indexOf(":") + 1);

            AliasedCommand newCommand = new AliasedCommand(aliasName, _plugin, commandName);

            _plugin.getCommandMap().register("", newCommand);
            _aliases.put(aliasName, newCommand);
        }
    }
    
    public void saveAliases(){
        List<String> contents = new ArrayList<String>();
        File file = new File("plugins/UsefulCommands");

        file.mkdirs();
        file = new File("plugins/UsefulCommands/aliases");

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(String alias : _aliases.keySet()){
            String line = alias + ":" + _aliases.get(alias).getCommandName();

            contents.add(line);
        }

        try {
            Utils.writeFile(file.getAbsolutePath(), contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
