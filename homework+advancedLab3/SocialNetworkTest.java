import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class SocialNetworkTest {
    private SocialNetwork network;

    @BeforeEach
    void setUp() {
        network = new SocialNetwork();
    }

    @Test
    void testArticulationPointsInLineGraph() {
        Person a = new Person("Alice", "Iasi", "1990");
        Person b = new Person("Bob", "Iasi", "1991");
        Person c = new Person("Charlie", "Iasi", "1992");

        a.addRelationship(b, "friend");
        b.addRelationship(a, "friend");
        b.addRelationship(c, "friend");
        c.addRelationship(b, "friend");

        network.addProfile(a);
        network.addProfile(b);
        network.addProfile(c);

        network.analyzeConnectivity();

        assertTrue(network.getArticulationPoints().contains(b), "Bob ar trebui să fie punct de articulare!");
        assertEquals(1, network.getArticulationPoints().size(), "Ar trebui să existe un singur punct de articulare.");
    }

    @Test
    void testNoArticulationPointsInCycle() {
        Person a = new Person("A", "X", "0");
        Person b = new Person("B", "X", "0");
        Person c = new Person("C", "X", "0");

        a.addRelationship(b, "rel"); b.addRelationship(a, "rel");
        b.addRelationship(c, "rel"); c.addRelationship(b, "rel");
        c.addRelationship(a, "rel"); a.addRelationship(c, "rel");

        network.addProfile(a);
        network.addProfile(b);
        network.addProfile(c);

        network.analyzeConnectivity();

        assertTrue(network.getArticulationPoints().isEmpty(), "Un ciclu nu ar trebui să aibă puncte de articulare.");
    }
}