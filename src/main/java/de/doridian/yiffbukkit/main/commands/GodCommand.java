package de.doridian.yiffbukkit.main.commands;

import de.doridian.yiffbukkit.core.util.PlayerHelper;
import de.doridian.yiffbukkit.main.PermissionDeniedException;
import de.doridian.yiffbukkit.main.YiffBukkitCommandException;
import de.doridian.yiffbukkit.main.commands.system.ICommand.Help;
import de.doridian.yiffbukkit.main.commands.system.ICommand.Names;
import de.doridian.yiffbukkit.main.commands.system.ICommand.Permission;
import de.doridian.yiffbukkit.main.commands.system.ICommand.Usage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Set;

@Names("god")
@Help("Activates or deactivates god mode.")
@Usage("[<name>] [on|off]")
@Permission("yiffbukkit.players.god")
public class GodCommand extends AbstractPlayerStateCommand implements Listener {
	private final Set<String> godded = states;

	public GodCommand() {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent event) {
		final Entity entity = event.getEntity();
		if (entity instanceof Player) {
			handleEntityDamage(event, entity);
			return;
		}

		final Entity passenger = entity.getPassenger();
		if (passenger instanceof Player) {
			handleEntityDamage(event, passenger);
		}
	}

	private void handleEntityDamage(EntityDamageEvent event, Entity entity) {
		final Player ply = (Player) entity;

		final String playerName = ply.getName();

		if (godded.contains(playerName))
			event.setCancelled(true);
	}

	@Override
	protected void onStateChange(boolean prevState, boolean newState, String targetName, CommandSender commandSender) throws YiffBukkitCommandException {
		if (!commandSender.getName().equals(targetName)) {
			if (!commandSender.hasPermission("yiffbukkit.players.god.others"))
				throw new PermissionDeniedException();
		}

		final String commandSenderName = commandSender.getName();
		final Player target = plugin.getServer().getPlayer(targetName);

		if (targetName.equals(commandSenderName)) {
			if (newState) {
				if (prevState)
					PlayerHelper.sendDirectedMessage(commandSender, "You are already invincible.");
				else {
					PlayerHelper.sendServerMessage(commandSenderName + " made themselves invincible.", commandSender);
					PlayerHelper.sendDirectedMessage(commandSender, "You are now invincible.");
				}
			}
			else {
				if (prevState) {
					PlayerHelper.sendServerMessage(commandSenderName + " made themselves no longer invincible.", commandSender);
					PlayerHelper.sendDirectedMessage(commandSender, "You are no longer invincible.");
				}
				else
					PlayerHelper.sendDirectedMessage(commandSender, "You are not invincible.");
			}
		}
		else {
			if (newState) {
				if (prevState)
					PlayerHelper.sendDirectedMessage(commandSender, targetName+" is already invincible.");
				else {
					PlayerHelper.sendServerMessage(commandSenderName + " made " + targetName + " invincible.", commandSender, target);
					PlayerHelper.sendDirectedMessage(commandSender, "You made "+targetName+" invincible.");
					if (target != null)
						PlayerHelper.sendDirectedMessage(target, commandSenderName+" made you invincible.");
				}
			}
			else {
				if (prevState) {
					PlayerHelper.sendServerMessage(commandSenderName + " made " + targetName + " no longer invincible.", commandSender, target);
					PlayerHelper.sendDirectedMessage(commandSender, "You made "+targetName+" no longer invincible.");
					if (target != null)
						PlayerHelper.sendDirectedMessage(target, commandSenderName+" made you no longer invincible.");
				}
				else
					PlayerHelper.sendDirectedMessage(commandSender, targetName+" is not invincible.");
			}
		}
	}
}
