package org.basex.query.func.request;

import org.basex.query.*;
import org.basex.query.value.item.*;
import org.basex.util.*;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-19, BSD License
 * @author Christian Gruen
 */
public final class RequestPort extends RequestFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return Int.get(request(qc).getServerPort());
  }
}
