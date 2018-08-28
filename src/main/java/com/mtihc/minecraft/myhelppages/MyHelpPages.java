package com.mtihc.minecraft.myhelppages;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MyHelpPages extends JavaPlugin {
    private HelpConfiguration config;
    private HelpPagesConfiguration pages;

    /*
     * (non-Javadoc)
     *
     * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
     */
    @Override
    public void onEnable() {

        // config.yml
        config = new HelpConfiguration(this);
        // pages.yml
        pages = new HelpPagesConfiguration(this);

        // command
        getCommand("help").setExecutor(new HelpCommandExecutor(config, pages));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.bukkit.plugin.java.JavaPlugin#getConfig()
     */
    @Override
    public FileConfiguration getConfig() {
        return config.getConfig();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.bukkit.plugin.java.JavaPlugin#reloadConfig()
     */
    @Override
    public void reloadConfig() {
        config.reload();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.bukkit.plugin.java.JavaPlugin#saveConfig()
     */
    @Override
    public void saveConfig() {
        config.save();
    }
}
