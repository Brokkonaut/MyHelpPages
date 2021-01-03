package com.mtihc.minecraft.myhelppages;

import com.mtihc.minecraft.myhelppages.util.YamlFile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Implementation of the <code>IHelpConfiguration</code> interface.
 *
 * @author Mitch
 *
 */
public class HelpConfiguration extends YamlFile implements IHelpConfiguration {
    public HelpConfiguration(JavaPlugin plugin) {
        super(plugin, "config");
        reload();
        if (getConfig().contains("messages")) {
            return;
        }
        addDefaults("config");
    }

    private ConfigurationSection messages() {
        YamlConfiguration pages = getConfig();
        ConfigurationSection result = pages.getConfigurationSection("messages");
        if (result != null) {
            return result;
        }
        return pages.createSection("messages");
    }

    @Override
    public String getMessageNoPagePermission() {
        return messages().getString("noPagePermission");
    }

    @Override
    public String getMessagePageNotFound() {
        return messages().getString("pageNotFound");
    }

    @Override
    public String getMessagePageTile() {
        return messages().getString("pageTitle");
    }

    public void setMessageNoPagePermission(String value) {
        messages().set("noPagePermission", value);
    }

    public void setMessagePageNotFound(String value) {
        messages().set("pageNotFound", value);
    }

    public void setMessagePageTile(String value) {
        messages().set("pageTitle", value);
    }
}
