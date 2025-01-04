package cn.ksmcbrigade.bsq.network;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.network.FriendlyByteBuf;

public record BlackMessage(JsonObject data) {

    public static void encode(BlackMessage MSG, FriendlyByteBuf buf) {
        buf.writeUtf(MSG.data.toString());
    }

    public static BlackMessage decode(FriendlyByteBuf buf) {
        return new BlackMessage(JsonParser.parseString(buf.readUtf()).getAsJsonObject());
    }
}
