package org.noip.evan1026.alias;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class AliasedCommand extends Command {

    private String     _command;
    private JavaPlugin _plugin;
    
    public AliasedCommand(String name, JavaPlugin plugin, String command) {
        super(name);
        _plugin = plugin;
        _command = command;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        return _plugin.getServer().dispatchCommand(sender, _command);
    }
    
    public String getCommandName(){
        return _command;
    }

}
