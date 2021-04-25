package util;

import model.Entity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

class FileReaderTest {

    @Test
    void extractEntitiesFromFileTestAndCorrectFilePassedAsParamThenShouldReturnListWithEntities() {
        File file = new File("src/test/java/resource/correct_file.xlsx");
        List<Entity> result = FileReader.extractEntitiesFromFile(file);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }
}