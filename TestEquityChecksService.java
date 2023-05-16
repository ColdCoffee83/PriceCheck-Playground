import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestEquityChecksService {

   @Test
    void testGetFilterEquityChecks () {

        // arrange
        EquityChecksService service = mock(EquityChecksService.class);
        String searchString = "search";
        String fieldStationList = "fieldStation1";
        String requestingOffice = "office1";

        // act
        List<EquityChecksModel> result = service.getFilterEquityChecks(searchString, fieldStationList, requestingOffice);

        // assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("search", result.get(0).getEquiNumber());
        assertEquals("fieldStation1", result.get(0).getFieldStationList().get(0).getName());
        assertEquals("office1", result.get(0).getRequestingOffice());
    }

    @Test
    void testCreateOrUpdateEquityCheck () {

        // arrange
        EquityChecksModel model = new EquityChecksModel();
        model.setEquiNumber("EQUI-123");
        model.setRequestingOffice("office1");
        model.setFieldStationList(Arrays.asList(new FieldStationModel("fieldStation1"), new FieldStationModel("fieldStation2")));

        EquityChecksService service = mock(EquityChecksService.class);

        // act
        EquityChecksModel result = service.createOrUpdateEquityCheck(model);

        // assert
        assertNotNull(result);
        assertEquals("EQUI123", result.getEquiNumber());
        assertEquals("office1", result.getRequestingOffice());
        assertEquals(2, result.getFieldStationList().size());
        assertEquals("fieldStation1", result.getFieldStationList().get(0).getName());
        assertEquals("fieldStation2", result.getFieldStationList().get(1).getName());
    }

  
 
}