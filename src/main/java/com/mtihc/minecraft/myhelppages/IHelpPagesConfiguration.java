package com.mtihc.minecraft.myhelppages;

import java.util.List;
import java.util.Set;

public interface IHelpPagesConfiguration {
    public List<String> getPage(String var1);

    public boolean hasPage(String var1);

    public Set<String> getPageNames();

    public void reload();
}
