package net.camtech.fopmremastered;

import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

public class FOPMR_PermissionsInterface
{
    public static HashMap<Player, PermissionAttachment> attachments = new HashMap<>();
    
    public static void attachPlayer(Player player)
    {
        attachments.put(player, player.addAttachment(FreedomOpModRemastered.plugin));
    }
    
    public static void addPermission(Player player, String permission)
    {
        if(!attachments.containsKey(player))
        {
            attachPlayer(player);
        }
        PermissionAttachment attach = attachments.get(player);
        attach.setPermission(permission, true);
    }
    
    public static void removePermission(Player player, String permission)
    {
        if(!attachments.containsKey(player))
        {
            attachPlayer(player);
        }
        PermissionAttachment attach = attachments.get(player);
        attach.setPermission(permission, false);
    }
}
