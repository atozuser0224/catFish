package org.CatAndDomi.components;

import org.CatAndDomi.CatFish;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class CatFishBuilder {

    private final JavaPlugin plugin;
    private ArrayList<Component> components = new ArrayList<>();

    public CatFishBuilder(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public CatFishBuilder addComponents(ComponentType componentType) {
        components.add(componentType.createComponent(plugin));
        return this;
    }

    public void build() {
        CatFish.getInstance().componentmap.put(plugin, components);
    }

}
