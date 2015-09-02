package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import net.camtech.fopmremastered.FOPMR_Bans;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.scheduler.BukkitRunnable;

public class Command_potato extends FOPMR_Command {

    public Command_potato() {
        super("potato", "/potato [[give | ban] [username] [reason]", "Potatoes.", Rank.OP);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.getName().equals("OxLemonxO") | sender.getName().equals("MashedPtatoz") | sender.getName().equals("DarkHorse108") | sender.getName().equals("vj13573")) {
            Player player;
            if (args.length == 2) {
                player = FOPMR_Rank.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage("Player: " + args[1] + " is not online.");
                    return true;
                }
                if (args[0].equalsIgnoreCase("give")) {
                    Bukkit.broadcastMessage(ChatColor.AQUA + "MashedPtatoz - Showering " + player.getName() + " in delicious potatoes!");
                    ItemStack potato = new ItemStack(Material.POTATO);
                    ItemMeta potatometa = potato.getItemMeta();
                    potatometa.setDisplayName("Potato");
                    ArrayList PotatoLore = new ArrayList();
                    PotatoLore.add("Given to you by - " + sender.getName());
                    potatometa.setLore(PotatoLore);
                    for (int i = 0; i < 1000; i++) {
                        player.getInventory().addItem(potato);
                    }
                }
                return false;
            }
            if (args.length >= 3) {
                if (args[0].equalsIgnoreCase("ban")) {

                    player = FOPMR_Rank.getPlayer(args[1]);
                    if (player == null) {
                        sender.sendMessage("Player: " + args[1] + " is not online.");
                        return true;
                    }
                    String reason;
                    reason = (String) args[2];
                    if (reason == null) {
                        sender.sendMessage(ChatColor.RED + "Please specify a reason.");
                        return true;
                    }
                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "MashedPtatoz - Showering " + player.getName() + " in malicious potatoes!");

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.broadcastMessage(ChatColor.DARK_RED + "Crushing " + player.getName() + "'s skull...");
                            player.setHealth(0.0);
                            for (int i = 0; i < 100; i++) {
                                player.getWorld().strikeLightning(player.getLocation());
                            }
                        }
                    }.runTaskLater(FreedomOpModRemastered.plugin, 2L * 20L);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.broadcastMessage(ChatColor.DARK_RED + "MashedPtatoz - Banning " + player.getName() + "and IPs: " + player.getPlayer().getAddress().getAddress().getHostAddress().trim());
                            for (int i = 0; i < 100; i++) {
                                player.getWorld().strikeLightning(player.getLocation());

                            }
                            FOPMR_Bans.addBan(player, reason, sender.getName());

                        }
                    }.runTaskLater(FreedomOpModRemastered.plugin, 3L * 21L);
                }
            }
        } else {
            Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "LFM" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_RED + sender.getName() + " has tried to use MashedPtatoz's command.");
        }
        return false;
    }
}
