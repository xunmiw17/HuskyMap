package sets;

import java.util.List;

/**
 * Represents an immutable set of points on the real line that is easy to
 * describe, either because it is a finite set, e.g., {p1, p2, ..., pN}, or
 * because it excludes only a finite set, e.g., R \ {p1, p2, ..., pN}. As with
 * FiniteSet, each point is represented by a Java float with a non-infinite,
 * non-NaN value.
 */
public class SimpleSet {

  // The points are stored in a FiniteSet, which can represent the points we have in the set or the points we want to
  // exclude from our set, based on the value (true or false) of the field "complement"
  //
  // RI: finiteSet != null and -infinity = finiteSet.vals[0] < finiteSet.vals[1] < ... <finiteSet.vals[vals.length-1] =
  // +infinity
  // AF(this) = { finiteSet.vals[1], ... ,finiteSet.vals[vals.length-2] }           if this.complement = true
  //            { R \ { finiteSet.vals[1], ... ,finiteSet.vals[vals.length-2] } }   otherwise
  private FiniteSet finiteSet;
  private boolean complement;



  /**
   * Creates a simple set containing only the given points.
   * @param vals Array containing the points to make into a SimpleSet
   * @spec.requires points != null and has no NaNs, no infinities, and no dups
   * @spec.effects this = {vals[0], vals[1], ..., vals[vals.length-1]}
   */
  public SimpleSet(float[] vals) {
    this.finiteSet = FiniteSet.of(vals);
    this.complement = false;
  }

