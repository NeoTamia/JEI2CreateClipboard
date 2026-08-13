
package re.neotamia.jei2cc;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.schematics.cannon.MaterialChecklist;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;
import java.util.Map;

public class Jei2CCButtonController<T> implements IIconButtonController {
    private final IJeiHelpers jeiHelpers;
    private final IDrawable icon;
    private final IRecipeLayoutDrawable<T> recipeLayoutDrawable;
    private final Minecraft mc = Minecraft.getInstance();

    public Jei2CCButtonController(IJeiHelpers jeiHelpers, IRecipeLayoutDrawable<T> recipeLayoutDrawable) {
        this.jeiHelpers = jeiHelpers;
        this.recipeLayoutDrawable = recipeLayoutDrawable;
        this.icon = jeiHelpers.getGuiHelper()
                .drawableBuilder(ResourceLocation.fromNamespaceAndPath("create", "textures/item/clipboard.png"), 0, 0, 10, 10)
                .setTextureSize(10, 10)
                .build();
    }

    @Override
    public void initState(IButtonState state) {
        state.setIcon(icon);
    }

    private boolean hasClipboardInHand(ItemStack itemStack) {
        return !itemStack.isEmpty() && itemStack.is(AllBlocks.CLIPBOARD.asItem());
    }

    private ItemStack getClipboardInHand() {
        if (this.mc.player == null) return ItemStack.EMPTY;
        ItemStack itemStack = mc.player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!this.hasClipboardInHand(itemStack)) {
            itemStack = mc.player.getItemInHand(InteractionHand.OFF_HAND);
        }
        if (!this.hasClipboardInHand(itemStack)) return ItemStack.EMPTY;
        return itemStack;
    }

    @Override
    public boolean onPress(IJeiUserInput input) {
        if (input.isSimulate()) return true;

        final ItemStack itemStack = this.getClipboardInHand();
        if (itemStack.isEmpty()) return false;

        final T recipe = this.recipeLayoutDrawable.getRecipe();
        System.out.println("Recipe: " + recipe.getClass());
        if (!(recipe instanceof RecipeHolder<?> holder)) return false;

        final IRecipeCategory<T> category = this.recipeLayoutDrawable.getRecipeCategory();
        final ResourceLocation recipeTypeUid = category.getRecipeType().getUid();
        final MaterialChecklist checklist = new MaterialChecklist();

        final List<ItemStack> ingredients = this.recipeLayoutDrawable.getRecipeSlotsView().getSlotViews().stream()
                .skip(1)
                .filter(slotView -> slotView.getDisplayedItemStack().isPresent())
                .map(slotView -> slotView.getDisplayedItemStack().get())
                .toList();
        System.out.println(ingredients);

        System.out.println("Recipe Type UID: " + recipeTypeUid);
        System.out.println("Recipe: " + holder);
        System.out.println("Category: " + category);

        return true;
    }

    @Override
    public void getTooltips(ITooltipBuilder tooltip) {
        tooltip.add(Component.literal("Create Clipboard"));
    }

    @Override
    public void updateState(IButtonState state) {
        final ItemStack itemStack = this.getClipboardInHand();
        if (itemStack.isEmpty()) return;
        state.setVisible(this.hasClipboardInHand(itemStack));
    }
}
