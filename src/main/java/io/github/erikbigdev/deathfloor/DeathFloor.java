package io.github.erikbigdev.deathfloor;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathFloor extends JavaPlugin implements Listener{

	@Override
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		//placedBlocks.add(Bukkit.getServer().getWorld("world").getSpawnLocation().subtract(0.0f, 1.0f, 0.0f).getBlock().getLocation());
	}
	
	@EventHandler
	void BlockPlaced(BlockPlaceEvent event) {
		placedBlocks.add(event.getBlockPlaced().getLocation());
	}

	@EventHandler
	void PlayerRespawned(PlayerRespawnEvent event) {
		Location loc = event.getRespawnLocation().subtract(0.0f, 1.0f, 0.0f).getBlock().getLocation();
		placedBlocks.add(loc);
		Bukkit.getLogger().info("PlayerRespawnEvent: " + loc.toString());
	}
	
	@EventHandler
	void PlayerJoined(PlayerJoinEvent event) {
		
		new BukkitRunnable() {
			
			Player player = event.getPlayer();
			
			int justCreated = 27;
			
			@Override
			public void run() {
				if(player == null) {
					Bukkit.getLogger().warning(player.getName() + " is null!!!");
					this.cancel();
					return;
				}
				if(player.isDead())
					return;
				
				Location loc = player.getLocation().subtract(0.0f, 1.0f, 0.0f).getBlock().getLocation();
				if(justCreated > 0) {
					placedBlocks.add(loc);
					justCreated--;
					return;
				}
				else if(!placedBlocks.contains(loc) && loc.getBlock().getType().isSolid()) {
					player.setHealth(0.0f);
					Bukkit.getLogger().info("Player Location: " + loc.toString());
					for(Location item : placedBlocks)
						Bukkit.getLogger().warning(item.toString());
				}
			}
		}.runTaskTimer(this, 0, 1);
	}
	
	HashSet<Location> placedBlocks = new HashSet<Location>();
	
	Location getBlockLoc(Location loc) {
		loc.setX(loc.getBlockX());
		loc.setY(loc.getBlockY());
		loc.setZ(loc.getBlockZ());
		
		return loc;
	}
}
