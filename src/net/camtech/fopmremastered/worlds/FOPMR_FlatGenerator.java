package net.camtech.fopmremastered.worlds;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class FOPMR_FlatGenerator extends ChunkGenerator
{

    @Override
    public boolean canSpawn(World world, int x, int z)
    {
        return true;
    }

    public int xyzToByte(int x, int y, int z)
    {
        return (x * 16 + z) * 128 + y;
    }

    @Override
    public byte[] generate(World world, Random rand, int chunkx, int chunkz)
    {
        byte[] result = new byte[32768];
        int y = 0;
        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                result[xyzToByte(x, y, z)] = (byte) Material.BEDROCK.getId();
            }
        }
        for(y = 1; y < 50; y++)
        {
            for(int x = 0; x < 16; x++)
            {
                for(int z = 0; z < 16; z++)
                {
                    result[xyzToByte(x, y, z)] = (byte) Material.DIRT.getId();
                }
            }
        }
        y = 50;
        for(int x = 0; x < 16; x++)
        {
            for(int z = 0; z < 16; z++)
            {
                result[xyzToByte(x, y, z)] = (byte) Material.GRASS.getId();
            }
        }
        return result;
    }

}
