package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.util.FormattedLine;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.WrittenBookItem;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.ByteBufCodecs;
//import net.minecraft.network.codec.StreamCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.WritableBookItem;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record WrittenBigBookContent(List<List<FormattedLine>> pages, String title, String author, Boolean written, int generation, int currentPage) {
    public static <T> Codec<Optional<T>> createOptionalCodec(Codec<T> elementCodec) {
        return RecordCodecBuilder.create(ins -> ins.group(
                        Codec.BOOL.fieldOf("isPresent").forGetter(Optional::isPresent),
                        elementCodec.optionalFieldOf("content").forGetter(o -> o))
                .apply(ins, (isPresent, content) -> isPresent && content.isPresent() ? content : Optional.empty()));
    }
    public static final Codec<WrittenBigBookContent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            createOptionalCodec(FormattedLine.CODEC).flatXmap(
                    x-> DataResult.success(x.orElse(FormattedLine.DEFAULT)),
                            y->DataResult.success(y == FormattedLine.DEFAULT ? Optional.empty() : Optional.of(y))
                    ).listOf().listOf().fieldOf("pages")
                    .forGetter(WrittenBigBookContent::pages),
            ExtraCodecs.sizeLimitedString(0, WrittenBookItem.TITLE_MAX_LENGTH).fieldOf("title").forGetter(WrittenBigBookContent::title),
            Codec.STRING.fieldOf("author").forGetter(WrittenBigBookContent::author),
            Codec.BOOL.fieldOf("written").forGetter(WrittenBigBookContent::written),
            ExtraCodecs.intRange(0, WrittenBookItem.MAX_GENERATION ).optionalFieldOf("generation", 0).forGetter(WrittenBigBookContent::generation),
            ExtraCodecs.intRange(0, 255).optionalFieldOf("current_page", 0).forGetter(WrittenBigBookContent::currentPage)
    ).apply(inst, WrittenBigBookContent::new));
//    public static final StreamCodec<RegistryFriendlyByteBuf, WrittenBigBookContent> STREAM_CODEC = StreamCodec.composite(
//            FormattedLine.STREAM_CODEC.apply(ByteBufCodecs.list()).apply(ByteBufCodecs.list()), WrittenBigBookContent::pages,
//            ByteBufCodecs.stringUtf8(WrittenBookContent.TITLE_MAX_LENGTH), WrittenBigBookContent::title,
//            ByteBufCodecs.STRING_UTF8, WrittenBigBookContent::author,
//            ByteBufCodecs.VAR_INT, WrittenBigBookContent::generation,
//            ByteBufCodecs.VAR_INT, WrittenBigBookContent::currentPage,
//            WrittenBigBookContent::new);
    public static final WrittenBigBookContent DEFAULT = new WrittenBigBookContent(List.of(), "", "", false, 0, 0);

    @Nullable
    public WrittenBigBookContent tryCraftCopy() {
        return generation >= WrittenBookItem.MAX_GENERATION  ? null : new WrittenBigBookContent(pages, title, author, true, generation + 1, currentPage);
    }

    public static WrittenBigBookContent decode(CompoundTag nbt)
    {

        Optional<WrittenBigBookContent> decoded = CODEC.parse(NbtOps.INSTANCE, nbt)
                .result();
        return decoded.orElse(null);
    }
    public static CompoundTag encode(WrittenBigBookContent content)
    {
        Optional<CompoundTag> tag = CODEC.encodeStart(NbtOps.INSTANCE, content)
                .result()
                .map(dynamic -> (CompoundTag) dynamic);
        return tag.orElse(null);
    }

}
