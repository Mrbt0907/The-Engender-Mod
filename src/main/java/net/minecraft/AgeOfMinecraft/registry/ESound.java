package net.minecraft.AgeOfMinecraft.registry;
import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ESound
{
	public static SoundEvent pegunfire;
	public static SoundEvent pegunjam;
	public static SoundEvent slashflesh;
	public static SoundEvent createMob;
	public static SoundEvent createBossMob;
	public static SoundEvent battlecry;
	public static SoundEvent moralHornBlow;
	public static SoundEvent dragonHornBlow;
	public static SoundEvent converting;
	public static SoundEvent converted;
	public static SoundEvent chaos;
	public static SoundEvent hero;
	public static SoundEvent portalMake;
	public static SoundEvent portalWhoosh;
	public static SoundEvent portalAmbient;
	public static SoundEvent buildingDeath;
	public static SoundEvent lightningShot;
	public static SoundEvent commandBlockWitherSpawn;
	public static SoundEvent commandBlockWitherIdle;
	public static SoundEvent commandBlockWitherGrow;
	public static SoundEvent commandBlockWitherHum;
	public static SoundEvent commandBlockWitherTheme;
	public static SoundEvent witherStormFirstRoar;
	public static SoundEvent witherStormFinish;
	public static SoundEvent witherStormAmbient;
	public static SoundEvent witherStormRoar;
	public static SoundEvent witherStormTentacleWhack;
	public static SoundEvent witherStormFall;
	public static SoundEvent witherStormHurt;
	public static SoundEvent witherStormHurtCommandBlock;
	public static SoundEvent witherStormDeath;
	public static SoundEvent witherStormTheme;
	public static SoundEvent witherStormTheme2;
	public static SoundEvent witherStormTheme3;
	public static SoundEvent fleshHit;
	public static SoundEvent fleshHitPierce;
	public static SoundEvent fleshHitCrush;
	public static SoundEvent fleshHitCrushHeavy;
	public static SoundEvent woodHit;
	public static SoundEvent woodHitPierce;
	public static SoundEvent woodHitCrush;
	public static SoundEvent metalHit;
	public static SoundEvent metalHitPierce;
	public static SoundEvent metalHitCrush;
	public static SoundEvent zombieSpecial;
	public static SoundEvent skeletonSpecial;
	public static SoundEvent pigSpecial;
	public static SoundEvent pigmanSpecial;
	public static SoundEvent golemSpecial;
	public static SoundEvent golemSmash;
	public static SoundEvent witchSpecial;
	public static SoundEvent bugSpecial;
	public static SoundEvent witherSpecial;
	public static SoundEvent heresJohnny;
	public static SoundEvent magicMissileFire;
	public static SoundEvent amalgamate;
	public static SoundEvent jzaharshout;
	public static SoundEvent blast;
	
	public static SoundEvent sans;
	public static SoundEvent nyehhehheh;
	public static SoundEvent bonetrousle;
	public static SoundEvent tstmpwyfSans;
	public static SoundEvent btertd;
	public static SoundEvent megalovania;
	public static SoundEvent dunksandjudgement;
	public static SoundEvent terrariathetwins;
	public static SoundEvent sovngarde;
	public static SoundEvent alduintheme;
	public static SoundEvent darkness;
	public static SoundEvent darknessMax;
	public static SoundEvent unowenwasher;
	public static SoundEvent septetteforthedeadprincess;
	public static SoundEvent battleagainstatruevampire;
	public static SoundEvent jzahartheme;
	public static SoundEvent withertheme;
	public static SoundEvent chaosguardiantheme;
	public static SoundEvent giantmagmagolemtheme;
	public static SoundEvent ghasthertheme;
	public static void registerSounds()
	{
		pegunfire = registerSound("pegunfire");
		pegunjam = registerSound("pegunjam");
		slashflesh = registerSound("slashflesh");
		createMob = registerSound("spawnMob");
		createBossMob = registerSound("bossBirth");
		battlecry = registerSound("battlecry");
		dragonHornBlow = registerSound("hornDragon");
		moralHornBlow = registerSound("hornMorallity");
		converting = registerSound("converting");
		converted = registerSound("converted");
		hero = registerSound("heroBirth");
		chaos = registerSound("chaos");
		portalMake = registerSound("portalCreation");
		portalWhoosh = registerSound("portalExist");
		portalAmbient = registerSound("portalAmbient");
		buildingDeath = registerSound("buildingDeath");
		lightningShot = registerSound("lightningShot");
		heresJohnny = registerSound("heresJohnny");
		magicMissileFire = registerSound("magicMissileFire");
		amalgamate = registerSound("amalgamate");
		jzaharshout = registerSound("jzaharshout");
		blast = registerSound("blast");
		sans = registerSound("sans");
		nyehhehheh = registerSound("nyehhehheh");
		bonetrousle = registerSound("bonetrousle");
		tstmpwyfSans = registerSound("tstmpwyfSans");
		btertd = registerSound("btertd");
		megalovania = registerSound("megalovania");
		dunksandjudgement = registerSound("dunksandjudgement");
		terrariathetwins = registerSound("terrariathetwins");
		sovngarde = registerSound("sovngarde");
		alduintheme = registerSound("alduintheme");
		unowenwasher = registerSound("unowenwasher");
		septetteforthedeadprincess = registerSound("septetteforthedeadprincess");
		battleagainstatruevampire = registerSound("battleagainstatruevampire");
		jzahartheme = registerSound("jzahartheme");
		withertheme = registerSound("withertheme");
		chaosguardiantheme = registerSound("chaosguardiantheme");
		giantmagmagolemtheme = registerSound("giantmagmagolemtheme");
		ghasthertheme = registerSound("ghasthertheme");
		darkness = registerSound("darkness");
		darknessMax = registerSound("darkness_max");
		
		commandBlockWitherSpawn = registerSound("commandBlockWitherSpawn");
		commandBlockWitherIdle = registerSound("commandBlockWitherIdle");
		commandBlockWitherGrow = registerSound("commandBlockWitherGrow");
		commandBlockWitherHum = registerSound("commandBlockWitherHum");
		commandBlockWitherTheme = registerSound("witherstormthemeinitial");
		witherStormFirstRoar = registerSound("witherStormFirstRoar");
		witherStormFinish = registerSound("witherStormFinish");
		witherStormAmbient = registerSound("witherStormAmbient");
		witherStormRoar = registerSound("witherStormRoar");
		witherStormTentacleWhack = registerSound("witherStormTentacleWhack");
		witherStormFall = registerSound("witherStormFall");
		witherStormHurt = registerSound("witherStormHurt");
		witherStormHurtCommandBlock = registerSound("witherStormHurtCommandBlock");
		witherStormDeath = registerSound("witherStormDeath");
		witherStormTheme = registerSound("witherStormTheme");
		witherStormTheme2 = registerSound("witherStormTheme2");
		witherStormTheme3 = registerSound("witherStormTheme3");
		fleshHit = registerSound("sliceFlesh");
		fleshHitPierce = registerSound("pierceFlesh");
		fleshHitCrush = registerSound("crushFlesh");
		fleshHitCrushHeavy = registerSound("crushFleshHeavy");
		woodHit = registerSound("sliceWood");
		woodHitPierce = registerSound("pierceWood");
		woodHitCrush = registerSound("crushWood");
		metalHit = registerSound("sliceMetal");
		metalHitPierce = registerSound("pierceMetal");
		metalHitCrush = registerSound("crushMetal");
		zombieSpecial = registerSound("zombieSpecial");
		pigSpecial = registerSound("pigSpecial");
		skeletonSpecial = registerSound("skeletonSpecial");
		golemSpecial = registerSound("golemSpecial");
		golemSmash = registerSound("golemSmash");
		bugSpecial = registerSound("bugSpecial");
		pigmanSpecial = registerSound("pigmanSpecial");
		witchSpecial = registerSound("witchSpecial");
		witherSpecial = registerSound("witherSpecial");
	}
	public static SoundEvent registerSound(String soundName)
	{
		ResourceLocation id = new ResourceLocation(EngenderMod.MODID, soundName);
		SoundEvent sound = new SoundEvent(id).setRegistryName(id);
		ForgeRegistries.SOUND_EVENTS.register(sound);
		return sound;
	}
}


