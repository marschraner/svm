package ch.metzenthin.svm.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Martin Schraner
 */
@Getter
@ConfigurationProperties(prefix = "svm")
public class SvmProperties2 {

  String theaterName = "Kinder- und Jugendtheater Metzenthin AG";
}
