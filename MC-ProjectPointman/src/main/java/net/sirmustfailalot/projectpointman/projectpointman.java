package net.sirmustfailalot.projectpointman;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(projectpointman.MOD_ID)
public class projectpointman
{
    public static final String MOD_ID = "projectpointman";
    private static final Logger LOGGER = LogUtils.getLogger();

    public projectpointman()    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(KeybindHandler::registerKeyMappings);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            KeybindHandler.registerClientEvents();
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class KeybindHandler {

        private static final KeyMapping keybind = new KeyMapping("key.projectpointman.crouch_and_click", GLFW.GLFW_KEY_V, "key.categories.projectpointman");

        private static boolean isCrouching = false;

        // Registering the key input handler on the Forge Event Bus
        public static void registerClientEvents() {
            MinecraftForge.EVENT_BUS.register(new KeybindHandler());
        }


        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (keybind.consumeClick()) {
                isCrouching = !isCrouching;
                Minecraft mc = Minecraft.getInstance();
                LocalPlayer player = mc.player;

                if (player != null) {
                    if (isCrouching) {
                        new Thread(() -> {
                            try {
                                while (isCrouching) {
                                    // Set crouch state
                                    mc.execute(() -> player.setShiftKeyDown(true));

                                    // Perform right-click if hitResult is a block
                                    mc.execute(() -> {
                                        if (mc.hitResult instanceof BlockHitResult blockHitResult) {
                                            mc.gameMode.useItemOn(player, InteractionHand.MAIN_HAND, blockHitResult);
                                        }
                                    });

                                    // Wait for the specified interval (e.g., 150 ms)
                                    Thread.sleep(150);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }).start();
                    } else {
                        mc.execute(() -> player.setShiftKeyDown(false));
                    }
                }
            }
        }

        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(keybind);
        }
    }
}

