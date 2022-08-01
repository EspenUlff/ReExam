package com.dtu.roboserver;

import com.dtu.common.model.FieldAction;
import com.dtu.common.model.fileaccess.FieldActionTypeAdapter;
import org.springframework.boot.autoconfigure.gson.GsonBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GsonConfig {
    @Bean
    public GsonBuilderCustomizer typeAdapter() {
        return gsonBuilder -> {
            gsonBuilder.registerTypeAdapter(FieldAction.class, new FieldActionTypeAdapter());
            //gsonBuilder.registerTypeAdapter(Player.class, new Adapter<Player>());
        };
    }
}
