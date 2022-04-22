package sets;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

/**
 * FiniteSetTest is a glassbox test of the FiniteSet class.
 */
public class FiniteSetTest {

  /** Test creating sets. */
  @Test
  public void testCreation() {
    assertEquals(Arrays.asList(),
        FiniteSet.of(new float[0]).getPoints());         // empty
    assertEquals(Arrays.asList(1f),
        FiniteSet.of(new float[] {1}).getPoints());      // one item
    assertEquals(Arrays.asList(1f, 2f),
        FiniteSet.of(new float[] {1, 2}).getPoints());   // two items
    assertEquals(Arrays.asList(1f, 2f),
        FiniteSet.of(new float[] {2, 1}).getPoints());   // two out-of-order
    assertEquals(Arrays.asList(-2f, 2f),
        FiniteSet.of(new float[] {2, -2}).getPoints());  // negative
  }

  // Some example sets used by the tests below.
  private static FiniteSet S0 = FiniteSet.of(new float[0]);
  private static FiniteSet S1 = FiniteSet.of(new float[] {1});
  private static FiniteSet S12 = FiniteSet.of(new float[] {1, 2});

  /** Test set equality. */
  @Test
  public void testEquals() {
    assertTrue(S0.equals(S0));
    assertFalse(S0.equals(S1));
    assertFalse(S0.equals(S12));

    assertFalse(S1.equals(S0));
    assertTrue(S1.equals(S1));
    assertFalse(S1.equals(S12));

    assertFalse(S12.equals(S0));
    assertFalse(S12.equals(S1));
    assertTrue(S12.equals(S12));
  }

  /** Test set size. */
  @Test
  public void testSize() {
    assertEquals(S0.size(), 0);
    assertEquals(S1.size(), 1);
    assertEquals(S12.size(), 2);
  }

  private static FiniteSet FS0 = FiniteSet.of(new float[0]);
  private static FiniteSet FS0Another = FiniteSet.of(new float[0]);
  private static FiniteSet FS1 = FiniteSet.of(new float[] {1});
  private static FiniteSet FS1Another = FiniteSet.of(new float[] {2});
  private static FiniteSet FS123 = FiniteSet.of(new float[] {1, 2, 3});
  private static FiniteSet FS456 = FiniteSet.of(new float[] {4, 5, 6});
  private static FiniteSet FS45 = FiniteSet.of(new float[] {4, 5});
  private static FiniteSet FSNegative = FiniteSet.of(new float[] {-2, 2});


  /** Tests forming the union of two finite sets. */
  @Test
  public void testUnion() {
    assertEquals(Arrays.asList(1f, 2f, 3f), FS0.union(FS123).getPoints());
    assertEquals(Arrays.asList(), FS0.union(FS0Another).getPoints());
    assertEquals(Arrays.asList(1f, 4f, 5f, 6f), FS1.union(FS456).getPoints());
    assertEquals(Arrays.asList(1f, 2f), FS1.union(FS1Another).getPoints());
    assertEquals(Arrays.asList(1f, 2f, 3f, 4f, 5f, 6f), FS123.union(FS456).getPoints());
    assertEquals(Arrays.asList(1f, 2f, 3f, 4f, 5f), FS123.union(FS45).getPoints());
    assertEquals(Arrays.asList(4f, 5f, 6f), FS456.union(FS45).getPoints());
    assertEquals(Arrays.asList(1f, 2f, 3f), FS123.union(FS123).getPoints());
    assertEquals(Arrays.asList(-2f, 2f, 4f, 5f, 6f), FSNegative.union(FS456).getPoints());
  }

  /** Tests forming the intersection of two finite sets. */
  @Test
  public void testIntersection() {
    assertEquals(Arrays.asList(), FS0.intersection(FS123).getPoints());
    assertEquals(Arrays.asList(), FS0.intersection(FS0Another).getPoints());
    assertEquals(Arrays.asList(), FS1.intersection(FS456).getPoints());
    assertEquals(Arrays.asList(), FS1.intersection(FS1Another).getPoints());
    assertEquals(Arrays.asList(), FS123.intersection(FS456).getPoints());
    assertEquals(Arrays.asList(), FS123.intersection(FS45).getPoints());
    assertEquals(Arrays.asList(4f, 5f), FS456.intersection(FS45).getPoints());
    assertEquals(Arrays.asList(1f, 2f, 3f), FS123.intersection(FS123).getPoints());
    assertEquals(Arrays.asList(), FSNegative.intersection(FS456).getPoints());
  }

  /** Tests forming the difference of two finite sets. */
  @Test
  public void testDifference() {
    assertEquals(Arrays.asList(), FS0.difference(FS123).getPoints());
    assertEquals(Arrays.asList(1f, 2f, 3f), FS123.difference(FS0).getPoints());
    assertEquals(Arrays.asList(), FS0.difference(FS0Another).getPoints());
    assertEquals(Arrays.asList(1f), FS1.difference(FS456).getPoints());
    assertEquals(Arrays.asList(4f, 5f, 6f), FS456.difference(FS1).getPoints());
    assertEquals(Arrays.asList(1f), FS1.difference(FS1Another).getPoints());
    assertEquals(Arrays.asList(1f, 2f, 3f), FS123.difference(FS456).getPoints());
    assertEquals(Arrays.asList(1f, 2f, 3f), FS123.difference(FS45).getPoints());
    assertEquals(Arrays.asList(6f), FS456.difference(FS45).getPoints());
    assertEquals(Arrays.asList(), FS123.difference(FS123).getPoints());
    assertEquals(Arrays.asList(-2f, 2f), FSNegative.difference(FS456).getPoints());
  }

}
