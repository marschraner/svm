package ch.metzenthin.svm.service;

import ch.metzenthin.svm.config.SvmProperties2;
import ch.metzenthin.svm.persistence.repository.RepositoryTestConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Hans Stamm
 */
@ComponentScan("ch.metzenthin.svm.service")
@EnableConfigurationProperties(SvmProperties2.class)
public class ServiceTestConfiguration extends RepositoryTestConfiguration {}
