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
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import obj.CasinoDeCartel;
import ruleta.ItemRuleta;
import ruleta.Puntuacion;

public class CristichiCasinosCartel extends JavaPlugin implements Listener {
	private static final Logger log = Logger.getLogger("Minecraft");
	public static Economy econ = null;

//	private PluginDescriptionFile desc = getDescription();

	public static final ChatColor mainColor = ChatColor.DARK_AQUA;
	public static final ChatColor textColor = ChatColor.GREEN;
	public static final ChatColor accentColor = ChatColor.GOLD;
	public static final ChatColor errorColor = ChatColor.RED;
	public final String header = mainColor + "[Casinos de Cartel] " + textColor;

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
				if (!econ.hasAccount(e.getPlayer())) {
					econ.createPlayerAccount(e.getPlayer());
				}
				if (econ.getBalance(e.getPlayer()) < cdc.getPrecio()) {
					e.getPlayer()
							.sendMessage(header + errorColor + "No puedes permitirte esta tirada. Tienes " + accentColor
									+ econ.getBalance(e.getPlayer()) + textColor + " y necesitas " + accentColor
									+ cdc.getPrecio() + textColor + ".");
					return;
				}
				econ.withdrawPlayer(e.getPlayer(), cdc.getPrecio());
				e.getPlayer().sendMessage(header + "Has pagado " + accentColor + cdc.getPrecio() + textColor
						+ " para hacer la tirada. ¡Mucha suerte!");
				int i = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
					@Override
					public void run() {
						cdc.girar(e.getPlayer());
//						ItemRuleta[][] items = cdc.getRuleta().actual();
//						e.getPlayer().sendMessage(new String[] {
//								"----",
//								items[0][0] + " " + items[0][1] + " " + items[0][2],
//								items[1][0] + " " + items[1][1] + " " + items[1][2],
//								items[2][0] + " " + items[2][1] + " " + items[2][2]
//						});
					}
				}, 0, 2);
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					@Override
					public void run() {
						Bukkit.getScheduler().cancelTask(i);
						cdc.ultimo(e.getPlayer());
						ItemRuleta[][] items = cdc.getRuleta().actual();
						Puntuacion punt = cdc.getRuleta().getPuntuacion();
						double ganado = cdc.getPrecio() * punt.getMult();
						econ.depositPlayer(e.getPlayer(), ganado);
						e.getPlayer().sendMessage(new String[] { header + "Resultado:",
								".                           " + items[0][0] + " " + items[0][1] + " " + items[0][2],
								".                           " + items[1][0] + " " + items[1][1] + " " + items[1][2],
								".                           " + items[2][0] + " " + items[2][1] + " " + items[2][2],
//								header + "Ruleta terminada, has ganado " + accentColor + ganado + textColor + ": " + accentColor + punt.getMotivo() });
								header + "Ruleta terminada, has ganado " + accentColor + ganado + textColor + "." });
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
