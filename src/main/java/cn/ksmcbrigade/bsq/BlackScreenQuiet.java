package cn.ksmcbrigade.bsq;

import cn.ksmcbrigade.bsq.network.BlackMessage;
import cn.ksmcbrigade.bsq.network.ClientHandler;
import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BlackScreenQuiet.MODID)
public class BlackScreenQuiet {

    public static final String MODID = "bsq";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID,"black"),()->"340",(a)->true,(b)->true);

    public BlackScreenQuiet() {
        MinecraftForge.EVENT_BUS.register(this);

        CHANNEL.registerMessage(0, BlackMessage.class,BlackMessage::encode,BlackMessage::decode,(msg, context)->{
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()-> ClientHandler.handle(msg,context));
            context.get().setPacketHandled(true);
        });

        LOGGER.info("Black Screen Quiet mod loaded.");
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event){
        event.getDispatcher().register(Commands.literal("black").requires(s->s.hasPermission(2)).then(Commands.argument("players", EntityArgument.players()).executes(context -> {
            JsonObject object = new JsonObject();
            object.addProperty("ticks",15L*20L);
            for (ServerPlayer player : EntityArgument.getPlayers(context, "players")) {
                CHANNEL.sendTo(new BlackMessage(object), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            }
            context.getSource().sendSystemMessage(CommonComponents.GUI_DONE);
            return 0;
        }).then(Commands.argument("seconds", LongArgumentType.longArg(0L)).executes(context -> {
                    JsonObject object = new JsonObject();
                    object.addProperty("ticks",LongArgumentType.getLong(context,"seconds")*20L);
                    for (ServerPlayer player : EntityArgument.getPlayers(context, "players")) {
                        CHANNEL.sendTo(new BlackMessage(object), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
                    }
                    context.getSource().sendSystemMessage(CommonComponents.GUI_DONE);
                    return 0;
        }).then(Commands.argument("text", StringArgumentType.string()).executes(context -> {
            JsonObject object = new JsonObject();
            object.addProperty("ticks",LongArgumentType.getLong(context,"seconds")*20L);
            object.addProperty("text",StringArgumentType.getString(context,"text"));
            for (ServerPlayer player : EntityArgument.getPlayers(context, "players")) {
                CHANNEL.sendTo(new BlackMessage(object), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            }
            context.getSource().sendSystemMessage(CommonComponents.GUI_DONE);
            return 0;
        }).then(Commands.argument("textColor", IntegerArgumentType.integer()).executes(context -> {
            JsonObject object = new JsonObject();
            object.addProperty("ticks",LongArgumentType.getLong(context,"seconds")*20L);
            object.addProperty("text",StringArgumentType.getString(context,"text"));
            object.addProperty("textColor",IntegerArgumentType.getInteger(context,"textColor"));
            for (ServerPlayer player : EntityArgument.getPlayers(context, "players")) {
                CHANNEL.sendTo(new BlackMessage(object), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            }
            context.getSource().sendSystemMessage(CommonComponents.GUI_DONE);
            return 0;
        }).then(Commands.argument("backgroundColor", IntegerArgumentType.integer()).executes(context -> {
            JsonObject object = new JsonObject();
            object.addProperty("ticks",LongArgumentType.getLong(context,"seconds")*20L);
            object.addProperty("text",StringArgumentType.getString(context,"text"));
            object.addProperty("textColor",IntegerArgumentType.getInteger(context,"textColor"));
            object.addProperty("background",IntegerArgumentType.getInteger(context,"backgroundColor"));
            for (ServerPlayer player : EntityArgument.getPlayers(context, "players")) {
                CHANNEL.sendTo(new BlackMessage(object), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            }
            context.getSource().sendSystemMessage(CommonComponents.GUI_DONE);
            return 0;
        })))))));

        event.getDispatcher().register(Commands.literal("end-black").then(Commands.argument("players",EntityArgument.players()).executes(context -> {
            JsonObject object = new JsonObject();
            object.addProperty("end",true);
            for (ServerPlayer player : EntityArgument.getPlayers(context, "players")) {
                CHANNEL.sendTo(new BlackMessage(object),player.connection.connection,NetworkDirection.PLAY_TO_CLIENT);
            }
            context.getSource().sendSystemMessage(CommonComponents.GUI_DONE);
            return 0;
        })));
    }
}
