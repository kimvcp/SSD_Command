
public abstract class Command {

	private int tick;
	private Player player;
	
	public Command(int tick, Player player) {
		this.tick = tick;
		this.player = player;
	}
	
	public abstract void execute();

	public int getTick() {
		return tick;
	}

	public void setTick(int tick) {
		this.tick = tick;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
}

