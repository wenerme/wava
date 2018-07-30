package com.github.wenerme.wava.error;

import lombok.Data;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 16/6/3
 */
@Data
public class GeneralMessage {

  private String id;
  private String message;
  private Boolean result;
  private Integer status = 200;
}
