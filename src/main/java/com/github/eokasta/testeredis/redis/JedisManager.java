package com.github.eokasta.testeredis.redis;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

@Data
public class JedisManager {

    private final JavaPlugin plugin;
    private final JedisPool jedisPool;
    private final JedisSerializer serializer;

    public JedisManager(JavaPlugin plugin, String host, int port) {
        this.plugin = plugin;
        this.jedisPool = new JedisPool(host, port);
        this.serializer = new JedisSerializer(jedisPool);
    }

    public void registerChannel(JedisPubSub channelClass, String channelName) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final Jedis resource = jedisPool.getResource();
            resource.subscribe(channelClass, channelName);
        });
    }

    public void sendMessageChannel(String channel, String message) {
        try (final Jedis resource = jedisPool.getResource()) {
            resource.publish(channel, message);
        }
    }

    public void close() {
        jedisPool.close();
    }

}
