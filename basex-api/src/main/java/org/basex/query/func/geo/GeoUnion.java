package org.basex.query.func.geo;

import org.basex.query.*;
import org.basex.query.value.item.*;
import org.basex.util.*;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-19, BSD License
 * @author Christian Gruen
 */
public final class GeoUnion extends GeoFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return toElement(checkGeo(0, qc).union(checkGeo(1, qc)), qc);
  }
}
