/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

public class WeatherHelper {
    public static WorldInfo ripOutWorldInfo(World world) {
        try {
            return (WorldInfo) (ReflectionHelper.findField(World.class, "worldInfo", "field_72986_A")).get(world);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("can't get worldInfo", e);
        }
    }

    public static void toggleRain(World world) {
        WorldInfo wi = ripOutWorldInfo(world);
        wi.setRaining(!wi.isRaining());
    }

    public static void toggleThundering(World world) {
        WorldInfo wi = ripOutWorldInfo(world);
        wi.setThundering(!wi.isThundering());
    }

    public static String generateWeatherInfo(World world) {
        WorldInfo wi = ripOutWorldInfo(world);
        return String.format("rain: %s (%d), thunder: %s (%d)",
                getWeatherBool(wi.isRaining()), wi.getRainTime(),
                getWeatherBool(wi.isThundering()), wi.getThunderTime()
        );
    }

    private static String getWeatherBool(boolean value) {
        return value ? "Y" : "n";
    }
}
