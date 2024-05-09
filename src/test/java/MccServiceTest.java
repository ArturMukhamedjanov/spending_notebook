import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import application.models.Mcc;
import application.service.MccService;
import application.service.DAO.MccDAO;

public class MccServiceTest {
    
    private MccService mccService;
    private MccDAO mccDAO;

    @Before
    public void setUp() {
        mccDAO = mock(MccDAO.class);
        mccService = new MccService(mccDAO);
    }

    @Test
    public void testSaveMcc() {
        Mcc mcc = new Mcc();
        mcc.setCode("1234");

        mccService.saveMcc(mcc);

        verify(mccDAO).saveMcc(mcc);
    }

    @Test
    public void testGetMccById() {
        Long id = 1L;
        Mcc mcc = new Mcc();
        mcc.setId(id);

        when(mccDAO.getMccById(id)).thenReturn(mcc);

        assertEquals(mcc, mccService.getMccById(id));
    }

    @Test
    public void testGetAllMccs() {
        List<Mcc> mccs = new ArrayList<>();
        mccs.add(new Mcc());
        mccs.add(new Mcc());

        when(mccDAO.getAllMccs()).thenReturn(mccs);

        assertEquals(mccs, mccService.getAllMccs());
    }

    @Test
    public void testUpdateMcc() {
        Mcc mcc = new Mcc();

        mccService.updateMcc(mcc);

        verify(mccDAO).updateMcc(mcc);
    }

    @Test
    public void testDeleteMcc() {
        Long id = 1L;

        mccService.deleteMcc(id);

        verify(mccDAO).deleteMcc(id);
    }

    @Test
    public void testGetMccByCode() {
        String code = "1234";
        Mcc mcc = new Mcc();
        mcc.setCode(code);

        when(mccDAO.getMccByCode(code)).thenReturn(mcc);

        assertEquals(mcc, mccService.getMccByCode(code));
    }

    @Test
    public void testIsMccCodeExists() {
        String code = "1234";
        Mcc mcc = new Mcc();
        mcc.setCode(code);

        when(mccDAO.getMccByCode(code)).thenReturn(mcc);

        assertTrue(mccService.isMccCodeExists(code));
    }
}
