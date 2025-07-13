package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;


import java.util.function.Supplier;

public record BigBookC2SSyncPacket(WrittenBigBookContent content, InteractionHand hand){
//    public static final Type<BigBookSyncPacket> TYPE = new Type<>(BCUtil.bcLoc("big_book_sync"));
//    public static final StreamCodec<RegistryFriendlyByteBuf, BigBookSyncPacket> STREAM_CODEC = StreamCodec.composite(
//            BigBookContent.STREAM_CODEC, BigBookSyncPacket::content,
//            BCUtil.INTERACTION_HAND_STREAM_CODEC, BigBookSyncPacket::hand,
//            BigBookSyncPacket::new);

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ctx.get().getSender().getItemInHand(hand).setTag(WrittenBigBookContent.encode(content));
        });
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(WrittenBigBookContent.encode(content));
        buf.writeEnum(hand);
    }
    public static BigBookC2SSyncPacket fromBytes(FriendlyByteBuf buf)
    {
        return new BigBookC2SSyncPacket(WrittenBigBookContent.decode(buf.readNbt()), buf.readEnum(InteractionHand.class));
    }

//    @Override
//    public Type<? extends CustomPacketPayload> type() {
//        return TYPE;
//    }
}
