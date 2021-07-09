package me.shedaniel.architectury.networking.simple;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;

/**
 * The base class for server -&gt; client packets managed by a {@link SimpleNetworkManager}.
 *
 * @author LatvianModder
 */
public abstract class BaseS2CPacket extends BasePacket {
    private void sendTo(ServerPlayer player, Packet<?> packet) {
        if (player == null) {
            throw new NullPointerException("Unable to send packet '" + getId().getId() + "' to a 'null' player!");
        }
        
        player.connection.send(packet);
    }
    
    /**
     * Sends this packet to a player.
     *
     * @param player the player
     */
    public final void sendTo(ServerPlayer player) {
        sendTo(player, toPacket());
    }
    
    /**
     * Sends this packet to multiple players.
     *
     * @param players the players
     */
    public final void sendTo(Iterable<ServerPlayer> players) {
        Packet<?> packet = toPacket();
        
        for (ServerPlayer player : players) {
            sendTo(player, packet);
        }
    }
    
    /**
     * Sends this packet to all players in the server.
     *
     * @param server the server
     */
    public final void sendToAll(MinecraftServer server) {
        sendTo(server.getPlayerList().getPlayers());
    }
    
    /**
     * Sends this packet to all players in a level.
     *
     * @param level the level
     */
    public final void sendToLevel(ServerLevel level) {
        sendTo(level.players());
    }
    
    /**
     * Sends this packet to all players listening to a chunk.
     *
     * @param chunk the listened chunk
     */
    public final void sendToChunkListeners(LevelChunk chunk) {
        Packet<?> packet = toPacket();
        ((ServerChunkCache) chunk.getLevel().getChunkSource()).chunkMap.getPlayers(chunk.getPos(), false).forEach(e -> sendTo(e, packet));
    }
}
