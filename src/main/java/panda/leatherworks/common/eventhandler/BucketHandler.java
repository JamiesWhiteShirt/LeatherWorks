package panda.leatherworks.common.eventhandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import panda.leatherworks.common.block.BlockTanninFluid;
import panda.leatherworks.init.LWBlocks;
import panda.leatherworks.init.LWFluids;
import panda.leatherworks.init.LWItems;

public class BucketHandler {

	@SubscribeEvent
	public void onRightClickHoldingBucket(FillBucketEvent event) {

		if (event.getEmptyBucket().getItem() != Items.BUCKET) {
			return;
		}
		if (event.getTarget() == null || event.getTarget().typeOfHit != RayTraceResult.Type.BLOCK) {
			return;
		}
		BlockPos blockpos = event.getTarget().getBlockPos();
		if (!event.getWorld().isBlockModifiable(event.getEntityPlayer(), blockpos)) {
			return;
		}
		if (!event.getEntityPlayer().canPlayerEdit(blockpos.offset(event.getTarget().sideHit),
				event.getTarget().sideHit, event.getEmptyBucket())) {
			return;
		}

		IBlockState iblockstate = event.getWorld().getBlockState(blockpos);
		Fluid filled_fluid = null;
		if (iblockstate.getBlock() == LWBlocks.TANNIN
				&& iblockstate.getValue(BlockTanninFluid.LEVEL) == 0) {
			filled_fluid = LWFluids.TANNIN;
		} else {
			return;
		}

		// remove the fluid and return the appropriate filled bucket
		event.setResult(Result.ALLOW);
		ItemStack bucket = new ItemStack(LWItems.TANNIN_BUCKET);
		event.setFilledBucket(bucket);
		event.getWorld().setBlockToAir(blockpos);
		// TODO:
		// event.entityPlayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(event.getEmptyBucket().getItem())]);
	}

}