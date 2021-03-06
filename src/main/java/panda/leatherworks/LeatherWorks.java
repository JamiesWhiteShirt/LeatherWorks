package panda.leatherworks;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.ExistingSubstitutionException;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.Type;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import panda.leatherworks.common.CommonProxy;
import panda.leatherworks.common.GuiHandler;
import panda.leatherworks.common.eventhandler.BucketHandler;
import panda.leatherworks.common.eventhandler.DebarkHandler;
import panda.leatherworks.common.eventhandler.LivingDropsHandler;
import panda.leatherworks.common.crafting.RecipeRepair;
import panda.leatherworks.common.crafting.RecipeRepairLeatherArmor;
import panda.leatherworks.common.crafting.RecipeScraping;
import panda.leatherworks.common.crafting.RecipeShears;
import panda.leatherworks.common.tileentity.TileEntityDryingRack;
import panda.leatherworks.init.LWItems;
import panda.leatherworks.init.LWRecipes;

@Mod(modid = LeatherWorks.MODID, name = LeatherWorks.NAME, version = LeatherWorks.VERSION)
public class LeatherWorks {
	public static final String MODID = "leatherworks";
	public static final String VERSION = "1.49.0";
	public static final String NAME = "Leather Works";
	public static SimpleNetworkWrapper wrapper;
	@Mod.Instance(MODID)
	public static LeatherWorks INSTANCE;

	@SidedProxy(
			clientSide = "panda.leatherworks.client.ClientProxy",
			serverSide = "panda.leatherworks.server.ServerProxy"
	)
	public static CommonProxy PROXY;

	public static Logger log = LogManager.getLogger(NAME);
//Add tanner to village?
	@EventHandler
	public void preinit(FMLPreInitializationEvent event) throws ExistingSubstitutionException {
		wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(LeatherWorks.MODID);
		PROXY.registerMessageHandlers(wrapper);

		LWRecipes.register();

		PROXY.registerModels();

		MinecraftForge.EVENT_BUS.register(new LivingDropsHandler());
		MinecraftForge.EVENT_BUS.register(new BucketHandler());
		MinecraftForge.EVENT_BUS.register(new DebarkHandler());
		
		GameRegistry.addSubstitutionAlias("minecraft:leather_chestplate", Type.ITEM, LWItems.LEATHER_CHESTPLATE);
		GameRegistry.addSubstitutionAlias("minecraft:leather_boots",      Type.ITEM, LWItems.LEATHER_BOOTS);
		GameRegistry.addSubstitutionAlias("minecraft:leather_helmet", 	  Type.ITEM, LWItems.LEATHER_HELMET);
		GameRegistry.addSubstitutionAlias("minecraft:leather_leggings",   Type.ITEM, LWItems.LEATHER_LEGGINGS);
		//GameRegistry.findItem("gotwood", name);
		
		
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());

		GameRegistry.registerTileEntity(TileEntityDryingRack.class, "leatherworks:drying_rack");
	}

	@EventHandler
	public void init(FMLInitializationEvent event) throws ExistingSubstitutionException {
		
		RecipeSorter.register("leatherworks:scrapingrecipe", RecipeScraping.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
		RecipeSorter.register("leatherworks:shearsrecipe", RecipeShears.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
		RecipeSorter.register("leatherworks:repairleatherarmorrecipe", RecipeRepairLeatherArmor.class,RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
		RecipeSorter.register("leatherworks:repairrecipe", RecipeRepair.class,RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

		PROXY.registerColorHandlers();

		CraftingManager.getInstance().addRecipe(new RecipeScraping(new ItemStack(LWItems.CRAFTING_LEATHER,1,0),new ArrayList<ItemStack>(Arrays.asList(new ItemStack(
			LWItems.RAWHIDE,1,OreDictionary.WILDCARD_VALUE), new ItemStack(Items.FLINT)))));
		CraftingManager.getInstance().addRecipe(new RecipeScraping(new ItemStack(LWItems.CRAFTING_LEATHER,1,0),new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Items.RABBIT_HIDE), new ItemStack(Items.FLINT)))));
		
		CraftingManager.getInstance().addRecipe(new RecipeShears(new ItemStack(LWItems.LEATHER_STRIP,4),new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Items.LEATHER), new ItemStack(Items.SHEARS,1,OreDictionary.WILDCARD_VALUE)))));
		CraftingManager.getInstance().addRecipe(new RecipeShears(new ItemStack(LWItems.LEATHER_STRIP,8),new ArrayList<ItemStack>(Arrays.asList(new ItemStack(
			LWItems.LEATHER_SHEET), new ItemStack(Items.SHEARS,1,OreDictionary.WILDCARD_VALUE)))));
		CraftingManager.getInstance().addRecipe( new RecipeRepairLeatherArmor());
		CraftingManager.getInstance().addRecipe( new RecipeRepair());
		
		
	
	}
	
	public static final CreativeTabs LeatherTab = new CreativeTabs(LeatherWorks.MODID) {
		@Override
		public Item getTabIconItem() {
			return LWItems.RAWHIDE;
		}

	};
	
}