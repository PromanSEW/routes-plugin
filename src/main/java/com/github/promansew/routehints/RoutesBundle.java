package com.github.promansew.routehints;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.*;

public class RoutesBundle extends DynamicBundle {
    @NonNls
    private static final String BUNDLE = "messages.RoutesBundle";
    private static final RoutesBundle INSTANCE = new RoutesBundle();

    private RoutesBundle() {
        super(BUNDLE);
    }

    @Nls
    @Contract(pure = true)
    public static @NotNull String message(@PropertyKey(resourceBundle = BUNDLE) String key, Object... params) {
        return INSTANCE.getMessage(key, params);
    }
}
