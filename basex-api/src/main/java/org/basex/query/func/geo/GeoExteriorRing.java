package org.basex.query.func.geo;

import org.basex.query.*;
import org.basex.query.value.item.*;
import org.basex.util.*;

import com.vividsolutions.jts.geom.*;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-22, BSD License
 * @author Christian Gruen
 */
public final class GeoExteriorRing extends GeoFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Geometry geo = geo(0, qc, POLYGON, Q_GML_POLYGON);
    return toElement(((Polygon) geo).getExteriorRing(), qc);
  }
}
