import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import sun.security.krb5.internal.ReplayCache;

public class World extends Observable {

	private int tick;
	private int size;

	private Player player;
	private Thread thread;
	private boolean notOver;
	private long delayed = 500;
	private int enemyCount = 10;
	private List<Command> histories = new ArrayList<Command>();
	private boolean replayMode = false;

	private Enemy[] enemies;

	public World(int size) {
		this.size = size;
		tick = 0;
		player = new Player(size / 2, size / 2);
		enemies = new Enemy[enemyCount];
		Random random = new Random();
		for (int i = 0; i < enemies.length; i++) {
			enemies[i] = new Enemy(random.nextInt(size), random.nextInt(size));
		}
	}

	private void setReplay() {
		this.replayMode = true;
	}

	public void start() {
		player.reset();
		player.setPosition(size / 2, size / 2);
		tick = 0;
		notOver = true;
		thread = new Thread() {
			@Override
			public void run() {
				while (notOver) {
					if (replayMode) {
						for (Command c : histories) {
							if (c.getTick() == tick) {
								c.execute();
							}
						}
					}
					tick++;
					player.move();
					checkCollisions();
					setChanged();
					notifyObservers();
					waitFor(delayed);
				}
			}
		};
		thread.start();
	}

	private void checkCollisions() {
		for (Enemy e : enemies) {
			if (e.hit(player)) {
				notOver = false;
			}
		}
	}

	private void waitFor(long delayed) {
		try {
			Thread.sleep(delayed);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int getTick() {
		return tick;
	}

	public int getSize() {
		return size;
	}

	public Player getPlayer() {
		return player;
	}

	public void turnPlayerNorth() {
		CommandNorth command = new CommandNorth(tick, player);
		command.execute();
		histories.add(command);
	}

	public void turnPlayerSouth() {
		CommandSouth command = new CommandSouth(tick, player);
		command.execute();
		histories.add(command);
	}

	public void turnPlayerWest() {
		CommandWest command = new CommandWest(tick, player);
		command.execute();
		histories.add(command);
	}

	public void turnPlayerEast() {
		CommandEast command = new CommandEast(tick, player);
		command.execute();
		histories.add(command);
	}

	public Enemy[] getEnemies() {
		return enemies;
	}

	public boolean isGameOver() {
		return !notOver;
	}
}
