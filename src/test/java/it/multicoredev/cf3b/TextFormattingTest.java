package it.multicoredev.cf3b;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextFormattingTest {
    @Test
    void convertsMiniMessageColorsForTheBrand() {
        assertEquals("§6Arma", TextFormatting.toLegacy("<gold>Arma</gold>"));
    }

    @Test
    void keepsAmpersandAndSectionLegacyColorsWorking() {
        assertEquals("§6Arma", TextFormatting.toLegacy("&6Arma"));
        assertEquals("§6Arma", TextFormatting.toLegacy("§6Arma"));
    }

    @Test
    void supportsMixedMiniMessageAndLegacyFormatting() {
        assertEquals("§6Arma §lMC", TextFormatting.toLegacy("&6Arma <bold>MC</bold>"));
    }

    @Test
    void escapesLiteralTagsInBackendBrands() {
        String backendBrand = TextFormatting.toMiniMessage("§6Paper <test>");

        assertEquals("§6Paper <test>", TextFormatting.toLegacy(backendBrand));
    }

    @Test
    void parsesConfiguredMessagesAsAdventureComponents() {
        assertEquals(Component.text("Reloaded", NamedTextColor.GREEN), TextFormatting.parse("<green>Reloaded</green>"));
    }
}
