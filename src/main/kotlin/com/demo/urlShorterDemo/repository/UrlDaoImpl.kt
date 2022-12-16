package com.demo.urlShorterDemo.repository

import com.demo.urlShorterDemo.model.Url
import java.util.List

import javax.transaction.Transactional

import org.hibernate.Criteria
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.criterion.Restrictions
import org.hibernate.exception.ConstraintViolationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository


@Transactional
@Repository
class UrlDaoImpl {

    private val logger: Logger = LoggerFactory.getLogger(UrlDaoImpl::class.java)

    @Autowired(required = true)
    private val sessionFactory: SessionFactory? = null

    fun getSession(): Session {
        return sessionFactory!!.openSession()
    }

    fun saveUrl(lUrl: Url, count: Int): Boolean? {
        var count = count
        val session: Session = getSession()
        val tx: Transaction = session.beginTransaction()
        val criteria: Criteria = session.createCriteria(Url::class.java)
        if (criteria != null) {
            try {
                session.saveOrUpdate(lUrl)
                tx.commit()
            } catch (e: ConstraintViolationException) {
                logger.info(
                    "ConstraintViolationException while persisting the Url Object {} and generatig new short Url",
                    lUrl.getLongUrl()
                )
                if (count <= 5) {
                    lUrl.setShortUrl(null)
                    saveUrl(lUrl, count++)
                }
            } catch (ex: Exception) {
                logger.error("Exception while persisting the Url Object {} ", ex.message)
                tx.rollback()
                return false
            }
        }
        return true
    }

    fun fetchAllUrl(): List<Url?>? {
        val session: Session = getSession()
        var result: List<Url?>? = null
        val criteria: Criteria = session.createCriteria(Url::class.java)
        if (criteria != null) {
            result = criteria.list() as List<Url?>
        }
        return result
    }

    fun fetchUrlDetails(columnName: String, lUrl: String): Url? {
        val session: Session = getSession()
        val criteria: Criteria = session.createCriteria(Url::class.java)

        return criteria.add(Restrictions.eq(columnName, lUrl)).uniqueResult() as Url?
    }

    fun deleteUrlFromDb(lUrlSet: Set<String?>) {
        val session: Session = getSession()
        val tx: Transaction = session.beginTransaction()
        val criteria: Criteria = session.createCriteria(Url::class.java)
        if (criteria != null) {
            try {
                session.delete(lUrlSet)
                tx.commit()
            } catch (e: Exception) {
                tx.rollback()
            }
        }
    }

}