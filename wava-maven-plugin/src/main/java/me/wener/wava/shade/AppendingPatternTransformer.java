package me.wener.wava.shade;

import com.google.common.collect.Maps;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.regex.Pattern;
import org.apache.maven.plugins.shade.relocation.Relocator;
import org.apache.maven.plugins.shade.resource.ResourceTransformer;
import org.codehaus.plexus.util.IOUtil;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/12/29
 */
public class AppendingPatternTransformer implements ResourceTransformer {
  String resource;
  Pattern pattern;
  Map<String, ByteArrayOutputStream> resources = Maps.newHashMap();

  public AppendingPatternTransformer() {}

  public boolean canTransformResource(String r) {
    if (resource != null) {
      if (pattern == null) {
        pattern = Pattern.compile(resource);
      }
      return pattern.matcher(r).find();
    }
    return false;
  }

  public void processResource(String resource, InputStream is, List<Relocator> relocators)
      throws IOException {
    ByteArrayOutputStream data = resources.get(resource);
    if (data == null) {
      resources.put(resource, data = new ByteArrayOutputStream());
    }
    IOUtil.copy(is, data);
    data.write('\n');
  }

  public boolean hasTransformedResource() {
    return resources.size() > 0;
  }

  public void modifyOutputStream(JarOutputStream jos) throws IOException {
    for (Entry<String, ByteArrayOutputStream> entry : resources.entrySet()) {
      jos.putNextEntry(new JarEntry(entry.getKey()));

      ByteArrayOutputStream data = entry.getValue();
      IOUtil.copy(new ByteArrayInputStream(data.toByteArray()), jos);
      data.reset();
    }
  }
}
