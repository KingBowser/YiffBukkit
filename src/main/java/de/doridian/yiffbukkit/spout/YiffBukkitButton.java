package de.doridian.yiffbukkit.spout;

import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.GenericButton;

import de.doridian.yiffbukkit.YiffBukkitCommandException;

public abstract class YiffBukkitButton extends GenericButton {
	public YiffBukkitButton(String text) {
		super(text);
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		try {
			onClicked(event);
		}
		catch(YiffBukkitCommandException e) {
			event.getPlayer().sendMessage("[YBSC] " + e.getMessage());
			//e.printStackTrace();
		}
	}
	
	public abstract void onClicked(ButtonClickEvent event) throws YiffBukkitCommandException;
}