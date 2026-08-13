package re.neotamia.jei2cc;

import com.simibubi.create.AllBlocks;
import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.buttons.IButtonState;
import mezz.jei.api.gui.buttons.IIconButtonController;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.inputs.IJeiUserInput;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class Jei2CCButtonController<T> implements IIconButtonController {
    private final IJeiHelpers jeiHelpers;
    private final IDrawable icon;
    private final IRecipeLayoutDrawable<T> recipeLayoutDrawable;

    public Jei2CCButtonController(IJeiHelpers jeiHelpers, IRecipeLayoutDrawable<T> recipeLayoutDrawable) {
        this.jeiHelpers = jeiHelpers;
        this.recipeLayoutDrawable = recipeLayoutDrawable;
        this.icon = jeiHelpers.getGuiHelper()
                .drawableBuilder(ResourceLocation.fromNamespaceAndPath("create", "textures/item/clipboard.png"), 0, 0, 10,10)
                .setTextureSize(10, 10)
                .build();
    }

    @Override
    public void initState(IButtonState state) {
        state.setIcon(icon);
    }

    @Override
    public boolean onPress(IJeiUserInput input) {
        if (input.isSimulate()) {
            return true;
        }

        final Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return false;

        final ItemStack itemStack = mc.player.getItemInHand(InteractionHand.MAIN_HAND);
        if (itemStack.isEmpty()) return false;
        if (!itemStack.is(AllBlocks.CLIPBOARD.asItem())) return false;

        final IRecipeCategory<T> category = this.recipeLayoutDrawable.getRecipeCategory();
        final T recipe = this.recipeLayoutDrawable.getRecipe();
        final ResourceLocation recipeTypeUid = category.getRecipeType().getUid();

        System.out.println("Recipe Type UID: " + recipeTypeUid);
        System.out.println("Recipe: " + recipe);
        System.out.println("Category: " + category);

        return true;
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
