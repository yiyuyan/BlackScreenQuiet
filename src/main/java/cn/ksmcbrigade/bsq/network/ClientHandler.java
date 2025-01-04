package cn.ksmcbrigade.bsq.network;

import cn.ksmcbrigade.bsq.screen.QuietScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class ClientHandler {
    public static void handle(BlackMessage msg, Supplier<NetworkEvent.Context> ctx){
        if(!msg.data().has("end")){
            try {
                Minecraft.getInstance().screen = new QuietScreen(msg.data());
            } catch (NoSuchFieldException e) {
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.displayClientMessage(Component.literal("Can't open the quiet screen."),false);
                }
                e.printStackTrace();
            }
        }
        else{
            if(Minecraft.getInstance().screen instanceof QuietScreen quietScreen){
                quietScreen.end();
            }
        }
    }
}
