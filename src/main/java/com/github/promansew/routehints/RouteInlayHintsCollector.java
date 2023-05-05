package com.github.promansew.routehints;

import com.intellij.codeInsight.hints.FactoryInlayHintsCollector;
import com.intellij.codeInsight.hints.InlayHintsSink;
import com.intellij.openapi.editor.BlockInlayPriority;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.search.searches.ReferencesSearch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public class RouteInlayHintsCollector extends FactoryInlayHintsCollector {
    public RouteInlayHintsCollector(@NotNull Editor editor) {
        super(editor);
    }

    @Override
    public boolean collect(@NotNull PsiElement element, @NotNull Editor editor, @NotNull InlayHintsSink sink) {
        if (!element.isValid() || element.getProject().isDefault()) return false;
        if (!isInteresting(element)) return true;
        int offset = element.getTextOffset();
        ReferencesSearch.search(element, new RoutesSearchScope()).forEach(reference -> {
            addRoute(sink, reference.getElement(), offset);
        });
        return true;
    }

    private void addRoute(@NotNull InlayHintsSink sink, @NotNull PsiElement element, int offset) {
        var document = element.getContainingFile().getViewProvider().getDocument();
        int end = element.getTextOffset();
        int start = document.getLineStartOffset(document.getLineNumber(end));
        var presentation = getFactory().text(document.getText(TextRange.create(start, end)).trim());
        sink.addBlockElement(offset, true, true, BlockInlayPriority.CODE_VISION_USAGES, presentation);
    }

    private static boolean isInteresting(PsiElement element) {
        return element instanceof PsiMethod method && isController(method.getContainingClass()) && isRouteMethod(method);
    }

    private static boolean isController(@Nullable PsiClass clazz) {
        if (clazz == null) return false;
        var superclass = clazz.getSuperClass();
        return superclass != null && "play.mvc.Controller".equals(superclass.getQualifiedName());
    }

    private static boolean isRouteMethod(@NotNull PsiMethod method) {
        var modifiers = method.getModifierList();
        if (!modifiers.hasModifierProperty(PsiModifier.PUBLIC) || modifiers.hasModifierProperty(PsiModifier.STATIC)) {
            return false;
        }
        var returnType = method.getReturnType();
        return returnType != null && returnType.equalsToText("play.mvc.Result");
    }
}
