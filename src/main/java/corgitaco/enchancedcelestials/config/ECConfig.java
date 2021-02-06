package corgitaco.enchancedcelestials.config;


import corgitaco.enchancedcelestials.EnhancedCelestials;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = EnhancedCelestials.MOD_ID)
public class ECConfig implements ConfigData {


    @ConfigEntry.Category("Blood_Moon_Settings")
    @ConfigEntry.Gui.CollapsibleObject
    public BloodMoonSettings bloodMoonSettings = new BloodMoonSettings();

    @ConfigEntry.Category("Harvest_Moon_Settings")
    @ConfigEntry.Gui.CollapsibleObject
    public HarvestMoonSettings harvestMoonSettings = new HarvestMoonSettings();

    public static class BloodMoonSettings {

        @Comment(value = "The chance of a blood moon occurring each night. Chance is rolled at the daytime 13005.\nDefault 0.05")
        @ConfigEntry.BoundedDiscrete(min = 0, max = 1)
        public double bloodMoonChance = 0.05;

        @Comment(value = "Multiply the monster spawn cap(70 monsters) by this value.\nRemember, more mobs = more server lag, so set this number responsibly!\nDefault is 5.0. aka 70 * 5.0 = 350 total mobs")
        @ConfigEntry.BoundedDiscrete(min = 2, max = 30)
        public double monsterSpawnCapMultiplier = 5;

        @Comment(value = "Are blood moon clouds red?\nDefault true")
        public boolean bloodMoonRedClouds = true;
    }

    public static class HarvestMoonSettings {

        @Comment(value = "The chance of a harvest moon occurring each night. Chance is rolled at the daytime 13005.\nDefault 0.025")
        @ConfigEntry.BoundedDiscrete(min = 0, max = 1)
        public double harvestMoonChance = 0.05;

        @Comment(value = "Multiplies the rate at which crops grow during harvest moons.\nDefault 15.0")
        @ConfigEntry.BoundedDiscrete(min = 1, max = 10000)
        public double cropGrowthMultiplier = 15.0;

        @Comment(value = "Multiplies the rate at which crop items drop during harvest moons.\nDefault 2.5")
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1000)
        public double cropItemDropMultiplier = 2.5;
    }
}
