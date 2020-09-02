package com.github.eokasta.testeredis;

import com.github.eokasta.testeredis.redis.JedisManager;
import com.github.eokasta.testeredis.redis.JedisSerializer;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class RedisPlugin extends JavaPlugin {

    private JedisManager jedisManager;

    @Override
    public void onEnable() {
        this.jedisManager = new JedisManager(this, "localhost", 6379);
        final JedisSerializer serializer = jedisManager.getSerializer();
        serializer.putPatternObject("teste", "EoKasta", 100000D);

        System.out.println(jedisManager.getSerializer().getPatternObject("teste", "EoKasta", Double.class));

    }

    @Override
    public void onDisable() {
        jedisManager.close();
    }

}
