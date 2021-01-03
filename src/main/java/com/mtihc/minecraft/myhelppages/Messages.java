package com.mtihc.minecraft.myhelppages;

import com.mtihc.minecraft.myhelppages.exceptions.HelpPageException;
import com.mtihc.minecraft.myhelppages.util.YamlFile;
import java.util.List;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Messages extends YamlFile implements IHelpPagesConfiguration, IHelpConfiguration {
    public Messages(JavaPlugin plugin) {
        super(plugin, "config");
        this.reload();
        this.addDefaults("default-pages");
    }

    private ConfigurationSection pages() {
        YamlConfiguration config = this.getConfig();
        ConfigurationSection result = config.getConfigurationSection("pages");
        if (result != null) {
            return result;
        }
        result = config.createSection("pages");
        this.addDefaults("default-pages");
        return result;
    }

    private ConfigurationSection messages() {
        return this.getConfig().getConfigurationSection("messages");
    }

    @Override
    public String getMessageNoPagePermission() {
        return this.messages().getString("noPagePermission");
    }

    @Override
    public String getMessagePageNotFound() {
        return this.messages().getString("pageNotFound");
    }

    @Override
    public String getMessagePageTile() {
        return this.messages().getString("pageTitle");
    }

    public void setMessageNoPagePermission(String value) {
        this.messages().set("noPagePermission", value);
    }

    public void setMessagePageNotFound(String value) {
        this.messages().set("pageNotFound", value);
    }

    public void setMessagePageTile(String value) {
        this.messages().set("pageTitle", value);
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
        this.pages().set(name, (Object) null);
    }
}
