package org.example.teledon.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.teledon.domain.Case;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class CaseORMRepository implements Repository<Case, Long> {
    SessionFactory sessionFactory;
    private static final Logger logger = LogManager.getLogger();

    public CaseORMRepository(SessionFactory sessionFactory) {
        logger.info("Initializing CasORMRepository");
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void add(Case elem) {
        logger.traceEntry("saving entity {}", elem);
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(elem);
                tx.commit();
            } catch (RuntimeException e) {
                logger.error(e);
                if (tx != null) {
                    tx.rollback();
                }
            }
        }
        logger.traceExit();
    }

    @Override
    public void delete(Case elem) {
        logger.traceEntry("deleting entity {}", elem);
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.delete(elem);
                tx.commit();
            } catch (RuntimeException e) {
                logger.error(e);
                if (tx != null) {
                    tx.rollback();
                }
            }
        }
        logger.traceExit();
    }

    @Override
    public void update(Case elem, Long aLong) {
        logger.traceEntry("updating entity {}", elem);
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                elem.setId(aLong);
                session.update(elem);
                tx.commit();
            } catch (RuntimeException e) {
                logger.error(e);
                if (tx != null) {
                    tx.rollback();
                }
            }
        }
        logger.traceExit();
    }

    @Override
    public Case findOne(Long aLong) {
        logger.traceEntry("getting case");
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List<Case> cases = session
                        .createQuery("from Case as c where c.id = :id", Case.class)
                        .setParameter("id", aLong)
                        .list();
                logger.info(cases.size() + " case(s) found:");

                tx.commit();
                return cases.stream().findFirst().orElse(null);
            } catch (RuntimeException e) {
                logger.error(e);
                if (tx != null) {
                    tx.rollback();
                }
            }
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Iterable<Case> findAll() {
        logger.traceEntry("getting cases");
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List<Case> cases = session
                        .createQuery("from Case as c order by c.id asc", Case.class)
                        .list();
                logger.info(cases.size() + " case(s) found:");

                tx.commit();
                return cases;
            } catch (RuntimeException e) {
                logger.error(e);
                if (tx != null) {
                    tx.rollback();
                }
            }
        }
        logger.traceExit();
        return null;
    }
}