package org.basex.query.func.out;

import org.basex.query.*;
import org.basex.query.func.*;
import org.basex.query.value.item.*;
import org.basex.util.*;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-22, BSD License
 * @author Christian Gruen
 */
public final class OutCr extends StandardFunc {
  /** Carriage return character. */
  private static final Str CR = Str.get("\r");

  @Override
  public Item item(final QueryContext qc, final InputInfo ii) {
    return CR;
  }
}
