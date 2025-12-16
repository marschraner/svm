package ch.metzenthin.svm.service;

import ch.metzenthin.svm.persistence.repository.RepositoryTestConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Hans Stamm
 */
@ComponentScan("ch.metzenthin.svm.service.impl")
public class ServiceTestConfiguration extends RepositoryTestConfiguration {}
