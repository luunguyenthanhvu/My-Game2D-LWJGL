package no.arnemunthekaas.engine.entities.components;

import no.arnemunthekaas.utils.AssetPool;

public class Breakable extends Block {

    @Override
    void playerHit(PlayerController playerController) {
        if (!playerController.isSmall()) {
            AssetPool.getSound("assets/audio/assets_sounds_break_block.ogg").play();
            gameObject.destroy();
        }

    }
}
