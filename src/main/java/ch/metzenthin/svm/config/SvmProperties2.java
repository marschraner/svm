package ch.metzenthin.svm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Martin Schraner
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "svm")
public class SvmProperties2 {

  String theaterName = "Kinder- und Jugendtheater Metzenthin AG";

  String emailClientMultipleMailsSeparator = ";";
}
