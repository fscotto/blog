package it.plague.blog.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

@Slf4j
public final class BeanMonad {

  public static void copy(Object dest, Object src) {
    try {
      BeanUtils.copyProperties(dest, src);
    } catch (InvocationTargetException | IllegalAccessException e) {
      log.error(e.getMessage());
    }
  }

}
