package net.minecraft.AgeOfMinecraft.entity.tier3;
import javax.annotation.Nullable;

import net.endermanofdoom.mac.util.TranslateUtil;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityMagmaCube extends EntitySlime
{
	public EntityMagmaCube(World worldIn)
	{
		super(worldIn);
		this.isImmuneToFire = true;
	}
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
	}
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if (isWet())
		{
			playSound(SoundEvents.ENTITY_GENERIC_BURN, 1F, 1F);
			attackEntityFrom((new DamageSource("cooler")).setDamageBypassesArmor().setDamageIsAbsolute().setDifficultyScaled(), 4F);
		}
	}
	protected boolean isValidLightLevel()
	{
		return true;
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

			return TranslateUtil.translateServer("entity." + s + ".name");
		
	}
	public boolean isNotColliding()
	{
		return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(this.getEntityBoundingBox());
	}
	public void setSlimeSize(int size)
	{
		super.setSlimeSize(size);
		getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(size * 2);
	}
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender()
	{
		return 15728880;
	}
	public float getBrightness()
	{
		return 1.0F;
	}
	protected EnumParticleTypes getParticleType()
	{
		return EnumParticleTypes.FLAME;
	}
	protected EntitySlime createInstance()
	{
		return new EntityMagmaCube(this.world);
	}
	@Nullable
	protected ResourceLocation getLootTable()
	{
		return this.isSmallSlime() ? LootTableList.EMPTY : ELoot.ENTITIES_MAGMA_CUBE;
	}
	public boolean isBurning()
	{
		return false;
	}
	protected int getJumpDelay()
	{
		return super.getJumpDelay() * 4;
	}
	protected void alterSquishAmount()
	{
		this.squishAmount *= 0.9F;
	}
	protected void jump()
	{
		playSound(getJumpSound(), getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / (1.1F));
		this.motionY += (0.42F + getSlimeSize() * 0.1F);
		this.isAirBorne = true;
		net.minecraftforge.common.ForgeHooks.onLivingJump(this);
	}
	protected void handleJumpLava()
	{
		this.motionY = (0.22F + getSlimeSize() * 0.05F);
		this.isAirBorne = true;
	}
	protected boolean canDamagePlayer()
	{
		return true;
	}
	protected int getAttackStrength()
	{
		return super.getAttackStrength() + 2;
	}
	protected SoundEvent getJumpSound()
	{
		return func_189101_db() ? SoundEvents.ENTITY_SMALL_MAGMACUBE_SQUISH : SoundEvents.ENTITY_MAGMACUBE_JUMP;
	}
	protected boolean makesSoundOnLand()
	{
		return true;
	}
	protected SoundEvent getHurtSound(DamageSource source)
	{
		return func_189101_db() ? SoundEvents.ENTITY_SMALL_MAGMACUBE_HURT : SoundEvents.ENTITY_MAGMACUBE_HURT;
	}
	protected SoundEvent getDeathSound()
	{
		return func_189101_db() ? SoundEvents.ENTITY_SMALL_MAGMACUBE_DEATH : SoundEvents.ENTITY_MAGMACUBE_DEATH;
	}
	protected SoundEvent func_184709_cY()
	{
		return func_189101_db() ? SoundEvents.ENTITY_SMALL_MAGMACUBE_SQUISH : SoundEvents.ENTITY_MAGMACUBE_SQUISH;
	}
	protected SoundEvent func_184710_cZ()
	{
		return func_189101_db() ? SoundEvents.ENTITY_SMALL_MAGMACUBE_SQUISH : SoundEvents.ENTITY_MAGMACUBE_JUMP;
	}
}


