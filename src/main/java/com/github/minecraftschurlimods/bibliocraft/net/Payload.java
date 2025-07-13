package com.github.minecraftschurlimods.bibliocraft.net;

import com.github.minecraftschurlimods.bibliocraft.content.bigbook.BigBookSignPacket;
import com.github.minecraftschurlimods.bibliocraft.content.bigbook.BigBookC2SSyncPacket;
import com.github.minecraftschurlimods.bibliocraft.content.bigbook.SetBigBookPageInLecternPacket;
import com.github.minecraftschurlimods.bibliocraft.util.lectern.OpenBookInLecternPacket;
import com.github.minecraftschurlimods.bibliocraft.util.lectern.TakeLecternBookPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static com.github.minecraftschurlimods.bibliocraft.Bibliocraft.PROTOCOL_VERSION;

public class Payload {
    public final static SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(ResourceLocation.fromNamespaceAndPath("bibliocraft","main"),
            ()-> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    public static void registerMesssages()
    {
        int id = 0;
        CHANNEL.registerMessage(
                id++,
                BigBookSignPacket.class,
                BigBookSignPacket::toBytes,
                BigBookSignPacket::fromBytes,
                BigBookSignPacket::handle
        );
        CHANNEL.registerMessage(
                id++,
                BigBookC2SSyncPacket.class,
                BigBookC2SSyncPacket::toBytes,
                BigBookC2SSyncPacket::fromBytes,
                BigBookC2SSyncPacket::handle
        );
        CHANNEL.registerMessage(
                id++,
                SetBigBookPageInLecternPacket.class,
                SetBigBookPageInLecternPacket::toBytes,
                SetBigBookPageInLecternPacket::fromBytes,
                SetBigBookPageInLecternPacket::handle
        );
        CHANNEL.registerMessage(
                id++,
                OpenBookInLecternPacket.class,
                OpenBookInLecternPacket::toBytes,
                OpenBookInLecternPacket::fromBytes,
                OpenBookInLecternPacket::handle
        );
        CHANNEL.registerMessage(
                id++,
                TakeLecternBookPacket.class,
                TakeLecternBookPacket::toBytes,
                TakeLecternBookPacket::fromBytes,
                TakeLecternBookPacket::handle
        );
    }
}
