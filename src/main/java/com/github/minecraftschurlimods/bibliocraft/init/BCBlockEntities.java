//package com.github.minecraftschurlimods.bibliocraft.init;
//
//import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.function.Supplier;
//
//public interface BCBlockEntities {
//    /**
//     * Registration helper method that takes a supplier list instead of a vararg parameter.
//     *
//     * @param name     The registry name to use.
//     * @param supplier The block entity supplier to use.
//     * @param blocks   A list of block suppliers that are associated with the block entity.
//     * @param <T>      The exact type of the block entity.
//     * @return A block entity type supplier.
//     */
//    @SuppressWarnings("DataFlowIssue")
//    static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Collection<? extends Supplier<? extends Block>> blocks) {
//        return BCRegistries.BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(supplier, blocks.stream().map(Supplier::get).toList().toArray(new Block[0])).build(null));
//    }
//
//    /**
//     * Registration helper method that takes a supplier vararg parameter instead of a regular vararg parameter.
//     *
//     * @param name     The registry name to use.
//     * @param supplier The block entity supplier to use.
//     * @param blocks   A vararg of block suppliers that are associated with the block entity.
//     * @param <T>      The exact type of the block entity.
//     * @return A block entity type supplier.
//     */
//    @SafeVarargs
//    static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<? extends Block>... blocks) {
//        return register(name, supplier, List.of(blocks));
//    }
//
//    /**
//     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
//     */
//    static void init() {}
//}
