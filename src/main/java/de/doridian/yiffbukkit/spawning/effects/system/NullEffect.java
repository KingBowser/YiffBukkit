package de.doridian.yiffbukkit.spawning.effects.system;

public class NullEffect extends YBEffect {
	public NullEffect() {
		super(null);
	}

	@Override
	public void start() {
		done();
	}

	@Override
	protected void runEffect() { }
}
