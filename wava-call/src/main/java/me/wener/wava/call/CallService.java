package me.wener.wava.call;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.wener.wava.error.Errors;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/8/16
 */
public class CallService {

  final ServiceRegistry registry = new ServiceRegistry();
  private final Map<String, CallSession> sessions = Maps.newConcurrentMap();

  public CallRunner context(CallRequest request) {
    CallSession session = sessions.get(request.getCallerId());
    Preconditions.checkNotNull(session, "Caller %s session not found", request.getCallerId());
    return new CallRunner(this, request, session);
  }

  public CompletionStage<CallResponse> call(CallRequest request) {
    CallRunner context = context(request);
    CompletableFuture.runAsync(context);
    return context.getResponse();
  }

  public void registry(Object target) {
    registry(target, target.getClass(), target.getClass().getSimpleName());
  }

  public void registry(Object target, Class<?> type) {
    registry(target, type, type.getSimpleName());
  }

  public void registry(Object target, String name) {
    registry(target, target.getClass(), name);
  }

  public void registry(Object target, Class<?> type, String name) {
    Service service = new Service(name, target, type);
    registry.getServices().put(service.getName(), service);
  }

  public CallSession addSession(CallSession session) {
    sessions.put(session.getCallerId(), session);
    return session;
  }

  @Getter
  @Setter
  class ServiceRegistry {
    private Map<String, Service> services = Maps.newConcurrentMap();

    public Service require(String name) {
      Service v = services.get(name);
      Errors.badRequest().check(v != null, "服务不存在");
      return v;
    }
  }

  @Getter
  @Setter
  class Service {
    private final Map<String, ServiceMethod> methods = Maps.newHashMap();
    private final String name;
    private final Object target;
    private final Class<?> type;

    public Service(String name, Object target, Class<?> type) {
      this.name = name;
      this.target = target;
      this.type = type;

      for (Method method : target.getClass().getDeclaredMethods()) {
        ServiceMethod m = new ServiceMethod(method);

        ServiceMethod old = methods.put(m.getName(), m);
        if (old != null) {
          throw new IllegalArgumentException(
              String.format("Redefined method %s by %s", old.getMethod(), method));
        }
      }
    }

    public ServiceMethod require(String name) {
      ServiceMethod v = methods.get(name);
      Errors.badRequest().check(v != null, "服务不存在");
      return v;
    }
  }

  @Data
  class ServiceMethod {
    private final Method method;
    private final String name;
    private final int arity;

    public ServiceMethod(Method method) {
      this.method = method;
      name = method.getName();
      arity = method.getParameterCount();
    }
  }
}
