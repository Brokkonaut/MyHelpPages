package com.mtihc.minecraft.myhelppages;

import com.mtihc.minecraft.myhelppages.exceptions.HelpPageException;
import com.mtihc.minecraft.myhelppages.util.YamlFile;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class HelpPagesConfiguration extends YamlFile implements IHelpPagesConfiguration {
    public HelpPagesConfiguration(JavaPlugin plugin) {
        super(plugin, "pages");
        this.reload();
        if (this.getConfig().contains("pages")) {
            return;
        }
        this.addDefaults("pages");
    }

    private ConfigurationSection pages() {
        YamlConfiguration pages = this.getConfig();
        ConfigurationSection result = pages.getConfigurationSection("pages");
        if (result != null) {
            return result;
        }
        return pages.createSection("pages");
    }

    @Override
    public Set<String> getPageNames() {
        return this.pages().getKeys(false);
    }

    @Override
    public boolean hasPage(String name) {
        return this.pages().contains(name);
    }

    @Override
    public List<String> getPage(String name) {
        return this.pages().getStringList(name);
    }

    @Override
    public Collection<String> getPagesStartingWith(String name) {
        TreeSet<String> result = new TreeSet<>();
        ConfigurationSection pages = this.pages();
        for (String key : pages.getKeys(false)) {
            if (key.startsWith(name) && pages.isList(key)) {
                result.add(key);
            }
        }
        return result;
    }

    public void addPage(String name, List<String> lines) throws HelpPageException {
        if (!this.pages().contains(name)) {
            throw new HelpPageException(HelpPageException.Type.PAGE_ALREADY_EXIST, ChatColor.RED + "Page " + ChatColor.WHITE + "\"" + name + "\"" + ChatColor.RED + " already not exists.");
        }
        this.pages().set(name, lines);
    }

    public void removePage(String name) throws HelpPageException {
        if (!this.pages().contains(name)) {
            throw new HelpPageException(HelpPageException.Type.PAGE_NOT_EXIST, ChatColor.RED + "Page " + ChatColor.WHITE + "\"" + name + "\"" + ChatColor.RED + " does not exist.");
        }
        this.pages().set(name, null);
    }
}
