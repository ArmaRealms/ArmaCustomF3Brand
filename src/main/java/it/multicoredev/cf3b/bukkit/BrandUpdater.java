/*
 * Copyright 2021 - 2024 Lorenzo Magni
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS “AS IS”
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package it.multicoredev.cf3b.bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.injector.netty.WirePacket;
import it.multicoredev.cf3b.Static;
import it.multicoredev.cf3b.TextFormatting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;

public class BrandUpdater {
    private final CustomF3Brand plugin;
    private final List<String> brand;
    private final ProtocolManager manager;
    private int index = 0;
    private BukkitTask task;
    private boolean packetError = false;

    public BrandUpdater(CustomF3Brand plugin, List<String> brand, long period) {
        this.plugin = plugin;
        this.brand = brand;
        this.manager = ProtocolLibrary.getProtocolManager();

        if (brand.size() > 1) {
            long ticks = Math.max(1L, Math.ceilDiv(period, 50L));
            task = Bukkit.getScheduler().runTaskTimer(plugin, new UpdateBrandTask(), ticks, ticks);
        }
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    public void broadcast() {
        Bukkit.getOnlinePlayers().forEach(this::send);
    }

    public void send(Player player) {
        String str = brand.get(index)
                .replace("{name}", player.getName())
                .replace("{displayname}", player.getDisplayName());
        if (CustomF3Brand.PAPI) str = PlaceholderUtils.replacePlaceholders(str, player);

        try {
            byte[] data = createData(TextFormatting.toLegacy(str) + "§r");
            WirePacket customPacket = new WirePacket(PacketType.Play.Server.CUSTOM_PAYLOAD, data);
            manager.sendWirePacket(player, customPacket);
        } catch (Exception e) {
            if (!packetError) {
                packetError = true;
                plugin.getLogger().log(Level.WARNING, "Cannot send the F3 brand. Check ProtocolLib compatibility with your server.", e);
            }
        }
    }

    static byte[] createData(String brand) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        writeString(output, Static.BRAND);
        writeString(output, brand);
        return output.toByteArray();
    }

    private static void writeString(ByteArrayOutputStream output, String value) {
        if (value.length() > 32767) {
            throw new IllegalArgumentException("Brand strings cannot exceed 32767 characters");
        }

        byte[] data = value.getBytes(StandardCharsets.UTF_8);
        int length = data.length;
        while ((length & ~0x7F) != 0) {
            output.write((length & 0x7F) | 0x80);
            length >>>= 7;
        }
        output.write(length);
        output.writeBytes(data);
    }

    private class UpdateBrandTask implements Runnable {
        @Override
        public void run() {
            broadcast();
            ++index;
            if (index >= brand.size()) index = 0;
        }
    }
}
