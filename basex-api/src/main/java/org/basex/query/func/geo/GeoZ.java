package org.basex.query.func.geo;

import org.basex.query.*;
import org.basex.query.value.item.*;
import org.basex.util.*;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-22, BSD License
 * @author Christian Gruen
 */
public final class GeoZ extends GeoFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return Dbl.get(geo(0, qc, POINT, Q_GML_POINT).getCoordinate().z);
  }
}
