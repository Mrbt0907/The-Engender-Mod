package net.mrbt0907.ageofminecraft.events;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.items.ItemCommandingStaff;
import net.mrbt0907.ageofminecraft.items.capabilities.CapabilityCommandStaff;
import net.mrbt0907.ageofminecraft.network.PacketCommandStaff;
import net.mrbt0907.ageofminecraft.registry.TextureRegistry;

public class EngenderEventHandler
{
	public static final EngenderEventHandler INSTANCE = new EngenderEventHandler();

	@SideOnly(Side.CLIENT)
	private ItemStack staff;
	@SideOnly(Side.CLIENT)
	private CapabilityCommandStaff capability;
	@SideOnly(Side.CLIENT)
	private int slot = -1;
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerIcons(TextureStitchEvent.Pre event)
	{	
		TextureRegistry.init(event);	
	}
	
	@SubscribeEvent
	public void onSpawnEvent(EntityJoinWorldEvent event)
	{
		if (event.getEntity() instanceof EntityCreature)
		{
			EntityCreature entity = (EntityCreature) event.getEntity();
			for (EntityAITaskEntry task : entity.targetTasks.taskEntries)
				if (task.action.getClass().equals(EntityAINearestAttackableTarget.class) && ((EntityAINearestAttackableTarget<?>)task.action).targetClass.equals(EntityPlayer.class))
				{
					entity.targetTasks.addTask(task.priority, new EntityAINearestAttackableTarget<EntityEngendered>(entity, EntityEngendered.class, ((EntityAINearestAttackableTarget<?>)task.action).targetChance, ((EntityAITarget)task.action).shouldCheckSight, ((EntityAITarget)task.action).nearbyOnly, EntityEngendered.DONT_TARGET_WILD));
					break;
				}
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTickClient(ClientTickEvent event)
	{
		if (event.phase.equals(Phase.END))
		{
			net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
			if (mc.player != null)
			{
				ItemStack stack = mc.player.inventory.getCurrentItem();
				if (stack.getItem() instanceof ItemCommandingStaff)
				{
					staff = stack;
					slot = mc.player.inventory.currentItem;
				}
				else if (staff != null)
					if (slot > mc.player.inventory.currentItem)
					{
						if (capability == null && staff.hasCapability(CapabilityCommandStaff.Provider.INSTANCE, CapabilityCommandStaff.Provider.FACE))
						{
							capability = staff.getCapability(CapabilityCommandStaff.Provider.INSTANCE, CapabilityCommandStaff.Provider.FACE);
						}
							
						if (mc.player.isSneaking())
						{
							if (capability != null)
								capability.nextStance();
							PacketCommandStaff.changeStance(capability.getUnits(), capability.getStance());
							mc.player.inventory.currentItem = slot;
						}
						else
						{
							staff = null;
							slot = -1;
						}
					}
					else
					{
						if (mc.player.isSneaking())
						{
							if (capability != null)
								capability.nextStance(true);
							PacketCommandStaff.changeStance(capability.getUnits(), capability.getStance());
							mc.player.inventory.currentItem = slot;
						}
						else
						{
							staff = null;
							slot = -1;
						}
					}
			}
		}
	}
	
	/*@SubscribeEvent
	public void lootLoad(LootTableLoadEvent event)
	{
		LootPool main = event.getTable().getPool("main");
		ResourceLocation location = event.getName();
		
		if (main != null)
			if(location.equals(LootTableList.CHESTS_SPAWN_BONUS_CHEST))
				main.addEntry(new LootEntryItem(ItemRegistry.convertingStaff, 5, 0, new LootFunction[0], new LootCondition[0], EngenderMod.MODID + ":convertingstaff"));
			else if(location.equals(LootTableList.CHESTS_SIMPLE_DUNGEON))
			{
				main.addEntry(new LootEntryItem(ItemRegistry.zombieItem, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 2))}, new LootCondition[0], EngenderMod.MODID + ":zombie"));
				main.addEntry(new LootEntryItem(ItemRegistry.skeletonItem, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 2))}, new LootCondition[0], EngenderMod.MODID + ":skeleton"));
				main.addEntry(new LootEntryItem(ItemRegistry.spiderItem, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 3))}, new LootCondition[0], EngenderMod.MODID + ":spider"));
			}
			else if(location.equals(LootTableList.CHESTS_DESERT_PYRAMID))
				main.addEntry(new LootEntryItem(ItemRegistry.huskItem, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 2))}, new LootCondition[0], EngenderMod.MODID + ":husk"));
			else if(location.equals(LootTableList.CHESTS_IGLOO_CHEST))
				main.addEntry(new LootEntryItem(ItemRegistry.strayItem, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 2))}, new LootCondition[0], EngenderMod.MODID + ":stray"));
			else if(location.equals(LootTableList.CHESTS_ABANDONED_MINESHAFT))
				main.addEntry(new LootEntryItem(ItemRegistry.cavespiderItem, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(2, 8))}, new LootCondition[0], EngenderMod.MODID + ":cavespider"));
			else if(location.equals(LootTableList.CHESTS_VILLAGE_BLACKSMITH))
			{
				main.addEntry(new LootEntryItem(ItemRegistry.villagerItem, 10, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 4))}, new LootCondition[0], EngenderMod.MODID + ":villager"));
				main.addEntry(new LootEntryItem(ItemRegistry.villagergolemItem, 1, 0, new LootFunction[0], new LootCondition[0], EngenderMod.MODID + ":irongolem"));
			}
			else if(location.equals(LootTableList.CHESTS_STRONGHOLD_CORRIDOR) || event.getName().equals(LootTableList.CHESTS_STRONGHOLD_CROSSING) || event.getName().equals(LootTableList.CHESTS_STRONGHOLD_LIBRARY))
			{
				main.addEntry(new LootEntryItem(ItemRegistry.silverfishItem, 10, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 4))}, new LootCondition[0], EngenderMod.MODID + ":silverfish"));
				main.addEntry(new LootEntryItem(ItemRegistry.spiderItem, 10, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 2))}, new LootCondition[0], EngenderMod.MODID + ":spider"));
				main.addEntry(new LootEntryItem(ItemRegistry.endermanItem, 1, 0, new LootFunction[0], new LootCondition[0], EngenderMod.MODID + ":endermanItem"));
			}
			else if(location.equals(LootTableList.CHESTS_JUNGLE_TEMPLE))
			{
				main.addEntry(new LootEntryItem(ItemRegistry.zombieItem, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 2))}, new LootCondition[0], EngenderMod.MODID + ":zombie"));
				main.addEntry(new LootEntryItem(ItemRegistry.ozelotItem, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(2, 8))}, new LootCondition[0], EngenderMod.MODID + ":ozelot"));
			}
			else if(location.equals(LootTableList.CHESTS_WOODLAND_MANSION))
			{
				main.addEntry(new LootEntryItem(ItemRegistry.vindicatorItem, 5, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 2))}, new LootCondition[0], EngenderMod.MODID + ":vindicator"));
				main.addEntry(new LootEntryItem(ItemRegistry.vexItem, 10, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(2, 8))}, new LootCondition[0], EngenderMod.MODID + ":vex"));
				main.addEntry(new LootEntryItem(ItemRegistry.evokerItem, 1, 0, new LootFunction[0], new LootCondition[0], EngenderMod.MODID + ":evoker"));
			}
			else if(location.equals(LootTableList.CHESTS_END_CITY_TREASURE))
			{
				main.addEntry(new LootEntryItem(ItemRegistry.shulkerItem, 5, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 2))}, new LootCondition[0], EngenderMod.MODID + ":shulker"));
				main.addEntry(new LootEntryItem(ItemRegistry.endermiteItem, 10, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(2, 8))}, new LootCondition[0], EngenderMod.MODID + ":endermite"));
				main.addEntry(new LootEntryItem(ItemRegistry.endermanItem, 5, 0, new LootFunction[0], new LootCondition[0], EngenderMod.MODID + ":enderman"));
			}
	}*/
	
	/*@SubscribeEvent
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
				dragon.getFightManager().previouslyKilled = false;
				if (EngenderConfig.general.useMessage)
					for (EntityPlayer entityplayer : dragon.world.playerEntities)
						entityplayer.sendStatusMessage(new TextComponentTranslation(TextFormatting.BOLD + "The respawned dragon will drop another egg now."), true);
			}
		}
	}8/
	
	/*@SubscribeEvent
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

		if (attacker instanceof EntityLivingBase)
		{
			EntityLivingBase entity = (EntityLivingBase) attacker;
			
			if (entity.isChild())
				damage *= 2.0F;
			
			if (attacker instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)attacker).getOwner() != null)
			{
				((EntityLivingBase)attacker).recentlyHit = 100;
				entity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) ((EntityFriendlyCreature)event.getSource().getTrueSource()).getOwner()), 1);
			}
		}
		
		if (!victim.isEntityAlive())
		{
			event.setCanceled(true);
			return;
		}
		
		event.setAmount(damage);
	}*/
	
	/*@SubscribeEvent
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
			if (attacker instanceof net.mrbt0907.ageofminecraft.entity.tier3.EntityCreeper && ((net.mrbt0907.ageofminecraft.entity.tier3.EntityCreeper)attacker).getPowered() || attacker instanceof EntityCreeder && ((EntityCreeder)attacker).getPowered())
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
	}*/
	
	/*@SubscribeEvent
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

		else if (entity instanceof net.minecraft.entity.passive.EntityVillager)
		{
			net.minecraft.entity.passive.EntityVillager testificate = (net.minecraft.entity.passive.EntityVillager)entity;
			
			testificate.tasks.addTask(1, new EntityAIAvoidEntity<EntityLivingBase>(testificate, EntityLivingBase.class, ent -> ent.isEntityAlive() && ent instanceof IMob, 8.0F, 0.6D, 0.6D));
		}
		else if (entity instanceof EntityItem)
		{
			EntityItem itemEntity = (EntityItem)entity;
			Item item = itemEntity.getItem().getItem();
			
			if (item instanceof ItemFusionSpawner || item instanceof ItemFusion)
				itemEntity.setNoDespawn();
			
			if (item == ItemRegistry.witheredNetherStar ||
			item == ItemRegistry.witherStormItem || item == ItemRegistry.fusionItemWitherStorm ||
			item == ItemRegistry.jzaharItem || item == ItemRegistry.fusionItemJzahar || item == ItemRegistry.chaosGuardianItem ||
			item == ItemRegistry.fusionItemChaosGuardian || item == Item.getItemFromBlock(Blocks.COMMAND_BLOCK) ||
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
	}*/
}