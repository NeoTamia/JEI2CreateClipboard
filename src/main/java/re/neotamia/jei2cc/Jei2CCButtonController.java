package re.neotamia.jei2cc;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.schematics.cannon.MaterialChecklist;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.buttons.IButtonState;
import mezz.jei.api.gui.buttons.IIconButtonController;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.inputs.IJeiUserInput;
import mezz.jei.api.helpers.IJeiHelpers;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Jei2CCButtonController<T> implements IIconButtonController {
    private final IDrawable icon;
    private final IRecipeLayoutDrawable<T> recipeLayoutDrawable;
    private final Minecraft mc = Minecraft.getInstance();

    public Jei2CCButtonController(IJeiHelpers jeiHelpers, IRecipeLayoutDrawable<T> recipeLayoutDrawable) {
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

    @Override
    public boolean onPress(IJeiUserInput input) {
        if (input.isSimulate()) return true;

        final ItemStack clipboard = this.getClipboardInHand();
        if (clipboard.isEmpty()) return false;

        final MaterialChecklist checklist = new MaterialChecklist();
        final List<ItemStack> ingredients = this.recipeLayoutDrawable.getRecipeSlotsView().getSlotViews().stream()
                .skip(1)
                .filter(slotView -> slotView.getDisplayedItemStack().isPresent())

                .flatMap(IRecipeSlotView::getAllIngredients)
                .map(iTypedIngredient -> iTypedIngredient.castToItemStackType().getItemStack())
                .filter(Optional::isPresent)
                .map(Optional::get)

                .collect(Collectors.toMap(ItemStack::getItem, ItemStack::getCount, Integer::sum))
                .entrySet()
                .stream()
                .map(entry -> entry.getKey().getDefaultInstance().copyWithCount(entry.getValue()))
                .toList();

        checklist.require(new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, ingredients));
        final var newClipboard = checklist.createWrittenClipboard();

        mc.player.getInventory().setItem(mc.player.getInventory().selected, newClipboard);

        return true;
    }

    @Override
    public void getTooltips(ITooltipBuilder tooltip) {
        tooltip.add(Component.literal("Create Clipboard"));
    }

    @Override
    public void updateState(IButtonState state) {
        final ItemStack itemStack = this.getClipboardInHand();
        state.setVisible(this.hasClipboardInHand(itemStack));
    }


    private boolean hasClipboardInHand(ItemStack itemStack) {
        return !itemStack.isEmpty() && itemStack.is(AllBlocks.CLIPBOARD.asItem());
    }

    private ItemStack getClipboardInHand() {
        if (this.mc.player == null) return ItemStack.EMPTY;
        ItemStack itemStack = mc.player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!this.hasClipboardInHand(itemStack))
            itemStack = mc.player.getItemInHand(InteractionHand.OFF_HAND);
        if (!this.hasClipboardInHand(itemStack)) return ItemStack.EMPTY;
        return itemStack;
    }
}
