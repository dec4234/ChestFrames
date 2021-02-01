package net.dec4234.chestframes.nms;

import com.nesaak.noreflection.NoReflection;
import com.nesaak.noreflection.access.DynamicCaller;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class NMS {

	private static String version = null;

	public static void closeChest(Block chest) {
		try {
			Class classBlock = getClass(getNMSVersion("Block"));
			Class classCraftBlock = getClass(getBukkitVersion("block.CraftBlock"));
			Class classServer = getClass(getNMSVersion("WorldServer"));
			Class classCraftWorld = getClass(getBukkitVersion("CraftWorld"));
			Class classBlockPosition = getClass(getNMSVersion("BlockPosition"));

			Object craftWorld = classCraftWorld.cast(chest.getLocation().getWorld());
			Object craftBlock = classCraftBlock.cast(chest);


			Constructor constructorBlockPosition = classBlockPosition.getConstructor(int.class, int.class, int.class);

			Method methodGetHandleBlock = classCraftBlock.getDeclaredMethod("getNMSBlock");
			Method methodGetHandleWorld = classCraftWorld.getDeclaredMethod("getHandle");
			Method methodPlayBlockAction = classServer.getDeclaredMethod("playBlockAction", classBlockPosition, classBlock, int.class, int.class);

			DynamicCaller NR_mghb = NoReflection.shared().get(methodGetHandleBlock);
			DynamicCaller NR_mghw = NoReflection.shared().get(methodGetHandleWorld);
			DynamicCaller NR_mpba = NoReflection.shared().get(methodPlayBlockAction);

			Object nmsBlock = NR_mghb.call(craftBlock);
			Object handleWorld = NR_mghw.call(craftWorld);
			Object blockPosition = NoReflection.shared().get(constructorBlockPosition).call(chest.getX(), chest.getY(), chest.getZ());

			NR_mpba.call(handleWorld, blockPosition, nmsBlock, 1, 0);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	public static Class getClass(String classPath) {
		try {
			return Class.forName(classPath);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getNMSVersion(String className) {
		return "net.minecraft.server." + getVersion() + "." + className;
	}

	public static String getBukkitVersion(String className) {
		return "org.bukkit.craftbukit." + getVersion() + "." + className;
	}

	private static String getVersion() {
		if (version == null) {
			String s = Bukkit.getServer().getClass().getPackage().getName();

			version = s.substring(s.lastIndexOf('.') + 1);
		}

		return version;
	}
}
