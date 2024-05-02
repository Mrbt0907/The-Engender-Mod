package net.minecraft.AgeOfMinecraft.registry;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityChicken;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySpider;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.DungeonHooks;

public class SpawnerRegistry 
{
	public static final BiConsumer<EntityPlayer, Object[]> SPAWN_NORMAL = new BiConsumer<EntityPlayer, Object[]>()
	{
		@SuppressWarnings("unchecked")
		@Override
		public void accept(EntityPlayer player, Object[] args)
		{
			int amount = (int) args[0];
			BlockPos pos = (BlockPos) args[2];
			EntityFriendlyCreature entity;
			
			for (int i = 0; i < amount; i++)
			{
				try
				{
					entity = (EntityFriendlyCreature) ((Class<?>)args[1]).getConstructor(World.class).newInstance(player.world);
				}
				catch (Exception e)
				{
					player.sendMessage(new TextComponentString(TextFormatting.RED + "Failed to spawn engendered mob. See logs for details."));
					EngenderMod.error(e);
					return;
				}
				
				pos = pos.offset((EnumFacing) args[3]);
				entity.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, MathHelper.wrapDegrees(player.world.rand.nextFloat() * 360.0F), 0.0F);
				entity.rotationYawHead = entity.rotationYaw;
				entity.renderYawOffset = entity.rotationYaw;
				entity.onInitialSpawn(player.world.getDifficultyForLocation(new BlockPos(entity)), null);
				if (args[7] != null)
					((Consumer<EntityFriendlyCreature>)args[7]).accept(entity);
				player.world.spawnEntity(entity);
				
				if (!player.world.getGameRules().getBoolean("disableExpItemDrops") && !player.capabilities.isCreativeMode)
					for (int ii = entity.getSpawnEXP(); ii > 0;)
					{
						int j = EntityXPOrb.getXPSplit(ii);
						ii -= j;
						player.world.spawnEntity(new EntityXPOrb(player.world, entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ, j));
					}
				
				if (!(boolean)args[8])
					entity.setOwnerId(player.getUniqueID());
				entity.playLivingSound();
			}
		}
	};
	public static final BiConsumer<EntityPlayer, Object[]> SPAWN_JOCKEY = new BiConsumer<EntityPlayer, Object[]>()
	{
		@SuppressWarnings("unchecked")
		@Override
		public void accept(EntityPlayer player, Object[] args)
		{
			int amount = (int) args[0];
			BlockPos pos = (BlockPos) args[2];
			EntityFriendlyCreature entity;
			
			for (int i = 0; i < amount; i++)
			{
				try
				{
					entity = (EntityFriendlyCreature) ((Class<?>)args[1]).getConstructor(World.class).newInstance(player.world);
				}
				catch (Exception e)
				{
					player.sendMessage(new TextComponentString(TextFormatting.RED + "Failed to spawn engendered mob. See logs for details."));
					EngenderMod.error(e);
					return;
				}
				
				pos = pos.offset((EnumFacing) args[3]);
				entity.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, MathHelper.wrapDegrees(player.world.rand.nextFloat() * 360.0F), 0.0F);
				entity.rotationYawHead = entity.rotationYaw;
				entity.renderYawOffset = entity.rotationYaw;
				entity.onInitialSpawn(player.world.getDifficultyForLocation(new BlockPos(entity)), null);
				if (args[7] != null)
					((Consumer<EntityFriendlyCreature>)args[7]).accept(entity);
				player.world.spawnEntity(entity);
				
				if (!player.world.getGameRules().getBoolean("disableExpItemDrops") && !player.capabilities.isCreativeMode)
					for (int ii = entity.getSpawnEXP(); ii > 0;)
					{
						int j = EntityXPOrb.getXPSplit(ii);
						ii -= j;
						player.world.spawnEntity(new EntityXPOrb(player.world, entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ, j));
					}
				
				if (!(boolean)args[8])
					entity.setOwnerId(player.getUniqueID());
				entity.playLivingSound();
				
				EntitySpider entityMount = new EntitySpider(player.world);
				pos = pos.offset((EnumFacing) args[3]);
				entityMount.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, MathHelper.wrapDegrees(player.world.rand.nextFloat() * 360.0F), 0.0F);
				entityMount.rotationYawHead = entityMount.rotationYaw;
				entityMount.renderYawOffset = entityMount.rotationYaw;
				entityMount.onInitialSpawn(player.world.getDifficultyForLocation(new BlockPos(entityMount)), null);
				player.world.spawnEntity(entityMount);
				if (!(boolean)args[8])
					entityMount.setOwnerId(player.getUniqueID());
				entity.startRiding(entityMount, true);
			}
		}
	};
	public static final BiConsumer<EntityPlayer, Object[]> SPAWN_CHICKEN_JOCKEY = new BiConsumer<EntityPlayer, Object[]>()
	{
		@SuppressWarnings("unchecked")
		@Override
		public void accept(EntityPlayer player, Object[] args)
		{
			int amount = (int) args[0];
			BlockPos pos = (BlockPos) args[2];
			EntityFriendlyCreature entity;
			
			for (int i = 0; i < amount; i++)
			{
				try
				{
					entity = (EntityFriendlyCreature) ((Class<?>)args[1]).getConstructor(World.class).newInstance(player.world);
				}
				catch (Exception e)
				{
					player.sendMessage(new TextComponentString(TextFormatting.RED + "Failed to spawn engendered mob. See logs for details."));
					EngenderMod.error(e);
					return;
				}
				
				pos = pos.offset((EnumFacing) args[3]);
				entity.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, MathHelper.wrapDegrees(player.world.rand.nextFloat() * 360.0F), 0.0F);
				entity.rotationYawHead = entity.rotationYaw;
				entity.renderYawOffset = entity.rotationYaw;
				entity.onInitialSpawn(player.world.getDifficultyForLocation(new BlockPos(entity)), null);
				if (args[7] != null)
					((Consumer<EntityFriendlyCreature>)args[7]).accept(entity);
				player.world.spawnEntity(entity);
				
				if (!player.world.getGameRules().getBoolean("disableExpItemDrops") && !player.capabilities.isCreativeMode)
					for (int ii = entity.getSpawnEXP(); ii > 0;)
					{
						int j = EntityXPOrb.getXPSplit(ii);
						ii -= j;
						player.world.spawnEntity(new EntityXPOrb(player.world, entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ, j));
					}
				
				if (!(boolean)args[8])
					entity.setOwnerId(player.getUniqueID());
				entity.playLivingSound();
				
				EntityChicken entityMount = new EntityChicken(player.world);
				pos = pos.offset((EnumFacing) args[3]);
				entityMount.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, MathHelper.wrapDegrees(player.world.rand.nextFloat() * 360.0F), 0.0F);
				entityMount.rotationYawHead = entityMount.rotationYaw;
				entityMount.renderYawOffset = entityMount.rotationYaw;
				entityMount.onInitialSpawn(player.world.getDifficultyForLocation(new BlockPos(entityMount)), null);
				player.world.spawnEntity(entityMount);
				if (!(boolean)args[8])
					entityMount.setOwnerId(player.getUniqueID());
				entity.startRiding(entityMount, true);
			}
		}
	};
	public static final BiConsumer<EntityPlayer, Object[]> SPAWN_FOUR_HORSEMEN = new BiConsumer<EntityPlayer, Object[]>()
	{
		@SuppressWarnings("unchecked")
		@Override
		public void accept(EntityPlayer player, Object[] args)
		{
			int amount = (int) args[0] * 4;
			BlockPos pos = (BlockPos) args[2];
			EntityFriendlyCreature entity;
			
			for (int i = 0; i < amount; i++)
			{
				try
				{
					entity = (EntityFriendlyCreature) ((Class<?>)args[1]).getConstructor(World.class).newInstance(player.world);
				}
				catch (Exception e)
				{
					player.sendMessage(new TextComponentString(TextFormatting.RED + "Failed to spawn engendered mob. See logs for details."));
					EngenderMod.error(e);
					return;
				}
				
				pos = pos.offset((EnumFacing) args[3]);
				entity.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, MathHelper.wrapDegrees(player.world.rand.nextFloat() * 360.0F), 0.0F);
				entity.rotationYawHead = entity.rotationYaw;
				entity.renderYawOffset = entity.rotationYaw;
				entity.onInitialSpawn(player.world.getDifficultyForLocation(new BlockPos(entity)), null);
				if (args[7] != null)
					((Consumer<EntityFriendlyCreature>)args[7]).accept(entity);
				entity.hurtResistantTime = 200;
				entity.enablePersistence();
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
				EnchantmentHelper.addRandomEnchantment(entity.getRNG(), entity.getHeldItemMainhand(), 30, true);
				EnchantmentHelper.addRandomEnchantment(entity.getRNG(), entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD), 30, true);
				
				player.world.spawnEntity(entity);
				
				if (!player.world.getGameRules().getBoolean("disableExpItemDrops") && !player.capabilities.isCreativeMode)
					for (int ii = entity.getSpawnEXP(); ii > 0;)
					{
						int j = EntityXPOrb.getXPSplit(ii);
						ii -= j;
						player.world.spawnEntity(new EntityXPOrb(player.world, entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ, j));
					}
				
				if (!(boolean)args[8])
					entity.setOwnerId(player.getUniqueID());
				entity.playLivingSound();
				
				EntitySkeletonHorse entityMount = new EntitySkeletonHorse(player.world);
				pos = pos.offset((EnumFacing) args[3]);
				entityMount.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, MathHelper.wrapDegrees(player.world.rand.nextFloat() * 360.0F), 0.0F);
				entityMount.rotationYawHead = entityMount.rotationYaw;
				entityMount.renderYawOffset = entityMount.rotationYaw;
				entityMount.onInitialSpawn(player.world.getDifficultyForLocation(new BlockPos(entityMount)), null);

				entityMount.hurtResistantTime = 200;
				entityMount.enablePersistence();
				entityMount.setHorseTamed(true);
				entityMount.setGrowingAge(0);
				player.world.spawnEntity(entityMount);
				entityMount.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(53.0D);
				entityMount.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(12.0D);
				entityMount.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3375D);
				entityMount.addVelocity(entityMount.getRNG().nextGaussian() * 0.5D, 0.0D, entityMount.getRNG().nextGaussian() * 0.5D);
				entity.startRiding(entityMount, true);
			}
		}
	};
	
	public static void init()
	{
		DungeonHooks.addDungeonMob(new ResourceLocation("creeper"), 25);
		DungeonHooks.addDungeonMob(new ResourceLocation("cave_spider"), 50);
		DungeonHooks.addDungeonMob(new ResourceLocation("silverfish"), 50);
		DungeonHooks.addDungeonMob(new ResourceLocation("enderman"), 10);
		DungeonHooks.addDungeonMob(new ResourceLocation("wither_skeleton"), 1);
		DungeonHooks.addDungeonMob(new ResourceLocation("endermite"), 1);
		DungeonHooks.addDungeonMob(new ResourceLocation("blaze"), 1);
		DungeonHooks.addDungeonMob(new ResourceLocation("ageofminecraft","zombiehelpful"), 50);
		DungeonHooks.addDungeonMob(new ResourceLocation("ageofminecraft","skeletonhelpful"), 25);
		DungeonHooks.addDungeonMob(new ResourceLocation("ageofminecraft","spiderhelpful"), 25);
		DungeonHooks.addDungeonMob(new ResourceLocation("ageofminecraft","creeperhelpful"), 10);
		DungeonHooks.addDungeonMob(new ResourceLocation("ageofminecraft","silverfishhelpful"), 10);
		DungeonHooks.addDungeonMob(new ResourceLocation("ageofminecraft","endermanhelpful"), 1);
		DungeonHooks.addDungeonMob(new ResourceLocation("ageofminecraft","blazehelpful"), 10);
	}
}
