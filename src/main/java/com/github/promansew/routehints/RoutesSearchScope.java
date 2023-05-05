package com.github.promansew.routehints;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

public class RoutesSearchScope extends GlobalSearchScope {
    @Override
    public boolean isSearchInModuleContent(@NotNull Module aModule) {
        return true;
    }

    @Override
    public boolean isSearchInLibraries() {
        return false;
    }

    @Override
    public boolean contains(@NotNull VirtualFile file) {
        return "routes".equals(file.getName());
    }
}
