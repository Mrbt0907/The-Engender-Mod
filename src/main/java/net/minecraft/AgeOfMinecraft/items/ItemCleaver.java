/*******************************************************************************
* AbyssalCraft
* Copyright (c) 2012 - 2017 Shinoow.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Lesser Public License v3
* which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/lgpl-3.0.txt
*
* Contributors:
*Shinoow -implementation
******************************************************************************/package net.minecraft.AgeOfMinecraft.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;


public class ItemCleaver extends ItemSword{
	public ItemCleaver(ToolMaterial material, String name, CreativeTabs tab)
	{
		this(material, name, tab, null);
	}

	public ItemCleaver(ToolMaterial material, String name, CreativeTabs tab, TextFormatting format)
	{
		super(material);
		setRegistryName(name);
		setUnlocalizedName(name);
		setCreativeTab(tab);
	}

	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand)
	{
		try
		{
			if (!player.world.isRemote && target.isEntityAlive() && target instanceof EntityFriendlyCreature)
			{
				EntityFriendlyCreature mob = (EntityFriendlyCreature)target;
				if (!mob.isChild() && mob.canBeButchered())
				{
					player.swingArm(hand);
					this.hitEntity(stack, mob, player);
					mob.cleave((int)this.getAttackDamage() + (net.minecraftforge.common.ForgeHooks.getLootingLevel(target, player, DamageSource.causePlayerDamage(player)) * 3) + 3, DamageSource.causePlayerDamage(player));
				}
				return true;
			}
			if (target.isEntityAlive() && target instanceof EntityAgeable && target instanceof EntityAnimal && !(target instanceof EntityTameable))
			{
				EntityAgeable mob = (EntityAgeable)target;
				if (!mob.isChild() && !((EntityAnimal)mob).isInLove() && mob.createChild(mob) != null && mob.createChild(mob).getClass() == mob.getClass())
				{
					player.swingArm(hand);
					mob.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
					this.hitEntity(stack, mob, player);
					mob.attackEntityFrom(DamageSource.causePlayerDamage(player), mob.getMaxHealth());
					mob.setNoAI(true);
					for (int ai = 0; ai <= (int)this.getAttackDamage() + net.minecraftforge.common.ForgeHooks.getLootingLevel(target, player, DamageSource.causePlayerDamage(player)) * 3 + 3; ++ai)
					{
						EntityAgeable addon = (EntityAgeable) mob.createChild(mob);
						addon.copyLocationAndAnglesFrom(mob);
						addon.renderYawOffset = addon.rotationYaw = addon.rotationYawHead = mob.rotationYawHead;
						mob.world.spawnEntity(addon);
						addon.setNoAI(true);
						addon.onDeath(DamageSource.causePlayerDamage(player));
						addon.setDead();
					}
					mob.onDeath(DamageSource.causePlayerDamage(player));
					mob.setHealth(0F);
				}
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		stack.damageItem(1, attacker);
		target.playSound(ESound.slashflesh, 1.0F, (target.getRNG().nextFloat() - target.getRNG().nextFloat()) * 0.2F + 1.0F);
		if (!target.isEntityAlive() && !target.world.isRemote)
		{
			if (target.getClass() == net.minecraft.entity.monster.EntitySkeleton.class)
			{
				EntityItem entityitem = new EntityItem(target.world, target.posX, target.posY + (double)target.getEyeHeight(), target.posZ, new ItemStack(Items.SKULL, 1, 0));
				entityitem.setDefaultPickupDelay();
				entityitem.setNoDespawn();
				++entityitem.motionY;
				target.world.spawnEntity(entityitem);
			}
			if (target.getClass() == net.minecraft.entity.monster.EntityWitherSkeleton.class)
			{
				EntityItem entityitem = new EntityItem(target.world, target.posX, target.posY + (double)target.getEyeHeight(), target.posZ, new ItemStack(Items.SKULL, 1, 1));
				entityitem.setDefaultPickupDelay();
				entityitem.setNoDespawn();
				++entityitem.motionY;
				target.world.spawnEntity(entityitem);
			}
			if (target.getClass() == net.minecraft.entity.monster.EntityZombie.class)
			{
				EntityItem entityitem = new EntityItem(target.world, target.posX, target.posY + (double)target.getEyeHeight(), target.posZ, new ItemStack(Items.SKULL, 1, 2));
				entityitem.setDefaultPickupDelay();
				entityitem.setNoDespawn();
				++entityitem.motionY;
				target.world.spawnEntity(entityitem);
			}
			if (target.getClass() == net.minecraft.entity.player.EntityPlayer.class)
			{
				EntityItem entityitem = new EntityItem(target.world, target.posX, target.posY + (double)target.getEyeHeight(), target.posZ, new ItemStack(Items.SKULL, 1, 3));
				entityitem.setDefaultPickupDelay();
				entityitem.setNoDespawn();
				++entityitem.motionY;
				target.world.spawnEntity(entityitem);
			}
			if (target.getClass() == net.minecraft.entity.monster.EntityCreeper.class)
			{
				EntityItem entityitem = new EntityItem(target.world, target.posX, target.posY + (double)target.getEyeHeight(), target.posZ, new ItemStack(Items.SKULL, 1, 4));
				entityitem.setDefaultPickupDelay();
				entityitem.setNoDespawn();
				++entityitem.motionY;
				target.world.spawnEntity(entityitem);
			}
			if (target.getClass() == net.minecraft.entity.boss.EntityWither.class)
			{
				float f = MathHelper.cos((target.renderYawOffset + (float)(180 * (1 - 1))) * 0.017453292F);
				float f1 = MathHelper.cos((target.renderYawOffset + (float)(180 * (2 - 1))) * 0.017453292F);
				float f2 = MathHelper.sin((target.renderYawOffset + (float)(180 * (1 - 1))) * 0.017453292F);
				float f3 = MathHelper.sin((target.renderYawOffset + (float)(180 * (2 - 1))) * 0.017453292F);
				
				EntityItem entityitem = new EntityItem(target.world, target.posX, target.posY + 3.0D, target.posZ, new ItemStack(Items.SKULL, 1, 1));
				entityitem.setDefaultPickupDelay();
				entityitem.setNoDespawn();
				++entityitem.motionY;
				target.world.spawnEntity(entityitem);
				EntityItem entityitem1 = new EntityItem(target.world, target.posX + (double)f * 1.3D, target.posY + 2.2D, target.posZ + (double)f2 * 1.3D, new ItemStack(Items.SKULL, 1, 1));
				entityitem1.setDefaultPickupDelay();
				entityitem1.setNoDespawn();
				++entityitem1.motionY;
				target.world.spawnEntity(entityitem1);
				EntityItem entityitem11 = new EntityItem(target.world, target.posX + (double)f1 * 1.3D, target.posY + 2.2D, target.posZ + (double)f3 * 1.3D, new ItemStack(Items.SKULL, 1, 1));
				entityitem11.setDefaultPickupDelay();
				entityitem11.setNoDespawn();
				++entityitem11.motionY;
				target.world.spawnEntity(entityitem11);
			}
		}
		return true;
	}
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("Gain extra animal drops and chop off any head");
		tooltip.add(TextFormatting.GOLD + "Right click to butcher animals");
	}
}