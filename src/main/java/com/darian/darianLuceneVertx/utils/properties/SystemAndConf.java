package com.darian.darianLuceneVertx.utils.properties;

import io.netty.util.internal.StringUtil;
import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Supplier;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a>
 * @date 2020/11/14  20:15
 */
public final class SystemAndConf {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemAndConf.class);

    public static Long getLong(AbstractVerticle abstractVerticle, String name) {
        return catchException(() -> getLong_private(abstractVerticle, name), name);
    }

    private static Long getLong_private(AbstractVerticle abstractVerticle, String name) {
        String value = System.getProperty(name);

        if (StringUtil.isNullOrEmpty(value)) {
            Long aLong = abstractVerticle.config().getLong(name);
            Objects.requireNonNull(aLong);
            return aLong;
        }

        Objects.requireNonNull(value);

        return Long.valueOf(value);
    }

    public static Integer getInteger(AbstractVerticle abstractVerticle, String name) {
        return catchException(() -> getInteger_private(abstractVerticle, name), name);
    }

    private static Integer getInteger_private(AbstractVerticle abstractVerticle, String name) {
        String value = System.getProperty(name);

        if (StringUtil.isNullOrEmpty(value)) {
            Integer integer = abstractVerticle.config().getInteger(name);
            Objects.requireNonNull(integer);
            return integer;
        }

        Objects.requireNonNull(value);

        return Integer.valueOf(value);
    }

    public static String getString(AbstractVerticle abstractVerticle, String name) {
        return catchException(() -> getString_private(abstractVerticle, name), name);
    }

    private static String getString_private(AbstractVerticle abstractVerticle, String name) {
        String value = System.getProperty(name);

        if (StringUtil.isNullOrEmpty(value)) {
            value = abstractVerticle.config().getString(name);
            Objects.requireNonNull(value);
            return value;
        }

        Objects.requireNonNull(value);
        return value;
    }


    private static Boolean getBoolean_private(AbstractVerticle abstractVerticle, String name) {
        String value = System.getProperty(name);

        if (StringUtil.isNullOrEmpty(value)) {
            Boolean aBoolean = abstractVerticle.config().getBoolean(name);
            Objects.requireNonNull(aBoolean);
            return aBoolean;
        }

        Objects.requireNonNull(value);
        return Boolean.valueOf(value);
    }

    public static Boolean getBoolean(AbstractVerticle abstractVerticle, String name) {

        return catchException(() -> getBoolean_private(abstractVerticle, name), name);
    }

    private static <T> T catchException(Supplier<T> supplier, String name) {
        try {
            return supplier.get();
        } catch (NullPointerException e) {
            LOGGER.error("getProperties[ " + name + " ] get null, please check");
        } catch (Exception e) {
            LOGGER.error("getProperties[ " + name + " ]", e);
        }
        return null;
    }

}
