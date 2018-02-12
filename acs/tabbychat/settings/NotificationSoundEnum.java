package acs.tabbychat.settings;

import net.minecraft.client.resources.I18n;

public enum NotificationSoundEnum
{
    ORB(I18n.format("sounds.orb", new Object[0]), "random.orb"),
    ANVIL(I18n.format("sounds.anvil", new Object[0]), "random.anvil_land"),
    BOWHIT(I18n.format("sounds.bowhit", new Object[0]), "random.bowhit"),
    BREAK(I18n.format("sounds.break", new Object[0]), "random.break"),
    CLICK(I18n.format("sounds.click", new Object[0]), "random.click"),
    GLASS(I18n.format("sounds.glass", new Object[0]), "random.glass"),
    BASS(I18n.format("sounds.bass", new Object[0]), "note.bassattack"),
    HARP(I18n.format("sounds.harp", new Object[0]), "note.harp"),
    PLING(I18n.format("sounds.pling", new Object[0]), "note.pling"),
    CAT(I18n.format("sounds.cat", new Object[0]), "mob.cat.meow"),
    BLAST(I18n.format("sounds.blast", new Object[0]), "fireworks.blast"),
    SPLASH(I18n.format("sounds.splash", new Object[0]), "liquid.splash"),
    SWIM(I18n.format("sounds.swim", new Object[0]), "liquid.swim"),
    BAT(I18n.format("sounds.bat", new Object[0]), "mob.bat.hurt"),
    BLAZE(I18n.format("sounds.blaze", new Object[0]), "mob.blaze.hit"),
    CHICKEN(I18n.format("sounds.chicken", new Object[0]), "mob.chicken.hurt"),
    COW(I18n.format("sounds.cow", new Object[0]), "mob.cow.hurt"),
    DRAGON(I18n.format("sounds.dragon", new Object[0]), "mob.enderdragon.hit"),
    ENDERMEN(I18n.format("sounds.endermen", new Object[0]), "mob.endermen.hit"),
    GHAST(I18n.format("sounds.ghast", new Object[0]), "mob.ghast.moan"),
    PIG(I18n.format("sounds.pig", new Object[0]), "mob.pig.say"),
    WOLF(I18n.format("sounds.wolf", new Object[0]), "mob.wolf.bark");
    private String title;
    private String file;

    private NotificationSoundEnum(String _title, String _file)
    {
        this.title = _title;
        this.file = _file;
    }

    public String toString()
    {
        return this.title;
    }

    public String file()
    {
        return this.file;
    }
}
