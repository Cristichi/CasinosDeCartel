package main;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.plugins.Economy_Essentials;
import obj.CasinoDeCartel;

public class CristichiCasinosCartel extends JavaPlugin implements Listener {
	private static final Logger log = Logger.getLogger("Minecraft");
	public static Economy econ = null;

//	private PluginDescriptionFile desc = getDescription();

	public static final ChatColor mainColor = ChatColor.DARK_AQUA;
	public static final ChatColor textColor = ChatColor.WHITE;
	public static final ChatColor accentColor = ChatColor.GOLD;
	public static final ChatColor errorColor = ChatColor.RED;
	public final String header = mainColor + "[Casinos de Cartel] " + textColor;

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		getServer().getServicesManager().register(Economy_Essentials.class, new Economy_Essentials(this), this,
				ServicePriority.Normal);
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	@Override
	public void onEnable() {
		if (setupEconomy()) {
			getServer().getPluginManager().registerEvents(this, this);
			getLogger().info("Enabled");
		} else {
			log.severe(header + " Este plugin necesita Vault y EssentialsX para funcionar.");
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	@EventHandler
	private void onSignChanged(SignChangeEvent e) {
		CasinoDeCartel cdc = CasinoDeCartel.colocar(e,
				new String[] { this.header + errorColor + "Para crear un Casino de Cartel debes escribir en el cartel:",
						textColor + "   [CC]", textColor + "   precio" });
		if (cdc != null) {
			e.getPlayer().sendMessage(header + "Has creado un Casino de Cartel.");
		}
	}

	@EventHandler
	private void onSignClicked(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block block = e.getClickedBlock();
			CasinoDeCartel cdc = CasinoDeCartel.check(block);
			if (cdc != null) {
				cdc.onPlayerTira(this, e.getPlayer(), econ, header, textColor.toString(), accentColor.toString(),
						errorColor.toString());
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0)
			return false;
		switch (args[0]) {
		case "help":
			sender.sendMessage(new String[] { header + "Para empezar, coloca un cartel con las siguientes l�neas:",
					accentColor + "  [CC]", accentColor + "(precio)" });
			return true;
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
//		System.out.println("---------------");
//		System.out.println(command.getName());
//		System.out.println(alias + ": " + Arrays.toString(args));
		List<String> ret = new ArrayList<>(1);
		if (args.length <= 1) {
			ret.add("help");
		}
		return ret;
	}
}
