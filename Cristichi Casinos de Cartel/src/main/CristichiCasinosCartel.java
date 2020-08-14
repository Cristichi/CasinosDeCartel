package main;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import obj.CasinoDeCartel;

public class CristichiCasinosCartel extends JavaPlugin implements Listener {
	private static final Logger log = Logger.getLogger("Minecraft");
	public static Economy econ = null;

	private PluginDescriptionFile desc = getDescription();

	public static final ChatColor mainColor = ChatColor.BLUE;
	public static final ChatColor textColor = ChatColor.AQUA;
	public static final ChatColor accentColor = ChatColor.DARK_AQUA;
	public static final ChatColor errorColor = ChatColor.RED;
	public final String header = mainColor + "[" + desc.getName() + "] " + textColor;

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	@Override
	public void onEnable() {
		if (!setupEconomy()) {
			log.severe(header + " Este plugin necesita Vault para funcionar.");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("Enabled");
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
			final CasinoDeCartel cdc = CasinoDeCartel.check(block);
			if (cdc != null) {
				int i = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
					@Override
					public void run() {
						cdc.girar(e.getPlayer());
//						e.getPlayer().sendMessage(header + cdc.getRuleta().toString());
					}
				}, 0, 5);
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

					@Override
					public void run() {
						Bukkit.getScheduler().cancelTask(i);
						String[] items = cdc.getRuleta().actual();
						e.getPlayer().sendMessage(new String[] {
							header + "Resultado:",
							items[0], items[1], items[2],
							header + "Ruleta terminada, NPI de lo que has ganado porque no está terminada"
						});
					}
				}, 60);
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					@Override
					public void run() {
						cdc.terminarGiro(e.getPlayer());
					}
				}, 100);
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return super.onCommand(sender, command, label, args);
	}
}
