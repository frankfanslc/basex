package org.basex.query.func.fn;

import static org.basex.query.QueryError.*;
import static org.basex.util.Token.*;

import java.util.regex.*;

import org.basex.query.*;
import org.basex.query.value.item.*;
import org.basex.util.*;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-22, BSD License
 * @author Christian Gruen
 */
public final class FnReplace extends RegEx {
  @Override
  public Str item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] value = toZeroToken(exprs[0], qc);
    final byte[] pttrn = toToken(exprs[1], qc), rplc = toToken(exprs[2], qc);

    if(exprs.length < 4) {
      final int sp = patternChar(pttrn), rp = patternChar(rplc);
      if(sp != -1 && rp != -1) return Str.get(replace(value, sp, rp));
    }

    final RegExpr regExpr = regExpr(pttrn, exprs.length == 4 ? exprs[3] : null, qc, true);
    final String input = string(value);
    String replace = string(rplc);

    if((regExpr.pattern.flags() & Pattern.LITERAL) == 0) {
      // standard parsing: raise errors for some special cases
      final int rl = rplc.length;
      for(int r = 0; r < rl; ++r) {
        final int n = r + 1 == rl ? 0 : rplc[r + 1];
        if(rplc[r] == '\\') {
          if(n != '\\' && n != '$') throw FUNREPBS_X.get(info, rplc);
          ++r;
        } else if(rplc[r] == '$' && (r == 0 || rplc[r - 1] != '\\') && !digit(n)) {
          throw FUNREPDOL_X.get(info, rplc);
        }
      }

      // remove unused group references
      if(Strings.contains(replace, '$')) {
        final StringBuilder sb = new StringBuilder();
        final int sl = replace.length();
        for(int s = 0; s < sl;) {
          int i = replace.indexOf('$', s);
          if(i == -1) {
            sb.append(replace, s, sl);
            s = sl;
          } else if(i == 0 || replace.charAt(i - 1) != '\\') {
            sb.append(replace, s, i);
            s = ++i;
            if(i < sl && Character.isDigit(replace.charAt(i))) i++;
            final int n = Integer.parseInt(replace.substring(s, i));
            if(n <= regExpr.groups) sb.append('$').append(n);
            s = i;
          } else {
            sb.append(replace, s, i + 1);
            s = i + 1;
          }
        }
        replace = sb.toString();
      }
    } else {
      // literal parsing: add backslashes
      replace = replace.replace("\\", "\\\\").replace("$", "\\$");
    }

    try {
      return Str.get(regExpr.pattern.matcher(input).replaceAll(replace));
    } catch(final Exception ex) {
      if(ex.getMessage().contains("No group")) throw REGROUP.get(info);
      throw REGPAT_X.get(info, ex);
    }
  }
}
