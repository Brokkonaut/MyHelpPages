package com.mtihc.minecraft.myhelppages;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface IHelpPagesConfiguration {
    public List<String> getPage(String name);

    public boolean hasPage(String name);

    public Set<String> getPageNames();

    public void reload();

    public Collection<String> getPagesStartingWith(String name);
}
