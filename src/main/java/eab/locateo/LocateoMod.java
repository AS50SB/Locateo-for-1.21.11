package eab.locateo;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import eab.locateo.command.LocateoCommand; // 导入命令类

public class LocateoMod implements ModInitializer {
    @Override
    public void onInitialize() {
        System.out.println("Locateo Mod initialized!");

        // 注册命令
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LocateoCommand.register(dispatcher);
        });
    }
}