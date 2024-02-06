package net.minecraft.AgeOfMinecraft.entity.tier4;
import java.util.Collection;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.endermanofdoom.mac.util.TranslateUtil;
import net.minecraft.AgeOfMinecraft.EngenderConfig;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Light;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIAvoidEntitySPC;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySpider;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityCreeder extends EntitySpider implements Light
{
	private static final DataParameter<Integer> STATE = EntityDataManager.createKey(EntityCreeder.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> POWERED = EntityDataManager.createKey(EntityCreeder.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> IGNITED = EntityDataManager.createKey(EntityCreeder.class, DataSerializers.BOOLEAN);
	private int lastActiveTime;
	private int timeSinceIgnited;
	private int fuseTime = 30;
	private int explosionRadius = 5;
	public EntityCreeder(World worldIn)
	{
		super(worldIn);
		this.tasks.addTask(0, new EntityAIAvoidEntitySPC(this, EntityOcelot.class, new Predicate<EntityOcelot>()
		{
			public boolean apply(EntityOcelot p_apply_1_)
			{
				return (p_apply_1_ != null) && p_apply_1_.isEntityAlive() && EntityCreeder.this.getIntelligence() < 10F;
			}
		}, 6.0F, 1.0D, 1.2D));
			this.tasks.addTask(0, new EntityAIAvoidEntitySPC(this, net.minecraft.AgeOfMinecraft.entity.tier1.EntityOcelot.class, new Predicate<net.minecraft.AgeOfMinecraft.entity.tier1.EntityOcelot>()
			{
				public boolean apply(net.minecraft.AgeOfMinecraft.entity.tier1.EntityOcelot p_apply_1_)
				{
					return (p_apply_1_ != null) && p_apply_1_.isEntityAlive() && EntityCreeder.this.getIntelligence() < 10F;
				}
			}, 6.0F, 1.0D, 1.2D));
				setSize(0.5F, 1.85F);
				this.experienceValue = 10;
			}

			public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
			{
				return new EntityCreeder(this.world);
			}
			protected void applyEntityAttributes()
			{
				super.applyEntityAttributes();
				getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33D);
				getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4D);
				getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D);
			}
			public EnumTier getTier()
			{
				return EnumTier.TIER4;
			}

			/**
			* Bonus damage vs mobs that implement Light
			*/
			public float getBonusVSLight()
			{
				return 1.1F;
			}

			/**
			* Bonus damage vs mobs that implement Armored
			*/
			public float getBonusVSArmored()
			{
				return 1.5F;
			}

			/**
			* Bonus damage vs mobs that implement Massive
			*/
			public float getBonusVSMassive()
			{
				return 0.75F;
			}

			public String getName()
			{
				if (hasCustomName())
				{
					return getCustomNameTag();
				}

				String s = EntityList.getEntityString(this);
				
				if (s == null)
				{
					s = "generic";
				}

				return getPowered() ? TranslateUtil.translateServer("entity.ChargedCreederHelpful.name") : TranslateUtil.translateServer("entity." + s + ".name");
			}

			public int getMaxFallHeight()
			{
				return getAttackTarget() == null ? 3 : 3 + (int)(getHealth() - 1.0F);
			}
			public boolean takesFallDamage()
			{
				return false;
			}
			public void fall(float distance, float damageMultiplier)
			{
				super.fall(distance, damageMultiplier);
				this.timeSinceIgnited = ((int)(this.timeSinceIgnited + distance * 1.5F));
				if (this.timeSinceIgnited > this.fuseTime - 5)
				{
					this.timeSinceIgnited = (this.fuseTime - 5);
				}
			}
			protected void entityInit()
			{
				super.entityInit();
				this.dataManager.register(STATE, Integer.valueOf(-1));
				this.dataManager.register(POWERED, Boolean.valueOf(false));
				this.dataManager.register(IGNITED, Boolean.valueOf(false));
			}
			public void writeEntityToNBT(NBTTagCompound tagCompound)
			{
				super.writeEntityToNBT(tagCompound);
				if (((Boolean)this.dataManager.get(POWERED)).booleanValue())
				{
					tagCompound.setBoolean("powered", true);
				}
				tagCompound.setShort("Fuse", (short)this.fuseTime);
				tagCompound.setByte("ExplosionRadius", (byte)this.explosionRadius);
				tagCompound.setBoolean("ignited", hasIgnited());
			}
			public void readEntityFromNBT(NBTTagCompound tagCompund)
			{
				super.readEntityFromNBT(tagCompund);
				this.dataManager.set(POWERED, Boolean.valueOf(tagCompund.getBoolean("powered")));
				if (tagCompund.hasKey("Fuse", 99))
				{
					this.fuseTime = tagCompund.getShort("Fuse");
				}
				if (tagCompund.hasKey("ExplosionRadius", 99))
				{
					this.explosionRadius = tagCompund.getByte("ExplosionRadius");
				}
				if (tagCompund.getBoolean("ignited"))
				{
					ignite();
				}
			}
			public void onUpdate()
			{
				if (isEntityAlive())
				{
					this.lastActiveTime = this.timeSinceIgnited;
					if (hasIgnited())
					{
						setCreeperState(1);
					}
					int i = getCreeperState();
					if ((i > 0) && (this.timeSinceIgnited == 0))
					{
						playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
					}
					this.timeSinceIgnited += i;
					if (this.timeSinceIgnited < 0)
					{
						this.timeSinceIgnited = 0;
					}
					if (this.timeSinceIgnited >= this.fuseTime)
					{
						this.timeSinceIgnited = this.fuseTime;
						explode();
					}
				}

				super.onUpdate();
			}
			protected SoundEvent getHurtSound(DamageSource source)
			{
				return SoundEvents.ENTITY_CREEPER_HURT;
			}
			protected SoundEvent getDeathSound()
			{
				return SoundEvents.ENTITY_CREEPER_DEATH;
			}
			public void onDeath(DamageSource cause)
			{
				super.onDeath(cause);
				if (this.world.getGameRules().getBoolean("doMobLoot"))
				{
					if ((cause.getTrueSource() instanceof net.minecraft.entity.monster.AbstractSkeleton))
					{
						int i = Item.getIdFromItem(Items.RECORD_13);
						int j = Item.getIdFromItem(Items.RECORD_WAIT);
						int k = i + this.rand.nextInt(j - i + 1);
						this.dropItem(Item.getItemById(k), 1);
					}
				}
			}
			public void inflictEngenderMobDamage(EntityLivingBase entity, String killmessage, DamageSource attacktype, float damage)
			{
				if (this.world.getGameRules().getBoolean("doMobLoot"))
				{
					if (!entity.isEntityAlive() && (this.getPowered() && (this.rand.nextInt(20) == 0 || this.isHero())))
					{
						if (entity instanceof net.minecraft.entity.monster.EntitySkeleton)
						entity.entityDropItem(new ItemStack(Items.SKULL, 1, 0), 0.0F);
						if (entity instanceof net.minecraft.entity.monster.EntityWitherSkeleton)
						entity.entityDropItem(new ItemStack(Items.SKULL, 1, 1), 0.0F);
						if (entity instanceof net.minecraft.entity.monster.EntityZombie && !(entity instanceof net.minecraft.entity.monster.EntityPigZombie))
						entity.entityDropItem(new ItemStack(Items.SKULL, 1, 2), 0.0F);
						if (entity instanceof net.minecraft.entity.monster.EntityCreeper)
						entity.entityDropItem(new ItemStack(Items.SKULL, 1, 4), 0.0F);
						if (entity instanceof net.minecraft.entity.boss.EntityDragon)
						entity.entityDropItem(new ItemStack(Items.SKULL, 1, 5), 0.0F);
					}
				}

				if (this.getPowered())
				{
					this.world.addWeatherEffect(new EntityLightningBolt(this.world, entity.posX - 0.5D, entity.posY + entity.height, entity.posZ - 0.5D, true));
					entity.onStruckByLightning(new EntityLightningBolt(this.world, entity.posX - 0.5D, entity.posY + entity.height, entity.posZ - 0.5D, true));
				}
				super.inflictEngenderMobDamage(entity, killmessage, attacktype, damage);
			}
			public boolean attackEntityAsMob(Entity entity)
			{
				if (super.attackEntityAsMob(entity))
				{
					if (this.world.getGameRules().getBoolean("doMobLoot"))
					{
						if (!entity.isEntityAlive() && (this.getPowered() && (this.rand.nextInt(20) == 0 || this.isHero())))
						{
							if (entity instanceof net.minecraft.entity.monster.EntitySkeleton)
							entity.entityDropItem(new ItemStack(Items.SKULL, 1, 0), 0.0F);
							if (entity instanceof net.minecraft.entity.monster.EntityWitherSkeleton)
							entity.entityDropItem(new ItemStack(Items.SKULL, 1, 1), 0.0F);
							if (entity instanceof net.minecraft.entity.monster.EntityZombie && !(entity instanceof net.minecraft.entity.monster.EntityPigZombie))
							entity.entityDropItem(new ItemStack(Items.SKULL, 1, 2), 0.0F);
							if (entity instanceof net.minecraft.entity.monster.EntityCreeper)
							entity.entityDropItem(new ItemStack(Items.SKULL, 1, 4), 0.0F);
							if (entity instanceof net.minecraft.entity.boss.EntityDragon)
							entity.entityDropItem(new ItemStack(Items.SKULL, 1, 5), 0.0F);
						}
					}

					if (this.getPowered())
					{
						this.world.addWeatherEffect(new EntityLightningBolt(this.world, entity.posX - 0.5D, entity.posY + entity.height, entity.posZ - 0.5D, true));
						entity.onStruckByLightning(new EntityLightningBolt(this.world, entity.posX - 0.5D, entity.posY + entity.height, entity.posZ - 0.5D, true));
					}

					return true;
				}
				else
				{
					return false;
				}
			}
			public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn)
			{
				float f = super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn) * 0.5F;
				Block block = blockStateIn.getBlock();
				
				if (net.minecraft.entity.boss.EntityWither.canDestroyBlock(block))
				{
					f = Math.min(0.8F, f);
				}

				return f;
			}
			public boolean getPowered()
			{
				return ((Boolean)this.dataManager.get(POWERED)).booleanValue();
			}
			public void setPowered(boolean powered)
			{
				if (powered)
				this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7D);
				this.dataManager.set(POWERED, Boolean.valueOf(powered));
			}
			@SideOnly(Side.CLIENT)
			public float getCreeperFlashIntensity(float p_70831_1_)
			{
				return (this.lastActiveTime + (this.timeSinceIgnited - this.lastActiveTime) * p_70831_1_) / (this.fuseTime - 2);
			}
			@Nullable
			protected ResourceLocation getLootTable()
			{
				return ELoot.ENTITIES_CREEDER;
			}
			public int getCreeperState()
			{
				return ((Integer)this.dataManager.get(STATE)).intValue();
			}
			public void setCreeperState(int state)
			{
				this.dataManager.set(STATE, Integer.valueOf(state));
			}
			public void onStruckByLightning(EntityLightningBolt lightningBolt)
			{
				this.setPowered(true);
			}
			public boolean interact(EntityPlayer player, EnumHand hand)
			{
				ItemStack stack = player.getHeldItem(hand);
				new ItemStack(stack.getItem());
				
				if (!stack.isEmpty() && (stack.getItem() == Items.FLINT_AND_STEEL))
				{
					this.world.playSound(player, this.posX, this.posY, this.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
					player.swingArm(hand);
					if (!this.world.isRemote)
					{
						if (hasOwner(player))
						{
							if (player.isSneaking())
							ignite();
							if (!getPowered())
							{
								if (this.world.canBlockSeeSky(getPosition()))
								this.world.addWeatherEffect(new EntityLightningBolt(this.world, this.posX - 0.5D, this.posY + 1.625D, this.posZ - 0.5D, false));
								else
								this.spawnExplosionParticle();
							}else{
									heal(1.0F);
								}
							}
							else{
								ignite();
							}
							stack.damageItem(1, player);
						}

						return true;
					}
					else
					{
						return false;
					}
				}
				public void performSpecialAttack()
				{
					ignite();
					playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 10.0F, 1.0F);
				}
				private void explode()
				{
					if (!this.world.isRemote)
					{
						boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
						float f = getPowered() ? 2.0F : 1.0F;
						
						if ((getAttackTarget() != null) && (getSpecialAttackTimer() <= 0) && (isHero()))
						{
							createEngenderModExplosionFireless(this, this.posX, this.posY + 1.0D, this.posZ, 20.0F * f, flag);
							this.dataManager.set(IGNITED, Boolean.valueOf(false));
							setCreeperState(-1);
							setSpecialAttackTimer(800);
							this.timeSinceIgnited = 0;
							this.motionY = 0.0D;
						}
						else {
							this.timeSinceIgnited = 0;
							this.dataManager.set(IGNITED, Boolean.valueOf(false));
							setCreeperState(-1);
							createEngenderModExplosionFireless(this, this.posX, this.posY + 1.0D, this.posZ, this.explosionRadius * f, flag);
						}
						if (getHealth() <= getMaxHealth() / 2.0F || isWild())
						{
							setDead();
							Collection<PotionEffect> collection = this.getActivePotionEffects();
							
							if (!collection.isEmpty())
							{
								EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
								entityareaeffectcloud.setRadius(2.5F);
								entityareaeffectcloud.setRadiusOnUse(-0.5F);
								entityareaeffectcloud.setWaitTime(10);
								entityareaeffectcloud.setDuration(entityareaeffectcloud.getDuration() / 2);
								entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / (float)entityareaeffectcloud.getDuration());
								
								for (PotionEffect potioneffect : collection)
								{
									entityareaeffectcloud.addEffect(new PotionEffect(potioneffect));
								}

								this.world.spawnEntity(entityareaeffectcloud);
							}

							if ((!this.world.isRemote) && EngenderConfig.general.useMessage && !isWild() && getOwner() instanceof EntityPlayerMP)
							{
								getOwner().sendMessage(new TextComponentTranslation("death.attack.self_destruct", new Object[] {this.getDisplayName()}));
							}
						}
						else
						{
							this.timeSinceIgnited = 0;
							this.dataManager.set(IGNITED, Boolean.valueOf(false));
						}
					}
				}
				public void onLivingUpdate()
				{
					super.onLivingUpdate();
					
					if (this.getPowered())
					this.extinguish();
					
					if ((getAttackTarget() != null) && (!hasIgnited()) && (getDistanceSq(getAttackTarget()) < 128.0D) && (getSpecialAttackTimer() <= 0) && (isHero()))
					{
						performSpecialAttack();
					}
				}
				public boolean hasIgnited()
				{
					return ((Boolean)this.dataManager.get(IGNITED)).booleanValue();
				}
				public void ignite()
				{
					this.dataManager.set(IGNITED, Boolean.valueOf(true));
				}

				public class EntityAICreeperSwell extends EntityAIBase
				{
					EntityCreeder swellingCreeper;
					EntityLivingBase creeperAttackTarget;
					public EntityAICreeperSwell(EntityCreeder p_i1655_1_)
					{
						this.swellingCreeper = p_i1655_1_;
						setMutexBits(1);
					}
					public boolean shouldExecute()
					{
						EntityLivingBase entitylivingbase = this.swellingCreeper.getAttackTarget();
						return ((this.swellingCreeper.getCreeperState() > 0) || ((entitylivingbase != null) && (this.swellingCreeper.getDistanceSq(entitylivingbase) < 9.0D))) && (this.swellingCreeper.getHealth() <= this.swellingCreeper.getMaxHealth() / 2.0F);
					}
					public void startExecuting()
					{
						this.swellingCreeper.getNavigator().clearPath();
						this.creeperAttackTarget = this.swellingCreeper.getAttackTarget();
					}
					public void resetTask()
					{
						this.creeperAttackTarget = null;
					}
					public void updateTask()
					{
						if (this.creeperAttackTarget == null)
						{
							this.swellingCreeper.setCreeperState(-1);
						}
						else if (this.swellingCreeper.getDistanceSq(this.creeperAttackTarget) > 49.0D)
						{
							this.swellingCreeper.setCreeperState(-1);
						}
						else if (!this.swellingCreeper.getEntitySenses().canSee(this.creeperAttackTarget))
						{
							this.swellingCreeper.setCreeperState(-1);
						}
						else
						{
							this.swellingCreeper.setCreeperState(1);
						}
					}
				}
			}