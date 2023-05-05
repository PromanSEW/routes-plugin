package com.andromeda.routesplugin;

import com.intellij.codeInsight.hints.*;
import com.intellij.lang.Language;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

@SuppressWarnings("UnstableApiUsage")
public class RouteInlayHintsProvider implements InlayHintsProvider<NoSettings> {

    private static final SettingsKey<NoSettings> KEY = new SettingsKey<>("PlayRoute");

    @Override
    public boolean isVisibleInSettings() {
        return false;
    }

    @NotNull
    @Override
    public SettingsKey<NoSettings> getKey() {
        return KEY;
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getName() {
        return "Play route inlay hints";
    }

    @Nullable
    @Override
    public String getPreviewText() {
        return null;
    }

    @NotNull
    @Override
    public ImmediateConfigurable createConfigurable(@NotNull NoSettings settings) {
        return listener -> {
            JPanel panel = new JPanel();
            panel.setVisible(false);
            return panel;
        };
    }

    @NotNull
    @Override
    public NoSettings createSettings() {
        return new NoSettings();
    }

    @Nullable
    @Override
    public InlayHintsCollector getCollectorFor(@NotNull PsiFile psiFile, @NotNull Editor editor, @NotNull NoSettings settings, @NotNull InlayHintsSink sink) {
        return new RouteHintsCollector(editor);
    }

    @Override
    public boolean isLanguageSupported(@NotNull Language language) {
        return language.getID().equals("JAVA");
    }
}
