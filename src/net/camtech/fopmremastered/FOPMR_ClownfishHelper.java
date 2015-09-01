/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.camtech.fopmremastered;

import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.material.MaterialData;

@SuppressWarnings("deprecation")
public class FOPMR_ClownfishHelper {
 
    public static Material getMaterial(int id) {
        return Material.getMaterial(id);
    }

    public static byte getData_MaterialData(MaterialData md) {
        return md.getData();
    }

    public static void setData_MaterialData(MaterialData md, byte data) {
        md.setData(data);
    }

    public static byte getData_Block(Block block) {
        return block.getData();
    }

    public static void setData_Block(Block block, byte data) {
        block.setData(data);
    }
}
