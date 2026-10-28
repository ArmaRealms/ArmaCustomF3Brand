package it.multicoredev.cf3b.bukkit;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BrandUpdaterTest {
    private static final byte[] CHANNEL = "\u000fminecraft:brand".getBytes(StandardCharsets.UTF_8);

    @Test
    void encodesTheChannelAndBrandAsSeparateProtocolStrings() {
        byte[] data = BrandUpdater.createData("Arma");

        assertArrayEquals(CHANNEL, Arrays.copyOf(data, CHANNEL.length));
        assertArrayEquals(new byte[]{4, 'A', 'r', 'm', 'a'}, Arrays.copyOfRange(data, CHANNEL.length, data.length));
    }

    @Test
    void countsUtf8BytesIncludingColorsAndSupplementaryCharacters() {
        byte[] data = BrandUpdater.createData("§6Gênesis 😀");
        byte[] expected = "§6Gênesis 😀".getBytes(StandardCharsets.UTF_8);

        assertEquals(16, data[CHANNEL.length]);
        assertArrayEquals(expected, Arrays.copyOfRange(data, CHANNEL.length + 1, data.length));
    }

    @Test
    void encodesTwoByteVarIntLengths() {
        byte[] data = BrandUpdater.createData("x".repeat(128));

        assertEquals(0x80, data[CHANNEL.length] & 0xFF);
        assertEquals(0x01, data[CHANNEL.length + 1] & 0xFF);
        assertEquals(CHANNEL.length + 2 + 128, data.length);
    }

    @Test
    void encodesThreeByteVarIntLengths() {
        byte[] data = BrandUpdater.createData("x".repeat(16384));

        assertArrayEquals(new byte[]{(byte) 0x80, (byte) 0x80, 0x01},
                Arrays.copyOfRange(data, CHANNEL.length, CHANNEL.length + 3));
        assertEquals(CHANNEL.length + 3 + 16384, data.length);
    }

    @Test
    void rejectsStringsBeyondTheMinecraftCharacterLimit() {
        assertThrows(IllegalArgumentException.class, () -> BrandUpdater.createData("x".repeat(32768)));
    }
}
