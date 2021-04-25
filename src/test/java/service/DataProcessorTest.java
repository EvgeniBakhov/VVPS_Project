package service;

import common.TestBase;
import exception.NullDataException;
import model.Entity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DataProcessorTest {

    @Test
    void findRelativeFrequencyForUserTestEntriesContainEventThenShouldCalculateRelativeFrequency() throws NullDataException {
        Map<String, Double> result = DataProcessor.findRelativeFrequencyForUser(createList(), "Wiki page updated");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(100.0, result.get("7912"));
    }

    @Test
    void findRelativeFrequencyForUserTestEntitiesParamIsNullThenExceptionIsThrown() {
        Assertions.assertThrows(NullDataException.class, () -> {
            DataProcessor.findRelativeFrequencyForUser(null, "Name");
        });
    }

    @Test
    void findRelativeFrequencyForUserTestEventNameParamIsNullThenExceptionIsThrown() {
        Assertions.assertThrows(NullDataException.class, () -> {
            DataProcessor.findRelativeFrequencyForUser(new ArrayList<>(), null);
        });
    }

    @Test
    void findAbsFrequencyForUserTestEntitiesContainEventThenShouldCalculateAbsFrequency() throws NullDataException {
        List<Entity> entities = new ArrayList<>();
        entities.add(TestBase.createDefaultEntity());
        entities.add(TestBase.createDefaultEntity());
        Map<String, Long> result = DataProcessor.findAbsFrequencyForUser(entities, "Wiki page updated");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(2, result.get("7912"));
    }

    @Test
    void findAbsFrequencyForUserTestEntitiesContainsEventForDifferentUsersThenShouldCalculateAbsFrequency() throws NullDataException {
        List<Entity> entities = new ArrayList<>();
        entities.add(TestBase.createDefaultEntity());
        Entity entity = TestBase.createEntityWithFields(
                "Wiki page updated",
                "The user with id \'7913\' updated the page with id \'285\' for the wiki with course module id \'5131\'",
                "Component",
                "Context",
                LocalDateTime.MIN);
        entities.add(entity);
        Map<String, Long> result = DataProcessor.findAbsFrequencyForUser(entities, "Wiki page updated");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1, result.get("7912"));
        Assertions.assertEquals(1, result.get("7913"));
    }

    @Test
    void findAbsFrequencyForUserTestEntitiesParamIsNullThenExceptionIsThrown() {
        Assertions.assertThrows(NullDataException.class, () -> {
            DataProcessor.findAbsFrequencyForUser(null, "Name");
        });
    }

    @Test
    void findAbsFrequencyForUserTestEventNameParamIsNullThenExceptionIsThrown() {
        Assertions.assertThrows(NullDataException.class, () -> {
            DataProcessor.findAbsFrequencyForUser(new ArrayList<>(), null);
        });
    }

    @Test
    void findMedianForUserTestEntitiesContainsEventThenShouldCalculateMedian() throws NullDataException {
        double median = DataProcessor.findMedian(createList(), "Wiki page updated");
        Assertions.assertEquals(1.0, median);
    }

    @Test
    void findMedianForUserTestEntitiesContainsTwoEventsThenShouldCalculateMedian() throws NullDataException {
        List<Entity> entities = new ArrayList<>();
        entities.add(TestBase.createDefaultEntity());
        entities.add(TestBase.createEntityWithFields("Wiki page updated",
                "The user with id \'7913\' updated the page with id \'285\' for the wiki with course module id \'5131\'",
                "Component", "Context", LocalDateTime.MIN));
        double median = DataProcessor.findMedian(entities, "Wiki page updated");
        Assertions.assertEquals(1.0, median);
    }

    @Test
    void findMedianForUserTestEntitiesParamIsNullThenExceptionIsThrown() {
        Assertions.assertThrows(NullDataException.class, () -> {
            DataProcessor.findMedian(null, "Name");
        });
    }

    @Test
    void findMedianForUserTestEventNameParamIsNullThenExceptionIsThrown() {
        Assertions.assertThrows(NullDataException.class, () -> {
            DataProcessor.findMedian(new ArrayList<>(), null);
        });
    }

    @Test
    void findScopeTestEntitiesContainsEventThenShouldCalculateScope() throws NullDataException {
        long scope = DataProcessor.findScope(createList(), "Wiki page updated");
        Assertions.assertEquals(0, scope);
    }

    @Test
    void findScopeForUserTestEntitiesParamIsNullThenExceptionIsThrown() {
        Assertions.assertThrows(NullDataException.class, () -> {
            DataProcessor.findScope(null, "Name");
        });
    }

    @Test
    void findScopeForUserTestEventNameParamIsNullThenExceptionIsThrown() {
        Assertions.assertThrows(NullDataException.class, () -> {
            DataProcessor.findScope(new ArrayList<>(), null);
        });
    }

    @Test
    void findMinMaxTestContainsEventThenShouldReturnMapWithMinMaxValues() {
        Map<String, Long> unsortedMap = new HashMap<>();
        unsortedMap.put("1234", 1L);
        unsortedMap.put("1235", 3L);
        unsortedMap.put("1236", 2L);
        Map<String, Long> result = DataProcessor.findMinMax(unsortedMap);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1L, result.get("Minimum"));
        Assertions.assertEquals(3L, result.get("Maximum"));
    }

    private List<Entity> createList() {
        List<Entity> list = new ArrayList<>();
        list.add(TestBase.createDefaultEntity());
        return list;
    }
}