package ch.metzenthin.svm.common;

import ch.metzenthin.svm.commands.CommandInvoker;
import ch.metzenthin.svm.commands.CommandInvokerImpl;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Hans Stamm
 */
public class SvmContext {

    private EntityManagerFactory entityManagerFactory;

    public SvmContext() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("svm");
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }


    public CommandInvoker getCommandInvoker() {
        return new CommandInvokerImpl(entityManagerFactory);
    }
}
