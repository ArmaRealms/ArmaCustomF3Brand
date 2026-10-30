package it.multicoredev.cf3b;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class TextFormatting {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer AMPERSAND = LegacyComponentSerializer.legacyAmpersand();
    private static final LegacyComponentSerializer SECTION = LegacyComponentSerializer.legacySection();

    private TextFormatting() {
    }

    public static Component parse(String text) {
        return MINI_MESSAGE.deserialize(text);
    }

    public static String toMiniMessage(String text) {
        return MINI_MESSAGE.serialize(AMPERSAND.deserialize(text.replace('§', '&')));
    }

    public static String toLegacy(String text) {
        String normalized = AMPERSAND.serialize(AMPERSAND.deserialize(text.replace('§', '&')));
        String rendered = AMPERSAND.serialize(MINI_MESSAGE.deserialize(normalized));
        return SECTION.serialize(AMPERSAND.deserialize(rendered));
    }
}
