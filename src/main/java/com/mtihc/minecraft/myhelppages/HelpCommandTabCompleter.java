package com.mtihc.minecraft.myhelppages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class HelpCommandTabCompleter implements TabCompleter {
    private IHelpPagesConfiguration pages;

    public HelpCommandTabCompleter(IHelpConfiguration config, IHelpPagesConfiguration pages) {
        this.pages = pages;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length >= 1 && args[0].startsWith("-")) {
            if (args.length > 1) {
                return Collections.emptyList();
            }
            ArrayList<String> completions = new ArrayList<>();
            if ("-help".startsWith(args[0])) {
                completions.add("-help");
            }
            if ("-list".startsWith(args[0]) && sender.hasPermission(Permission.LIST.toString())) {
                completions.add("-list");
            }
            if ("-reload".startsWith(args[0]) && sender.hasPermission(Permission.RELOAD.toString())) {
                completions.add("-reload");
            }
            return completions;
        }
        ArrayList<String> completions = new ArrayList<>();
        String pageName = HelpCommandExecutor.convertArgsToPageName(args);
        Collection<String> possiblePages = pages.getPagesStartingWith(pageName);
        for (String name : possiblePages) {
            String perm = Permission.convertPageNameToPermission(name);
            if (sender.hasPermission(Permission.ALLPAGES.toString()) || sender.hasPermission(perm)) {
                String userFriendlyName = name.replace("-", " ");
                String[] nameParts = userFriendlyName.split(" ");
                if (nameParts.length >= args.length + 1) { // first part is always "help"
                    completions.add(nameParts[args.length]);
                }
            }
        }
        completions.sort(null);
        return completions;
    }

}
