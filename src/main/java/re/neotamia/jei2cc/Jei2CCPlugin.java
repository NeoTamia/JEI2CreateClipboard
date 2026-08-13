package re.neotamia.jei2cc;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.api.gui.buttons.IIconButtonController;
import mezz.jei.api.recipe.advanced.IRecipeButtonControllerFactory;
import mezz.jei.api.registration.IAdvancedRegistration;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class Jei2CCPlugin implements IModPlugin {

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Jei2CC.ID, "jei_plugin");
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {
        IRecipeButtonControllerFactory factory = new IRecipeButtonControllerFactory() {
            @Override
            public <T> @NotNull IIconButtonController createButtonController(IRecipeLayoutDrawable<T> recipeLayoutDrawable) {
                return new Jei2CCButtonController(registration.getJeiHelpers());
            }
        };
        registration.addRecipeButtonFactory(factory);
    }
}
