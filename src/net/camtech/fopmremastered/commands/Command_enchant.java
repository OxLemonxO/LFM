package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Commons;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandParameters(description = "Enchant items.", usage = "/enchant <list | addall | reset | add <name> | remove <name> | god <level>>", name = "enchant")
public class Command_enchant
{
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if (!(sender instanceof Player))
        {
            FOPMR_Commons.playerMsg(sender, "This can only be used in-game.");
            return true;
        }
        Player sender_p = (Player) sender;
        if (args.length < 1)
        {
            return false;
        }

        ItemStack itemInHand = sender_p.getItemInHand();

        if (itemInHand == null)
        {
            FOPMR_Commons.playerMsg(sender, "You are holding an invalid item.");
            return true;
        }

        if (args[0].equalsIgnoreCase("list"))
        {
            boolean has_enchantments = false;

            StringBuilder possible_ench = new StringBuilder("Possible enchantments for held item: ");
            for (Enchantment ench : Enchantment.values())
            {
                if (ench.canEnchantItem(itemInHand))
                {
                    has_enchantments = true;
                    possible_ench.append(ench.getName()).append(", ");
                }
            }

            if (has_enchantments)
            {
                FOPMR_Commons.playerMsg(sender, possible_ench.toString());
            }
            else
            {
                FOPMR_Commons.playerMsg(sender, "The held item has no enchantments.");
            }
        }
        else if (args[0].equalsIgnoreCase("addall"))
        {
            for (Enchantment ench : Enchantment.values())
            {
                try
                {
                    if (ench.canEnchantItem(itemInHand))
                    {
                        itemInHand.addEnchantment(ench, ench.getMaxLevel());
                    }
                } catch (Exception ignored)
                {

                }
            }

            FOPMR_Commons.playerMsg(sender, "Added all possible enchantments for this item.");
        }
        else if (args[0].equalsIgnoreCase("reset"))
        {
            for (Enchantment ench : itemInHand.getEnchantments().keySet())
            {
                itemInHand.removeEnchantment(ench);
            }

            FOPMR_Commons.playerMsg(sender, "Removed all enchantments.");
        }
        else
        {
            if (args.length < 2)
            {
                return false;
            }
            if (args[0].equalsIgnoreCase("god"))
            {
                int level;
                try
                {
                    level = Integer.parseInt(args[1]);
                } catch (Exception ex)
                {
                    return false;
                }
                for (Enchantment ench : Enchantment.values())
                {
                    if(ench.equals(Enchantment.LOOT_BONUS_MOBS) || ench.equals(Enchantment.LOOT_BONUS_BLOCKS))
                    {
                        continue;
                    }
                    itemInHand.addUnsafeEnchantment(ench, level);
                }
                return true;
            }

            Enchantment ench = null;

            try
            {
                ench = Enchantment.getByName(args[1]);
            } catch (Exception ignored)
            {
            }

            if (ench == null)
            {
                FOPMR_Commons.playerMsg(sender, args[1] + " is an invalid enchantment for the held item. Type \"/enchant list\" for valid enchantments for this item.");
                return true;
            }

            if (args[0].equalsIgnoreCase("add"))
            {
                if (ench.canEnchantItem(itemInHand))
                {
                    itemInHand.addEnchantment(ench, ench.getMaxLevel());

                    FOPMR_Commons.playerMsg(sender, "Added enchantment: " + ench.getName());
                }
                else
                {
                    FOPMR_Commons.playerMsg(sender, "Can't use this enchantment on held item.");
                }
            }
            else if (args[0].equals("remove"))
            {
                itemInHand.removeEnchantment(ench);

                FOPMR_Commons.playerMsg(sender, "Removed enchantment: " + ench.getName());
            }
        }

        return true;
    }
}