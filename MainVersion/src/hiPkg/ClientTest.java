package hiPkg;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class ClientTest {

	private Client cl;
	private Condition co;
	private ArrayList<Condition> coAL;
	@Before
	public void setUp() throws Exception {
		//Initialize a client object. 
		co = new Condition(1, "Name", 100);
		coAL = new ArrayList<Condition>();
		coAL.add(co);
		cl = new Client("First", "Last", 0, coAL);
	}

	@Test
	public void testGetConditions() {
		boolean correctName = coAL.get(0).getName() == "Name";
		boolean correctDegree = coAL.get(0).getDegree() == 100;
		boolean correctID = coAL.get(0).getID() == 1;
		//check all three have returned to true values and indicate a pass of the test
		assertTrue(correctName && correctDegree && correctID );
		
		
	}

}
