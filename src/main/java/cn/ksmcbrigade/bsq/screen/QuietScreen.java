package cn.ksmcbrigade.bsq.screen;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class QuietScreen extends Screen {

    private final long ticks;
    private long tick;

    private String text = "黑屏安静";

    private int backgroundColor = Color.BLACK.getRGB();
    private int textColor = Color.YELLOW.getRGB();

    public QuietScreen(JsonObject data) throws NoSuchFieldException {
        super(Component.literal("QuietScreen"));

        this.ticks = data.get("ticks").getAsLong();
        tick = 0;

        if(data.get("text")!=null){
            text = data.get("text").getAsString();
        }
        if(data.get("background")!=null){
            backgroundColor = data.get("background").getAsInt();
        }
        if(data.get("textColor")!=null){
            textColor = data.get("textColor").getAsInt();
        }

        this.minecraft = Minecraft.getInstance();
    }

    @Override
    public boolean mouseClicked(double p_94695_, double p_94696_, int p_94697_) {
        return false;
    }

    @Override
    public boolean mouseReleased(double p_94722_, double p_94723_, int p_94724_) {
        return false;
    }

    @Override
    public boolean mouseScrolled(double p_94686_, double p_94687_, double p_94688_) {
        return false;
    }

    @Override
    public boolean keyPressed(int p_96552_, int p_96553_, int p_96554_) {
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void onClose() {
    }

    @Override
    protected void init() {
        if(minecraft==null) minecraft = Minecraft.getInstance();
        minecraft.getToasts().clear();
    }

    @Override
    public void render(GuiGraphics p_281549_, int p_281550_, int p_282878_, float p_282465_) {
        p_281549_.fill(0,0,p_281549_.guiWidth(),p_281549_.guiHeight(),backgroundColor);
        if(!this.text.contains("|")){
            p_281549_.drawString(Minecraft.getInstance().font, this.text,(p_281549_.guiWidth() - Minecraft.getInstance().font.width(this.text)) /2,p_281549_.guiHeight() /2 - Minecraft.getInstance().font.lineHeight,textColor);
        }
        else{
            String[] texts = this.text.split("\\|");
            for (int i = 0; i < this.text.split("\\|").length; i++) {
                p_281549_.drawString(Minecraft.getInstance().font, texts[i],(p_281549_.guiWidth() - Minecraft.getInstance().font.width(texts[i])) /2,p_281549_.guiHeight() /2 - Minecraft.getInstance().font.lineHeight+i*Minecraft.getInstance().font.lineHeight,textColor);
            }
        }
    }

    @Override
    public void tick() {
        if(tick>=ticks){
            tick = 0;
            Minecraft.getInstance().screen = null;
        }
        else{
            tick++;
        }
        if(minecraft==null) minecraft = Minecraft.getInstance();
        minecraft.getToasts().clear();
    }

    public void end() {
        this.tick = 0;
        Minecraft.getInstance().screen = null;
    }
}
