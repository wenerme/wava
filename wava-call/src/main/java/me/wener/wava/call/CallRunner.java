package me.wener.wava.call;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import jodd.typeconverter.TypeConverterManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.wener.wava.call.CallService.Service;
import me.wener.wava.call.CallService.ServiceMethod;
import me.wener.wava.error.ErrorDetail;
import me.wener.wava.error.ErrorDetailException;
import me.wener.wava.error.Errors;
import me.wener.wava.util.Later;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/8/17
 */
@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
public class CallRunner implements Runnable {
  private static final ThreadLocal<CallContext> currentContext = new ThreadLocal<>();
  private static final ThreadLocal<CallSession> currentSession = new ThreadLocal<>();
  private final CallService callService;
  private final CallRequest request;
  private final CallSession session;
  private final CompletableFuture<CallResponse> response = new CompletableFuture<>();
  private CallContext context;

  public static CallContext requireContext() {
    return getContext().get();
  }

  public static Optional<CallContext> getContext() {
    return Optional.ofNullable(currentContext.get());
  }

  public void run() {
    context = session.createContext(request);
    currentContext.set(context);

    try {
      Preconditions.checkNotNull(request.getService(), "No service");
      Preconditions.checkNotNull(request.getMethod(), "No method");

      Service service = callService.registry.require(request.getService());
      ServiceMethod method = service.require(request.getMethod());
      Object[] args =
          parameterConvert(request.getParameters(), method.getMethod().getParameterTypes());
      Object result = method.getMethod().invoke(service.getTarget(), args);
      CompletionStage<Object> future = Later.asCompletionStage(result);
      buildResponse(request, future).whenComplete(Later.complete(response));
    } catch (Exception e) {
      log.warn(
          "Failed to run request {}#{} {}.{}",
          request.getCallerId(),
          request.getCallId(),
          request.getService(),
          request.getMethod(),
          e);
      try {
        response.complete(buildResponse(request, e));
      } catch (Exception ex) {
        log.warn("Failed to build response", ex);
      }
    }
  }

  private CallResponse buildResponse(CallRequest request, Object result) {
    CallResponse response = buildResponse(request);
    response.setSuccess(true).setResponse(result);
    return response;
  }

  private CompletionStage<CallResponse> buildResponse(
      CallRequest request, CompletionStage<Object> result) {
    return result
        .thenApply((v) -> buildResponse(request, v))
        .exceptionally((e) -> buildResponse(request, e));
  }

  private CallResponse buildResponse(CallRequest request, Throwable throwable) {
    CallResponse response = buildResponse(request);
    response.setSuccess(false);
    ErrorDetail detail = wrapErrorDetail(throwable);
    response.setStatus(detail.getStatus()).setMessage(detail.getMessage());
    return response;
  }

  private ErrorDetail wrapErrorDetail(Throwable throwable) {
    Throwable e = throwable;
    int i = 0;
    do {
      if (e == null) {
        break;
      }
      if (e instanceof ErrorDetailException) {
        return ((ErrorDetailException) e).getDetail();
      }
      e = e.getCause();
    } while (i++ < 5);

    return Errors.internalServerError().withMessage(throwable.getMessage());
  }

  private CallResponse buildResponse(CallRequest request) {
    CallResponse response = new CallResponse();
    response.setCallId(request.getCallId()).setResponseTime(System.currentTimeMillis());
    return response;
  }

  private Object[] parameterConvert(List<Object> parameters, Class<?>[] parameterTypes) {
    Errors.badRequest().check(parameters.size() == parameterTypes.length, "方法参数不一致");
    Object[] args = new Object[parameterTypes.length];
    for (int i = 0; i < parameterTypes.length; i++) {
      Class<?> type = parameterTypes[i];
      Object o = TypeConverterManager.get().convertType(parameters.get(i), type);
      args[i] = o;
    }
    return args;
  }
}
