package ruleta;

public class Puntuacion {
	private String motivo;
	private double mult;

	public Puntuacion(String motivo, double d) {
		this.motivo = motivo;
		this.mult = d;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public double getMult() {
		return mult;
	}

	public void setMult(float mult) {
		this.mult = mult;
	}
}
