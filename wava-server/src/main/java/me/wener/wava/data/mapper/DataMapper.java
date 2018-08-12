package me.wener.wava.data.mapper;

import me.wener.wava.util.Lambdas;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Persistable;

/**
 * SpringData/JPA 相关映射
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/5/30
 */
public interface DataMapper {

  // NOTE: 不能直接使用泛型的方法, 这样 MapStruct 无法对结果类型进行检测做二次转换
  // 例如: Persistable<ID> -> ID 如果 ID 为 Long, 无法再将 Long 转为其他类型

  static <T extends Persistable<Long>> Long persistableToLong(T s) {
    return Holder.persistableToId(s);
  }

  static <T extends Persistable<Long>> String persistableLongToString(T s) {
    return Lambdas.apply(Holder.persistableToId(s), Object::toString);
  }

  static <T extends Persistable<Long>> Integer persistableToInteger(T s) {
    Long l = Lambdas.apply(s, Persistable::getId);
    return l == null ? null : Ints.checkedCast(l);
  }

  static Long[] persistableListToLongArray(List<? extends Persistable<Long>> s) {
    if (s == null) {
      return null;
    }
    Long[] longTmp = new Long[s.size()];

    int i = 0;
    for (Persistable<Long> persistable : s) {
      longTmp[i] = persistableToLong(persistable);
      i++;
    }
    return longTmp;
  }

  final class Holder {
    /** Persistable to ID type */
    private static <T extends Persistable<ID>, ID extends Serializable> ID persistableToId(T s) {
      return Lambdas.apply(s, Persistable::getId);
    }

    private static <ID extends Serializable> List<ID> persistableListToIdList(
        List<? extends Persistable<ID>> s) {
      if (s == null) {
        return null;
      }
      ArrayList<ID> ids = Lists.newArrayListWithExpectedSize(s.size());
      for (Persistable<ID> persistable : s) {
        ids.add(persistableToId(persistable));
      }
      return ids;
    }
  }
}
