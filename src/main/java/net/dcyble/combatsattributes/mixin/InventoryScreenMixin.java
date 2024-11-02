package net.dcyble.combatsattributes.mixin;

import io.netty.buffer.Unpooled;
import net.dcyble.combatsattributes.CombatsAttributesClient;
import net.dcyble.combatsattributes.playerprogress.clientlogic.FreePoints;
import net.dcyble.combatsattributes.playerprogress.clientlogic.Level;
import net.dcyble.combatsattributes.playerprogress.clientlogic.XP;
import net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Agility;
import net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Intelligence;
import net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Strength;
import net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Vitality;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.dcyble.combatsattributes.CombatsAttributesClient.CLIENT_TO_SERVER_A;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.FreePoints.freePoints;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Strength.strength;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Agility.agility;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Intelligence.intelligence;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Vitality.vitality;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Strength.addedStrength;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Agility.addedAgility;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Intelligence.addedIntelligence;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Vitality.addedVitality;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends HandledScreen<GenericContainerScreenHandler> {

    public InventoryScreenMixin(GenericContainerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Unique
    private TexturedButtonWidget panelSwitchButton;
    @Unique
    private TexturedButtonWidget strengthGainButton;
    @Unique
    private TexturedButtonWidget agilityGainButton;
    @Unique
    private TexturedButtonWidget intelligenceGainButton;
    @Unique
    private TexturedButtonWidget vitalityGainButton;
    @Unique
    private TexturedButtonWidget strengthLossButton;
    @Unique
    private TexturedButtonWidget agilityLossButton;
    @Unique
    private TexturedButtonWidget intelligenceLossButton;
    @Unique
    private TexturedButtonWidget vitalityLossButton;
    @Unique
    private TexturedButtonWidget acceptButton;

    @Unique
    private Identifier panelSwitchButtonTexture = new Identifier("combats_attributes", "textures/gui/panel_button.png");
    @Unique
    private Identifier panelTexture = new Identifier("combats_attributes", "textures/gui/attribute_panel.png");
    @Unique
    private Identifier gainButtonTexture = new Identifier("combats_attributes", "textures/gui/gain_button.png");
    @Unique
    private Identifier acceptButtonTexture = new Identifier("combats_attributes", "textures/gui/accept_button.png");


    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo info) {
        panelSwitchButton = new TexturedButtonWidget(
                x + 140, y + 61, 18, 18, 0, 0, 0,
                panelSwitchButtonTexture, 18, 56, button ->
                CombatsAttributesClient.isPanelVisible = !CombatsAttributesClient.isPanelVisible);

        strengthGainButton = new TexturedButtonWidget(
                x + backgroundWidth + 99, y + 40, 7, 7, 0, 0, 0,
                gainButtonTexture, 7, 7, button -> {
                    if (freePoints > 0 && addedStrength < 9) {
                        freePoints -= 1;
                        addedStrength += 1;
                    }
                    sendPlayerData();
                });
        agilityGainButton = new TexturedButtonWidget(
                x + backgroundWidth + 99, y + 55, 7, 7, 0, 0, 0,
                gainButtonTexture, 7, 7, button -> {
                    if (freePoints > 0 && addedAgility < 9) {
                        freePoints -= 1;
                        addedAgility += 1;
                    }
                    sendPlayerData();
                });
        intelligenceGainButton = new TexturedButtonWidget(
                x + backgroundWidth + 99, y + 70, 7, 7, 0, 0, 0,
                gainButtonTexture, 7, 7, button -> {
                    if (freePoints > 0 && addedIntelligence < 9) {
                        freePoints -= 1;
                        addedIntelligence += 1;
                    }
                    sendPlayerData();
                });
        vitalityGainButton = new TexturedButtonWidget(
                x + backgroundWidth + 99, y + 85, 7, 7, 0, 0, 0,
                gainButtonTexture, 7, 7,button -> {
                    if (freePoints > 0 && addedVitality < 9) {
                        freePoints -= 1;
                        addedVitality += 1;
                    }
                    sendPlayerData();
                });

        strengthLossButton = new TexturedButtonWidget(
                x + backgroundWidth + 107, y + 40, 7, 7, 0, 0, 0,
                gainButtonTexture, 7, 7, button -> {
                    if (addedStrength > 0) {
                        addedStrength -= 1;
                        freePoints += 1;
                    }
                    sendPlayerData();
                });
        agilityLossButton = new TexturedButtonWidget(
                x + backgroundWidth + 107, y + 55, 7, 7, 0, 0, 0,
                gainButtonTexture, 7, 7, button -> {
                    if (addedAgility > 0) {
                        addedAgility -= 1;
                        freePoints += 1;
                    }
                    sendPlayerData();
                });
        intelligenceLossButton = new TexturedButtonWidget(
                x + backgroundWidth + 107, y + 70, 7, 7, 0, 0, 0,
                gainButtonTexture, 7, 7, button -> {
                    if (addedIntelligence > 0) {
                        addedIntelligence -= 1;
                        freePoints += 1;
                    }
                    sendPlayerData();
                });
        vitalityLossButton = new TexturedButtonWidget(
                x + backgroundWidth + 107, y + 85, 7, 7, 0, 0, 0,
                gainButtonTexture, 7, 7, button -> {
                    if (addedVitality > 0) {
                        addedVitality -= 1;
                        freePoints += 1;
                    }
                    sendPlayerData();
                });

        acceptButton = new TexturedButtonWidget(x + backgroundWidth + 99, y + 101, 15, 15, 0, 0, 0,
                acceptButtonTexture, 15, 15, button -> {
            strength += addedStrength;
            agility += addedAgility;
            intelligence += addedIntelligence;
            vitality += addedVitality;

            addedStrength -= addedStrength;
            addedAgility -= addedAgility;
            addedIntelligence -= addedIntelligence;
            addedVitality -= addedVitality;

            sendPlayerData();

            if (freePoints == 0) {
                remove(strengthGainButton);
                remove(agilityGainButton);
                remove(intelligenceGainButton);
                remove(vitalityGainButton);

                remove(strengthLossButton);
                remove(agilityLossButton);
                remove(intelligenceLossButton);
                remove(vitalityLossButton);

                remove(acceptButton);
            }
            });

        addDrawableChild(panelSwitchButton);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta, CallbackInfo info) {
        panelSwitchButton.setPosition(x + 140, y + 61);

        if (CombatsAttributesClient.isPanelVisible) {
            panelSwitchButton.drawTexture(drawContext, panelSwitchButtonTexture,
                    x + 140, y + 61, 0, 38, 0,
                    18, 18, 18, 56);

            if (freePoints > 0 ||
                    addedStrength > 0 || addedAgility > 0 || addedIntelligence > 0 || addedVitality > 0 ) {
                if (!children().contains(acceptButton)) {
                    addDrawableChild(strengthGainButton);
                    addDrawableChild(agilityGainButton);
                    addDrawableChild(intelligenceGainButton);
                    addDrawableChild(vitalityGainButton);

                    addDrawableChild(strengthLossButton);
                    addDrawableChild(agilityLossButton);
                    addDrawableChild(intelligenceLossButton);
                    addDrawableChild(vitalityLossButton);

                    addDrawableChild(acceptButton);
                }
            }

            drawContext.drawTexture(panelTexture,
                    x + backgroundWidth + 2, y,
                    0, 0, 120, this.backgroundHeight);

            if (client != null) {
                String title = Text.translatable("attributes").getString();
                int titleX = x + backgroundWidth + 62 - (client.textRenderer.getWidth(title) / 2);
                drawContext.drawText(client.textRenderer, title, titleX, y + 10, 0x3C3C3C, false);

                drawContext.drawText(client.textRenderer,
                        Text.translatable("strength").getString() + " " + Strength.getStrength(),
                        x + backgroundWidth + 10, y + 40, 0x3C3C3C, false);
                drawContext.drawText(client.textRenderer,
                        Text.translatable("agility").getString() + " " + Agility.getAgility(),
                        x + backgroundWidth + 10, y + 55, 0x3C3C3C, false);
                drawContext.drawText(client.textRenderer,
                        Text.translatable("intelligence").getString() + " " + Intelligence.getIntelligence(),
                        x + backgroundWidth + 10, y + 70, 0x3C3C3C, false);
                drawContext.drawText(client.textRenderer,
                        Text.translatable("vitality").getString() + " " + Vitality.getVitality(),
                        x + backgroundWidth + 10, y + 85, 0x3C3C3C, false);

                if (children().contains(acceptButton)) {
                    drawContext.drawText(client.textRenderer, Text.literal("+" + Strength.getAddedStrength()),
                            x + backgroundWidth + 86, y + 40, 0x189622, false);
                    drawContext.drawText(client.textRenderer, Text.literal("+" + Agility.getAddedAgility()),
                            x + backgroundWidth + 86, y + 55, 0x189622, false);
                    drawContext.drawText(client.textRenderer, Text.literal("+" + Intelligence.getAddedIntelligence()),
                            x + backgroundWidth + 86, y + 70, 0x189622, false);
                    drawContext.drawText(client.textRenderer, Text.literal("+" + Vitality.getAddedVitality()),
                            x + backgroundWidth + 86, y + 85, 0x189622, false);

                    strengthGainButton.setPosition(x + backgroundWidth + 99, y + 40);
                    agilityGainButton.setPosition(x + backgroundWidth + 99, y + 55);
                    intelligenceGainButton.setPosition(x + backgroundWidth + 99, y + 70);
                    vitalityGainButton.setPosition(x + backgroundWidth + 99, y + 85);

                    strengthLossButton.setPosition(x + backgroundWidth + 107, y + 40);
                    agilityLossButton.setPosition(x + backgroundWidth + 107, y + 55);
                    intelligenceLossButton.setPosition(x + backgroundWidth + 107, y + 70);
                    vitalityLossButton.setPosition(x + backgroundWidth + 107, y + 85);

                    acceptButton.setPosition(x + backgroundWidth + 99, y + 101);

                    strengthGainButton.drawTexture(drawContext, gainButtonTexture,
                            x + backgroundWidth + 99, y + 40,
                            0, 0, 0,
                            7, 7, 7, 7);
                    agilityGainButton.drawTexture(drawContext, gainButtonTexture,
                            x + backgroundWidth + 99, y + 55,
                            0, 0, 0,
                            7, 7, 7, 7);
                    intelligenceGainButton.drawTexture(drawContext, gainButtonTexture,
                            x + backgroundWidth + 99, y + 70,
                            0, 0, 0,
                            7, 7, 7, 7);
                    vitalityGainButton.drawTexture(drawContext, gainButtonTexture,
                            x + backgroundWidth + 99, y + 85,
                            0, 0, 0,
                            7, 7, 7, 7);

                    strengthLossButton.drawTexture(drawContext, gainButtonTexture,
                            x + backgroundWidth + 107, y + 40,
                            0, 0, 0,
                            7, 7, 7, 7);
                    agilityLossButton.drawTexture(drawContext, gainButtonTexture,
                            x + backgroundWidth + 107, y + 55,
                            0, 0, 0,
                            7, 7, 7, 7);
                    intelligenceLossButton.drawTexture(drawContext, gainButtonTexture,
                            x + backgroundWidth + 107, y + 70,
                            0, 0, 0,
                            7, 7, 7, 7);
                    vitalityLossButton.drawTexture(drawContext, gainButtonTexture,
                            x + backgroundWidth + 107, y + 85,
                            0, 0, 0,
                            7, 7, 7, 7);

                    acceptButton.drawTexture(drawContext, acceptButtonTexture,
                            x + backgroundWidth + 99, y + 101,
                            0, 0, 0,
                            15, 15, 15, 15);
                }

                drawContext.drawText(client.textRenderer,
                        Text.translatable("free_points").getString() + " " + FreePoints.getFreePoints(),
                        x + backgroundWidth + 10, y + 105, 0x3C3C3C, false);

                String level = Text.translatable("level").getString() + " " + Level.getLevel();
                int levelY = y + backgroundHeight - 29;
                drawContext.drawText(client.textRenderer, level, x + backgroundWidth + 10, levelY, 0x3C3C3C, false);

                String progress = Text.translatable("progress").getString() + " " + XP.getXp();
                int progressY = y + backgroundHeight - 14;
                drawContext.drawText(client.textRenderer, progress, x + backgroundWidth + 10, progressY, 0x3C3C3C, false);
            }
        } else {

            if (children().contains(acceptButton)) {
                remove(strengthGainButton);
                remove(agilityGainButton);
                remove(intelligenceGainButton);
                remove(vitalityGainButton);

                remove(strengthLossButton);
                remove(agilityLossButton);
                remove(intelligenceLossButton);
                remove(vitalityLossButton);

                remove(acceptButton);
            }

            if (panelSwitchButton.isMouseOver(mouseX, mouseY)) {
                panelSwitchButton.drawTexture(drawContext, panelSwitchButtonTexture,
                        x + 140, y + 61, 0, 19, 0,
                        18, 18, 18, 56);
            }
        }
    }

    @Unique
    private void sendPlayerData() {
        int[] playerData = new int[] {
                XP.getXp(),
                Level.getLevel(),
                FreePoints.getFreePoints(),
                Strength.getStrength(),
                Agility.getAgility(),
                Intelligence.getIntelligence(),
                Vitality.getVitality(),
                Strength.getAddedStrength(),
                Agility.getAddedAgility(),
                Intelligence.getAddedIntelligence(),
                Vitality.getAddedVitality()};

        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeIntArray(playerData);
        ClientPlayNetworking.send(CLIENT_TO_SERVER_A, buf);
    }
}
