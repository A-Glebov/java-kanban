package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Test
    @BeforeEach
    @Override
    public void init() throws IOException {
        taskManager = (InMemoryTaskManager) Managers.getDefault();
        super.init();
    }
    
}