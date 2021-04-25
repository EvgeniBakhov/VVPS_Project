package common;

import model.Entity;

import java.time.LocalDateTime;

public class TestBase {

    /**
     * Creates a default {@link Entity} for testing purposes.
     *
     * @return {@link Entity} with default values.
     */
    public static Entity createDefaultEntity() {
        Entity entity = new Entity();
        entity.setEventName("Wiki page updated");
        entity.setDescription("The user with id \'7912\' updated the page with id \'285\' for the wiki with course module id \'5131\'");
        entity.setComponent("Wiki");
        entity.setEventContext("Context");
        entity.setTime(LocalDateTime.MIN);
        return entity;
    }

    /**
     * Allows to create an {@link Entity} with custom fields.
     *
     * @param name name of event.
     * @param description description of event.
     * @param component component for event.
     * @param context context of event.
     * @param time time of event occurrence.
     * @return {@link Entity} with custom-set fields.
     */
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
