package net.minecraft.AgeOfMinecraft.effects;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;

public class PotionBleeding extends PotionEngender 
{
	public PotionBleeding() 
	{
		super("bleeding", true, 16711680, 1, 0, -0.1D);
	}
	
    public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier)
    {
        return modifier.getAmount() * (double)(amplifier + 1);
    }

	public void performEffect(EntityLivingBase mob, int amplifier)
	{
		if(!EngenderMod.doesntHaveTimeToBleed(mob))
		{
			if (mob.world.isRemote)
			mob.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, true, mob.posX + (mob.getRNG().nextFloat() - 0.5D) * mob.width, mob.posY + (mob.getRNG().nextFloat()* mob.height), mob.posZ + (mob.getRNG().nextFloat() - 0.5D) * mob.width, 4.0D * (mob.getRNG().nextFloat() - 0.5D), 0.5D, (mob.getRNG().nextFloat() - 0.5D) * 4.0D, new int[] { Block.getStateId(Blocks.REDSTONE_BLOCK.getDefaultState()) });
			mob.attackEntityFrom((new DamageSource("bleed")).setDamageBypassesArmor(), 1);
			if (mob instanceof EntityPlayer)
				((EntityPlayer)mob).addExhaustion(1F * (float)(amplifier + 1));
			if (mob instanceof EntityFriendlyCreature)
				((EntityFriendlyCreature)mob).setEnergy(((EntityFriendlyCreature)mob).getEnergy() - (1F * (float)(amplifier + 1)));
		}

	}

	public boolean isReady(int par1, int par2)
	{
        int k = 400 >> par2;

        if (k > 0)
        {
            return par1 % k == 0;
        }
        else
        {
            return true;
        }
	}

	public List<ItemStack> getCurativeItems()
	{
		List<ItemStack> list = new ArrayList<>();
		return list;
	}
}