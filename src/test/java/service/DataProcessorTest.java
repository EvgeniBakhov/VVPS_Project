package service;

import common.TestBase;
import exception.NullDataException;
import model.Entity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class DataProcessorTest {

    @Test
    void findRelativeFrequencyForUserTestEntriesContainEvent() throws NullDataException {
        Map<String, Double> result = DataProcessor.findRelativeFrequencyForUser(createList(), "Wiki page updated");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(100.0, result.get("7912"));
    }

    @Test
    void findRelativeFrequencyForUserTestEntitiesParamIsNull() throws NullDataException {
        Assertions.assertThrows(NullDataException.class, () -> {
            DataProcessor.findRelativeFrequencyForUser(null, "Name");
        });
    }

    @Test
    void findRelativeFrequencyForUserTestEventNameParamIsNull() throws NullDataException {
        Assertions.assertThrows(NullDataException.class, () -> {
            DataProcessor.findRelativeFrequencyForUser(new ArrayList<>(), null);
        });
    }

    @Test
    void findAbsFrequencyForUserTest() {
    }

    @Test
    void findMedianTest() {
    }

    @Test
    void findScopeTest() {
    }

    @Test
    void findMinMaxTest() {
    }

    private List<Entity> createList() {
        List<Entity> list = new ArrayList<>();
        list.add(TestBase.createDefaultEntity());
        return list;
    }
}