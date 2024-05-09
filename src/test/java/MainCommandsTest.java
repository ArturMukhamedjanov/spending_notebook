import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import application.cli.MainCommands;
import application.cli.exceptions.MccCastException;
import application.cli.exceptions.MccExistException;
import application.models.Category;
import application.models.Mcc;
import application.service.MccService;

public class MainCommandsTest {

    private MainCommands mainCommands;
    private MccService mccService;

    @Before
    public void setUp() {
        mccService = mock(MccService.class);
        mainCommands = new MainCommands(null, mccService, null);
    }

    @Test
    public void testCheckMccsFree_NoExistingMccs() throws MccExistException {
        List<Mcc> mccs = new ArrayList<>();
        Mcc mcc1 = new Mcc();
        mcc1.setCode("1234");;
        mccs.add(mcc1);
        Mcc mcc2 = new Mcc();
        mcc2.setCode("5678");;
        mccs.add(mcc2);
        when(mccService.isMccCodeExists("1234")).thenReturn(false);
        when(mccService.isMccCodeExists("5678")).thenReturn(false);

        assertTrue(mainCommands.checkMccsFree(mccs));
    }

    @Test
    public void testCastIntArrToMcc_ValidMccCodes() throws MccCastException {
        int[] mccCodes = { 1234, 5678 };
        Category category = new Category();

        List<Mcc> mccs = mainCommands.castIntArrToMcc(mccCodes, category);

        assertEquals(2, mccs.size());
        assertEquals("1234", mccs.get(0).getCode());
        assertEquals("5678", mccs.get(1).getCode());
        assertEquals(category, mccs.get(0).getCategory());
        assertEquals(category, mccs.get(1).getCategory());
    }

    @Test(expected = MccCastException.class)
    public void testCastIntArrToMcc_InvalidMccCode() throws MccCastException {
        int[] mccCodes = { 123, 5678 }; // Invalid MCC code 123
        Category category = new Category();

        mainCommands.castIntArrToMcc(mccCodes, category);
    }


}
