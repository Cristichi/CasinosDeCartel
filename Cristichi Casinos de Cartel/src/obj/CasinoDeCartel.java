package obj;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import main.CristichiCasinosCartel;
import ruleta.ItemRuleta;
import ruleta.Ruleta;

public class CasinoDeCartel {
	public static final String[] HEADER_CARTEL_ESCRITO = new String[] { "[Casino Cartel]", "[CC]" };
	public static final String HEADER_CARTEL_VISIBLE = CristichiCasinosCartel.mainColor + HEADER_CARTEL_ESCRITO[0];
	public static final String HEADER_CARTEL_ERROR = CristichiCasinosCartel.errorColor + HEADER_CARTEL_ESCRITO[0];

	private Sign cartel;
	private double precio;
	private Ruleta ruleta;

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
								e.setLine(3, "Esperando...");
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
		cartel.setLine(0, ChatColor.GOLD+"-"+ChatColor.RESET+" [" + items[0][0] + "][" + items[0][1] + "][" + items[0][2] + "] "+ChatColor.GOLD+"-");
		cartel.setLine(1, ChatColor.GOLD+"-"+ChatColor.RESET+" [" + items[1][0] + "][" + items[1][1] + "][" + items[1][2] + "] "+ChatColor.GOLD+"-");
		cartel.setLine(2, ChatColor.GOLD+"-"+ChatColor.RESET+" [" + items[2][0] + "][" + items[2][1] + "][" + items[2][2] + "] "+ChatColor.GOLD+"-");
		cartel.setLine(3, p.getDisplayName());
		cartel.update();
	}

	public void terminarGiro(Player p) {
		cartel.setLine(0, HEADER_CARTEL_VISIBLE);
		cartel.setLine(1, Double.toString(precio));
		cartel.setLine(2, "¡Prueba Suerte!");
		cartel.setLine(3, "Esperando...");
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

}
