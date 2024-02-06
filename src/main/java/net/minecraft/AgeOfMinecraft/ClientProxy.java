package net.minecraft.AgeOfMinecraft;

import net.minecraft.AgeOfMinecraft.entity.EntityManaOrb;
import net.minecraft.AgeOfMinecraft.entity.EntityPortal;
import net.minecraft.AgeOfMinecraft.entity.EntityPortalLightning;
import net.minecraft.AgeOfMinecraft.entity.cameos.Darkness.EntityDarkProjectile;
import net.minecraft.AgeOfMinecraft.entity.cameos.Darkness.EntityDarkness;
import net.minecraft.AgeOfMinecraft.entity.tier1.*;
import net.minecraft.AgeOfMinecraft.entity.tier2.*;
import net.minecraft.AgeOfMinecraft.entity.tier3.*;
import net.minecraft.AgeOfMinecraft.entity.tier4.*;
import net.minecraft.AgeOfMinecraft.entity.tier5.*;
import net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases.EntityAreaEffectCloudOther;
import net.minecraft.AgeOfMinecraft.entity.tier6.*;
import net.minecraft.AgeOfMinecraft.registry.EItem;
import net.minecraft.AgeOfMinecraft.registry.EParticle;
import net.minecraft.AgeOfMinecraft.renders.*;
import net.minecraft.AgeOfMinecraft.registry.EBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.entity.RenderTippedArrow;
import net.minecraft.init.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent e)
	{
		MinecraftForge.EVENT_BUS.register(EParticle.class);
		renderEntities();
		super.preInit(e);
	}

	@Override
	public void init(FMLInitializationEvent e)
	{
		super.init(e);
	}

	@Override
	public void postInit(FMLPostInitializationEvent e)
	{
		super.postInit(e);
		EItem.registerRenders();
		EBlock.registerRenders();
	}

	public void renderEntities()
	{
		//TileEntityRendererDispatcher.instance.renderers.put(TileEntityBeaconSPC.class, new TileEntityRenderBeaconSPC());
		
		 RenderingRegistry.registerEntityRenderingHandler(EntityManaOrb.class, manager -> new RenderManaOrb(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityAreaEffectCloudOther.class, manager -> new RenderNullEntity(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityEversource.class, manager -> new RenderEversource(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityIceSpider.class, manager -> new RenderIceSpider(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityCreeder.class, manager -> new RenderCreeder(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityIcyEnderCreeper.class, manager -> new RenderIcyEnderCreeper(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityIceGolem.class, manager -> new RenderIceGolem(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityMagmaGolem.class, manager -> new RenderMagmaGolem(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityPrisonSlime.class, manager -> new RenderPrisonSlime(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityPrisonSpider.class, manager -> new RenderPrisonSpider(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityPrisonGolem.class, manager -> new RenderPrisonGolem(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityGhasther.class, manager -> new RenderGhasther(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityAbomniableSnowman.class, manager -> new RenderAbomniableSnowman(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityMagicMissile.class, manager -> new RenderMagicMissile<EntityMagicMissile>(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityEvoker.class, manager -> new RenderEvoker(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityLlama.class, manager -> new RenderLlama(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityIllusioner.class, manager -> new RenderIllusioner(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityVindicator.class, manager -> new RenderVindicator(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityVex.class, manager -> new RenderVex(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityPolarBear.class, manager -> new RenderPolarBear(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityCaveSpider.class, manager -> new RenderCaveSpider(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntitySpider.class, manager -> new RenderSpider<EntitySpider>(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityPig.class, manager -> new RenderPig(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntitySheep.class, manager -> new RenderSheep(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityCow.class, manager -> new RenderCow(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityMooshroom.class, manager -> new RenderMooshroom(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityWolf.class, manager -> new RenderWolf(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityChicken.class, manager -> new RenderChicken(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityParrot.class, manager -> new RenderParrot(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityOcelot.class, manager -> new RenderOcelot(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityRabbit.class, manager -> new RenderRabbit(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntitySilverfish.class, manager -> new RenderSilverfish(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityEndermite.class, manager -> new RenderEndermite(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityCreeper.class, manager -> new RenderCreeper(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityEnderman.class, manager -> new RenderEnderman(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntitySnowman.class, manager -> new RenderSnowMan(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntitySkeleton.class, manager -> new RenderSkeleton(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityWitch.class, manager -> new RenderWitch(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityBlaze.class, manager -> new RenderBlaze(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityPigZombie.class, manager -> new RenderPigZombie(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityZombie.class, manager -> new RenderZombie(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntitySlime.class, manager -> new RenderSlime(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityMagmaCube.class, manager -> new RenderMagmaCube(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityGiant.class, manager -> new RenderGiant(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityGhast.class, manager -> new RenderGhast(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntitySquid.class, manager -> new RenderSquid(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityVillager.class, manager -> new RenderVillager(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityIronGolem.class, manager -> new RenderIronGolem(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityBat.class, manager -> new RenderBat(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityGuardian.class, manager -> new RenderGuardian(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityElderGuardian.class, manager -> new RenderGuardian(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityShulker.class, manager -> new RenderShulker(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityEnderDragon.class, manager -> new RenderEnderDragon(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityWither.class, manager -> new RenderWither(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityCommandBlockWither.class, manager -> new RenderCommandBlockWither(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityWitherStormSkull.class, manager -> new RenderStormSkull(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityWitherStorm.class, manager -> new RenderWitherStorm(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityWitherStormHead.class, manager -> new RenderWitherStormHead(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityWitherStormTentacle.class, manager -> new RenderWitherStormTentacle(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityWitherStormTentacleDevourer.class, manager -> new RenderWitherStormTentacleDevourer(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityPortal.class, manager -> new RenderPortal(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityPortalLightning.class, manager -> new RenderNullEntity(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityDisintigrationRay.class, manager -> new RenderNullEntity(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityInvisibleFangsProjectile.class, manager -> new RenderNullEntity(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityFrostRay.class, manager -> new RenderNullEntity(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntitySnowballHarmful.class, manager -> new RenderSnowball<EntitySnowballHarmful>(manager, Items.SNOWBALL, Minecraft.getMinecraft().getRenderItem()));
		 RenderingRegistry.registerEntityRenderingHandler(EntityTippedArrowOther.class, manager -> new RenderTippedArrow(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityDarkness.class, manager -> new RenderDarkness(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityDarkProjectile.class, manager -> new RenderDarkProjectile(manager));
	}
}