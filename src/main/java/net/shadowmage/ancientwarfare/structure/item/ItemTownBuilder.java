package net.shadowmage.ancientwarfare.structure.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.shadowmage.ancientwarfare.core.config.AWLog;
import net.shadowmage.ancientwarfare.core.interfaces.IItemClickable;
import net.shadowmage.ancientwarfare.core.interfaces.IItemKeyInterface;
import net.shadowmage.ancientwarfare.core.util.BlockPosition;
import net.shadowmage.ancientwarfare.core.util.BlockTools;
import net.shadowmage.ancientwarfare.structure.town.TownBoundingArea;
import net.shadowmage.ancientwarfare.structure.town.TownGenerator;
import net.shadowmage.ancientwarfare.structure.town.TownPlacementValidator;
import net.shadowmage.ancientwarfare.structure.town.TownTemplate;
import net.shadowmage.ancientwarfare.structure.town.TownTemplateManager;

public class ItemTownBuilder extends Item implements IItemKeyInterface, IItemClickable
{

/**
 * @param itemID
 */
public ItemTownBuilder(String itemName)
  {
  this.setUnlocalizedName(itemName);
  this.setCreativeTab(AWStructuresItemLoader.structureTab);
  this.setMaxStackSize(1);  
  this.setTextureName("ancientwarfare:structure/structure_builder");//TODO make texture...
  }

@Override
public boolean cancelRightClick(EntityPlayer player, ItemStack stack)
  {
  return true;
  }

@Override
public boolean cancelLeftClick(EntityPlayer player, ItemStack stack)
  {
  return false;
  }

//@SuppressWarnings({ "unchecked", "rawtypes" })
//@Override
//public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
//  {
//  String structure = "guistrings.no_selection";
//  ItemStructureSettings.getSettingsFor(stack, viewSettings);
//  if(viewSettings.hasName())
//    {
//    structure = viewSettings.name;
//    }  
//  list.add(StatCollector.translateToLocal("guistrings.current_structure")+" "+StatCollector.translateToLocal(structure));
//  }

@Override
public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player)
  {
  return false;
  }

@Override
public boolean onKeyActionClient(EntityPlayer player, ItemStack stack, ItemKey key)
  {
  return key==ItemKey.KEY_0;
  }

@Override
public void onKeyAction(EntityPlayer player, ItemStack stack, ItemKey key)
  {
  if(player==null || player.worldObj.isRemote)
    {
    return;
    }
  BlockPosition pos = BlockTools.getBlockClickedOn(player, player.worldObj, true);
  if(pos!=null)
    {
    TownBoundingArea area = TownPlacementValidator.findGenerationPosition(player.worldObj, pos.x, pos.y, pos.z);
    AWLog.logDebug("Found area: "+area);
    if(area!=null)
      {
      TownTemplate template = TownTemplateManager.instance().selectTemplateForGeneration(player.worldObj, pos.x, pos.z, area);
      if(template!=null)
        {
        TownGenerator gen = new TownGenerator(player.worldObj, area, template);
        gen.generate();        
        }
      else
        {
        AWLog.logDebug("Could not retrieve template for generation...");
        }
      }
    }
  }

@Override
public boolean onRightClickClient(EntityPlayer player, ItemStack stack){return false;}//TODO return true when switching to using GUI

@Override
public void onRightClick(EntityPlayer player, ItemStack stack){}//TODO open town-type selection GUI

@Override
public boolean onLeftClickClient(EntityPlayer player, ItemStack stack){return false;}

@Override
public void onLeftClick(EntityPlayer player, ItemStack stack){}

}
