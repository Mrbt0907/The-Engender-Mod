package net.mrbt0907.ageofminecraft.renders;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.models.ModelZombieVillager;
@SideOnly(Side.CLIENT)

public class LayerVillagerArmor extends LayerBipedArmor
{
	public LayerVillagerArmor(RenderLivingBase<?> rendererIn)
	{
		super(rendererIn);
	}
	protected void initArmor()
	{
		this.modelLeggings = new ModelZombieVillager(0.5F, 0.0F, true);
		this.modelArmor = new ModelZombieVillager(1.0F, 0.0F, true);
	}
}


