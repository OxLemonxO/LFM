/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.camtech.fopmremastered;

import net.camtech.fopmremastered.FOPMR_PlayerData;
import org.bukkit.Location;
import org.bukkit.block.*;
import org.bukkit.Material;
import org.bukkit.SkullType;

public class FOPMR_CageHelper {

    public static void buildHistory(Location location, int length, FOPMR_PlayerData playerdata) {
        final Block center = location.getBlock();
        for (int xOffset = -length; xOffset <= length; xOffset++) {
            for (int yOffset = -length; yOffset <= length; yOffset++) {
                for (int zOffset = -length; zOffset <= length; zOffset++) {
                    final Block block = center.getRelative(xOffset, yOffset, zOffset);
                    playerdata.insertHistoryBlock(block.getLocation(), block.getType());
                }
            }
        }
    }
        public static void generateCube(Location location, int length, Material material)
    {
        final Block center = location.getBlock();
        for (int xOffset = -length; xOffset <= length; xOffset++)
        {
            for (int yOffset = -length; yOffset <= length; yOffset++)
            {
                for (int zOffset = -length; zOffset <= length; zOffset++)
                {
                    final Block block = center.getRelative(xOffset, yOffset, zOffset);
                    if (block.getType() != material)
                    {
                        block.setType(material);
                    }
                }
            }
        }
    }
        public static void generateHollowCube(Location location, int length, Material material)
    {
        final Block center = location.getBlock();
        for (int xOffset = -length; xOffset <= length; xOffset++)
        {
            for (int yOffset = -length; yOffset <= length; yOffset++)
            {
                for (int zOffset = -length; zOffset <= length; zOffset++)
                {
                    // Hollow
                    if (Math.abs(xOffset) != length && Math.abs(yOffset) != length && Math.abs(zOffset) != length)
                    {
                        continue;
                    }

                    final Block block = center.getRelative(xOffset, yOffset, zOffset);

                    if (material != Material.SKULL)
                    {
                        // Glowstone light
                        if (material != Material.GLASS && xOffset == 0 && yOffset == 2 && zOffset == 0)
                        {
                            block.setType(Material.GLOWSTONE);
                            continue;
                        }

                        block.setType(material);
                    }
                    else // Darth mode
                    {
                        if (Math.abs(xOffset) == length && Math.abs(yOffset) == length && Math.abs(zOffset) == length)
                        {
                            block.setType(Material.GLOWSTONE);
                            continue;
                        }

                        block.setType(Material.SKULL);
                        final Skull skull = (Skull) block.getState();
                        skull.setSkullType(SkullType.PLAYER);
                        skull.setOwner("Prozza");
                        skull.update();
                    }
                }
            }
        }
    }
        
}
