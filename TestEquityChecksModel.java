import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestEquityChecksModel {
    @Test
    public void testInternalDueDate() {
        EquityChecksModel model = new EquityChecksModel();
        Long date = System.currentTimeMillis();
        model.setInternalDueDate(date);
        assertEquals(date, model.getInternalDueDate());
    }

    @Test 
    public void testRequestedDueDate() {
        EquityChecksModel model = new EquityChecksModel();
        Long date = System.currentTimeMillis();
        model.setRequestedDueDate(date);
        assertEquals(date, model.getRequestedDueDate());
    }
    
    @Test
    public void testIsEmptyEquityCheck() {
        EquityChecksModel model = new EquityChecksModel();
        model.setIsEmptyEquityCheck(true);
        assertTrue(model.getIsEmptyEquityCheck());
        
        model.setIsEmptyEquityCheck(false);
        assertFalse(model.getIsEmptyEquityCheck());
    }
    
    @Test
    public void testHasEverBeenSaved() {
        EquityChecksModel model = new EquityChecksModel();
        model.setHasEverBeenSaved(true);
        assertTrue(model.getHasEverBeenSaved());
        
        model.setHasEverBeenSaved(false);
        assertFalse(model.getHasEverBeenSaved());
    }
}
