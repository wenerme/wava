package me.wener.wava.util;

import com.google.common.base.CaseFormat;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.FieldMask;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Durations;
import com.google.protobuf.util.Timestamps;
import java.time.Duration;
import java.util.Date;

/**
 * Utils for protobuf
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/4/16
 */
public interface Protos {

  /** Normal field name to JSON field name */
  static String toJsonFieldName(String s) {
    return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, s);
  }

  /** JSON field name to normal field name */
  static String toFieldName(String s) {
    return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, s);
  }

  /** generate {@link FieldMask.Builder} from message or builder */
  static FieldMask.Builder generateFieldMaskBuilder(MessageOrBuilder message) {
    FieldMask.Builder builder = FieldMask.newBuilder();
    for (FieldDescriptor fieldDescriptor : message.getDescriptorForType().getFields()) {
      if (fieldDescriptor.isRepeated()) {
        if (message.getRepeatedFieldCount(fieldDescriptor) > 0) {
          builder.addPaths(fieldDescriptor.getName());
        }
      } else if (message.hasField(fieldDescriptor)) {
        builder.addPaths(fieldDescriptor.getName());
      }
    }
    return builder;
  }

  /** convert proto duration to jdk */
  static Duration toDuration(com.google.protobuf.Duration duration) {
    if (duration == null) {
      return null;
    }
    return Duration.ofNanos(Durations.toNanos(duration));
  }
  /** convert jdk duration to proto */
  static com.google.protobuf.Duration fromDuration(Duration duration) {
    if (duration == null) {
      return null;
    }
    return Durations.fromNanos(duration.toNanos());
  }
  /** convert proto Timestamp to jdk Date */
  static Date toDate(Timestamp timestamp) {
    if (timestamp == null) {
      return null;
    }
    if (!Timestamps.isValid(timestamp)) {
      return null;
    }
    if (timestamp.getDefaultInstanceForType() == timestamp) {
      return null;
    }
    return new Date(Timestamps.toMillis(timestamp));
  }
  /** convert jdk Date to proto Timestamp */
  static Timestamp fromDate(Date date) {
    if (date == null) {
      return null;
    }
    return Timestamps.fromMillis(date.getTime());
  }
}
