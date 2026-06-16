package com.mtihc.minecraft.myhelppages;

import de.iani.cubesideutils.ComponentUtilAdventure;
import de.iani.cubesideutils.NamedChatColor;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * This class can execute the commands of the plugin.
 *
 * <p>
 * It can be used as a paremeter in the <code>setExecutor()</code> method
 * of a <code>PluginCommand</code>.
 * </p>
 *
 * <p>
 * For example:
 *
 * <pre>
 * plugin.getCommand(label).setExecutor(executor);
 * </pre>
 * </p>
 *
 * @author Mitch
 *
 */
public class HelpCommandExecutor implements CommandExecutor {
    private IHelpPagesConfiguration pages;
    private IHelpConfiguration config;

    /**
     * Constructor.
     *
     * @param config
     *            The configuration for this executor
     */
    public HelpCommandExecutor(IHelpConfiguration config, IHelpPagesConfiguration pages) {
        this.config = config;
        this.pages = pages;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            this.help(sender, args);
            return true;
        }
        String nested = args[0].toLowerCase();
        if (nested.equals("-list")) {
            this.list(sender, Arrays.copyOfRange(args, 1, args.length));
            return true;
        }
        if (nested.equals("-reload")) {
            this.reload(sender);
            return true;
        }
        if (nested.equals("?") || nested.equals("help") || nested.equals("-help")) {
            this.commandHelp(sender);
            return true;
        }
        this.help(sender, args);
        return true;
    }

    /**
     * Send command help
     *
     * @param sender
     *            The command sender
     */
    public void commandHelp(CommandSender sender) {
        sender.sendMessage(Component.text("Command list:", NamedTextColor.GREEN));
        sendCommandHelpLine(sender, "/help", " See the main help page.");
        sendCommandHelpLine(sender, "/help [page name]", " See a help page.");
        sendCommandHelpLine(sender, "/help -list [number]", " List all help pages.");
        sendCommandHelpLine(sender, "/help -reload", " Reload the configuration and pages.");
    }

    /**
     * The help command. Shows a help page to the command sender.
     *
     * @param sender
     *            The command sender
     * @param args
     *            The command arguments
     */
    public void help(CommandSender sender, String[] args) {

        // get page name from aguments
        String name = convertArgsToPageName(args);

        // replace - with space
        String userFriendlyName = name.replace("-", " ");

        // check existance
        if (!pages.hasPage(name)) {
            String msg = config.getMessagePageNotFound();
            msg = msg.replace("%page%", userFriendlyName);
            sender.sendMessage(convertColors(msg));
            return;
        }

        // get permission by page name
        String perm = Permission.convertPageNameToPermission(name);

        // check permission
        if (!sender.hasPermission(Permission.ALLPAGES.toString()) && !sender.hasPermission(perm)) {
            String msg = config.getMessageNoPagePermission();
            msg = msg.replace("%page%", userFriendlyName);
            sender.sendMessage(convertColors(msg));
            return;
        }

        // send configured title
        String title = config.getMessagePageTile();
        title = title.replace("%page%", userFriendlyName);
        sender.sendMessage(convertColors(title));

        // send lines of page
        for (String line : pages.getPage(name)) {
            line = line.replace("%page%", userFriendlyName);
            sender.sendMessage(convertColors(line));
        }
    }

    /**
     * The list command. Send a list of names of all pages that the command sender has permission for.
     *
     * @param sender
     *            The command sender
     * @param args
     *            The command arguments
     */
    public void list(CommandSender sender, String[] args) {
        if (!sender.hasPermission(Permission.LIST.toString())) {
            sender.sendMessage(Component.text("You don't have permission for the list command.", NamedTextColor.RED));
            return;
        }
        int pageNumber = 1;
        if (args.length > 0) {
            try {
                pageNumber = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Component.text("Expected a page number.", NamedTextColor.RED));
                return;
            }
        }
        if (sender.hasPermission(Permission.ALLPAGES.toString())) {
            // sender has permission for all pages

            // send list of pages
            sendPageList(sender, pageNumber, pages.getPageNames());
            return;
        }
        // sender has permission for some pages

        // only show the pages he has permission for
        Set<String> original = pages.getPageNames();
        HashSet<String> pagesWithPermission = new HashSet<>();
        for (String pageName : original) {
            String perm = Permission.convertPageNameToPermission(pageName);
            if (sender.hasPermission(perm)) {
                pagesWithPermission.add(pageName);
            }
        }

        // send list of pages
        sendPageList(sender, pageNumber, pagesWithPermission);
    }

    /**
     * The reload command. Reload the configuration files.
     *
     * @param sender
     *            The command sender.
     */
    public void reload(CommandSender sender) {
        if (!sender.hasPermission(Permission.RELOAD.toString())) {
            sender.sendMessage(Component.text("You don't have permission to reload the configuration.", NamedTextColor.RED));
            return;
        }
        config.reload();
        pages.reload();
        sender.sendMessage(Component.text("Configuration and Pages reloaded.", NamedTextColor.GREEN));
    }

    /**
     * Used by list command. Send a list of page names to the command sender.
     *
     * @param sender
     *            The command sender
     * @param page
     *            The page number for the list of names
     * @param names
     *            The list of names
     */
    private static void sendPageList(CommandSender sender, int page, Set<String> names) {
        int total = names.size();
        int totalPerPage = 10;
        int totalPages = Math.max(1, (total + totalPerPage - 1) / totalPerPage);
        int startIndex = (page - 1) * totalPerPage;
        int endIndex = startIndex + totalPerPage;

        sender.sendMessage(Component.text("List of all help pages ", NamedTextColor.GREEN)
                .append(Component.text("(" + page + "/" + totalPages + ")", NamedTextColor.WHITE))
                .append(Component.text(":")));

        String[] nameArray = names.toArray(new String[names.size()]);

        for (int i = startIndex; i < endIndex && i < total; i++) {
            String userFriendly = "/" + nameArray[i].replace("-", " ");
            sender.sendMessage(Component.text(" " + i + ". ", NamedTextColor.DARK_GRAY)
                    .append(Component.text(userFriendly, NamedTextColor.WHITE)));
        }
        String nextPage;
        if (page >= totalPages) {
            nextPage = "1";
        } else {
            nextPage = String.valueOf(page + 1);
        }
        sender.sendMessage(Component.text("Next page: ", NamedTextColor.GREEN)
                .append(Component.text("/help -list " + nextPage, NamedTextColor.WHITE)));
    }

    private static void sendCommandHelpLine(CommandSender sender, String command, String description) {
        sender.sendMessage(Component.text(command, NamedTextColor.WHITE)
                .append(Component.text(description, NamedTextColor.DARK_GREEN)));
    }

    /**
     * Replace variable names for colors, to actual color values in a string.
     *
     * @param source
     *            The string with color variable names
     * @return The colored string
     */
    private static Component convertColors(String source) {
        String result = source;
        // iterate over chat colors
        for (NamedChatColor color : NamedChatColor.values()) {
            // get normal name of color
            String name = color.name().replace("_", "").toLowerCase();
            // replace variable name with real color
            result = result.replace("%" + name + "%", "&" + color.getChar());
        }
        try {
            return ComponentUtilAdventure.convertEscaped(result);
        } catch (ParseException e) {
            return Component.text("Could not parse string line: " + source);
        }
    }

    /**
     * Used to convert a list of strings to a page name.
     *
     * @param args
     *            The arguments passed in a command
     * @return The page name
     */
    protected static String convertArgsToPageName(String[] args) {
        StringBuilder result = new StringBuilder("help");
        for (String arg : args) {
            result.append("-").append(arg);
        }
        return result.toString();
    }
}
