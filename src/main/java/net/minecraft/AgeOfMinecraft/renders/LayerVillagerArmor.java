package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.models.ModelZombieVillager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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


