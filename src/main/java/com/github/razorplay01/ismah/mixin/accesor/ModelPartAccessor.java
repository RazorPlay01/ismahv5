package com.github.razorplay01.ismah.mixin.accesor;

import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;

@Mixin(ModelPart.class)
public interface ModelPartAccessor {
	@Mutable
	@Accessor("cubes")
	void ismah$setCubes(List<ModelPart.Cube> cubes);

	@Accessor("cubes")
	List<ModelPart.Cube> ismah$getCubes();

	@Accessor("children")
	Map<String, ModelPart> ismah$getChildren();
}
