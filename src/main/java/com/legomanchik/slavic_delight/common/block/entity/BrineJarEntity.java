package com.legomanchik.slavic_delight.common.block.entity;

import com.legomanchik.slavic_delight.common.registry.ModBlockEntities;
import com.legomanchik.slavic_delight.common.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;


public class BrineJarEntity extends BlockEntity {

    private int bottleCount;
    public BrineJarEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BRINE_JAR_ENTITY.get(), pos, state);
        this.bottleCount = 3;
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        compound.putInt("Bottles", this.bottleCount);
        super.saveAdditional(compound, registries);
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        this.bottleCount = compound.getInt("Bottles");
        super.loadAdditional(compound, registries);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if (this.level != null && this.level.isClientSide())
            this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public void minusBottle() {
        this.bottleCount--;
        if(isEmpty()) {
            this.level.setBlockAndUpdate(getBlockPos(), ModBlocks.JAR.get().defaultBlockState());
        }
        setChanged();
    }

    public void plusBottle() {
        this.bottleCount++;
        setChanged();
    }

    public boolean isEmpty() {
        return this.bottleCount <= 0;
    }

    public float getBottles() {
        return this.bottleCount;
    }

    public void setBottleCount(int bottleCount) {
        this.bottleCount = bottleCount;
        setChanged();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
