package hiPkg;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class PolicyTest {
	
	private Policy p;
	private PolicyType plT = new PolicyType("Sample", 100, 1);

	@Before
	public void setUp() throws Exception {
		//sample objects to complete policy constructor
		ArrayList<Condition> cAL = new ArrayList<Condition>();
		Client cl = new Client("Sample", "Client", 21, cAL);
		ArrayList<Client> clAL = new ArrayList<Client>();
		clAL.add(cl);
		PolicyType plT = new PolicyType("Sample", 100, 1);
		p = new Policy(clAL, "sampleEmail", "sampleContactNo", plT);
	}

	@Test
	public void testGetQuote() {
		//build quote using known base price and only one client
		double cost = 100;
		//multiply using impact
		cost = cost*plT.getImpact();
		assertEquals(p.getQuote(), cost, 0); 
		
	}

}