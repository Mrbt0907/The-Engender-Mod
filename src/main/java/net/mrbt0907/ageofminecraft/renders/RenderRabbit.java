package net.mrbt0907.ageofminecraft.renders;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.tier1.EntityRabbit;
import net.mrbt0907.ageofminecraft.models.ModelRabbit;
@SideOnly(Side.CLIENT)

public class RenderRabbit extends RenderEngendered<EntityRabbit>
{
	private static final ResourceLocation BROWN = new ResourceLocation("textures/entity/rabbit/brown.png");
	private static final ResourceLocation WHITE = new ResourceLocation("textures/entity/rabbit/white.png");
	private static final ResourceLocation BLACK = new ResourceLocation("textures/entity/rabbit/black.png");
	private static final ResourceLocation GOLD = new ResourceLocation("textures/entity/rabbit/gold.png");
	private static final ResourceLocation SALT = new ResourceLocation("textures/entity/rabbit/salt.png");
	private static final ResourceLocation WHITE_SPLOTCHED = new ResourceLocation("textures/entity/rabbit/white_splotched.png");
	private static final ResourceLocation TOAST = new ResourceLocation("textures/entity/rabbit/toast.png");
	private static final ResourceLocation CAERBANNOG = new ResourceLocation("textures/entity/rabbit/caerbannog.png");
	
	public RenderRabbit(RenderManager manager)
	{
		super(manager, new ModelRabbit(), 0.3F);
	}
	
	protected ResourceLocation getEntityTexture(EntityRabbit entity)
	{
		String s = TextFormatting.getTextWithoutFormattingCodes(entity.getName());
		
		if (s != null && s.equals("Toast"))
			return TOAST;
		else
			switch (entity.getRabbitType())
			{
				case 0:
				default:
					return BROWN;
				case 1:
					return WHITE;
				case 2:
					return BLACK;
				case 3:
					return WHITE_SPLOTCHED;
				case 4:
					return GOLD;
				case 5:
					return SALT;
				case 99:
					return CAERBANNOG;
			}
	}

	protected boolean isVisible(EntityRabbit entity)
	{
		return !entity.isInvisible() || renderOutlines;
	}
}