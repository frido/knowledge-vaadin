package com.example.application;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.example.application.annotation.MyAnnotatedObject;
import com.example.application.annotation.MyAnnotation;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * Annotation is just marker (with values).
 * We can get annotations only with reflection.
 */
public class AnotationsTest {
    
    /**
     * Simulate create query based on class annotations
     */
    @Test
    public void annotations() {
        Class<?> clazz = MyAnnotatedObject.class;
        MyAnnotation entityAnnotation = clazz.getDeclaredAnnotation(MyAnnotation.class);
        if (entityAnnotation == null) {
            fail();
        }
        String tableName = entityAnnotation.value();

        List<String> columns = new ArrayList<>();
        for(Field field : clazz.getDeclaredFields()) {
            MyAnnotation fieldAnnotation = field.getDeclaredAnnotation(MyAnnotation.class);
            if (fieldAnnotation == null) {
                fail();
            }
            String columnName = fieldAnnotation.value();
            columns.add(columnName);
        }

        String select = StringUtils.join(columns, ", ");
        String sql = String.format("SELECT %s FROM %s", select, tableName);

        assertEquals("SELECT COL_1, COL_2 FROM TABLE_1", sql);
    }
}
