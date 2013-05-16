package org.noip.evan1026;

import java.lang.reflect.Field;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.noip.evan1026.alias.*;

public class UsefulCommands extends JavaPlugin {
    
    private AliasHandler _aliasHandler = new AliasHandler(this);

    public void onEnable(){
        loadFiles();
    }
    public void onDisable(){
        saveFiles();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if      (command.getName().equalsIgnoreCase("alias"))       return _aliasHandler.createAlias(sender, args);
        else if (command.getName().equalsIgnoreCase("removeAlias")) return _aliasHandler.removeAlias(sender, args);
        else if (command.getName().equalsIgnoreCase("aliasList"))   return _aliasHandler.aliasList(sender);

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

    private void loadFiles(){
        _aliasHandler.loadAliases();
    }

    private void saveFiles(){
        _aliasHandler.saveAliases();
    }

    
}
