package com.barbearia.fahcortes.infra.config;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true) // Habilita a busca por campos
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE) // Permite acessar campos privados
                .setMatchingStrategy(MatchingStrategies.STRICT); // Estratégia rígida para não confundir nomes

        return modelMapper;
    }
}
