package org.basex.query.func.fn;

import static org.basex.query.func.Function.*;

import org.basex.query.*;
import org.basex.query.CompileContext.*;
import org.basex.query.expr.*;
import org.basex.query.func.*;
import org.basex.query.value.item.*;
import org.basex.query.value.type.*;
import org.basex.util.*;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-22, BSD License
 * @author Christian Gruen
 */
public final class FnExists extends FnEmpty {
  @Override
  public Bln item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return Bln.get(!empty(qc));
  }

  @Override
  protected Expr opt(final CompileContext cc) throws QueryException {
    return opt(false, cc);
  }

  @Override
  public Expr simplifyFor(final Simplify mode, final CompileContext cc) throws QueryException {
    // if(exists(nodes))  ->  if(nodes)
    if(mode == Simplify.EBV || mode == Simplify.PREDICATE) {
      final Expr expr = exprs[0];
      if(expr.seqType().type instanceof NodeType) {
        return cc.simplify(this, expr.simplifyFor(mode, cc));
      }
    }
    return this;
  }

  @Override
  public Expr mergeEbv(final Expr expr, final boolean or, final CompileContext cc)
      throws QueryException {

    if(or && Function.EXISTS.is(expr)) {
      final Expr args = List.get(cc, info, exprs[0], expr.arg(0));
      return cc.function(Function.EXISTS, info, args);
    }
    if(_UTIL_COUNT_WITHIN.is(expr)) {
      return expr.mergeEbv(this, or, cc);
    }
    return null;
  }
}
