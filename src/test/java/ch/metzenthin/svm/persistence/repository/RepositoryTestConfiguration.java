package ch.metzenthin.svm.persistence.repository;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Martin Schraner
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan("ch.metzenthin.svm.persistence.repository")
@EnableJpaRepositories("ch.metzenthin.svm.persistence.repository")
@EntityScan("ch.metzenthin.svm.persistence.entities")
public class RepositoryTestConfiguration {}
