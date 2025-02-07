package org.basex.query.func.geo;

import org.basex.query.*;
import org.basex.query.value.item.*;
import org.basex.util.*;

import com.vividsolutions.jts.io.*;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-22, BSD License
 * @author Christian Gruen
 */
public final class GeoAsText extends GeoFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return Str.get(new WKTWriter().write(checkGeo(0, qc)));
  }
}
