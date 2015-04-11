/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import bean.ArticleColloque;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Naoufal
 */
@Stateless
public class ArticleColloqueFacade extends AbstractFacade<ArticleColloque> {
    @PersistenceContext(unitName = "Biblio_NetPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ArticleColloqueFacade() {
        super(ArticleColloque.class);
    }
    
}
