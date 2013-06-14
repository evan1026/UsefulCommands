package org.noip.evan1026.playerInfo;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class PlayerInfoHandler {

    private JavaPlugin _plugin;

    public PlayerInfoHandler(JavaPlugin plugin){
        _plugin = plugin;
    }

    public boolean getPlayerInfo(CommandSender sender, String[] args){

        //Name and nick
        //Position, World
        //Health
        //Hunger

        if (!(sender instanceof Player) && !(args.length > 0)){
            sender.sendMessage(ChatColor.RED + "A player name must be specified to use this command from the console.");
            return true;
        }
        
        Player player = _plugin.getServer().getPlayerExact((args.length > 0) ? args[0] : sender.getName());
        
        if (player == null){
            sender.sendMessage(ChatColor.RED + "That player is not currently online.");
            return true;
        }
        
        
        String lastLine = ChatColor.BLUE + "~~~~~~~~~~~~~~~~";
        for (int i = 0; i < player.getName().length(); i++){
            lastLine += "~";
        }
        
        String nickname = (player.getDisplayName() != player.getName()) ? ChatColor.WHITE + player.getDisplayName() : ChatColor.WHITE +  "None";
        
        sender.sendMessage(new String[] {
                ChatColor.BLUE + "~~~~~Info on " + player.getName() + "~~~~~",
                
                ChatColor.BLUE + "Nickname: " + nickname,
                
                ChatColor.BLUE  + "World: " + ChatColor.WHITE + player.getWorld().getName(),
                
                ChatColor.BLUE  + "Position: ",
                ChatColor.GREEN + "     x: " + ChatColor.WHITE + player.getLocation().getBlockX(),
                ChatColor.GREEN + "     y: " + ChatColor.WHITE + player.getLocation().getBlockY(),
                ChatColor.GREEN + "     z: " + ChatColor.WHITE + player.getLocation().getBlockZ(),
                
                ChatColor.BLUE  + "Health: " + ChatColor.WHITE + player.getHealth()    + " / " + player.getMaxHealth(),
                ChatColor.BLUE  + "Hunger: " + ChatColor.WHITE + player.getFoodLevel() + " / 20",
                
                lastLine
        });
        

        return true;
    }
}
