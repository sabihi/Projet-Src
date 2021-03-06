/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import bean.Ouvrage;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Naoufal
 */
@Stateless
public class OuvrageFacade extends AbstractFacade<Ouvrage> {
    @PersistenceContext(unitName = "Biblio_NetPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OuvrageFacade() {
        super(Ouvrage.class);
    }
    
}
