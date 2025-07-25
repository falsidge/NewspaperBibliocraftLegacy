package com.github.minecraftschurlimods.bibliocraft.util;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;

import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;
import java.util.Optional;

public record FormattedLine(String text, Style style, int size, Mode mode, Alignment alignment) {
    public static final int MIN_SIZE = 5;
    public static final int MAX_SIZE = 35;

    public static final Codec<FormattedLine> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.STRING.fieldOf("text").forGetter(FormattedLine::text),
            Style.FORMATTING_CODEC.fieldOf("style").forGetter(FormattedLine::style),
            ExtraCodecs.intRange(MIN_SIZE, MAX_SIZE).fieldOf("size").forGetter(FormattedLine::size),
            Mode.CODEC.fieldOf("mode").forGetter(FormattedLine::mode),
            Alignment.CODEC.fieldOf("alignment").forGetter(FormattedLine::alignment)
    ).apply(inst, FormattedLine::new));


    public static final FormattedLine DEFAULT = new FormattedLine("", Style.EMPTY, 10, Mode.NORMAL, Alignment.LEFT);

    public FormattedLine withText(String text) {
        return new FormattedLine(text, style, size, mode, alignment);
    }

    public FormattedLine withStyle(Style style) {
        return new FormattedLine(text, style, size, mode, alignment);
    }

    public FormattedLine withSize(int size) {
        return new FormattedLine(text, style, size, mode, alignment);
    }

    public FormattedLine withMode(Mode mode) {
        return new FormattedLine(text, style, size, mode, alignment);
    }

    public FormattedLine withAlignment(Alignment alignment) {
        return new FormattedLine(text, style, size, mode, alignment);
    }

    public enum Mode implements StringRepresentable {
        NORMAL, SHADOW, GLOWING;
        public static final Codec<Mode> CODEC = BCUtil.enumCodec(Mode::values);
//        public static final StreamCodec<ByteBuf, Mode> STREAM_CODEC = BCUtil.enumStreamCodec(Mode::values, Mode::ordinal);

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }

        public String getTranslationKey() {
            return "gui." + Bibliocraft.MOD_ID + ".formatted_line.mode." + getSerializedName();
        }
    }

    public enum Alignment implements StringRepresentable {
        LEFT, CENTER, RIGHT;
        public static final Codec<Alignment> CODEC = BCUtil.enumCodec(Alignment::values);
//        public static final StreamCodec<ByteBuf, Alignment> STREAM_CODEC = BCUtil.enumStreamCodec(Alignment::values, Alignment::ordinal);

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }

        public String getTranslationKey() {
            return "gui." + Bibliocraft.MOD_ID + ".formatted_line.alignment." + getSerializedName();
        }
    }
//    public static CompoundTag writeToNBT(FormattedLine formattedLine)
//    {
//        CompoundTag tag = new CompoundTag();
//        tag.putString("text", formattedLine.text);
//        tag.putString("style", Style.FORMATTING_CODEC.encode(formattedLine.style));
//    }
}
