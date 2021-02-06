package corgitaco.enchancedcelestials.client;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import net.fabricmc.api.ClientModInitializer;

public class EnhancedCelestialsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EnhancedCelestials.clientSetup();
    }
}
