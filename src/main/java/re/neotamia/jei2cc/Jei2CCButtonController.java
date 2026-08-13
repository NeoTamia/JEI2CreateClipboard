package re.neotamia.jei2cc;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.clipboard.ClipboardBlock;
import com.simibubi.create.content.equipment.clipboard.ClipboardBlockItem;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.buttons.IButtonState;
import mezz.jei.api.gui.buttons.IIconButtonController;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.inputs.IJeiUserInput;
import mezz.jei.api.helpers.IJeiHelpers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class Jei2CCButtonController implements IIconButtonController {
    private final IDrawable icon;

    public Jei2CCButtonController(IJeiHelpers jeiHelpers) {
        this.icon = jeiHelpers.getGuiHelper().createDrawableItemStack(new ItemStack(AllBlocks.CLIPBOARD));
    }

    @Override
    public void initState(IButtonState state) {
        state.setIcon(icon);
    }

    @Override
    public boolean onPress(IJeiUserInput input) {
        return false;
    }

    @Override
    public void getTooltips(ITooltipBuilder tooltip) {
        tooltip.add(Component.literal("Create Clipboard"));
    }

    @Override
    public void updateState(IButtonState state) {
        IIconButtonController.super.updateState(state);
    }
}
