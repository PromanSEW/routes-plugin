package com.andromeda.routesplugin;

import com.intellij.codeInsight.hints.FactoryInlayHintsCollector;
import com.intellij.codeInsight.hints.InlayHintsSink;
import com.intellij.openapi.editor.BlockInlayPriority;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.search.searches.ReferencesSearch;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class RouteHintsCollector extends FactoryInlayHintsCollector {
    public RouteHintsCollector(@NotNull Editor editor) {
        super(editor);
    }

    @Override
    public boolean collect(@NotNull PsiElement element, @NotNull Editor editor, @NotNull InlayHintsSink sink) {
        if (!element.isValid() || element.getProject().isDefault()) return false;
        if (!(element instanceof PsiMethod method)) return true;
        var clazz = method.getContainingClass();
        if (clazz == null) return true;
        var superclass = clazz.getSuperClass();
        if (superclass == null || !"play.mvc.Controller".equals(superclass.getQualifiedName())) return true;
        var modifiers = method.getModifierList();
        if (!modifiers.hasModifierProperty(PsiModifier.PUBLIC) || modifiers.hasModifierProperty(PsiModifier.STATIC)) {
            return true;
        }
        var returnType = method.getReturnType();
        if (returnType == null || !returnType.equalsToText("play.mvc.Result")) return true;
        var reference = ReferencesSearch.search(element, new RoutesSearchScope()).findFirst();
        if (reference == null) return true;
        int offset = element.getTextOffset();
        element = reference.getElement();
        var document = element.getContainingFile().getViewProvider().getDocument();
        int end = element.getTextOffset();
        int start = document.getLineStartOffset(document.getLineNumber(end));
        var presentation = getFactory().text(document.getText(TextRange.create(start, end)).trim());
        sink.addBlockElement(offset, true, true, BlockInlayPriority.CODE_VISION_USAGES, presentation);
        return true;
    }
}
