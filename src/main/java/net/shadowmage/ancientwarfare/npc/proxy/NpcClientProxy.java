package net.shadowmage.ancientwarfare.npc.proxy;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.MinecraftForge;
import net.shadowmage.ancientwarfare.core.config.ClientOptions;
import net.shadowmage.ancientwarfare.core.input.InputHandler;
import net.shadowmage.ancientwarfare.core.input.InputHandler.InputCallback;
import net.shadowmage.ancientwarfare.core.network.NetworkHandler;
import net.shadowmage.ancientwarfare.core.util.RayTraceUtils;
import net.shadowmage.ancientwarfare.npc.AncientWarfareNPC;
import net.shadowmage.ancientwarfare.npc.entity.NpcBase;
import net.shadowmage.ancientwarfare.npc.gui.GuiNpcInventory;
import net.shadowmage.ancientwarfare.npc.gui.GuiUpkeepOrder;
import net.shadowmage.ancientwarfare.npc.gui.GuiWorkOrder;
import net.shadowmage.ancientwarfare.npc.item.AWNpcItemLoader;
import net.shadowmage.ancientwarfare.npc.npc_command.NpcCommand;
import net.shadowmage.ancientwarfare.npc.npc_command.NpcCommand.CommandType;
import net.shadowmage.ancientwarfare.npc.render.RenderCommandOverlay;
import net.shadowmage.ancientwarfare.npc.render.RenderNpcBase;
import net.shadowmage.ancientwarfare.npc.render.RenderWorkLines;
import net.shadowmage.ancientwarfare.npc.skin.NpcSkinManager;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

public class NpcClientProxy extends NpcCommonProxy
{

@Override
public void registerClient()
  {
  NetworkHandler.registerGui(NetworkHandler.GUI_NPC_INVENTORY, GuiNpcInventory.class);
  NetworkHandler.registerGui(NetworkHandler.GUI_NPC_WORK_ORDER, GuiWorkOrder.class);
  NetworkHandler.registerGui(NetworkHandler.GUI_NPC_UPKEEP_ORDER, GuiUpkeepOrder.class);
  RenderingRegistry.registerEntityRenderingHandler(NpcBase.class, new RenderNpcBase());
  MinecraftForge.EVENT_BUS.register(RenderWorkLines.INSTANCE);
  FMLCommonHandler.instance().bus().register(RenderCommandOverlay.INSTANCE);//register overlay renderer
  MinecraftForge.EVENT_BUS.register(RenderCommandOverlay.INSTANCE);//register block/entity highlight renderer
  registerClientOptions();
  registerKeybinds();
  }

private void registerKeybinds()
  {
  InputHandler.instance().registerKeybind(InputHandler.KEY_NPC_ATTACK, Keyboard.KEY_X);
  InputHandler.instance().registerKeybind(InputHandler.KEY_NPC_MOVE, Keyboard.KEY_C);//TODO register new inputcallback
  InputHandler.instance().registerKeybind(InputHandler.KEY_NPC_HOME, Keyboard.KEY_V);//TODO register new inputcallback
  InputHandler.instance().registerKeybind(InputHandler.KEY_NPC_UPKEEP, Keyboard.KEY_B);//TODO register new inputcallback
  InputHandler.instance().addInputCallback(InputHandler.KEY_NPC_ATTACK, new BatonInputCallback(CommandType.ATTACK));
  }

private class BatonInputCallback extends InputCallback
{
CommandType type;
private BatonInputCallback(CommandType type){this.type=type;}
@Override
public void onKeyPressed()
  {  
  }
@Override
public void onKeyReleased()
  {
  Minecraft mc = Minecraft.getMinecraft();
  if(mc==null || mc.thePlayer==null || mc.currentScreen!=null || mc.thePlayer.getCurrentEquippedItem()==null || mc.thePlayer.getCurrentEquippedItem().getItem()!=AWNpcItemLoader.commandBaton){return;}
  MovingObjectPosition pos = RenderCommandOverlay.INSTANCE.getClientTarget();
  if(pos!=null){NpcCommand.handleCommandClient(type, pos);}
  }
}

private void registerClientOptions()
  {
  ClientOptions.INSTANCE.registerClientOption(ClientOptions.OPTION_RENDER_NPC_ADDITIONAL_INFO, "Main control for additional npc-related rendering", true, AncientWarfareNPC.config);
  ClientOptions.INSTANCE.registerClientOption(ClientOptions.OPTION_RENDER_NPC_AI, "Render NPC AI Tasks", true, AncientWarfareNPC.config);
  ClientOptions.INSTANCE.registerClientOption(ClientOptions.OPTION_RENDER_NPC_FRIENDLY_NAMES, "Render friendly/neutral NPC nameplates", true, AncientWarfareNPC.config);
  ClientOptions.INSTANCE.registerClientOption(ClientOptions.OPTION_RENDER_NPC_HOSTILE_NAMES, "Render hostile NPC nameplates", true, AncientWarfareNPC.config);
  }

@Override
public void loadSkins()
  {
  NpcSkinManager.INSTANCE.loadSkinPacks();
  }
}
