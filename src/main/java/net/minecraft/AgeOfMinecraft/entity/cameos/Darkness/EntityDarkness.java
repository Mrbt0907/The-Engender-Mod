package net.minecraft.AgeOfMinecraft.entity.cameos.Darkness;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import net.endermanofdoom.mac.MACCore;
import net.endermanofdoom.mac.dialogue.DialogueManager;
import net.endermanofdoom.mac.dialogue.SubDialogueMessage;
import net.endermanofdoom.mac.music.IMusicInteractable;
import net.endermanofdoom.mac.util.ReflectionUtil;
import net.endermanofdoom.mac.util.TranslateUtil;
import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.other.BeamHitbox;
import net.minecraft.AgeOfMinecraft.entity.other.IBeamHitboxHandler;
import net.minecraft.AgeOfMinecraft.entity.sources.EngenderDamageSources;
import net.minecraft.AgeOfMinecraft.events.MobChunkLoader;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.AgeOfMinecraft.util.DialogColors;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.gen.feature.WorldGenEndPodium;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityDarkness extends EntityFriendlyCreature implements IEntityMultiPart, IBeamHitboxHandler, IMusicInteractable
{
	public static boolean hardmode = false;
	private static final DataParameter<Integer> PHASE = EntityDataManager.createKey(EntityDarkness.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> MOVESET = EntityDataManager.createKey(EntityDarkness.class, DataSerializers.VARINT);
	/** Ring buffer array for the last 64 Y-positions and yaw rotations. Used to calculate offsets for the animations. */
	public double[][] ringBuffer = new double[64][3];
	/** Index into the ring buffer. Incremented once per tick and restarts at 0 once it reaches the end of the buffer. */
	public int ringBufferIndex = -1;
	/** An array containing all body parts of this dragon */
	public MultiPartEntityPart[] dragonPartArray;
	/** The head bounding box of a dragon */
	public MultiPartEntityPart dragonPartHead = new MultiPartEntityPart(this, "head", 12.0F, 12.0F);
	public MultiPartEntityPart dragonPartNeck = new MultiPartEntityPart(this, "neck", 12.0F, 12.0F);
	/** The body bounding box of a dragon */
	public MultiPartEntityPart dragonPartBody = new MultiPartEntityPart(this, "body", 16.0F, 16.0F);
	public MultiPartEntityPart dragonPartTail1 = new MultiPartEntityPart(this, "tail", 8.0F, 8.0F);
	public MultiPartEntityPart dragonPartTail2 = new MultiPartEntityPart(this, "tail", 8.0F, 8.0F);
	public MultiPartEntityPart dragonPartTail3 = new MultiPartEntityPart(this, "tail", 8.0F, 8.0F);
	public MultiPartEntityPart dragonPartWing1 = new MultiPartEntityPart(this, "wing", 8.0F, 8.0F);
	public MultiPartEntityPart dragonPartWing2 = new MultiPartEntityPart(this, "wing", 8.0F, 8.0F);
	public final List<Vec3d> ROUTE = new ArrayList<Vec3d>();
	public Vec3d path;
	private BeamHitbox lesserBeam = new BeamHitbox(this, world, "bob", 2.0D);
	protected EntityLivingBase target;
	/** Animation time at previous tick. */
	public float prevAnimTime;
	/** Animation time, used to control the speed of the animation cycles (wings flapping, jaw opening, etc.) */
	public float animTime;
	/** Activated if the dragon is flying though obsidian, white stone or bedrock. Slows movement and animation speed. */
	public boolean slowed;
	public int deathTicks;
	/** The current endercrystal that is healing this dragon */
	public final List<EntityEnderCrystal> endCrystals = new ArrayList<EntityEnderCrystal>();
	private float dragonH, dragonP;
	private int sayTime = 200, healTime = 300;
	private int tickLastHit;
	private int tickMoveset;
	
	private double nDragonX, nDragonY, nDragonZ;
	private boolean[] antiCheat = new boolean[10];
	private boolean[] said = new boolean[30];
	
	public EntityDarkness(World worldIn)
	{
		super(worldIn);
		
		dragonPartArray = new MultiPartEntityPart[] {dragonPartHead, dragonPartNeck, dragonPartBody, dragonPartTail1, dragonPartTail2, dragonPartTail3, dragonPartWing1, dragonPartWing2};

		setSize(16.0F * getScale() * 0.8F, 8.0F * getScale() * 0.45F);
		noClip = true;
		isImmuneToFire = true;
		ignoreFrustumCheck = true;
		bossInfo.setColor(Color.PINK);
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		dragonH = dragonP = 800.0F;
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(300.0D);
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(dragonP);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0D);
		getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
		getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20.0D);
		getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(5.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.25D);
		super.setHealth(dragonH);
	}

	protected void entityInit()
	{
		super.entityInit();
		getDataManager().register(PHASE, Integer.valueOf(0));
		getDataManager().register(MOVESET, Integer.valueOf(0));
		nDragonX = motionX;
		nDragonY = motionY;
		nDragonZ = motionZ;
	}

	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setFloat("dragonHealth", dragonH);
		compound.setInteger("dragonPhase", getPhase());
		compound.setInteger("dragonMove", getCurrentMove());
	}

	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		
		if (compound.hasKey("dragonHealth"))
			dragonH = compound.getFloat("dragonHealth");
		if (compound.hasKey("dragonPhase"))
			setPhase(compound.getInteger("dragonPhase"));
		if (compound.hasKey("dragonMove"))
			setCurrentMove(compound.getInteger("dragonMove"));
	}
	
	public void onUpdate()
	{
		super.onUpdate();
		int phase = getPhase();
		
		if (!world.isRemote)
		{
			if (isEntityAlive())
			{
				MobChunkLoader.updateLoaded(this);
				
				if (isDead)
				{
					isDead = false;
					say(4, false, true);
				}
			}
			else
				MobChunkLoader.stopLoading(this);
			
				
			
			if (ticksExisted == 1)
				say(5, true, false);
		}
		
		onPhaseUpdate(phase);
		onMovesetUpdate(phase, getCurrentMove());
		tickMoveset++;
	}
	
	protected void onPhaseUpdate(final int phase)
	{
		if (world.isRemote)
			;
		else
			switch(phase)
			{
				case 0: // First Phase - Trial
					if (dragonH < dragonP * 0.25F)
						say(4, true, false);
					else if (dragonH < dragonP * 0.5F)
						say(3, true, false);
					else if (dragonH < dragonP * 0.75F)
						say(2, true, false);
					
					if (dragonH < dragonP * 0.35F)
					{
						getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
						nextPhase();
					}
					break;
				case 1: // Start Of Second Phase
					if (healTime > 0)
					{
						if (dragonH < dragonP);
							dragonH += dragonP * 0.0025F;
						healTime--;
					}
					else
					{
						said[2] = false;
						said[3] = false;
						said[4] = false;
						getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(20.0D);
						getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(2.0D);
						nextPhase();
					}
					break;
				case 3: break; // Death
				default: // Phase 2 - True God
					if (dragonH < dragonP * 0.25F)
						say(4, true, false);
					else if (dragonH < dragonP * 0.5F)
						say(3, true, false);
					else if (dragonH < dragonP * 0.75F)
						say(2, true, false);
			}
	}
	
	protected void onMovesetUpdate(final int phase, final int moveset)
	{
		if (world.isRemote);
		else
		{
			EntityLivingBase target = getAttackTarget();
			switch(moveset)
			{
				case 1:
					if (target != null && target.isEntityAlive())
					{
						lesserBeam.setPosition(dragonPartHead.posX, dragonPartHead.posY, dragonPartHead.posZ, target.posX, target.posY, target.posZ);
						if (tickMoveset > 60)
							setCurrentMove(0);
					}
					else
						setCurrentMove(0);
					break;
				case 2:
					if (target != null && target.isEntityAlive())
					{
						if (tickMoveset % 20 == 0)
						{
							EntityDarkProjectile projectile = new EntityDarkProjectile(world, this, getAttackTarget(), (byte) (phase > 1 ? 1 : 0), true);
							projectile.setPosition(dragonPartHead.posX, dragonPartHead.posY, dragonPartHead.posZ);
							world.spawnEntity(projectile);
						}
						
						if (tickMoveset % 2 == 0 && rand.nextInt(5) == 0)
						{
							EntityDarkProjectile projectile = new EntityDarkProjectile(world, this, getAttackTarget(), (byte) 2, true);
							projectile.setPosition(dragonPartHead.posX, dragonPartHead.posY, dragonPartHead.posZ);
							world.spawnEntity(projectile);
						}
						
						if (tickMoveset > 60)
							setRandomMove(0, 5);
					}
					else
						setRandomMove(0, 5);
					break;
				case 3:
					if (ROUTE.isEmpty() || tickMoveset > 200)
						setRandomMove(0, 1, 2, 5);
					break;
				case 5:
					if (target != null && target.isEntityAlive())
					{
						if (tickMoveset % 2 == 0)
						{
							EntityDarkProjectile projectile = new EntityDarkProjectile(world, this, getAttackTarget(), (byte) 2, true);
							projectile.setPosition(dragonPartHead.posX, dragonPartHead.posY, dragonPartHead.posZ);
							world.spawnEntity(projectile);
						}
								
						if (tickMoveset > 60)
							setRandomMove(0, 2);
					}
					else
						setRandomMove(0, 2);
					break;
				default:
					if (tickMoveset > 200)
						setRandomMove(1, 2, 3, 5);	
			}
		}
	}
	
	protected void onMovesetChanged(final int phase, final int moveset)
	{
		if (lesserBeam.hasPosition())
			lesserBeam.resetPosition();
		
		if (world.isRemote);
		else
		{
			EntityLivingBase target = getAttackTarget();
			clearPath();
			EngenderMod.debug("New moveset: " + moveset);
			
			switch(phase)
			{
				default:
					switch(moveset)
					{
						case 1: case 2:
							if (target != null && target.isEntityAlive())
								for (int i = 0; i < 2; i++)
									addPath(new Vec3d(target.posX + (rand.nextInt(16) - rand.nextInt(16)), target.posY + 50.0D, target.posZ + (rand.nextInt(16) - rand.nextInt(16))));
							break;
						case 3:
							if (target != null && target.isEntityAlive())
								addPath(new Vec3d(target.posX, target.posY - 16.0D, target.posZ));
							break;
						default:
							for (int i = 0; i < 20; i++)
								addPath(new Vec3d(rand.nextInt(150) - rand.nextInt(150), 80 + rand.nextInt(40), rand.nextInt(150) - rand.nextInt(150)));
					}
			}
		}
	}
	
	public void onLivingUpdate()
	{
		super.setHealth(dragonH);
		if (world.isRemote)
		{
			if (!isSilent())
			{
				float f = MathHelper.cos(animTime * ((float)Math.PI * 2F));
				float f1 = MathHelper.cos(prevAnimTime * ((float)Math.PI * 2F));

				if (f1 <= -0.3F && f >= -0.3F)
					world.playSound(posX, posY, posZ, SoundEvents.ENTITY_ENDERDRAGON_FLAP, getSoundCategory(), 15.0F, 0.5F + rand.nextFloat() * 0.2F, false);
			}
		}
		else
		{
			if (!isSilent() && --sayTime <= 0)
				if (getAttackTarget() != null)
					say(1, false, true, getAttackTarget());
				else
					say(0, false, true);
		}
		
		prevAnimTime = animTime;

		if (getHealth() <= 0.0F)
		{
			float f12 = (rand.nextFloat() - 0.5F) * 8.0F * getScale();
			float f13 = (rand.nextFloat() - 0.5F) * 4.0F * getScale();
			float f15 = (rand.nextFloat() - 0.5F) * 8.0F * getScale();
			world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, posX + (double)f12, posY + 2.0D + (double)f13, posZ + (double)f15, 0.0D, 0.0D, 0.0D);
		}
		else
		{
			updateEndCrystals();
			lesserBeam.onUpdate();
			
			float f11 = 0.2F / (MathHelper.sqrt(nDragonX * nDragonX + nDragonZ * nDragonZ) * 5.0F + 1.0F);
			f11 = f11 * (float)Math.pow(2.0D, nDragonY);

			if (slowed)
				animTime += f11 * 0.25F;
			else
				animTime += f11 * 0.30F;

			rotationYaw = MathHelper.wrapDegrees(rotationYaw);

			if (isAIDisabled())
				animTime = 0.5F;
			else
			{
				if (ringBufferIndex < 0)
					for (int i = 0; i < ringBuffer.length; ++i)
					{
						ringBuffer[i][0] = (double)rotationYaw;
						ringBuffer[i][1] = posY;
					}

				if (++ringBufferIndex == ringBuffer.length)
				{
					ringBufferIndex = 0;
				}

				ringBuffer[ringBufferIndex][0] = (double)rotationYaw;
				ringBuffer[ringBufferIndex][1] = posY;

				if (world.isRemote)
				{	
					if (newPosRotationIncrements > 0)
					{
						double d5 = posX + (interpTargetX - posX) / (double)newPosRotationIncrements;
						double d0 = posY + (interpTargetY - posY) / (double)newPosRotationIncrements;
						double d1 = posZ + (interpTargetZ - posZ) / (double)newPosRotationIncrements;
						double d2 = MathHelper.wrapDegrees(interpTargetYaw - (double)rotationYaw);
						rotationYaw = (float)((double)rotationYaw + d2 / (double)newPosRotationIncrements);
						rotationPitch = (float)((double)rotationPitch + (interpTargetPitch - (double)rotationPitch) / (double)newPosRotationIncrements);
						--newPosRotationIncrements;
						setPosition(d5, d0, d1);
						setRotation(rotationYaw, rotationPitch);
					}
				}
				else
				{
					EntityLivingBase target = getAttackTarget();
					
					if (target != null && !target.isEntityAlive())
					{
						EngenderMod.debug("Target " + target.getName() + " is dead. Grabbing new target...");
						target = null;
					}
					
					if (target == null)
					{
						List<Entity> entities = world.getEntitiesInAABBexcluding(this, getEntityBoundingBox().grow(300.0D), null);
						EntityLivingBase newTarget = null;
						double distance = Double.MAX_VALUE, curDistance;
						boolean player;
						
						for(Entity entity : entities)
						{
							curDistance = entity.getDistanceSq(this);
							player = entity instanceof EntityPlayer;
							
							if(entity.isEntityAlive() && entity instanceof EntityLivingBase && curDistance < distance && (player && !((EntityPlayer)entity).isCreative() && !((EntityPlayer)entity).isSpectator() || !player))
							{
								if (!(entity instanceof EntityShulker || entity instanceof EntityEnderman || entity instanceof EntityDragon))
								{
									distance = curDistance;
									newTarget = (EntityLivingBase) entity;
								}
							}
						}
						
						target = newTarget;
						setAttackTarget(newTarget);
					}
					
					if (finishedPath())
						moveNextPath();
					
					Vec3d vec3d = path;

					motionX = nDragonX;
					motionY = nDragonY;
					motionZ = nDragonZ;
					
					
					
					if (vec3d != null)
					{
						double d6 = vec3d.x - posX;
						double d7 = vec3d.y - posY;
						double d8 = vec3d.z - posZ;
						double d3 = d6 * d6 + d7 * d7 + d8 * d8;
						float f5 = 10;
						d7 = MathHelper.clamp(d7 / (double)MathHelper.sqrt(d6 * d6 + d8 * d8), (double)(-f5), (double)f5);
						motionY += d7 * 0.10000000149011612D;
						rotationYaw = MathHelper.wrapDegrees(rotationYaw);
						double d4 = MathHelper.clamp(MathHelper.wrapDegrees(180.0D - MathHelper.atan2(d6, d8) * (180D / Math.PI) - (double)rotationYaw), -50.0D, 50.0D);
						Vec3d vec3d1 = (new Vec3d(vec3d.x - posX, vec3d.y - posY, vec3d.z - posZ)).normalize();
						Vec3d vec3d2 = (new Vec3d((double)MathHelper.sin(rotationYaw * 0.017453292F), motionY, (double)(-MathHelper.cos(rotationYaw * 0.017453292F)))).normalize();
						float f7 = Math.max(((float)vec3d2.dotProduct(vec3d1) + 0.5F) / 1.5F, 0.0F);
						randomYawVelocity *= getPhase() > 2 ? 0.8F : 0.2F;
						randomYawVelocity = (float)((double)randomYawVelocity + d4 * 1.0F);
						rotationYaw += randomYawVelocity * 0.2F;
						float f8 = (float)(2.0D / (d3 + 1.0D));
						moveRelative(0.0F, 0.0F, -1.0F, 0.06F * (f7 * f8 + (1.0F - f8)));

						if (slowed)
							move(MoverType.SELF, motionX * 0.800000011920929D, motionY * 0.800000011920929D, motionZ * 0.800000011920929D);
						else
							move(MoverType.SELF, motionX, motionY, motionZ);

						Vec3d vec3d3 = (new Vec3d(motionX, motionY, motionZ)).normalize();
						float f10 = ((float)vec3d3.dotProduct(vec3d2) + 1.0F) / 2.0F;
						f10 = (float) (0.8D + 0.15D * f10 * getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue());
						motionX *= (double)f10;
						motionZ *= (double)f10;
						motionY *= 0.9100000262260437D;
					}
					else
					{
						motionX = 0;
						motionY = 0;
						motionZ = 0;
					}
					
					nDragonX = MathHelper.clamp(motionX, -2.0F, 2.0F);
					nDragonY = MathHelper.clamp(motionY, -2.0F, 2.0F);
					nDragonZ = MathHelper.clamp(motionZ, -2.0F, 2.0F);
				}
				
				float scale = getScale() * 0.9F;
				renderYawOffset = rotationYaw;
				dragonPartHead.width = 1.0F * scale;
				dragonPartHead.height = 1.0F * scale;
				dragonPartNeck.width = 3.0F * scale;
				dragonPartNeck.height = 3.0F * scale;
				dragonPartTail1.width = 2.0F * scale;
				dragonPartTail1.height = 2.0F * scale;
				dragonPartTail2.width = 2.0F * scale;
				dragonPartTail2.height = 2.0F * scale;
				dragonPartTail3.width = 2.0F * scale;
				dragonPartTail3.height = 2.0F * scale;
				dragonPartBody.height = 3.0F * scale;
				dragonPartBody.width = 5.0F * scale;
				dragonPartWing1.height = 2.0F * scale;
				dragonPartWing1.width = 4.0F * scale;
				dragonPartWing2.height = 3.0F * scale;
				dragonPartWing2.width = 4.0F * scale;
				Vec3d[] avec3d = new Vec3d[dragonPartArray.length];

				for (int j = 0; j < dragonPartArray.length; ++j)
					avec3d[j] = new Vec3d(dragonPartArray[j].posX, dragonPartArray[j].posY, dragonPartArray[j].posZ);

				float f14 = (float)(getMovementOffsets(5, 1.0F)[1] - getMovementOffsets(10, 1.0F)[1]) * 10.0F * 0.017453292F;
				float f16 = MathHelper.cos(f14);
				float f2 = MathHelper.sin(f14);
				float f17 = rotationYaw * 0.017453292F;
				float f3 = MathHelper.sin(f17);
				float f18 = MathHelper.cos(f17);
				dragonPartBody.onUpdate();
				dragonPartBody.setLocationAndAngles(posX + (double)(f3 * 0.5F) * scale, posY, posZ - (double)(f18 * 0.5F) * scale, 0.0F, 0.0F);
				dragonPartWing1.onUpdate();
				dragonPartWing1.setLocationAndAngles(posX + (double)(f18 * 4.5F) * scale, posY + 2.0D * scale, posZ + (double)(f3 * 4.5F) * scale, 0.0F, 0.0F);
				dragonPartWing2.onUpdate();
				dragonPartWing2.setLocationAndAngles(posX - (double)(f18 * 4.5F) * scale, posY + 2.0D * scale, posZ - (double)(f3 * 4.5F) * scale, 0.0F, 0.0F);

				
				if (!world.isRemote)
				{
					collideWithEntities(world.getEntitiesWithinAABBExcludingEntity(this, dragonPartWing1.getEntityBoundingBox().grow(1.0D).offset(0.0D, -2.0D, 0.0D)));
					collideWithEntities(world.getEntitiesWithinAABBExcludingEntity(this, dragonPartWing2.getEntityBoundingBox().grow(1.0D).offset(0.0D, -2.0D, 0.0D)));
					collideWithEntities(world.getEntitiesWithinAABBExcludingEntity(this, dragonPartBody.getEntityBoundingBox().grow(1.0D).offset(0.0D, -2.0D, 0.0D)));
					attackEntitiesInList(world.getEntitiesWithinAABBExcludingEntity(this, dragonPartBody.getEntityBoundingBox().grow(1.0D)));
					attackEntitiesInList(world.getEntitiesWithinAABBExcludingEntity(this, dragonPartHead.getEntityBoundingBox().grow(1.0D)));
					attackEntitiesInList(world.getEntitiesWithinAABBExcludingEntity(this, dragonPartNeck.getEntityBoundingBox().grow(1.0D)));
				}

				double[] adouble = getMovementOffsets(5, 1.0F);
				float f19 = MathHelper.sin(rotationYaw * 0.017453292F - randomYawVelocity * 0.01F);
				float f4 = MathHelper.cos(rotationYaw * 0.017453292F - randomYawVelocity * 0.01F);
				dragonPartHead.onUpdate();
				dragonPartNeck.onUpdate();
				float f20 = getHeadYOffset();
				dragonPartHead.setLocationAndAngles(posX + (double)(f19 * 6.5F * f16) * scale, posY + ((double)f20 + (double)(f2 * 6.5F)) * scale, posZ - (double)(f4 * 6.5F * f16) * scale, 0.0F, 0.0F);
				dragonPartNeck.setLocationAndAngles(posX + (double)(f19 * 5.5F * f16) * scale, posY + ((double)f20 + (double)(f2 * 5.5F)) * scale, posZ - (double)(f4 * 5.5F * f16) * scale, 0.0F, 0.0F);

				
				for (int k = 0; k < 3; ++k)
				{
					MultiPartEntityPart multipartentitypart = null;

					if (k == 0)
						multipartentitypart = dragonPartTail1;
					else if (k == 1)
						multipartentitypart = dragonPartTail2;
					else if (k == 2)
						multipartentitypart = dragonPartTail3;

					double[] adouble1 = getMovementOffsets(12 + k * 2, 1.0F);
					float f21 = rotationYaw * 0.017453292F + simplifyAngle(adouble1[0] - adouble[0]) * 0.017453292F;
					float f6 = MathHelper.sin(f21);
					float f22 = MathHelper.cos(f21);
					float f24 = (float)(k + 1) * 2.0F;
					multipartentitypart.onUpdate();
					multipartentitypart.setLocationAndAngles(posX - (double)((f3 * 1.5F + f6 * f24) * f16) * scale, posY + ((adouble1[1] - adouble[1]) - (double)((f24 + 1.5F) * f2) + 1.5D) * scale, posZ + (double)((f18 * 1.5F + f22 * f24) * f16) * scale, 0.0F, 0.0F);
				}

				if (!world.isRemote)
				{
					slowed = destroyBlocksInAABB(dragonPartHead.getEntityBoundingBox()) | destroyBlocksInAABB(dragonPartNeck.getEntityBoundingBox()) | destroyBlocksInAABB(dragonPartBody.getEntityBoundingBox());
					if (dragonH > dragonP)
						dragonH = dragonP;
					for (int l = 0; l < dragonPartArray.length; ++l)
					{
						dragonPartArray[l].prevPosX = avec3d[l].x;
						dragonPartArray[l].prevPosY = avec3d[l].y;
						dragonPartArray[l].prevPosZ = avec3d[l].z;
					}
				}
			}
			
				
		}
	}

	protected void onDeathUpdate()
	{
		if (isEntityAlive()) return;
		++deathTicks;
		
		
		if (deathTicks >= 180 && deathTicks <= 200)
		{
			float f = (rand.nextFloat() - 0.5F) * 8.0F;
			float f1 = (rand.nextFloat() - 0.5F) * 4.0F;
			float f2 = (rand.nextFloat() - 0.5F) * 8.0F;
			world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX + (double)f, posY + 2.0D + (double)f1, posZ + (double)f2, 0.0D, 0.0D, 0.0D);
		}

		boolean flag = world.getGameRules().getBoolean("doMobLoot");
		int i = 12000;

		if (!world.isRemote)
		{
			
			if (deathTicks > 150 && deathTicks % 5 == 0 && flag)
			{
				dropExperience(MathHelper.floor((float)i * 0.08F));
			}

			if (deathTicks == 1)
			{
				endCrystals.clear();
				say(9, true, false);
				world.playBroadcastSound(1028, new BlockPos(this), 0);
			}
		}

		move(MoverType.SELF, 0.0D, 0.10000000149011612D, 0.0D);
		rotationYaw += 20.0F;
		renderYawOffset = rotationYaw;

		if (deathTicks == 200 && !world.isRemote)
		{
			if (flag)
				dropExperience(MathHelper.floor((float)i * 0.2F));
			say(10, true, false);
			setDead();
		}
	}
	
	@Override
	public void onBlockCollision(BlockPos pos)
	{
		if (ticksExisted % 2 == 0)
			createEngenderModExplosion(this, pos.getX(), pos.getY(), pos.getZ(), 10.0F, false, false);
	}
	
	@Override
	public void onEntityCollision(Entity entity)
	{
		boolean player = entity instanceof EntityPlayer;
		if (ticksExisted % 2 == 0 && !entity.equals(this) && (player && !((EntityPlayer)entity).isSpectator() || !player))
		{
			int phase = getPhase();
			boolean isNonPlayer = !(entity instanceof EntityPlayer);
			if (ticksExisted % 4 == 0 && isNonPlayer)
				createEngenderModExplosion(this, entity.posX, entity.posY, entity.posZ, 1.0F, false, false);
			
			if (isNonPlayer)
				entity.hurtResistantTime = 0;
			if (entity instanceof EntityLivingBase)
			{
				
				ReflectionUtil.set(EntityLivingBase.class, entity, "recentlyHit", "field_70718_bc", 100);
				((EntityLivingBase) entity).setHealth(((EntityLivingBase) entity).getHealth() - (phase > 1 ? 2.0F : 1F));
			}
			
			if (phase > 1)
				entity.attackEntityFrom(EngenderDamageSources.ERASURE, 2F);
			else
				entity.attackEntityFrom(EngenderDamageSources.VOID, 1F);
		}
	}
	
	/**
	 * Updates the state of the enderdragon's current endercrystal.
	 */
	private void updateEndCrystals()
	{
		if (rand.nextInt(10) == 0)
		{
			endCrystals.clear();
			endCrystals.addAll(world.<EntityEnderCrystal>getEntitiesWithinAABB(EntityEnderCrystal.class, getEntityBoundingBox().grow(256.0D)));
		}
		
		Iterator<EntityEnderCrystal> iterator = endCrystals.iterator();
		EntityEnderCrystal endCrystal;
			
		while(iterator.hasNext())
		{
			endCrystal = iterator.next();
				
			if (endCrystal.isDead)
			{
				onCrystalDestroyed(endCrystal, endCrystal.getPosition(), null);
				iterator.remove();
			}
			else if (endCrystal.ticksExisted % 10 == 0 && getHealth() < getMaxHealth())
				heal(5.0F);
		}
	}
	
	public void onCrystalDestroyed(EntityEnderCrystal crystal, BlockPos pos, Entity attacker)
	{
		EntityPlayer player;
		
		if (attacker == null || !(attacker instanceof EntityPlayer))
			player = world.getNearestAttackablePlayer(pos, 300.0D, 300.0D);
		else
			player = (EntityPlayer) attacker;

		if (endCrystals.contains(crystal))
			attackEntityFromPart(dragonPartHead, DamageSource.causeExplosionDamage(player), 100.0F);
	}
	
	/**
	 * Returns a double[3] array with movement offsets, used to calculate trailing tail/neck positions. [0] = yaw
	 * offset, [1] = y offset, [2] = unused, always 0. Parameters: buffer index offset, partial ticks.
	 */
	public double[] getMovementOffsets(int p_70974_1_, float p_70974_2_)
	{
		if (getHealth() <= 0.0F)
		{
			p_70974_2_ = 0.0F;
		}

		p_70974_2_ = 1.0F - p_70974_2_;
		int i = ringBufferIndex - p_70974_1_ & 63;
		int j = ringBufferIndex - p_70974_1_ - 1 & 63;
		double[] adouble = new double[3];
		double d0 = ringBuffer[i][0];
		double d1 = MathHelper.wrapDegrees(ringBuffer[j][0] - d0);
		adouble[0] = d0 + d1 * (double)p_70974_2_;
		d0 = ringBuffer[i][1];
		d1 = ringBuffer[j][1] - d0;
		adouble[1] = d0 + d1 * (double)p_70974_2_;
		adouble[2] = ringBuffer[i][2] + (ringBuffer[j][2] - ringBuffer[i][2]) * (double)p_70974_2_;
		return adouble;
	}

	

	private float getHeadYOffset()
	{
		double d0;

			double[] adouble = getMovementOffsets(5, 1.0F);
			double[] adouble1 = getMovementOffsets(0, 1.0F);
			d0 = adouble[1] - adouble1[1];

		return (float)d0;
	}

	

	/**
	 * Pushes all entities inside the list away from the enderdragon.
	 */
	private void collideWithEntities(List<Entity> p_70970_1_)
	{
		double d0 = (dragonPartBody.getEntityBoundingBox().minX + dragonPartBody.getEntityBoundingBox().maxX) / 2.0D;
		double d1 = (dragonPartBody.getEntityBoundingBox().minZ + dragonPartBody.getEntityBoundingBox().maxZ) / 2.0D;

		for (Entity entity : p_70970_1_)
			if (entity instanceof EntityLivingBase)
			{
				double d2 = entity.posX - d0;
				double d3 = entity.posZ - d1;
				double d4 = d2 * d2 + d3 * d3;
				entity.addVelocity(d2 / d4 * 4.0D, 0.20000000298023224D, d3 / d4 * 4.0D);
			}
	}

	/**
	 * Attacks all entities inside this list, dealing 5 hearts of damage.
	 */
	private void attackEntitiesInList(List<Entity> p_70971_1_)
	{
		int phase = getPhase();
		for (Entity entity : p_70971_1_)
			if (entity instanceof EntityLivingBase)
			{
				entity.attackEntityFrom(DamageSource.causeMobDamage(this), phase > 1 ? 50.0F : 15.0F);
				applyEnchantments(this, entity);
			}
	}

	/**
	 * Simplifies the value of a number by adding/subtracting 180 to the point that the number is between -180 and 180.
	 */
	private float simplifyAngle(double p_70973_1_)
	{
		return (float)MathHelper.wrapDegrees(p_70973_1_);
	}

	/**
	 * Destroys all blocks that aren't associated with 'The End' inside the given bounding box.
	 */
	private boolean destroyBlocksInAABB(AxisAlignedBB p_70972_1_)
	{
		int i = MathHelper.floor(p_70972_1_.minX);
		int j = MathHelper.floor(p_70972_1_.minY);
		int k = MathHelper.floor(p_70972_1_.minZ);
		int l = MathHelper.floor(p_70972_1_.maxX);
		int i1 = MathHelper.floor(p_70972_1_.maxY);
		int j1 = MathHelper.floor(p_70972_1_.maxZ);
		boolean flag = false;
		boolean flag1 = false;

		for (int k1 = i; k1 <= l; ++k1)
		{
			for (int l1 = j; l1 <= i1; ++l1)
			{
				for (int i2 = k; i2 <= j1; ++i2)
				{
					BlockPos blockpos = new BlockPos(k1, l1, i2);
					IBlockState iblockstate = world.getBlockState(blockpos);
					Block block = iblockstate.getBlock();

					if (!block.isAir(iblockstate, world, blockpos) && iblockstate.getMaterial() != Material.FIRE)
					{
						if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(world, this))
							flag = true;
						else if (block != net.minecraft.init.Blocks.BARRIER && block != net.minecraft.init.Blocks.OBSIDIAN && block != net.minecraft.init.Blocks.END_STONE && block != net.minecraft.init.Blocks.BEDROCK && block != net.minecraft.init.Blocks.END_PORTAL && block != net.minecraft.init.Blocks.END_PORTAL_FRAME && block != net.minecraft.init.Blocks.COMMAND_BLOCK && block != net.minecraft.init.Blocks.REPEATING_COMMAND_BLOCK && block != net.minecraft.init.Blocks.CHAIN_COMMAND_BLOCK && block != net.minecraft.init.Blocks.IRON_BARS && block != net.minecraft.init.Blocks.END_GATEWAY && block.canEntityDestroy(iblockstate, world, blockpos, this) && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, iblockstate))
						{
							
							
							if (block != Blocks.COMMAND_BLOCK && block != Blocks.REPEATING_COMMAND_BLOCK && block != Blocks.CHAIN_COMMAND_BLOCK && block != Blocks.IRON_BARS && block != Blocks.END_GATEWAY)
							{
								flag1 = world.setBlockToAir(blockpos) || flag1;
							}
							else
							{
								flag = true;
							}
						}
						else
						{
							flag = true;
						}
					}
				}
			}
		}

		if (flag1)
		{
			double d0 = p_70972_1_.minX + (p_70972_1_.maxX - p_70972_1_.minX) * (double)rand.nextFloat();
			double d1 = p_70972_1_.minY + (p_70972_1_.maxY - p_70972_1_.minY) * (double)rand.nextFloat();
			double d2 = p_70972_1_.minZ + (p_70972_1_.maxZ - p_70972_1_.minZ) * (double)rand.nextFloat();
			world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}

		return flag;
	}

	public boolean attackEntityFromPart(MultiPartEntityPart dragonPart, DamageSource source, float damage)
	{
		if (source.damageType.equals("infinity") && damage < 500)
		{
			damage = 500.0F;
			say(18, false, false, source.getTrueSource());
		}
		
		damage -= 5.0F;
		
		if (dragonPart != dragonPartHead)
			damage = Math.max(damage * 0.25F, 0.0F);
		
		if (damage < 2.0F)
		{
			say(19, false, true, source.getTrueSource());
			return false;
		}
		else
		{
			//TODO: Implement anticheat config before adding in one shot detection
			attackDragonFrom(source, damage);
			
			if (dragonH <= 0.0F)
			{
				dragonH = 0.0F;
				setPhase(3);
			}
			return true;
		}
	}

	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (source instanceof EntityDamageSource && ((EntityDamageSource)source).getIsThornsDamage())
			attackEntityFromPart(dragonPartBody, source, amount);
		return false;
	}

	/**
	 * Provides a way to cause damage to an ender dragon.
	 */
	protected boolean attackDragonFrom(DamageSource source, float amount)
	{
		return super.attackEntityFrom(source, amount);
	}

	@Override
	protected void damageEntity(DamageSource source, float amount)
	{
		float factor = MathHelper.clamp((ticksExisted - tickLastHit) / 20.0F, 0.1F, 1.0F), damage = dragonP / amount;
		tickLastHit = ticksExisted;
		
		if (factor < 0.3)
			say(24, false, true, source.getTrueSource());
		else if (factor < 0.5)
			say(23, false, true, source.getTrueSource());
		
		if (damage > 0.1F)
			say(11, false, true, source.getTrueSource());
		if (dragonP / amount > 0.05F)
			say(22, false, true, source.getTrueSource());
		else if (dragonP / amount > 0.01F)
			say(21, false, true, source.getTrueSource());
		else
			say(20, false, true, source.getTrueSource());			
		amount *= factor;
		
		dragonH -= amount;
	}
	
	/**
	 * Called by the /kill command.
	 */
	public void onKillCommand()
	{
		say(15, false, true);
		attackEntityFromPart(dragonPartBody, DamageSource.GENERIC, dragonP * 0.2F);
		//dragonH = 0;
	}

	private void dropExperience(int p_184668_1_)
	{
		while (p_184668_1_ > 0)
		{
			int i = EntityXPOrb.getXPSplit(p_184668_1_);
			p_184668_1_ -= i;
			world.spawnEntity(new EntityXPOrb(world, posX, posY, posZ, i));
		}
	}


	public static void registerFixesDragon(DataFixer fixer)
	{
		EntityLiving.registerFixesMob(fixer, EntityDragon.class);
	}
	
	protected void despawnEntity() {}

	/**
	 * Return the Entity parts making up this Entity (currently only for dragons)
	 */
	public Entity[] getParts()
	{
		return dragonPartArray;
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	public boolean canBeCollidedWith()
	{
		return false;
	}

	public World getWorld()
	{
		return world;
	}

	public SoundCategory getSoundCategory()
	{
		return SoundCategory.HOSTILE;
	}

	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_ENDERDRAGON_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return SoundEvents.ENTITY_ENDERDRAGON_HURT;
	}
	
	@Override
	protected float getSoundVolume()
	{
		return 20.0F;
	}

	@Override
	protected float getSoundPitch()
	{
		return super.getSoundPitch() - 0.2F;
	}
	
	@Nullable
	protected ResourceLocation getLootTable()
	{
		return LootTableList.ENTITIES_ENDER_DRAGON;
	}

	@SideOnly(Side.CLIENT)
	public float getHeadPartYOffset(int p_184667_1_, double[] p_184667_2_, double[] p_184667_3_)
	{
		double d0;
		BlockPos blockpos = world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION);
		float f = Math.max(MathHelper.sqrt(getDistanceSqToCenter(blockpos)) / 4.0F, 1.0F);
		d0 = (double)((float)p_184667_1_ / f);
		return (float)d0;
	}

	public Vec3d getHeadLookVec(float p_184665_1_)
	{
		Vec3d vec3d;

			BlockPos blockpos = world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION);
			float f = Math.max(MathHelper.sqrt(getDistanceSqToCenter(blockpos)) / 4.0F, 1.0F);
			float f1 = 6.0F / f;
			float f2 = rotationPitch;
			rotationPitch = -f1 * 1.5F * 5.0F;
			vec3d = getLook(p_184665_1_);
			rotationPitch = f2;
		return vec3d;
	}

	public int getPhase()
	{
		return ((Integer) dataManager.get(PHASE)).intValue();
	}
	
	public void setPhase(int phase)
	{
		dataManager.set(PHASE, Integer.valueOf(Math.min(phase, 3)));
	}
	
	public int getCurrentMove()
	{
		return dataManager.get(MOVESET).intValue();
	}
	
	public void setCurrentMove(int moveset)
	{
		dataManager.set(MOVESET, Integer.valueOf(moveset));
		tickMoveset = 0;
		onMovesetChanged(getPhase(), moveset);
	}
	
	public void setRandomMove(int... movesets)
	{
		if (movesets.length > 0)
			setCurrentMove(movesets[rand.nextInt(movesets.length)]);
	}
	
	public void nextPhase()
	{
		int phase = getPhase() + 1; 
		switch(phase)
		{
			case 0:
				break;
			case 1:
				say(7, true, false);
				List<EntityPlayerMP> players = getServer().getPlayerList().getPlayers();
				for (EntityPlayerMP player : players)
					DialogueManager.sendSubDialogue(player, "", getName() + " is rallying!", null, false, DialogColors.COLOR_TEXT_ENEMY, DialogColors.COLOR_BACKGROUND_ENEMY);
				break;
			case 3:
				break;
			default:
				say(8, true, false);
		}
		setPhase(phase);
	}
	
	public void addPotionEffect(PotionEffect potioneffectIn) {}

	protected boolean canBeRidden(Entity entityIn) {return false;}

	public float getScale()
	{
		return 2.5F;
	}
	
	@Override
	public boolean isEntityAlive()
	{
		return world.isRemote || !world.getMinecraftServer().isServerRunning() ? super.isEntityAlive() : dragonH > 0.0F;
	}
	
	@Override
	public void setDead()
	{
		if (world.isRemote || !isEntityAlive())
		{
			if (!world.isRemote)
				hardmode = true;
			super.setDead();
		}
		else
		{
			say(15, false, true);
			attackEntityFromPart(dragonPartBody, DamageSource.GENERIC, dragonP * 0.08F);
		}
	}
	
	@Override
	public void onDeath(DamageSource cause)
	{
		if(world.isRemote || !isEntityAlive())
			super.onDeath(cause);
		else
			say(16, false, true, cause.getTrueSource());
	}
	
	@Override
	public void heal(float healAmount)
	{
		dragonH += Math.max(healAmount, 0.0F);
	}
	
	@Override
	public void setHealth(float health)
	{
		if (getMaxHealth() < dragonP)
		{
			getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(dragonP);
			say(14, false, true);
		}
		else if (getHealth() != dragonH)
			say(13, false, true);
		else if (health < dragonH)
			attackEntityFromPart(dragonPartBody, DamageSource.GENERIC, (dragonP - health) * 0.08F);
	}

	@Nullable
	@Override
	public EntityLivingBase getAttackTarget()
	{
		EntityLivingBase target = super.getAttackTarget();
		return target == null ? this.target : target;
	}

	@Override
	public void setAttackTarget(@Nullable EntityLivingBase entity)
	{
		target = entity;
		super.setAttackTarget(entity);
	}
	
	/**Sends a message to everyone in the server
	 * <br>0 - Idle
	 * <br>1 - In Combat
	 * <br>2 - 75% Health
	 * <br>3 - 50% Health
	 * <br>4 - 25% Health
	 * <br>5 - Initial Spawn
	 * <br>6 - Respawn 
	 * <br>7 - Entering Phase 2
	 * <br>8 - Start Of Phase 2
	 * <br>9 - Dying
	 * <br>10 - Dead
	 * <br>11 - Anti Cheat: Extremely Heavy Damage
	 * <br>12 - Anti Cheat: One Shot
	 * <br>13 - Anti Cheat: Set Health
	 * <br>14 - Anti Cheat: Set Max Health
	 * <br>15 - Anti Cheat: Set Dead
	 * <br>16 - Anti Cheat: OnDeath
	 * <br>17 - Anti Cheat: isDeath
	 * <br>18 - Anti Cheat: infinity
	 * <br>19 - Overall Damage: Pitiful
	 * <br>20 - Overall Damage: Normal
	 * <br>21 - Overall Damage: Heavy
	 * <br>22 - Overall Damage: Very Heavy
	 * <br>23 - Heavy Combo
	 * <br>24 - Very Heavy Combo*/
	private void say(int type, boolean forced, boolean repeat, Object... args)
	{
		if (!world.isRemote && (forced || sayTime <= 0) && !said[type])
		{
			String name = args.length > 0 && args[0] != null && args[0] instanceof Entity ? " " + ((Entity)args[0]).getName() : "";
			String key = "";
			int phase = getPhase();
			boolean truth = true;
			
			world.playSound(posX, posY, posZ, SoundEvents.ENTITY_ENDERDRAGON_GROWL, getSoundCategory(), 30.0F, 0.7F + rand.nextFloat() * 0.1F, false);
			
			switch(type)
			{
				case 0:
					switch(phase)
					{
						case 0:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase1.idle", 10);
							break;
						case 2:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase2.idle", 10);
							break;
						default:
							truth = false;
					}
					break;
				case 1:
					switch(phase)
					{
						case 0:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase1.incombat", 10);
							break;
						case 2:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase2.incombat", 10);
							break;
						default:
							truth = false;
					}
					break;
				case 2:
					switch(phase)
					{
						case 0:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase1.mediumhealth", 5);
							break;
						case 2:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase2.mediumhealth", 5);
							break;
						default:
							truth = false;
					}
					break;
				case 3:
					switch(phase)
					{
						case 0:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase1.lowhealth", 5);
							break;
						case 2:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase2.lowhealth", 5);
							break;
						default:
							truth = false;
					}
					break;
				case 4:
					switch(phase)
					{
						case 0:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase1.criticalhealth", 5);
							break;
						case 2:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase2.criticalhealth", 5);
							break;
						default:
							truth = false;
					}
					break;
				case 5:
					switch(phase)
					{
						case 0:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase1.spawn", 5);
							break;
						case 2:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase2.spawn", 5);
							break;
						default:
							truth = false;
					}
					break;
				case 6:
					switch(phase)
					{
						case 0:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase1.respawn", 5);
							break;
						case 2:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase2.respawn", 5);
							break;
						default:
							truth = false;
					}
					break;
				case 7:
					key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase2", 5);
					break;
				case 8:
					key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase2.start", 5);
					break;
				case 9:
					switch(phase)
					{
						case 0:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase1.dying" + (hardmode ? ".hardmode" : ""), 5);
							break;
						case 1:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase2.prematuredeath" + (hardmode ? ".hardmode" : ""), 5);
							break;
						default:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase2.dying" + (hardmode ? ".hardmode" : ""), 5);
							break;
					}
					break;
				case 10:
					switch(phase)
					{
						case 0:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase1.death" + (hardmode ? ".hardmode" : ""), 5);
							break;
						default:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase2.death" + (hardmode ? ".hardmode" : ""), 5);
							break;
					}
					break;
				case 11:
					if (!antiCheat[0])
					{
						key = TranslateUtil.getTranslationKey("entity.darkness.dialog.cheat.heavydamage", 5);
						antiCheat[0] = true;
					}
					else
						truth = false;
					break;
				case 12:
					if (!antiCheat[1])
					{
						key = TranslateUtil.getTranslationKey("entity.darkness.dialog.cheat.oneshot", 5);
						antiCheat[1] = true;
					}
					else
						truth = false;
					break;
				case 13:
					if (!antiCheat[2])
					{
						key = TranslateUtil.getTranslationKey("entity.darkness.dialog.cheat.sethealth", 5);
						antiCheat[2] = true;
					}
					else
						truth = false;
					break;
				case 14:
					if (!antiCheat[3])
					{
						key = TranslateUtil.getTranslationKey("entity.darkness.dialog.cheat.setmaxhealth", 5);
						antiCheat[3] = true;
					}
					else
						truth = false;
					break;
				case 15:
					if (!antiCheat[4])
					{
						key = TranslateUtil.getTranslationKey("entity.darkness.dialog.cheat.setdead", 5);
						antiCheat[4] = true;
					}
					else
						truth = false;
					break;
				case 16:
					if (!antiCheat[5])
					{
						key = TranslateUtil.getTranslationKey("entity.darkness.dialog.cheat.ondeath", 5);
						antiCheat[5] = true;
					}
					else
						truth = false;
					break;
				case 17:
					if (!antiCheat[6])
					{
						key = TranslateUtil.getTranslationKey("entity.darkness.dialog.cheat.isdead", 5);
						antiCheat[6] = true;
					}
					else
						truth = false;
					break;
				case 18:
					if (!antiCheat[7])
					{
						key = TranslateUtil.getTranslationKey("entity.darkness.dialog.cheat.infinity", 5);
						antiCheat[7] = true;
					}
					else
						truth = false;
					break;
				case 19:
					switch(phase)
					{
						case 0:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase1.lowdamage", 5);
							break;
						case 2:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase2.lowdamage", 5);
							break;
						default:
							truth = false;
					}
					break;
				case 20:
					switch(phase)
					{
						case 0:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase1.normaldamage", 5);
							break;
						case 2:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase2.normaldamage", 5);
							break;
						default:
							truth = false;
					}
					break;
				case 21:
					switch(phase)
					{
						case 0:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase1.highdamage", 5);
							break;
						case 2:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase2.highdamage", 5);
							break;
						default:
							truth = false;
					}
					break;
				case 22:
					switch(phase)
					{
						case 0:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase1.veryhighdamage", 5);
							break;
						case 2:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase2.veryhighdamage", 5);
							break;
						default:
							truth = false;
					}
					break;
				case 23:
					switch(phase)
					{
						case 0:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase1.highcombo", 5);
							break;
						case 2:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase2.highcombo", 5);
							break;
						default:
							truth = false;
					}
					break;
				case 24:
					switch(phase)
					{
						case 0:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase1.veryhighcombo", 5);
							break;
						case 2:
							key = TranslateUtil.getTranslationKey("entity.darkness.dialog.phase2.veryhighcombo", 5);
							break;
						default:
							truth = false;
					}
					break;
				default:
					return;
			}
			if (!key.isEmpty())
			{
				List<EntityPlayerMP> players = getServer().getPlayerList().getPlayers();
				if (!forced)
				{
					for (EntityPlayerMP player : players)
						DialogueManager.sendSubDialogue(player, getName(), TranslateUtil.translateServer(key, player.getDisplayNameString(), name), null, false, DialogColors.COLOR_TEXT_ENEMY, DialogColors.COLOR_BACKGROUND_NORMAL);
				}
				else
				{
					for (EntityPlayerMP player : players)
						DialogueManager.sendSubDialogue(player, getName(), TranslateUtil.translateServer(key, player.getDisplayNameString(), name), null, true, DialogColors.COLOR_TEXT_ENEMY, DialogColors.COLOR_BACKGROUND_NORMAL);
				}
			}
			
			if (truth)
			{
				sayTime = 200 + rand.nextInt(800);
				said[type] = true;
			}
		}
		else if (repeat && said[type])
			said[type] = false;
	}

	public void addPath(Vec3d... route)
	{
		for (Vec3d path : route)
			ROUTE.add(path);
	}
	
	public void moveNextPath()
	{
		if (!ROUTE.isEmpty())
		{
			if (path != null)
				ROUTE.remove(0);
			path = ROUTE.isEmpty() ? null : ROUTE.get(0);
		}
		else if (path != null)
			path = null;
	}
	
	public void clearPath()
	{
		ROUTE.clear();
		path = null;
	}
	
	public boolean finishedPath()
	{
		return ROUTE.size() > 0 && (path == null || ROUTE.get(0).distanceTo(getPositionVector()) < 15.0D) || ROUTE.isEmpty();
	}
	
	@Override
	public boolean isBoss()
	{
		return true;
	}

	@Override
	public EnumTier getTier()
	{
		return EnumTier.TIER7;
	}

	public BeamHitbox getLesserBeam()
	{
		return lesserBeam;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isMusicDead()
	{
		return isDead;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getMusicPriority()
	{
		return 7;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public SoundEvent getMusic()
	{
		return getPhase() > 0 ? ESound.darknessMax : ESound.darkness;
	}
}
