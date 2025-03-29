package dev.maikotui.bugboys;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.spell_engine.internals.SpellRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BugBoysMC implements ModInitializer {
	public static final String MOD_ID = "bugboys";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Initialized BugBoys Core");

		// This helps with datagen
		ServerLifecycleEvents.SERVER_STARTED.register((MinecraftServer server) -> {
			List<String> spells = new ArrayList<>();
			for (Map.Entry<Identifier, SpellRegistry.SpellEntry> entry : SpellRegistry.all().entrySet()) {
				spells.add('"' + entry.getKey().toString() + '"');
			}
			LOGGER.info("Registered Spells:\n[{}]", String.join(", ", spells));

		});
	}
}