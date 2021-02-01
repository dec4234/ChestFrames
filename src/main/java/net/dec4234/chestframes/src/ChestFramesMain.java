package net.dec4234.chestframes.src;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChestFramesMain extends JavaPlugin {

	@Getter private static ChestFramesMain instance;

	@Override
	public void onEnable() {
		instance = this;

		register();
	}

	private void register() {
		PluginManager pm = Bukkit.getPluginManager();


	}
}
