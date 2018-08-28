package com.mtihc.minecraft.myhelppages;

/**
 * This interface is used by the <code>HelpCommandExecutor</code> to send messages and help pages.
 *
 * @author Mitch
 *
 */
public interface IHelpConfiguration {
    /**
     * @return the title of a page
     */
    public abstract String getMessagePageTile();

    /**
     * @return message to be sent when the player has no permission to view the requested page
     */
    public abstract String getMessageNoPagePermission();

    /**
     * @return message to be sent when the requested page was not found
     */
    public abstract String getMessagePageNotFound();

    /**
     * Reloads any changes to the configuration
     */
    public abstract void reload();

}
