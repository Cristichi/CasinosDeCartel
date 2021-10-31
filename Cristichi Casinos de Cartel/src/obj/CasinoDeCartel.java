package obj;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.Plugin;

import main.CristichiCasinosCartel;
import net.milkbowl.vault.economy.Economy;
import ruleta.ItemRuleta;
import ruleta.Puntuacion;
import ruleta.Ruleta;

public class CasinoDeCartel {
	public static final String[] HEADER_CARTEL_ESCRITO = new String[] { "[Casino Cartel]", "[CC]", "[CDC]" };
	public static final String HEADER_CARTEL_VISIBLE = CristichiCasinosCartel.mainColor + HEADER_CARTEL_ESCRITO[0];
	public static final String HEADER_CARTEL_ERROR = CristichiCasinosCartel.errorColor + HEADER_CARTEL_ESCRITO[0];

	private Sign cartel;
	private double precio;
	private Ruleta ruleta;
	private int hiloGirar;

	public static CasinoDeCartel colocar(SignChangeEvent e, String[] error) {
		Block bloque = e.getBlock();
		CasinoDeCartel cdc = null;
		if (bloque != null) {
			BlockState bs = bloque.getState();
			if (bs instanceof Sign) {
				Sign s = (Sign) bloque.getState();
				for (String header : CasinoDeCartel.HEADER_CARTEL_ESCRITO) {
					if (e.getLine(0).equalsIgnoreCase(header)) {
						cdc = new CasinoDeCartel();
						e.setLine(0, CasinoDeCartel.HEADER_CARTEL_VISIBLE);
						if (e.getLine(1).trim().isEmpty()) {
							e.setLine(0, CasinoDeCartel.HEADER_CARTEL_ERROR);
							e.setLine(1, "");
							e.setLine(2, "");
							e.setLine(3, "");
							e.getPlayer().sendMessage(error);
							cdc = null;
						} else {
							try {
								double d = Double.parseDouble(e.getLine(1));
								cdc.setPrecio(d);
								cdc.ruleta = new Ruleta();
								cdc.cartel = s;
								e.setLine(2, "¡Prueba Suerte!");
								e.setLine(3, "Click derecho");
							} catch (NumberFormatException ex) {
								e.setLine(0, CasinoDeCartel.HEADER_CARTEL_ERROR);
								e.setLine(1, "");
								e.setLine(2, "");
								e.setLine(3, "");
								cdc = null;
							}
						}
					}
				}
			}
		}
		return cdc;
	}

	public static CasinoDeCartel check(Block bloque) {
		CasinoDeCartel cdc = new CasinoDeCartel();
		if (bloque != null) {
			BlockState bs = bloque.getState();
			if (bs instanceof Sign) {
				Sign s = (Sign) bloque.getState();
				String[] lineas = s.getLines();
				if (lineas[0].equalsIgnoreCase(HEADER_CARTEL_VISIBLE)) {
					if (lineas[1].trim().isEmpty()) {
						s.setLine(0, HEADER_CARTEL_ERROR);
						s.setLine(1, "");
						s.setLine(2, "");
						s.setLine(3, "");
						return null;
					} else {
						try {
							double d = Double.parseDouble(lineas[1]);
							cdc.ruleta = new Ruleta();
							cdc.precio = d;
							cdc.cartel = s;
							return cdc;
						} catch (NumberFormatException e) {
							s.setLine(0, HEADER_CARTEL_ERROR);
							s.setLine(1, "");
							s.setLine(2, "");
							s.setLine(3, "");
							return null;
						}
					}
				}
			}
		}
		return null;
	}

	public void reset(int minGiros, int maxGiros, int dif) {
		ruleta.reset(minGiros, maxGiros, dif);
	}

	public boolean isFinished() {
		return ruleta.isFinished();
	}

	public void girar(Player p) {
		ItemRuleta[][] items = ruleta.girar();
		cartel.setLine(0, "- [" + items[0][0] + "][" + items[0][1] + "][" + items[0][2] + "] -");
		cartel.setLine(1, "- [" + items[1][0] + "][" + items[1][1] + "][" + items[1][2] + "] -");
		cartel.setLine(2, "- [" + items[2][0] + "][" + items[2][1] + "][" + items[2][2] + "] -");
		cartel.setLine(3, p.getDisplayName());
		cartel.update();
	}

	public void ultimo(Player p) {
		ItemRuleta[][] items = ruleta.actual();
		cartel.setLine(0, ChatColor.GOLD + "-" + ChatColor.RESET + " [" + items[0][0] + "][" + items[0][1] + "]["
				+ items[0][2] + "] " + ChatColor.GOLD + "-");
		cartel.setLine(1, ChatColor.GOLD + "-" + ChatColor.RESET + " [" + items[1][0] + "][" + items[1][1] + "]["
				+ items[1][2] + "] " + ChatColor.GOLD + "-");
		cartel.setLine(2, ChatColor.GOLD + "-" + ChatColor.RESET + " [" + items[2][0] + "][" + items[2][1] + "]["
				+ items[2][2] + "] " + ChatColor.GOLD + "-");
		cartel.setLine(3, p.getDisplayName());
		cartel.update();
	}

	public void terminarGiro(Player p) {
		cartel.setLine(0, HEADER_CARTEL_VISIBLE);
		cartel.setLine(1, Double.toString(precio));
		cartel.setLine(2, "¡Prueba Suerte!");
		cartel.setLine(3, "Click derecho");
		cartel.update();
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public Ruleta getRuleta() {
		return ruleta;
	}

	@Override
	public String toString() {
		return "CasinoDeCartel [precio=" + precio + "]";
	}

	public void onPlayerTira(Plugin plugin, Player p, Economy econ, String header, String textColor,
			String accentColor, String errorColor) {
		if (!econ.hasAccount(p)) {
			econ.createPlayerAccount(p);
		}
		if (econ.getBalance(p) < getPrecio()) {
			p.sendMessage(header + errorColor + "No puedes permitirte esta tirada. Tienes " + accentColor
					+ econ.getBalance(p) + errorColor + " y necesitas " + accentColor + getPrecio() + errorColor + ".");
			return;
		}
		econ.withdrawPlayer(p, getPrecio());
		p.sendMessage(header + "Has pagado " + accentColor + getPrecio() + textColor
				+ " para hacer la tirada. ¡Mucha suerte!");

		reset(20, 40, 10);
		Runnable run = new Runnable() {
			boolean finished = false;

			@Override
			public void run() {
				if (!finished)
					if (isFinished()) {
//						cancelTask();
						Bukkit.getScheduler().cancelTask(hiloGirar);
						ultimo(p);
						ItemRuleta[][] items = getRuleta().actual();
						Puntuacion punt = getRuleta().getPuntuacion();
						double ganado = getPrecio() * punt.getMult();
						econ.depositPlayer(p, ganado);
						p.sendMessage(new String[] { header + "Resultado:",
								".                           " + items[0][0] + " " + items[0][1] + " " + items[0][2],
								".                           " + items[1][0] + " " + items[1][1] + " " + items[1][2],
								".                           " + items[2][0] + " " + items[2][1] + " " + items[2][2],
								header + "Has ganado " + accentColor + ganado + textColor + " por " + accentColor
										+ punt.getMotivo() + textColor + ". Tu dinero actual: "
										+ econ.getBalance(p), });

						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								terminarGiro(p);
							}
						}, 100);
						finished = true;
					} else {
						girar(p);
					}
			}
		};
		hiloGirar = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, run, 0, 6);
	}
}
