package net.environmentz.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.At;

import net.environmentz.access.PlayerEnvAccess;
import net.environmentz.network.EnvironmentServerPacket;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    @Inject(method = "onPlayerConnect", at = @At(value = "TAIL"))
    private void onPlayerConnectMixin(ClientConnection connection, ServerPlayerEntity player, CallbackInfo info) {
        EnvironmentServerPacket.writeS2CSyncEnvPacket(player, ((PlayerEnvAccess) player).isHotEnvAffected(), ((PlayerEnvAccess) player).isColdEnvAffected());
        EnvironmentServerPacket.writeS2CTemperaturePacket(player, ((PlayerEnvAccess) player).getPlayerTemperature(), ((PlayerEnvAccess) player).getPlayerWetIntensityValue());
    }

    @Inject(method = "respawnPlayer", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void respawnPlayerMixin(ServerPlayerEntity player, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> info, BlockPos blockPos, float f, boolean bl, ServerWorld serverWorld,
            Optional<Vec3d> optional2, ServerWorld serverWorld2, ServerPlayerEntity serverPlayerEntity) {
        EnvironmentServerPacket.writeS2CSyncEnvPacket(player, ((PlayerEnvAccess) player).isHotEnvAffected(), ((PlayerEnvAccess) player).isColdEnvAffected());
    }
}
