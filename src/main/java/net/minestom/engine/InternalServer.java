package net.minestom.engine;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.ChunkPopulator;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InternalServer {
    public static boolean start(Editor editor) {
        var server = MinecraftServer.init();

        var instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setChunkGenerator(new ChunkGenerator() {
            @Override
            public void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ) {
                for (byte x = 0; x < 16; ++x)
                    for (byte z = 0; z < 16; ++z)
                        for (byte y = 0; y < 40; ++y)
                            batch.setBlock(x, y, z, y < 35 ? Block.STONE : y < 39 ? Block.DIRT : Block.GRASS_BLOCK);
            }

            @Override
            public @Nullable List<ChunkPopulator> getPopulators() {
                return null;
            }
        });

        editor.setInstance(instance);

        MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, event -> {
            event.setSpawningInstance(instance);
            event.getPlayer().setGameMode(GameMode.CREATIVE);
            event.getPlayer().setRespawnPoint(new Pos(0, 42, 0));

            if (editor.getMode() != Editor.Mode.TEST_OPEN) {
                if (MinecraftServer.getConnectionManager().getOnlinePlayers().size() > 1
                        || !event.getPlayer().getPlayerConnection().getRemoteAddress().toString().contains("127.0.0.1")) {
                    System.out.println(event.getPlayer().getPlayerConnection().getRemoteAddress() + " tried to connect");
                    event.getPlayer().kick("Not testing");
                    // return;
                } else {
                    editor.setTestPlayerName(event.getPlayer().getUsername());
                }
            }
        });

        server.start("0.0.0.0", 25565);

        return true;
    }
}
