package com.edgarssilva.cobblemon_riding;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public class ModKeyBindings {
    public static final String KEY_CATEGORY = "Cobblemon Riding";
    public static final String KEY_DESC = "Descend";

    public static final String KEY_HOLD = "Hold";

    public static final KeyMapping HOLD = new KeyMapping(KEY_HOLD, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_H, KEY_CATEGORY);
    public static final KeyMapping DESCEND = new KeyMapping(KEY_DESC, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_C, KEY_CATEGORY);

}
