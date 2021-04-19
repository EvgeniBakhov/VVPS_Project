package common;

import model.Entity;

import java.time.LocalDateTime;

public class TestBase {

    public static Entity createDefaultEntity() {
        Entity entity = new Entity();
        entity.setEventName("Wiki page updated");
        entity.setDescription("The user with id \'7912\' updated the page with id \'285\' for the wiki with course module id \'5131\'");
        entity.setComponent("Wiki");
        entity.setEventContext("Context");
        entity.setTime(LocalDateTime.MIN);
        return entity;
    }

    public static Entity createEntityWithFields(String name,
                                                String description,
                                                String component,
                                                String context,
                                                LocalDateTime time) {
        Entity entity = new Entity();
        entity.setEventName(name);
        entity.setDescription(description);
        entity.setComponent(component);
        entity.setEventContext(context);
        entity.setTime(time);
        return entity;
    }
}
