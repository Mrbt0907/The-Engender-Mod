package net.mrbt0907.ageofminecraft.entity.tier1;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.entity.EnumTier;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EnumAIStance;
import net.mrbt0907.ageofminecraft.registry.LootRegistry;

public class EntityOcelot extends EntityEngendered
{
	public EntityOcelot(World worldIn)
	{
		super(worldIn);
		setSize(0.6F, 0.7F);
	}
	
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
	}

	//----- OVERRIDES -----\\
	protected ResourceLocation getLootTable() {return LootRegistry.ENTITIES_OCELOT;}
	protected SoundEvent getHurtSound(DamageSource source) {return SoundEvents.ENTITY_CAT_HURT;}
	protected SoundEvent getDeathSound() {return SoundEvents.ENTITY_CAT_DEATH;}
	
	@Override
	public EnumTier getTier() {return null;}
	@Override
	public long getBaseVigor() {return 10;}
	@Override
	public long getBaseStrength() {return 0;}
	@Override
	public long getBaseStamina() {return 0;}
	@Override
	public long getBaseIntelligence() {return 0;}
	@Override
	public long getBaseDexterity() {return 0;}
	@Override
	public long getBaseAgility() {return 0;}
	@Override
	public EnumAIStance getDefaultStance() {return EnumAIStance.AGGRESSIVE;}
	
	public void updateAITasks()
	{
		super.updateAITasks();
		if (getMoveHelper().isUpdating())
		{
			double speed = getMoveHelper().getSpeed();
			if (speed <= 0.6D)
			{
				setSneaking(true);
				setSprinting(false);
			}
			else if (speed >= 1.33D)
			{
				setSneaking(false);
				setSprinting(true);
			}
			else
			{
				setSneaking(false);
				setSprinting(false);
			}
		}
		else
		{
			setSneaking(false);
			setSprinting(false);
		}
	}
}