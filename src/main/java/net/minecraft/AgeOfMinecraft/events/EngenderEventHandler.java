package net.minecraft.AgeOfMinecraft.events;

import java.util.List;

import net.endermanofdoom.mac.internal.music.MusicManager;
import net.endermanofdoom.mac.util.ReflectionUtil;
import net.minecraft.AgeOfMinecraft.EngenderConfig;
import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EntityManaOrb;
import net.minecraft.AgeOfMinecraft.entity.cameos.Darkness.EntityDarkness;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityBat;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityChicken;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityCow;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityMooshroom;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityOcelot;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityRabbit;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntitySheep;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityEndermite;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityLlama;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntitySilverfish;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntitySnowman;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntitySquid;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityVillager;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityWolf;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityCreeper;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityMagmaCube;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySkeleton;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySlime;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySpider;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityVex;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityZombie;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityBlaze;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityCaveSpider;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityCreeder;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityEnderman;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityGhast;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityGuardian;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityPigZombie;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityShulker;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityVindicator;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityWitch;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityElderGuardian;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEvoker;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityGiant;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityIronGolem;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityWither;
import net.minecraft.AgeOfMinecraft.items.ItemFusion;
import net.minecraft.AgeOfMinecraft.items.ItemTierItem;
import net.minecraft.AgeOfMinecraft.nexudium.NexudiumClient;
import net.minecraft.AgeOfMinecraft.nexudium.NexudiumServer;
import net.minecraft.AgeOfMinecraft.registry.EEffect;
import net.minecraft.AgeOfMinecraft.registry.EItem;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.AgeOfMinecraft.registry.ETextures;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.event.world.WorldEvent.Save;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class EngenderEventHandler
{
	public static final EngenderEventHandler INSTANCE = new EngenderEventHandler();
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerIcons(TextureStitchEvent.Pre event)
	{	
		ETextures.init(event);	
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onClientTick(ClientTickEvent event)
	{
		if (event.phase == Phase.START)
			NexudiumClient.onTick();
	}
	
	@SubscribeEvent
	public void onServerTick(ServerTickEvent event)
	{
		if (event.phase == Phase.START)
			NexudiumServer.onTick();
	}
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event)
	{
		if (event.player instanceof EntityPlayerMP)
			NexudiumServer.onPlayerLogin((EntityPlayerMP) event.player);
	}
	
	@SubscribeEvent
	public void onWorldSave(Save event)
	{
		if (!event.getWorld().isRemote)
			NexudiumServer.saveData();
	}
	
	@SubscribeEvent
	public void lootLoad(LootTableLoadEvent event)
	{
		LootPool main = event.getTable().getPool("main");
		ResourceLocation location = event.getName();
		
		if (main != null)
			if(location.equals(LootTableList.CHESTS_SPAWN_BONUS_CHEST))
				main.addEntry(new LootEntryItem(EItem.convertingStaff, 5, 0, new LootFunction[0], new LootCondition[0], EngenderMod.MODID + ":convertingstaff"));
			else if(location.equals(LootTableList.CHESTS_SIMPLE_DUNGEON))
			{
				main.addEntry(new LootEntryItem(EItem.zombieItem, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 2))}, new LootCondition[0], EngenderMod.MODID + ":zombie"));
				main.addEntry(new LootEntryItem(EItem.skeletonItem, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 2))}, new LootCondition[0], EngenderMod.MODID + ":skeleton"));
				main.addEntry(new LootEntryItem(EItem.spiderItem, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 3))}, new LootCondition[0], EngenderMod.MODID + ":spider"));
			}
			else if(location.equals(LootTableList.CHESTS_DESERT_PYRAMID))
				main.addEntry(new LootEntryItem(EItem.huskItem, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 2))}, new LootCondition[0], EngenderMod.MODID + ":husk"));
			else if(location.equals(LootTableList.CHESTS_IGLOO_CHEST))
				main.addEntry(new LootEntryItem(EItem.strayItem, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 2))}, new LootCondition[0], EngenderMod.MODID + ":stray"));
			else if(location.equals(LootTableList.CHESTS_ABANDONED_MINESHAFT))
				main.addEntry(new LootEntryItem(EItem.cavespiderItem, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(2, 8))}, new LootCondition[0], EngenderMod.MODID + ":cavespider"));
			else if(location.equals(LootTableList.CHESTS_VILLAGE_BLACKSMITH))
			{
				main.addEntry(new LootEntryItem(EItem.villagerItem, 10, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 4))}, new LootCondition[0], EngenderMod.MODID + ":villager"));
				main.addEntry(new LootEntryItem(EItem.villagergolemItem, 1, 0, new LootFunction[0], new LootCondition[0], EngenderMod.MODID + ":irongolem"));
			}
			else if(location.equals(LootTableList.CHESTS_STRONGHOLD_CORRIDOR) || event.getName().equals(LootTableList.CHESTS_STRONGHOLD_CROSSING) || event.getName().equals(LootTableList.CHESTS_STRONGHOLD_LIBRARY))
			{
				main.addEntry(new LootEntryItem(EItem.silverfishItem, 10, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 4))}, new LootCondition[0], EngenderMod.MODID + ":silverfish"));
				main.addEntry(new LootEntryItem(EItem.spiderItem, 10, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 2))}, new LootCondition[0], EngenderMod.MODID + ":spider"));
				main.addEntry(new LootEntryItem(EItem.endermanItem, 1, 0, new LootFunction[0], new LootCondition[0], EngenderMod.MODID + ":endermanItem"));
			}
			else if(location.equals(LootTableList.CHESTS_JUNGLE_TEMPLE))
			{
				main.addEntry(new LootEntryItem(EItem.zombieItem, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 2))}, new LootCondition[0], EngenderMod.MODID + ":zombie"));
				main.addEntry(new LootEntryItem(EItem.ozelotItem, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(2, 8))}, new LootCondition[0], EngenderMod.MODID + ":ozelot"));
			}
			else if(location.equals(LootTableList.CHESTS_WOODLAND_MANSION))
			{
				main.addEntry(new LootEntryItem(EItem.vindicatorItem, 5, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 2))}, new LootCondition[0], EngenderMod.MODID + ":vindicator"));
				main.addEntry(new LootEntryItem(EItem.vexItem, 10, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(2, 8))}, new LootCondition[0], EngenderMod.MODID + ":vex"));
				main.addEntry(new LootEntryItem(EItem.evokerItem, 1, 0, new LootFunction[0], new LootCondition[0], EngenderMod.MODID + ":evoker"));
			}
			else if(location.equals(LootTableList.CHESTS_END_CITY_TREASURE))
			{
				main.addEntry(new LootEntryItem(EItem.shulkerItem, 5, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 2))}, new LootCondition[0], EngenderMod.MODID + ":shulker"));
				main.addEntry(new LootEntryItem(EItem.endermiteItem, 10, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(2, 8))}, new LootCondition[0], EngenderMod.MODID + ":endermite"));
				main.addEntry(new LootEntryItem(EItem.endermanItem, 5, 0, new LootFunction[0], new LootCondition[0], EngenderMod.MODID + ":enderman"));
			}
	}
	
	@SubscribeEvent
	public void onPotionAdded(PotionAddedEvent event)
	{
		if (event.getEntity() instanceof EntityLivingBase)
		{
			EntityLivingBase entity = (EntityLivingBase)event.getEntity();
			
			if (EngenderMod.doesntHaveTimeToBleed(entity) && event.getPotionEffect().getPotion().equals(EEffect.BLEEDING))
				entity.removeActivePotionEffect(event.getPotionEffect().getPotion());
		}
	}
	
	@SubscribeEvent
	public void onLivingEvent(LivingUpdateEvent event)
	{
		Entity entity = event.getEntity();
		
		if (entity instanceof EntityPlayer)
			((EntityPlayer)event.getEntity()).xpCooldown = 0;
		else if (event.getEntity() instanceof net.minecraft.entity.monster.EntityMagmaCube)
		{
			if (event.getEntity().isWet())
				event.getEntity().attackEntityFrom((new DamageSource("cooler")).setDamageBypassesArmor().setDamageIsAbsolute().setDifficultyScaled(), 1F);
		}
		else if (entity instanceof EntityDragon)
		{
			EntityDragon dragon = (EntityDragon)event.getEntity();
			
			if (!dragon.world.isRemote && EngenderConfig.general.dragonEgg && dragon.getFightManager() != null && dragon.getFightManager().hasPreviouslyKilledDragon())
			{
				ReflectionUtil.set(DragonFightManager.class, dragon.getFightManager(), "previouslyKilled", "field_186118_l", false);
				if (EngenderConfig.general.useMessage)
					for (EntityPlayer entityplayer : dragon.world.playerEntities)
						entityplayer.sendStatusMessage(new TextComponentTranslation(TextFormatting.BOLD + "The respawned dragon will drop another egg now."), true);
			}
		}
	}
	
	public static void playOnHitSound(DamageSource attacktype, Entity entity, float damage)
	{
		if (attacktype.getDamageType() != "yell")
			if (EngenderMod.isWoodLikeMob(entity))
			{
				if (attacktype.isProjectile() && attacktype.getDamageType() != "fireball")
					entity.playSound(ESound.woodHitPierce, 2F, 1.0F);
				else if (damage >= 6.0F || attacktype.isExplosion() || attacktype.isDamageAbsolute() || attacktype.isUnblockable() || attacktype == DamageSource.ANVIL || attacktype.canHarmInCreative() || (attacktype.isMagicDamage()) || attacktype == DamageSource.LAVA)
					entity.playSound(ESound.woodHitCrush, 2F, 1.0F);
				else
					entity.playSound(ESound.woodHit, 2F, 1.0F);
			}
			else if (EngenderMod.isMetalLikeMob(entity))
			{
				if (attacktype.isProjectile() && attacktype.getDamageType() != "fireball")
					entity.playSound(ESound.metalHitPierce, 2F, 1.0F);
				else if (damage >= 6.0F || attacktype.isExplosion() || attacktype.isDamageAbsolute() || attacktype.isUnblockable() || attacktype == DamageSource.ANVIL || attacktype.canHarmInCreative() || (attacktype.isMagicDamage()) || attacktype == DamageSource.LAVA)
					entity.playSound(ESound.metalHitCrush, 2F, 1.0F);
				else
					entity.playSound(ESound.metalHit, 2F, 1.0F);
			}
			else
			{
				if (attacktype.isProjectile() && attacktype.getDamageType() != "fireball")
					entity.playSound(ESound.fleshHitPierce, 2F, 1.0F);
				else if (damage >= 6.0F || attacktype.isExplosion() || attacktype.isDamageAbsolute() || attacktype.isUnblockable() || attacktype == DamageSource.ANVIL || attacktype.canHarmInCreative() || (attacktype.isMagicDamage()) || attacktype == DamageSource.LAVA)
				{
				if (entity.height >= 5.0F)
					entity.playSound(ESound.fleshHitCrushHeavy, 2F, 1.0F);
				else
					entity.playSound(ESound.fleshHitCrush, 2F, 1.0F);
				}
				else
					entity.playSound(ESound.fleshHit, 2F, 1.0F);
			}
	}
	
	@SubscribeEvent
	public void onMobHitEvent(LivingHurtEvent event)
	{
		DamageSource source = event.getSource();
		EntityLivingBase victim = event.getEntityLiving();
		Entity attacker = source.getTrueSource();
		float damage = event.getAmount();
		
		if (source.equals(DamageSource.LIGHTNING_BOLT) && victim instanceof net.minecraft.entity.monster.EntityCreeper)
		{
			event.setCanceled(true);
			return;
		}
		
		if (attacker instanceof EntityFriendlyCreature && (victim instanceof EntityPlayer || victim instanceof EntityFriendlyCreature) && attacker.isOnSameTeam(victim) && !victim.world.getGameRules().getBoolean("friendlyFire"))
		{
			event.setCanceled(true);
			return;
		}
		
		
		if (EngenderConfig.general.useBleeding && !source.isMagicDamage() && !source.isFireDamage() && !EngenderMod.doesntHaveTimeToBleed(victim))
			if ((damage > 1.5F || source.isProjectile() || source.isExplosion()) && victim.getRNG().nextFloat() >= 0.3F)
			{
				int level = damage > 10 ? 3 : damage > 6 ? 2 : damage > 4 ? 1 : 0;
				
				if (source.isProjectile() || source.isExplosion())
					damage += damage > 15 ? 3 : damage > 8 ? 2 : damage > 3 ? 1 : 0;
				
					if (victim.world.isRemote)
						victim.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, true, victim.posX + (victim.getRNG().nextFloat() - 0.5D) * victim.width, victim.posY + (victim.getRNG().nextFloat()* victim.height), victim.posZ + (victim.getRNG().nextFloat() - 0.5D) * victim.width, 4.0D * (victim.getRNG().nextFloat() - 0.5D), 0.5D, (victim.getRNG().nextFloat() - 0.5D) * 4.0D, new int[] { Block.getStateId(Blocks.REDSTONE_BLOCK.getDefaultState()) });
				victim.addPotionEffect(new PotionEffect(EEffect.BLEEDING, (int)(damage * (60 + (damage * level))), level, false, false));
			}
		
		if (attacker instanceof EntityLivingBase)
		{
			EntityLivingBase entity = (EntityLivingBase) attacker;
			
			if (entity.isChild())
				damage *= 2.0F;
			
			if (attacker instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)attacker).getOwner() != null)
			{
				ReflectionUtil.set(EntityLivingBase.class, entity, "recentlyHit", "field_70718_bc", 100);
				entity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) ((EntityFriendlyCreature)event.getSource().getTrueSource()).getOwner()), 1);
			}
		}
		
		if(EngenderConfig.mobs.useHitSounds && !event.isCanceled())
			playOnHitSound(source, victim, damage);
		
		if (!victim.isEntityAlive())
		{
			event.setCanceled(true);
			return;
		}
		
		event.setAmount(damage);
	}
	
	@SubscribeEvent
	public void onMobDeathEvent(LivingDeathEvent event)
	{
		EntityLivingBase victim = event.getEntityLiving();
		
		if (!victim.world.isRemote)
		{
			DamageSource source = event.getSource();	
			Entity attacker = source.getTrueSource();
			
			//Unknown
			if (victim instanceof EntityPlayer && attacker instanceof EntityLivingBase)
				((EntityLivingBase)attacker).onKillEntity(victim);
			
			//Friendly engender kills
			if (attacker instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)attacker).getOwner() instanceof EntityPlayer)
				((EntityPlayer) ((EntityFriendlyCreature)attacker).getOwner()).awardKillScore(victim, (int) ((EntityLivingBase) victim).getMaxHealth(), source);
			
			//Skull Drops
			if (attacker instanceof net.minecraft.AgeOfMinecraft.entity.tier3.EntityCreeper && ((net.minecraft.AgeOfMinecraft.entity.tier3.EntityCreeper)attacker).getPowered() || attacker instanceof EntityCreeder && ((EntityCreeder)attacker).getPowered())
			{
				if (victim instanceof net.minecraft.entity.monster.EntitySkeleton)
					victim.entityDropItem(new ItemStack(Items.SKULL, 1, 0), 0.0F);
				else if (victim instanceof net.minecraft.entity.monster.EntityWitherSkeleton)
					victim.entityDropItem(new ItemStack(Items.SKULL, 1, 1), 0.0F);
				else if (victim instanceof net.minecraft.entity.monster.EntityZombie && !(victim instanceof net.minecraft.entity.monster.EntityPigZombie))
					victim.entityDropItem(new ItemStack(Items.SKULL, 1, 2), 0.0F);
				else if (victim instanceof net.minecraft.entity.monster.EntityCreeper)
					victim.entityDropItem(new ItemStack(Items.SKULL, 1, 4), 0.0F);
				else if (victim instanceof net.minecraft.entity.boss.EntityDragon)
					victim.entityDropItem(new ItemStack(Items.SKULL, 1, 5), 0.0F);
			}
			
			//Drop Mana
			if (EngenderConfig.general.mana && attacker != null)
			{
				EntityPlayer player = victim.world.getClosestPlayerToEntity(victim, EngenderConfig.general.manaDistance);
				if (player != null && victim.world.getGameRules().getBoolean("doMobLoot") && !victim.isOnSameTeam(attacker))
				{
					float maxHealth = victim.getMaxHealth();
					int i = victim instanceof EntityAgeable ? (int) (maxHealth * 0.25F) : (int)maxHealth, j;
					
					while (i > 0)
					{
						j = EntityXPOrb.getXPSplit(i);
						i -= j;
						victim.world.spawnEntity(new EntityManaOrb(victim.world, victim.posX, victim.posY + victim.getEyeHeight(), victim.posZ, j, false));
					}
					
					if (maxHealth >= 100)
					{
						i = (int)(maxHealth * 0.1F);
						
						while (i > 0)
						{
							j = EntityXPOrb.getXPSplit(i);
							i -= j;
							victim.world.spawnEntity(new EntityManaOrb(victim.world, victim.posX, victim.posY + victim.getEyeHeight(), victim.posZ, j, true));
						}	
					}
				}
			}
			
			if (victim instanceof net.minecraft.entity.boss.EntityWither)
			{
				if ( victim.world.getGameRules().getBoolean("doMobLoot"))
				{
					int i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(victim, victim.world.getClosestPlayerToEntity(victim, -1D), 200);
					
					while (i > 0)
					{
						int j = EntityXPOrb.getXPSplit(i);
						i -= j;
						victim.world.spawnEntity(new EntityXPOrb(victim.world, victim.posX, victim.posY, victim.posZ, j));
					}
				}
			}
			
			//TODO: Spawn Darkness here
			if (victim instanceof net.minecraft.entity.boss.EntityDragon)
			{
				boolean spawn = !EntityDarkness.hardmode;
				List<Entity> entities = victim.world.loadedEntityList;
				
				for(Entity entity : entities)
					if((entity instanceof EntityEnderDragon || entity instanceof EntityDarkness) && entity.isEntityAlive())
					{
						spawn = false;
						break;
					}
				
				if (spawn)
				{
					EntityDarkness entity = new EntityDarkness(victim.world);
					entity.setPosition(victim.posX, 250D, victim.posZ + 100.0D);
					entity.renderYawOffset = entity.renderYawOffset;
					entity.rotationYaw = entity.rotationYaw;
					entity.rotationYawHead = entity.rotationYawHead;
					entity.rotationPitch = entity.rotationPitch;
					entity.onInitialSpawn(victim.world.getDifficultyForLocation(victim.getPosition()), null);
					victim.world.spawnEntity(entity);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onMobSpawnEvent(EntityJoinWorldEvent event)
	{
		Entity entity = event.getEntity();
		EntityLivingBase entityLiving = entity instanceof EntityLivingBase ? (EntityLivingBase) entity : null;
		
		if (!entity.world.isRemote)
		{
			//Natural Spawning Mobs
			if (EngenderConfig.mobs.naturalSpawns && entityLiving != null && entityLiving.getRNG().nextInt(entityLiving.isNonBoss() ? 15 : 2) == 0)
				EngenderEventHandler.changeMob(entity.world, entity.getPosition(), (EntityLivingBase)entity);
			
			//Setting Gamerules
			if (!entity.world.getGameRules().hasRule("friendlyFire"))
				entity.world.getGameRules().addGameRule("friendlyFire", "false", GameRules.ValueType.BOOLEAN_VALUE);
			if (!entity.world.getGameRules().hasRule("disableExpItemDrops"))
				entity.world.getGameRules().addGameRule("disableExpItemDrops", "false", GameRules.ValueType.BOOLEAN_VALUE);
			if (!entity.world.getGameRules().hasRule("disableCorpses"))
				entity.world.getGameRules().addGameRule("disableCorpses", "false", GameRules.ValueType.BOOLEAN_VALUE);
			
			if (entityLiving != null)
				NexudiumServer.onEntitySpawned(entityLiving);
		}
		else
		{
			if (entity instanceof EntityDragon)
				MusicManager.playMusic(entityLiving, SoundEvents.MUSIC_DRAGON, 3);
		}
		
		if (entity instanceof EntityLiving)
		{
			EntityLiving living = (EntityLiving)entity;
			
			if (entity instanceof IMob)
			{
				if (living instanceof EntityCreature)
				{
				EntityCreature cri = (EntityCreature)entity;
				
				cri.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityVillager>(cri, EntityVillager.class, false));
				}
				else
				{

				living.targetTasks.addTask(3, new EntityAIFindEntityNearest(living, EntityVillager.class));
				}
			}
			
			}
		
		if (entity instanceof EntityMob)
		{
			EntityMob mob = (EntityMob)entity;			
			mob.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityFriendlyCreature>(mob, EntityFriendlyCreature.class, 0, false, false, ent -> ent.isEntityAlive() && !ent.isOnSameTeam(mob)));
			mob.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityVillager>(mob, EntityVillager.class, true));
		}
		
		if (entity instanceof net.minecraft.entity.monster.EntityIronGolem)
		{
			net.minecraft.entity.monster.EntityIronGolem golems = (net.minecraft.entity.monster.EntityIronGolem)entity;			
			golems.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityLivingBase>(golems, EntityLivingBase.class, 0, false, false, ent -> IMob.MOB_SELECTOR.apply(ent)));
		}
		else if (entity instanceof net.minecraft.entity.passive.EntityVillager)
		{
			net.minecraft.entity.passive.EntityVillager testificate = (net.minecraft.entity.passive.EntityVillager)entity;
			
			testificate.tasks.addTask(1, new EntityAIAvoidEntity<EntityLivingBase>(testificate, EntityLivingBase.class, ent -> ent.isEntityAlive() && ent instanceof IMob, 8.0F, 0.6D, 0.6D));
		}
		else if (entity instanceof EntityItem)
		{
			EntityItem itemEntity = (EntityItem)entity;
			Item item = itemEntity.getItem().getItem();
			
			if (item instanceof ItemTierItem || item instanceof ItemFusion)
				itemEntity.setNoDespawn();
			
			if (item == EItem.witheredNetherStar ||
			item == EItem.witherStormItem || item == EItem.fusionItemWitherStorm ||
			item == EItem.jzaharItem || item == EItem.fusionItemJzahar || item == EItem.chaosGuardianItem ||
			item == EItem.fusionItemChaosGuardian || item == Item.getItemFromBlock(Blocks.COMMAND_BLOCK) ||
			item == Item.getItemFromBlock(Blocks.CHAIN_COMMAND_BLOCK) || item == Item.getItemFromBlock(Blocks.REPEATING_COMMAND_BLOCK) ||
			item == Item.getItemFromBlock(Blocks.BARRIER) || item == Item.getItemFromBlock(Blocks.BEDROCK) ||
			item == Item.getItemFromBlock(Blocks.STRUCTURE_BLOCK) || item == Item.getItemFromBlock(Blocks.STRUCTURE_VOID) ||
			item == Item.getItemFromBlock(Blocks.DRAGON_EGG))
				itemEntity.setEntityInvulnerable(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void changeMob(World world, BlockPos pos, EntityLivingBase mob)
	{
		if (mob instanceof net.minecraft.entity.passive.EntityBat)
		{
			EntityBat newmob = new EntityBat(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.passive.EntityChicken)
		{
			EntityChicken newmob = new EntityChicken(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.passive.EntityCow)
		{
			if (mob instanceof net.minecraft.entity.passive.EntityMooshroom)
			{
				EntityMooshroom newmob = new EntityMooshroom(world);
				newmob.copyLocationAndAnglesFrom(mob);
				newmob.renderYawOffset = mob.renderYawOffset;
				newmob.rotationYaw = mob.rotationYaw;
				newmob.rotationYawHead = mob.rotationYawHead;
				newmob.rotationPitch = mob.rotationPitch;
				newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
				newmob.setChild(mob.isChild());
				newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
				world.removeEntity(mob);
				world.spawnEntity(newmob);
				world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
			}
			else
			{
				EntityCow newmob = new EntityCow(world);
				newmob.copyLocationAndAnglesFrom(mob);
				newmob.renderYawOffset = mob.renderYawOffset;
				newmob.rotationYaw = mob.rotationYaw;
				newmob.rotationYawHead = mob.rotationYawHead;
				newmob.rotationPitch = mob.rotationPitch;
				newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
				newmob.setChild(mob.isChild());
				newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
				world.removeEntity(mob);
				world.spawnEntity(newmob);
				world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
			}
		}
		else if (mob instanceof net.minecraft.entity.passive.EntityOcelot)
		{
			EntityOcelot newmob = new EntityOcelot(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.passive.EntityRabbit)
		{
			EntityRabbit newmob = new EntityRabbit(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			newmob.setRabbitType(((net.minecraft.entity.passive.EntityRabbit)mob).getRabbitType());
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.passive.EntitySheep)
		{
			EntitySheep newmob = new EntitySheep(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			newmob.setFleeceColor(((net.minecraft.entity.passive.EntitySheep)mob).getFleeceColor());
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.monster.EntityEndermite)
		{
			EntityEndermite newmob = new EntityEndermite(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.passive.EntityLlama)
		{
			EntityLlama newmob = new EntityLlama(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			newmob.setVariant(((net.minecraft.entity.passive.EntityLlama)mob).getVariant());
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.monster.EntitySilverfish)
		{
			EntitySilverfish newmob = new EntitySilverfish(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.monster.EntitySnowman)
		{
			EntitySnowman newmob = new EntitySnowman(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.passive.EntitySquid)
		{
			EntitySquid newmob = new EntitySquid(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.passive.EntityVillager)
		{
			EntityVillager newmob = new EntityVillager(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			newmob.setProfession(((net.minecraft.entity.passive.EntityVillager)mob).getProfession());
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.passive.EntityWolf)
		{
			EntityWolf newmob = new EntityWolf(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.monster.EntityCreeper)
		{
			EntityCreeper newmob = new EntityCreeper(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			newmob.setPowered(((net.minecraft.entity.monster.EntityCreeper)mob).getPowered());
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.monster.EntitySlime)
		{
			if (mob instanceof net.minecraft.entity.monster.EntityMagmaCube)
			{
				EntityMagmaCube newmob = new EntityMagmaCube(world);
				newmob.copyLocationAndAnglesFrom(mob);
				newmob.renderYawOffset = mob.renderYawOffset;
				newmob.rotationYaw = mob.rotationYaw;
				newmob.rotationYawHead = mob.rotationYawHead;
				newmob.rotationPitch = mob.rotationPitch;
				newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
				newmob.setSlimeSize(((net.minecraft.entity.monster.EntityMagmaCube)mob).getSlimeSize());
				newmob.setChild(mob.isChild());
				newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
				world.removeEntity(mob);
				world.spawnEntity(newmob);
				world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
			}
			else
			{
				EntitySlime newmob = new EntitySlime(world);
				newmob.copyLocationAndAnglesFrom(mob);
				newmob.renderYawOffset = mob.renderYawOffset;
				newmob.rotationYaw = mob.rotationYaw;
				newmob.rotationYawHead = mob.rotationYawHead;
				newmob.rotationPitch = mob.rotationPitch;
				newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
				newmob.setSlimeSize(((net.minecraft.entity.monster.EntitySlime)mob).getSlimeSize());
				newmob.setChild(mob.isChild());
				newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
				world.removeEntity(mob);
				world.spawnEntity(newmob);
				world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
			}
		}
		else if (mob instanceof net.minecraft.entity.monster.EntitySkeleton)
		{
			EntitySkeleton newmob = new EntitySkeleton(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			newmob.setSkeletonType(0);
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.monster.EntityWitherSkeleton)
		{
			EntitySkeleton newmob = new EntitySkeleton(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			newmob.setSkeletonType(1);
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.monster.EntityStray)
		{
			EntitySkeleton newmob = new EntitySkeleton(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			newmob.setSkeletonType(2);
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.monster.EntitySpider)
		{
			if (mob instanceof net.minecraft.entity.monster.EntityCaveSpider)
			{
				EntityCaveSpider newmob = new EntityCaveSpider(world);
				newmob.copyLocationAndAnglesFrom(mob);
				newmob.renderYawOffset = mob.renderYawOffset;
				newmob.rotationYaw = mob.rotationYaw;
				newmob.rotationYawHead = mob.rotationYawHead;
				newmob.rotationPitch = mob.rotationPitch;
				newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
				newmob.setChild(mob.isChild());
				newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
				world.removeEntity(mob);
				world.spawnEntity(newmob);
				world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
			}
			else
			{
				EntitySpider newmob = new EntitySpider(world);
				newmob.copyLocationAndAnglesFrom(mob);
				newmob.renderYawOffset = mob.renderYawOffset;
				newmob.rotationYaw = mob.rotationYaw;
				newmob.rotationYawHead = mob.rotationYawHead;
				newmob.rotationPitch = mob.rotationPitch;
				newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
				newmob.setChild(mob.isChild());
				newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
				world.removeEntity(mob);
				world.spawnEntity(newmob);
				world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
			}
		}
		else if (mob instanceof net.minecraft.entity.monster.EntityVex)
		{
		EntityVex newmob = new EntityVex(world);
		newmob.copyLocationAndAnglesFrom(mob);
		newmob.renderYawOffset = mob.renderYawOffset;
		newmob.rotationYaw = mob.rotationYaw;
		newmob.rotationYawHead = mob.rotationYawHead;
		newmob.rotationPitch = mob.rotationPitch;
		newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
		newmob.setChild(mob.isChild());
		newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
		world.removeEntity(mob);
		world.spawnEntity(newmob);
		world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.monster.EntityZombie)
		{
			if (mob instanceof net.minecraft.entity.monster.EntityPigZombie)
			{
				EntityPigZombie newmob = new EntityPigZombie(world);
				newmob.copyLocationAndAnglesFrom(mob);
				newmob.renderYawOffset = mob.renderYawOffset;
				newmob.rotationYaw = mob.rotationYaw;
				newmob.rotationYawHead = mob.rotationYawHead;
				newmob.rotationPitch = mob.rotationPitch;
				newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
				newmob.setChild(mob.isChild());
				newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
				world.removeEntity(mob);
				world.spawnEntity(newmob);
				world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
			}
			else if (mob instanceof net.minecraft.entity.monster.EntityZombieVillager)
			{
				EntityZombie newmob = new EntityZombie(world);
				newmob.copyLocationAndAnglesFrom(mob);
				newmob.renderYawOffset = mob.renderYawOffset;
				newmob.rotationYaw = mob.rotationYaw;
				newmob.rotationYawHead = mob.rotationYawHead;
				newmob.rotationPitch = mob.rotationPitch;
				newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
				newmob.setVillagerType(((net.minecraft.entity.monster.EntityZombieVillager)mob).getProfession());
				newmob.setChild(mob.isChild());
				newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
				world.removeEntity(mob);
				world.spawnEntity(newmob);
				world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
			}
			else
			{
				EntityZombie newmob = new EntityZombie(world);
				newmob.copyLocationAndAnglesFrom(mob);
				newmob.renderYawOffset = mob.renderYawOffset;
				newmob.rotationYaw = mob.rotationYaw;
				newmob.rotationYawHead = mob.rotationYawHead;
				newmob.rotationPitch = mob.rotationPitch;
				newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
				newmob.setChild(mob.isChild());
				newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
				newmob.setZombieType(mob instanceof EntityHusk ? 1 : 0);
				world.removeEntity(mob);
				world.spawnEntity(newmob);
				world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
			}
		}
		else if (mob instanceof net.minecraft.entity.monster.EntityBlaze)
		{
			EntityBlaze newmob = new EntityBlaze(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.monster.EntityEnderman)
		{
			EntityEnderman newmob = new EntityEnderman(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setHeldBlockState(((net.minecraft.entity.monster.EntityEnderman)mob).getHeldBlockState());
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.monster.EntityGhast)
		{
			EntityGhast newmob = new EntityGhast(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.monster.EntityGuardian)
		{
			EntityGuardian newmob = new EntityGuardian(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.monster.EntityShulker)
		{
			EntityShulker newmob = new EntityShulker(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.monster.EntityVindicator)
		{
			EntityVindicator newmob = new EntityVindicator(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.monster.EntityWitch)
		{
			EntityWitch newmob = new EntityWitch(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.monster.EntityElderGuardian)
		{
			EntityElderGuardian newmob = new EntityElderGuardian(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.monster.EntityEvoker)
		{
			EntityEvoker newmob = new EntityEvoker(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.monster.EntityGiantZombie)
		{
			EntityGiant newmob = new EntityGiant(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.monster.EntityIronGolem)
		{
			EntityIronGolem newmob = new EntityIronGolem(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setChild(mob.isChild());
			newmob.setGrowingAge(mob.isChild() ? -60000 : 0);
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
		else if (mob instanceof net.minecraft.entity.boss.EntityWither)
		{
			EntityWither newmob = new EntityWither(world);
			newmob.copyLocationAndAnglesFrom(mob);
			newmob.renderYawOffset = mob.renderYawOffset;
			newmob.rotationYaw = mob.rotationYaw;
			newmob.rotationYawHead = mob.rotationYawHead;
			newmob.rotationPitch = mob.rotationPitch;
			newmob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			newmob.setInvulTime(((net.minecraft.entity.boss.EntityWither)mob).getInvulTime());
			world.removeEntity(mob);
			world.spawnEntity(newmob);
			world.playEvent((EntityPlayer)null, 1027, newmob.getPosition(), 0);
		}
	}
}