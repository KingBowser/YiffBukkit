package de.doridian.yiffbukkit.commands;

import java.util.Hashtable;

import org.bukkit.entity.Player;

import de.doridian.yiffbukkit.YiffBukkitCommandException;
import de.doridian.yiffbukkit.commands.ICommand.*;
import de.doridian.yiffbukkit.util.PlayerHelper.WeatherType;

@Names("servertime")
@Help("Forces/fixes current time *serverside*.")
@Usage("[normal|night|day|morning|afternoon|<0-23>]")
@Level(3)
@StringFlags("w")
public class ServerTimeCommand extends ICommand {
	Hashtable<String,Long> timeSwatches = new Hashtable<String,Long>();
	{
		timeSwatches.put("night", 0L);
		timeSwatches.put("morning", 6L);
		timeSwatches.put("day", 12L);
		timeSwatches.put("afternoon", 18L);
	};

	@Override
	public void Run(Player ply, String[] args, String argStr) throws YiffBukkitCommandException {
		args = parseFlags(args);

		String weather = stringFlags.get('w');
		final WeatherType weatherType;
		if (weather == null)
			weatherType = null;
		else if (weather.equalsIgnoreCase("rain"))
			weatherType = WeatherType.RAIN;
		else if (weather.equalsIgnoreCase("thunderstorm"))
			weatherType = WeatherType.THUNDERSTORM;
		else if (weather.equalsIgnoreCase("thunder"))
			weatherType = WeatherType.THUNDERSTORM;
		else if (weather.equalsIgnoreCase("none"))
			weatherType = WeatherType.NONE;
		else if (weather.equalsIgnoreCase("clear"))
			weatherType = WeatherType.NONE;
		else
			throw new YiffBukkitCommandException("Invalid weather specified.");

		long displayTime;

		if (args.length == 0 || args[0].equalsIgnoreCase("normal")) {
			setTime(ply, null, null, weatherType);
			return;
		}
		else if (timeSwatches.containsKey(argStr.toLowerCase())) {
			displayTime = timeSwatches.get(argStr.toLowerCase());
		}
		else {
			try
			{
				displayTime = Long.valueOf(argStr);
			}
			catch (Exception e) {
				throw new YiffBukkitCommandException("Usage: " + GetUsage(), e);
			}
		}

		final long setTime = ((displayTime+18)%24)*1000;
		setTime(ply, setTime, displayTime, weatherType);
	}

	protected void setTime(Player ply, Long setTime, Long displayTime, WeatherType setWeather) {
		playerHelper.frozenServerTime = setTime;
		if (setTime == null) {
			playerHelper.SendServerMessage(ply.getName() + " reset the server time back to normal!");
		}
		else {
			playerHelper.SendServerMessage(ply.getName() + " forced the server time to be: " + displayTime + ":00");
		}

		playerHelper.frozenServerWeather = setWeather;
		if (setWeather == null) {
			playerHelper.SendServerMessage(ply.getName() + " reset the server weather back to normal!");
		}
		else {
			playerHelper.SendServerMessage(ply.getName() + " forced the server weather to be: " + setWeather.name + ".");
		}
		playerHelper.pushWeather();
	}
}