  /**
   * Private constructor that directly fills in the fields above.
   * @param complement Whether this = points or this = R \ points
   * @param points List of points that are in the set or not in the set
   * @spec.requires points != null
   * @spec.effects this = R \ points if complement else points
   */
  private SimpleSet(boolean complement, FiniteSet points) {
    this.finiteSet = points;
    this.complement = complement;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof SimpleSet))
      return false;

    SimpleSet other = (SimpleSet) o;
    return this.finiteSet.equals(other.finiteSet) && this.complement == other.complement;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  /**
   * Returns the number of points in this set.
   * @return N      if this = {p1, p2, ..., pN} and
   *         infty  if this = R \ {p1, p2, ..., pN}
   */
  public float size() {
    // If we want the complement of the set, then the size is infinity. Otherwise it's just the size of the set.
    if (this.complement) {
      return Float.POSITIVE_INFINITY;
    } else {
      return this.finiteSet.size();
    }
  }

  /**
   * Returns a string describing the points included in this set.
   * @return the string "R" if this contains every point,
   *     a string of the form "R \ {p1, p2, .., pN}" if this contains all
   *        but {@literal N > 0} points, or
   *     a string of the form "{p1, p2, .., pN}" if this contains
   *        {@literal N >= 0} points,
   *     where p1, p2, ... pN are replaced by the individual numbers.
   */
  public String toString() {

    // We first check if there is no elements in the finite set since it is the special case. If there is no element and
    // the set is finite complement, we return "R" since this set contains every point. If there is no element and the set
    // is finite, we return "{}" as specified in the spec since there is literally no elements. After checking this boundary
    // case and we proceed, we are now sure that the set contains at least one point. Then we first check if the set is
    // finite complement again, and if it is, we add the prefix "R \ " to our final result, and the remaining part is the
    // same for both finite and finite complement set: it contains a "{" at the front, and after that there are all the
    // points separated by commas, and there is an ending "}" at last.

    StringBuilder sb = new StringBuilder();

    List<Float> points = this.finiteSet.getPoints();
    if (points.size() == 0) {
      if (this.complement) {
        return sb.append("R").toString();
      } else {
        return sb.append("{}").toString();
      }
    }

    if (this.complement) {
      sb.append("R \\ ");
    }
    sb.append("{");

    sb.append(points.get(0));
    int i = 1;
    // { Inv: sb = "{p1, p2, ..., pi"         if  complement = false
    //        sb = "R \ {p1, p2, ..., pi"     otherwise              }
    while (i < points.size()) {
      sb.append(", " + points.get(i));
      i++;
    }
    sb.append("}");

    return sb.toString();
  }

  /**
   * Returns a set representing the points R \ this.
   * @return R \ this
   */
  public SimpleSet complement() {
    // If the set "this" is finite (this.complement is false), then as a result we would not change the set but only
    // need to modify the "complement" field to be true so that we get the complement of our original set. Likewise, if
    // the set is infinite or complement of finite (this.complement is true), then we still don't need to change the set
    // but could only modify the "complement" field to be false so that we get the finite set originally represented by
    // the "finiteSet" field.
    return new SimpleSet(!this.complement, this.finiteSet);
  }

  /**
   * Returns the union of this and other.
   * @param other Set to union with this one.
   * @spec.requires other != null
   * @return this union other
   */
  public SimpleSet union(SimpleSet other) {

    /**
     * 1. When both this and other are finite, the result of union is simply the union of these two finite sets.
     *
     * 2. When this is finite complement and other is finite, the result of union is the complement of this finite set
     *    minus other finite set. This is because this finite set represents all of the elements that this does not have,
     *    and other finite set can be seen as a "supplement" to those elements. Therefore, the difference between these
     *    two finite sets is exactly the elements that are still "missing" after supplementing the elements from the other
     *    finite set, and the complement of it gives the final result.
     *
     * 3. When this is finite and other is finite complement, the result of union is the complement of other finite set
     *    minus this finite set. The calculation is exactly the same as 2. except that this and other are reversed.
     *
     * 4. When both this and other are finite complement, the result of union is the complemenet of the intersection of
     *    this and other. Both the finite sets of this and other represent the elements that are missing from each of the
     *    sets, and their intersection represent the elements that both are missing. Therefore, the result is the complement
     *    of the elements that both are missing.
     */

    if (!this.complement && !other.complement) {
      return new SimpleSet(false, this.finiteSet.union(other.finiteSet));
    } else if (this.complement && !other.complement) {
      return new SimpleSet(true, this.finiteSet.difference(other.finiteSet));
    } else if (!this.complement && other.complement) {
      return new SimpleSet(true, other.finiteSet.difference(this.finiteSet));
    } else {
      return new SimpleSet(true, this.finiteSet.intersection(other.finiteSet));
    }
  }

  /**
   * Returns the intersection of this and other.
   * @param other Set to intersect with this one.
   * @spec.requires other != null
   * @return this intersect other
   */
  public SimpleSet intersection(SimpleSet other) {
    /**
     * 1. When both this and other are finite, the result of the union of the complement of this and the complement of
     *    other is are all the elements that are either missing in this or missing in other. Therefore, the complement
     *    of the result is the intersection of this and other.
     *
     * 2. When this is finite complement and other is finite, the complement of this, call it C1, represents the finite
     *    sets of elements that this is missing, and the complement of other, call it C2, represents the finite complement
     *    of other. Thus, the result of the union of C1 and C2 represent all of the elements that are either missing in
     *    this or missing in other. Therefore, the complement of that result is our final result of intersection.
     *
     * 3. When this is finite and other is finite complement, the reasoning is exactly the same as 2. except that this
     *    and other are reversed.
     *
     * 4. When both this and other are finite complement, the complement of this and other are both finite set. We union
     *    these two finite sets to collect all the elements that are either missing from this or other. Then we take the
     *    complement of that result to get the intersection of these two sets.
     */

    return (this.complement().union(other.complement())).complement();
  }

  /**
   * Returns the difference of this and other.
   * @param other Set to difference from this one.
   * @spec.requires other != null
   * @return this minus other
   */
  public SimpleSet difference(SimpleSet other) {
    /**
     * 1. When both this and other are finite, the complement of other represents the infinite set of elements that are
     *    missing from other. We then take the intersection of it and this to get all the elements that are not in other
     *    and are in this, which is exactly the definition of the difference of two sets.
     *
     * 2. When this is finite complement and other is finite, the complement of other represents the infinite set of all
     *    elements that other does not have. In this case, both this and the complement of other are finite complement,
     *    and we union them to get the elements that exist in this but do not exist in other.
     *
     * 3. When this is finite and other is finite complement, the reasoning is exactly the same as 2. except that this
     *    and other are reversed.
     *
     * 4. When both this and other are finite complement, the complement of other represents the finite set of elements
     *    which other does not have. We then take the intersection of it and this to get all the elements that are not
     *    in other and are in this, which is exactly the definition of the difference of two sets.
     */

    return this.intersection(other.complement());
  }

}
