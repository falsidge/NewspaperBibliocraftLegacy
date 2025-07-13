package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class BookStorage extends SavedData {
    private final Map<UUID, CompoundTag> bookContents = new HashMap<>();
    private static final String SAVED_DATA_NAME = Bibliocraft.MOD_ID;

    private static final BookStorage clientStorageCopy = new BookStorage();

    public static BookStorage create()
    {
        return new BookStorage();
    }

    public static BookStorage get() {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                ServerLevel overworld = server.getLevel(Level.OVERWORLD);
                //noinspection ConstantConditions - by this time overworld is loaded
                DimensionDataStorage storage = overworld.getDataStorage();
                return storage.computeIfAbsent(BookStorage::load, BookStorage::create, SAVED_DATA_NAME);
            }
        }
        return clientStorageCopy;
    }


    @Override
    public CompoundTag save(CompoundTag tag)
    {
        ListTag backpackContentsNbt = new ListTag();
        for (Map.Entry<UUID, CompoundTag> entry : bookContents.entrySet()) {
            CompoundTag uuidContentsPair = new CompoundTag();
            uuidContentsPair.put("uuid", NbtUtils.createUUID(entry.getKey()));
            uuidContentsPair.put("contents", entry.getValue());
            backpackContentsNbt.add(uuidContentsPair);
        }
        tag.put("bookContents", backpackContentsNbt);
        return tag;
    }
    public static BookStorage load(CompoundTag tag)
    {
        BookStorage data = new BookStorage();
        for (Tag n : tag.getList("backpackContents", Tag.TAG_COMPOUND)) {
            CompoundTag uuidContentsPair = (CompoundTag) n;
            UUID uuid = NbtUtils.loadUUID(Objects.requireNonNull(uuidContentsPair.get("uuid")));
            CompoundTag contents = uuidContentsPair.getCompound("contents");
            data.bookContents.put(uuid,contents);
        }
        return data;
    }
}
