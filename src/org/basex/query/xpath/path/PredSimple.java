package org.basex.query.xpath.path;

import java.io.IOException;
import org.basex.data.Serializer;
import org.basex.query.QueryException;
import org.basex.query.xpath.XPContext;
import org.basex.query.xpath.expr.Expr;
import org.basex.query.xpath.expr.Pos;
import org.basex.query.xpath.item.Comp;
import org.basex.query.xpath.item.Dbl;
import org.basex.query.xpath.item.Item;
import org.basex.query.xpath.item.Nod;
import org.basex.query.xpath.item.NodeBuilder;

/**
 * XPath predicate.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-08, ISC License
 * @author Christian Gruen
 */
public final class PredSimple extends Pred {
  /** Predicate expression. */
  public Expr expr;

  /**
   * Constructor.
   * @param exp Expression to evaluate
   */
  PredSimple(final Expr exp) {
    expr = exp;
  }

  @Override
  NodeBuilder eval(final XPContext ctx, final NodeBuilder set)
      throws QueryException {

    final int[] nodes = set.nodes;
    final int size = set.size;
    final NodeBuilder tmp = new NodeBuilder();
    ctx.item.currSize = size;
    ctx.iu = false;

    for(int n = 0; n != size; n++) {
      final int pre = nodes[n];
      ctx.item.set(pre);
      ctx.item.currPos = n + 1;

      final Item v = ctx.eval(expr);
      if(v instanceof Dbl) {
        // occurs e.g. for last() function
        if(v.num() == n + 1) tmp.add(pre);
      } else if(v.bool()) {
        tmp.add(pre);
      }
    }
    return tmp;
  }
  
  @Override
  boolean eval(final XPContext ctx, final Nod nodes, final int pos)
      throws QueryException {

    nodes.currPos = pos;
    final Item v = ctx.eval(expr);
    return v instanceof Dbl ? v.num() == pos : v.bool();
  }

  @Override
  boolean usesSize() {
    return expr.usesSize();
  }

  @Override
  boolean usesPos() {
    return expr.usesPos();
  }

  @Override
  double posPred() {
    return 0;
  }

  @Override
  Pred compile(final XPContext ctx) throws QueryException {
    expr = expr.comp(ctx);

    // add position predicate [1] to evaluate only first hits
    if(expr instanceof LocPath) {
      ((LocPath) expr).addPosPred(ctx);
      return this;
    }

    // number: create explicit position predicate
    if(expr instanceof Dbl) expr = Pos.get(((Dbl) expr).num(), Comp.EQ);
   
    // check position test
    final Pred pred = PredPos.get(expr);
    return pred != null ? pred : this;
  }
  
  @Override
  boolean alwaysFalse() {
    return expr instanceof Item ? !((Item) expr).bool() : false;
  }

  @Override
  boolean alwaysTrue() {
    return expr instanceof Item ? ((Item) expr).bool() : false;
  }

  @Override
  public Expr indexEquivalent(final XPContext ctx, final Step step, 
      final boolean seq) throws QueryException {
    return expr.indexEquivalent(ctx, step, seq);
  }

  @Override
  public int indexSizes(final XPContext ctx, final Step curr, final int min) {
    return expr.indexSizes(ctx, curr, min);
  }

  @Override
  public boolean sameAs(final Pred pred) {
    return pred instanceof PredSimple && expr.sameAs(((PredSimple) pred).expr);
  }

  @Override
  public String toString() {
    return "[" + expr + "]";
  }

  @Override
  public void plan(final Serializer ser) throws IOException {
    ser.openElement(this);
    expr.plan(ser);
    ser.closeElement();
  }
}
